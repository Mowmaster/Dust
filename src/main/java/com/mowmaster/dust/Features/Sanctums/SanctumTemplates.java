package com.mowmaster.dust.Features.Sanctums;

import com.mojang.logging.LogUtils;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

// The sanctums' structure templates, and what the sanctum code reads from one (SANCTUMS.md says how to make them).
// A sanctum uses one of its element's own templates if there are any, else one of the shared ones, at random:
//   its own   dust:sanctum/<set>, dust:sanctum/<set>_2, dust:sanctum/<set>_3, ...   (e.g. dust:sanctum/pyreborn)
//   shared    dust:sanctum/sanctum, dust:sanctum/sanctum_2, ...
// numbered from 2 with no gaps (the first missing number ends the list). Minecraft finds templates in data/dust/structure/
// (the mod's, or a datapack's) and in the world's generated/dust/structures/ folder (where a structure block saves them,
// which wins over the others).
//
// Read from a template once (in its own coordinates, before it's turned; again if it changes):
//   anchor       the data marker "surface" (a structure block in Data mode with that name), standing on the ground:
//                the template is placed with it on the ground at the chosen spot. Without one, the middle of the
//                template's bottom layer goes in the ground's top layer.
//   ground       the template's blocks in the layer under the anchor (shored up where the ground dips: SanctumPiece),
//                and the corners of the area they cover (the ground there mustn't be too steep: SanctumStructure)
//   portal       the Sanctum Keystone, under one of the two bottom blocks of the frame's 2 x 3 opening: the opening runs
//                the way it's 2 blocks wide, and its front is the side with more room
//   return       the data marker "return", or else up to 4 blocks out in front of the portal: where players come back
//   spawners     every Arcane Spawner (the portal opens once they're all beaten)
//   containers   chests and barrels (any left empty in the template get the element's loot)
// Markers may be named with or without "dust:"; where they stand is left as air.
public final class SanctumTemplates
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String FOLDER = "sanctum/", SHARED = "sanctum";
    public static final String SURFACE = "surface", RETURN = "return";
    private static final int MAX_VARIANTS = 64;
    // How far out in front of the portal players come back (if the room allows), and the square round the anchor
    // checked for steep ground when the template has no ground layer
    private static final int RETURN_STEPS = 4, ANCHOR_SQUARE = 4;
    // Blocks that leave their place empty: air, and data markers (emptied as the template is placed)
    private static final Set<String> EMPTY = Set.of("minecraft:air", "minecraft:cave_air", "minecraft:void_air", "minecraft:structure_block");

    // By the template's palette: Minecraft makes a new one whenever a template is loaded again or saved over (a structure
    // block saving refills the same template), so a changed template is read again
    private static final Map<StructureTemplate.Palette, Layout> LAYOUTS = Collections.synchronizedMap(new WeakHashMap<>());

    // What the sanctum code needs from a template, in its own coordinates. filled: every position it fills, air included
    // (the rest is structure void, where the ground is left as it is).
    public record Layout(Vec3i size, BlockPos anchor, Set<BlockPos> filled, List<BlockPos> ground, List<BlockPos> corners,
                         List<BlockPos> spawners, List<BlockPos> containers, @Nullable Portal portal) {}

    // The keystone, the two bottom blocks of the opening (first: the one over the keystone), the way the opening runs,
    // the side it faces and the return point
    public record Portal(BlockPos keystone, BlockPos first, BlockPos second, Direction.Axis axis, Direction front, BlockPos returnPos) {}

    private SanctumTemplates() {}

    // A template for a sanctum of this element: one of its own, else a shared one; null if there are none
    public static @Nullable Identifier pick(StructureTemplateManager templates, ElementalArmorSet set, RandomSource random)
    {
        List<Identifier> choices = variants(templates, set.id);
        if (choices.isEmpty()) choices = variants(templates, SHARED);
        return choices.isEmpty() ? null : choices.get(random.nextInt(choices.size()));
    }

    // name, name_2, name_3, ... while they exist (Minecraft remembers which don't, so asking again is cheap)
    private static List<Identifier> variants(StructureTemplateManager templates, String name)
    {
        List<Identifier> found = new ArrayList<>();
        for (int number = 1; number <= MAX_VARIANTS; number++) {
            Identifier id = Identifier.fromNamespaceAndPath(DustReferences.MODID, FOLDER + (number == 1 ? name : name + "_" + number));
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
    // 26.1.2, a sanctum template that loaded as nothing but air is read again from its file in the mod or a datapack,
    // with the old names put back (in place: the template Minecraft keeps, so every piece gets the fixed one). Called
    // before anything uses a template (picking one, loading a piece, placing it). On 26.3: nothing to do.
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
            LOGGER.info("Sanctum template {} was saved by a newer Minecraft; read it with 26.1.2's block state names", id);
        } catch (java.io.IOException exception) {
            LOGGER.warn("Couldn't read sanctum template {}", id, exception);
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

    // The template's layout (id only names it in warnings)
    public static Layout layout(Identifier id, StructureTemplate template)
    {
        PaletteProbe probe = new PaletteProbe();
        template.filterBlocks(BlockPos.ZERO, probe, Blocks.STRUCTURE_BLOCK, false);
        if (probe.palette == null) return read(id, template);
        return LAYOUTS.computeIfAbsent(probe.palette, key -> read(id, template));
    }

    // Settings that note which palette a template hands them (filterBlocks has them pick one); none if it's empty
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

    private static Layout read(Identifier id, StructureTemplate template)
    {
        StructurePlaceSettings plain = new StructurePlaceSettings();
        BlockPos surface = null, back = null;
        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, plain, Blocks.STRUCTURE_BLOCK, false)) {
            CompoundTag nbt = info.nbt();
            if (nbt == null || !"DATA".equals(nbt.getStringOr("mode", ""))) continue;
            String name = nbt.getStringOr("metadata", "").trim();
            if (name.startsWith(DustReferences.MODID + ":")) name = name.substring(DustReferences.MODID.length() + 1);
            if (name.equals(SURFACE)) surface = info.pos();
            else if (name.equals(RETURN)) back = info.pos();
        }
        Vec3i size = template.getSize();
        BlockPos anchor = surface != null ? surface : new BlockPos(size.getX() / 2, 1, size.getZ() / 2);

        // Every position the template fills, which of those are empty, and its ground layer (from the saved template:
        // Minecraft offers no other way to list all of a template's blocks)
        Set<BlockPos> filled = new HashSet<>(), air = new HashSet<>();
        List<BlockPos> ground = new ArrayList<>();
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
            else if (pos.getY() == anchor.getY() - 1) ground.add(pos);
        }

        List<BlockPos> spawners = positions(template, plain, Sanctums.ARCANE_SPAWNER.get());
        List<BlockPos> keystones = positions(template, plain, Sanctums.SANCTUM_KEYSTONE.get());
        List<Block> containerBlocks = new ArrayList<>(List.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL));
        for (WoodSet wood : ElementalWoods.all()) containerBlocks.add(wood.chest().get());
        List<BlockPos> containers = new ArrayList<>();
        for (Block block : containerBlocks) containers.addAll(positions(template, plain, block));
        if (keystones.isEmpty()) LOGGER.warn("Sanctum template {} has no Sanctum Keystone, so its sanctums have no portal", id);
        else if (keystones.size() > 1) LOGGER.warn("Sanctum template {} has {} Sanctum Keystones; only one opens a portal", id, keystones.size());
        Portal portal = keystones.isEmpty() ? null : portal(keystones.get(0), air, back);
        return new Layout(size, anchor, Set.copyOf(filled), List.copyOf(ground), corners(ground, anchor), List.copyOf(spawners),
                List.copyOf(containers), portal);
    }

    private static List<BlockPos> positions(StructureTemplate template, StructurePlaceSettings plain, Block block)
    {
        List<BlockPos> found = new ArrayList<>();
        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(BlockPos.ZERO, plain, block, false)) found.add(info.pos());
        return found;
    }

    // The corners of the area the ground layer covers (a square round the anchor if it has none)
    private static List<BlockPos> corners(List<BlockPos> ground, BlockPos anchor)
    {
        int minX = anchor.getX() - ANCHOR_SQUARE, maxX = anchor.getX() + ANCHOR_SQUARE;
        int minZ = anchor.getZ() - ANCHOR_SQUARE, maxZ = anchor.getZ() + ANCHOR_SQUARE;
        if (!ground.isEmpty()) {
            minX = maxX = ground.get(0).getX();
            minZ = maxZ = ground.get(0).getZ();
            for (BlockPos pos : ground) {
                minX = Math.min(minX, pos.getX());
                maxX = Math.max(maxX, pos.getX());
                minZ = Math.min(minZ, pos.getZ());
                maxZ = Math.max(maxZ, pos.getZ());
            }
        }
        int y = anchor.getY() - 1;
        return List.of(new BlockPos(minX, y, minZ), new BlockPos(maxX, y, minZ), new BlockPos(minX, y, maxZ), new BlockPos(maxX, y, maxZ));
    }

    // The portal over the keystone: the opening is 2 wide one way (the frame closes it after 2), and opens onto the side
    // with more air in front of it
    private static Portal portal(BlockPos keystone, Set<BlockPos> air, @Nullable BlockPos back)
    {
        BlockPos above = keystone.above();
        int alongX = run(air, above, Direction.WEST) + 1 + run(air, above, Direction.EAST);
        int alongZ = run(air, above, Direction.NORTH) + 1 + run(air, above, Direction.SOUTH);
        Direction.Axis axis;
        if (alongX == 2 && alongZ != 2) axis = Direction.Axis.X;
        else if (alongZ == 2 && alongX != 2) axis = Direction.Axis.Z;
        else axis = alongX <= alongZ ? Direction.Axis.X : Direction.Axis.Z;
        Direction along = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        BlockPos second = air.contains(above.relative(along)) || !air.contains(above.relative(along.getOpposite()))
                ? above.relative(along) : above.relative(along.getOpposite());
        Direction across = Direction.fromAxisAndDirection(axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X, Direction.AxisDirection.POSITIVE);
        Direction front = run(air, above, across.getOpposite()) > run(air, above, across) ? across.getOpposite() : across;
        BlockPos returnPos = back != null ? back : above.relative(front, Math.max(1, Math.min(RETURN_STEPS, run(air, above, front))));
        return new Portal(keystone, above, second, axis, front, returnPos);
    }

    // How many empty blocks in a row there are going dir from pos (not counting pos), up to 8
    private static int run(Set<BlockPos> air, BlockPos pos, Direction dir)
    {
        int count = 0;
        while (count < 8 && air.contains(pos.relative(dir, count + 1))) count++;
        return count;
    }
}
