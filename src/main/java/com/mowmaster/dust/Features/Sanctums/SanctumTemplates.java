package com.mowmaster.dust.Features.Sanctums;

import com.mojang.logging.LogUtils;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
//   vaults       every Arcane Vault (anyone may open them again when the sanctum resets)
//   containers   chests and barrels (any left empty in the template get the element's loot)
// Markers may be named with or without "dust:"; where they stand is left as air. (Reading a template, its variants and
// 26.1.2's reading of 26.3 templates: TemplateScan.)
public final class SanctumTemplates
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String FOLDER = "sanctum/", SHARED = "sanctum";
    public static final String SURFACE = "surface", RETURN = "return";
    // How far out in front of the portal players come back (if the room allows), and the square round the anchor
    // checked for steep ground when the template has no ground layer
    private static final int RETURN_STEPS = 4, ANCHOR_SQUARE = 4;

    // By the template's palette (a template changed is read again: TemplateScan)
    private static final Map<StructureTemplate.Palette, Layout> LAYOUTS = Collections.synchronizedMap(new WeakHashMap<>());

    // What the sanctum code needs from a template, in its own coordinates. filled: every position it fills, air included
    // (the rest is structure void, where the ground is left as it is).
    public record Layout(Vec3i size, BlockPos anchor, Set<BlockPos> filled, List<BlockPos> ground, List<BlockPos> corners,
                         List<BlockPos> spawners, List<BlockPos> vaults, List<BlockPos> containers, @Nullable Portal portal) {}

    // The keystone, the two bottom blocks of the opening (first: the one over the keystone), the way the opening runs,
    // the side it faces and the return point
    public record Portal(BlockPos keystone, BlockPos first, BlockPos second, Direction.Axis axis, Direction front, BlockPos returnPos) {}

    private SanctumTemplates() {}

    // A template for a sanctum of this element: one of its own, else a shared one; null if there are none
    public static @Nullable Identifier pick(StructureTemplateManager templates, ElementalArmorSet set, RandomSource random)
    {
        List<Identifier> choices = TemplateScan.variants(templates, FOLDER + set.id);
        if (choices.isEmpty()) choices = TemplateScan.variants(templates, FOLDER + SHARED);
        return choices.isEmpty() ? null : choices.get(random.nextInt(choices.size()));
    }

    // The template's layout (id only names it in warnings)
    public static Layout layout(Identifier id, StructureTemplate template)
    {
        StructureTemplate.Palette palette = TemplateScan.palette(template);
        if (palette == null) return read(id, template);
        return LAYOUTS.computeIfAbsent(palette, key -> read(id, template));
    }

    private static Layout read(Identifier id, StructureTemplate template)
    {
        TemplateScan scan = TemplateScan.of(id, template);
        Vec3i size = scan.size();
        BlockPos surface = scan.marker(SURFACE);
        BlockPos anchor = surface != null ? surface : new BlockPos(size.getX() / 2, 1, size.getZ() / 2);
        List<BlockPos> ground = new ArrayList<>();
        for (BlockPos pos : scan.filled()) if (pos.getY() == anchor.getY() - 1 && !scan.air().contains(pos)) ground.add(pos);

        List<BlockPos> spawners = TemplateScan.blocks(template, Sanctums.ARCANE_SPAWNER.get());
        List<BlockPos> vaults = TemplateScan.blocks(template, Sanctums.ARCANE_VAULT.get());
        List<BlockPos> keystones = TemplateScan.blocks(template, Sanctums.SANCTUM_KEYSTONE.get());
        List<BlockPos> containers = containers(template);
        if (keystones.isEmpty()) LOGGER.warn("Sanctum template {} has no Sanctum Keystone, so its sanctums have no portal", id);
        else if (keystones.size() > 1) LOGGER.warn("Sanctum template {} has {} Sanctum Keystones; only one opens a portal", id, keystones.size());
        Portal portal = keystones.isEmpty() ? null : portal(keystones.get(0), scan, scan.marker(RETURN));
        return new Layout(size, anchor, scan.filled(), List.copyOf(ground), corners(ground, anchor), List.copyOf(spawners),
                List.copyOf(vaults), List.copyOf(containers), portal);
    }

    // Where a template's chests and barrels are (the element's wood chests too)
    public static List<BlockPos> containers(StructureTemplate template)
    {
        List<Block> blocks = new ArrayList<>(List.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL));
        for (WoodSet wood : ElementalWoods.all()) blocks.add(wood.chest().get());
        List<BlockPos> found = new ArrayList<>();
        for (Block block : blocks) found.addAll(TemplateScan.blocks(template, block));
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
    private static Portal portal(BlockPos keystone, TemplateScan scan, @Nullable BlockPos back)
    {
        BlockPos above = keystone.above();
        Opening opening = opening(above, scan);
        int room = scan.run(above, opening.front());
        BlockPos returnPos = back != null ? back : above.relative(opening.front(), Math.max(1, Math.min(RETURN_STEPS, room)));
        return new Portal(keystone, above, opening.second(), opening.axis(), opening.front(), returnPos);
    }

    // A 2 x 3 opening in a frame, from one of its bottom blocks (first): the way it runs (where it's 2 blocks wide), its
    // other bottom block, and the side with more air in front of it (also the labyrinth's exit portals)
    public record Opening(BlockPos first, BlockPos second, Direction.Axis axis, Direction front) {}

    public static Opening opening(BlockPos first, TemplateScan scan)
    {
        Set<BlockPos> air = scan.air();
        int alongX = scan.run(first, Direction.WEST) + 1 + scan.run(first, Direction.EAST);
        int alongZ = scan.run(first, Direction.NORTH) + 1 + scan.run(first, Direction.SOUTH);
        Direction.Axis axis;
        if (alongX == 2 && alongZ != 2) axis = Direction.Axis.X;
        else if (alongZ == 2 && alongX != 2) axis = Direction.Axis.Z;
        else axis = alongX <= alongZ ? Direction.Axis.X : Direction.Axis.Z;
        Direction along = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        BlockPos second = air.contains(first.relative(along)) || !air.contains(first.relative(along.getOpposite()))
                ? first.relative(along) : first.relative(along.getOpposite());
        Direction across = Direction.fromAxisAndDirection(axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X, Direction.AxisDirection.POSITIVE);
        Direction front = scan.run(first, across.getOpposite()) > scan.run(first, across) ? across.getOpposite() : across;
        return new Opening(first, second, axis, front);
    }
}
