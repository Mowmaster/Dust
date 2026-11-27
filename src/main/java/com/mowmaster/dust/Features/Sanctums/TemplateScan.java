package com.mowmaster.dust.Features.Sanctums;

import com.mojang.logging.LogUtils;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

// What's in a structure template, in its own coordinates (before it's turned), read once for the sanctums
// (SanctumTemplates) and the labyrinth (Features/Labyrinth/LabyrinthBuilder):
//   size     its size
//   filled   every position it fills, air included (the rest is structure void: whatever was there stays)
//   air      which of those are empty: air, and data markers (emptied as it's placed)
//   markers  its data markers by name: structure blocks in Data mode, the name typed in them ("dust:" in front of a
//            name is dropped)
// Kept by the template's palette: Minecraft makes a new palette whenever a template is loaded again or saved over (a
// structure block saving refills the same template), so a changed template is read again.
// Also: a template's numbered variants (variants), the positions of one block in it (blocks), and 26.1.2 reading
// templates saved by 26.3 (fixLegacy).
public record TemplateScan(Vec3i size, Set<BlockPos> filled, Set<BlockPos> air, Map<String, List<BlockPos>> markers)
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int MAX_VARIANTS = 64;
    // Blocks that leave their place empty: air, and data markers
    private static final Set<String> EMPTY = Set.of("minecraft:air", "minecraft:cave_air", "minecraft:void_air", "minecraft:structure_block");
    private static final Map<StructureTemplate.Palette, TemplateScan> SCANS = Collections.synchronizedMap(new WeakHashMap<>());

    // The first marker with this name, or null
    public @Nullable BlockPos marker(String name)
    {
        List<BlockPos> found = markers.get(name);
        return found == null || found.isEmpty() ? null : found.get(0);
    }

    // How many empty blocks in a row there are going dir from pos (not counting pos), up to 8
    public int run(BlockPos pos, Direction dir)
    {
        int count = 0;
        while (count < 8 && air.contains(pos.relative(dir, count + 1))) count++;
        return count;
    }

    // The template's scan (id only names it in warnings)
    public static TemplateScan of(Identifier id, StructureTemplate template)
    {
        StructureTemplate.Palette palette = palette(template);
        if (palette == null) return read(template);
        return SCANS.computeIfAbsent(palette, key -> read(template));
    }

    // The palette the template places (none if it's empty)
    public static StructureTemplate.@Nullable Palette palette(StructureTemplate template)
    {
        PaletteProbe probe = new PaletteProbe();
        template.filterBlocks(BlockPos.ZERO, probe, Blocks.STRUCTURE_BLOCK, false);
        return probe.palette;
    }

    // Settings that note which palette a template hands them (filterBlocks has them pick one)
    private static final class PaletteProbe extends StructurePlaceSettings
    {
        private StructureTemplate.@Nullable Palette palette;

        @Override
        public StructureTemplate.Palette getRandomPalette(List<StructureTemplate.Palette> palettes, @Nullable BlockPos pos)
        {
            StructureTemplate.Palette chosen = super.getRandomPalette(palettes, pos);
            palette = chosen;
            return chosen;
        }
    }

    private static TemplateScan read(StructureTemplate template)
    {
        StructurePlaceSettings plain = new StructurePlaceSettings();
        Map<String, List<BlockPos>> markers = new HashMap<>();
        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, plain, Blocks.STRUCTURE_BLOCK, false)) {
            CompoundTag nbt = info.nbt();
            if (nbt == null || !"DATA".equals(nbt.getStringOr("mode", ""))) continue;
            String name = nbt.getStringOr("metadata", "").trim();
            if (name.startsWith(DustReferences.MODID + ":")) name = name.substring(DustReferences.MODID.length() + 1);
            markers.computeIfAbsent(name, key -> new ArrayList<>()).add(info.pos());
        }
        // Every position and which are empty, from the saved template (Minecraft offers no other way to list them all)
        Set<BlockPos> filled = new HashSet<>(), air = new HashSet<>();
        CompoundTag saved = template.save(new CompoundTag());
        ListTag palette = saved.getListOrEmpty("palette");
        if (palette.isEmpty()) palette = saved.getListOrEmpty("palettes").getListOrEmpty(0);
        boolean[] empty = new boolean[palette.size()];
        for (int i = 0; i < palette.size(); i++) empty[i] = EMPTY.contains(blockName(palette.getCompoundOrEmpty(i)));
        ListTag blocks = saved.getListOrEmpty("blocks");
        for (int i = 0; i < blocks.size(); i++) {
            CompoundTag block = blocks.getCompoundOrEmpty(i);
            ListTag at = block.getListOrEmpty("pos");
            BlockPos pos = new BlockPos(at.getIntOr(0, 0), at.getIntOr(1, 0), at.getIntOr(2, 0));
            int state = block.getIntOr("state", -1);
            filled.add(pos);
            if (state >= 0 && state < empty.length && empty[state]) air.add(pos);
        }
        Map<String, List<BlockPos>> frozen = new HashMap<>();
        markers.forEach((name, list) -> frozen.put(name, List.copyOf(list)));
        return new TemplateScan(template.getSize(), Set.copyOf(filled), Set.copyOf(air), Map.copyOf(frozen));
    }

    // Where a block is in the template (its own coordinates)
    public static List<BlockPos> blocks(StructureTemplate template, Block block)
    {
        List<BlockPos> found = new ArrayList<>();
        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), block, false)) found.add(info.pos());
        return found;
    }

    // The templates dust:<path>, dust:<path>_2, dust:<path>_3, ... while they exist (Minecraft remembers which don't, so
    // asking again is cheap), each read with 26.1.2's names if it needs them (fixLegacy)
    public static List<Identifier> variants(StructureTemplateManager templates, String path)
    {
        List<Identifier> found = new ArrayList<>();
        for (int number = 1; number <= MAX_VARIANTS; number++) {
            Identifier id = Identifier.fromNamespaceAndPath(DustReferences.MODID, number == 1 ? path : path + "_" + number);
            Optional<StructureTemplate> template = templates.get(id);
            if (template.isEmpty()) break;
            fixLegacy(id, template.get());
            found.add(id);
        }
        return found;
    }

    // A block state's name in a saved template: "id" from 26.3 on, "Name" before
    private static String blockName(CompoundTag state)
    {
        return state.getStringOr("id", state.getStringOr("Name", ""));
    }

    // Stonecutter: 26.3 saves a template's block states as "id" and "properties"; 26.1.2 reads "Name" and "Properties"
    // and can't update a file saved by a newer Minecraft, so it loads a template saved in 26.3 as nothing but air. On
    // 26.1.2, a template that loaded as nothing but air is read again from its file in the mod or a datapack, with the
    // old names put back (in place: the template Minecraft keeps, so everything using it gets the fixed one). Called
    // before anything uses one of Dust's templates. On 26.3: nothing to do.
    //? if >=26.3 {
    public static void fixLegacy(Identifier id, StructureTemplate template)
    {
    }
    //?} else {
    /*private static final Set<StructureTemplate> CHECKED = Collections.newSetFromMap(new WeakHashMap<>());

    public static synchronized void fixLegacy(Identifier id, StructureTemplate template)
    {
        if (!CHECKED.add(template)) return;
        ListTag loaded = template.save(new CompoundTag()).getListOrEmpty("palette");
        for (int i = 0; i < loaded.size(); i++) if (!"minecraft:air".equals(blockName(loaded.getCompoundOrEmpty(i)))) return;
        net.minecraft.server.MinecraftServer server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;
        Identifier file = Identifier.fromNamespaceAndPath(id.getNamespace(), "structure/" + id.getPath() + ".nbt");
        Optional<net.minecraft.server.packs.resources.Resource> resource = server.getResourceManager().getResource(file);
        if (resource.isEmpty()) return;
        try (java.io.InputStream stream = resource.get().open()) {
            CompoundTag tag = net.minecraft.nbt.NbtIo.readCompressed(stream, net.minecraft.nbt.NbtAccounter.unlimitedHeap());
            boolean renamed = oldNames(tag.getListOrEmpty("palette"));
            ListTag palettes = tag.getListOrEmpty("palettes");
            for (int i = 0; i < palettes.size(); i++) renamed |= oldNames(palettes.getListOrEmpty(i));
            if (!renamed) return;
            template.load(net.minecraft.core.registries.BuiltInRegistries.BLOCK, tag);
            LOGGER.info("Structure template {} was saved by a newer Minecraft; read it with 26.1.2's block state names", id);
        } catch (java.io.IOException exception) {
            LOGGER.warn("Couldn't read structure template {}", id, exception);
        }
    }

    // "id" and "properties" back to "Name" and "Properties" in each block state; whether there were any
    private static boolean oldNames(ListTag palette)
    {
        boolean renamed = false;
        for (int i = 0; i < palette.size(); i++) {
            CompoundTag state = palette.getCompoundOrEmpty(i);
            if (state.contains("id") && !state.contains("Name")) {
                state.putString("Name", state.getStringOr("id", "minecraft:air"));
                state.remove("id");
                renamed = true;
            }
            if (state.contains("properties") && !state.contains("Properties")) {
                state.put("Properties", state.getCompoundOrEmpty("properties"));
                state.remove("properties");
            }
        }
        return renamed;
    }
    *///?}
}
