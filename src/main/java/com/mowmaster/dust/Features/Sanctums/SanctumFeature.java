package com.mowmaster.dust.Features.Sanctums;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Bosses.LootBags;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.phys.Vec3;
//? if <26.3 {
/*import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
*///?}

import java.util.ArrayList;
import java.util.List;

// A sanctum of one element: a small shrine on the surface over a spiral stair down to rooms 14 blocks underground,
// built of the element's palette (SanctumPalette), facing a random way. About one chunk in DustConfig.SANCTUM_RARITY
// gets one, in the middle of the chunk (so it fits the area a feature may build in), on dry land; its element is one
// whose trees grow in the biome (ElementalWood), or any of the fourteen.
//
//          [ portal chamber ]        the portal in its far wall (opens when every spawner is beaten), keystone below
//                 |
//   [alcove]-[  great hall  ]-[alcove]   two Arcane Spawners in the hall; a chest in each alcove
//                 |
//          [ antechamber  ]          one Arcane Spawner; the spiral stair comes down in its corner
//                 ^ shrine above
//
// Local coordinates: x to the right, z forward (towards the portal), y up from the rooms' floor.
//
// 26.3: a Feature is a record with a codec (no settings: a unit codec). Stonecutter: before 26.3 it's a class taking a
// NoneFeatureConfiguration, whose place(context) calls the same place(level, generator, random, origin).
//? if >=26.3 {
public record SanctumFeature() implements Feature
{
    public static final SanctumFeature INSTANCE = new SanctumFeature();
    public static final MapCodec<SanctumFeature> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<SanctumFeature> codec()
    {
        return CODEC;
    }
//?} else {
/*public class SanctumFeature extends Feature<NoneFeatureConfiguration>
{
    public SanctumFeature()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        return place(context.level(), context.chunkGenerator(), context.random(), context.origin());
    }
*///?}

    // How far below the surface the rooms' floor is
    private static final int DEPTH = 14;
    // The shrine and its stair: the shaft's middle (the stair winds round it)
    private static final int SHAFT_X = -4, SHAFT_Z = -14;
    private static final int[][] STEPS = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

    //? if >=26.3
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin)
    {
        if (random.nextInt(DustConfig.rarity(DustConfig.SANCTUM_RARITY)) != 0) return false;
        // The middle of the chunk, on the surface
        int cx = (origin.getX() & ~15) + 8, cz = (origin.getZ() & ~15) + 8;
        BlockPos centre = new BlockPos(cx, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, cx, cz), cz);
        if (!level.getFluidState(centre).isEmpty() || !level.getFluidState(centre.below()).isEmpty()) return false;
        Holder<Biome> biome = level.getBiome(centre);
        if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN) || biome.is(BiomeTags.IS_RIVER) || biome.is(BiomeTags.IS_BEACH)) return false;

        Direction forward = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Build probe = new Build(level, centre, forward);
        // The shrine needs dry, not too steep ground
        BlockPos shrine = probe.at(SHAFT_X, 0, SHAFT_Z);
        int shrineY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, shrine.getX(), shrine.getZ());
        if (!level.getFluidState(new BlockPos(shrine.getX(), shrineY, shrine.getZ())).isEmpty()) return false;
        for (int[] corner : new int[][] {{-4, -4}, {4, -4}, {-4, 4}, {4, 4}}) {
            BlockPos at = probe.at(SHAFT_X + corner[0], 0, SHAFT_Z + corner[1]);
            if (Math.abs(level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, at.getX(), at.getZ()) - shrineY) > 4) return false;
        }
        int floor = Math.min(centre.getY(), shrineY) - DEPTH;
        if (floor < level.getMinY() + 8) return false;

        ElementalArmorSet set = pickSet(biome, random);
        Build build = new Build(level, new BlockPos(cx, floor, cz), forward);
        build(build, set, random, shrineY - floor);
        return true;
    }

    // An element whose trees grow in this biome, or any
    private static ElementalArmorSet pickSet(Holder<Biome> biome, RandomSource random)
    {
        List<ElementalArmorSet> suited = new ArrayList<>();
        for (ElementalWood wood : ElementalWood.values()) {
            ElementalArmorSet set = ElementalArmorSet.of(wood.element());
            if (set != null && wood.growsIn(biome)) suited.add(set);
        }
        if (suited.isEmpty()) return ElementalArmorSet.values()[random.nextInt(ElementalArmorSet.values().length)];
        return suited.get(random.nextInt(suited.size()));
    }

    // shrineHeight: the surface at the shrine, above the rooms' floor
    private static void build(Build build, ElementalArmorSet set, RandomSource random, int shrineHeight)
    {
        SanctumPalette palette = SanctumPalette.of(set);
        BlockState spawner = Sanctums.ARCANE_SPAWNER.get().defaultBlockState().setValue(ArcaneSpawnerBlock.SET, set);
        List<BlockPos> spawners = new ArrayList<>();

        // The rooms
        build.room(-6, -17, 6, -9, 0, 5, palette, random);      // antechamber
        build.room(-7, -7, 7, 7, 0, 7, palette, random);        // great hall
        build.room(-12, -3, -9, 3, 0, 5, palette, random);      // alcoves
        build.room(9, -3, 12, 3, 0, 5, palette, random);
        build.room(-5, 9, 5, 19, 0, 8, palette, random);        // portal chamber
        // The ways between them
        build.air(-1, 0, -8, 1, 2, -8);
        build.air(-1, 0, 8, 1, 2, 8);
        build.air(-8, 0, -1, -8, 2, 1);
        build.air(8, 0, -1, 8, 2, 1);

        // Antechamber: its spawner, lights
        build.set(3, 0, -13, spawner);
        spawners.add(build.at(3, 0, -13));
        build.set(7, 2, -11, palette.light());
        build.set(7, 2, -15, palette.light());
        build.set(2, 5, -10, palette.light());

        // Great hall: two spawners, four pillars, a light set in the floor
        build.set(-3, 0, 0, spawner);
        build.set(3, 0, 0, spawner);
        spawners.add(build.at(-3, 0, 0));
        spawners.add(build.at(3, 0, 0));
        for (int x : new int[] {-5, 5}) for (int z : new int[] {-5, 5}) build.pillar(x, z, 0, 7, palette);
        build.set(0, -1, 0, palette.light());
        build.set(0, -1, 2, palette.accent());
        build.set(0, -1, -2, palette.accent());
        build.set(2, -1, 0, palette.accent());
        build.set(-2, -1, 0, palette.accent());

        // Alcoves: a light and a chest each
        build.set(-10, 5, 0, palette.light());
        build.set(10, 5, 0, palette.light());
        chest(build, -12, 0, build.right, set, random);
        chest(build, 12, 0, build.right.getOpposite(), set, random);

        // Portal chamber: pillars, the frame in the far wall with the keystone below, the threshold
        for (int x : new int[] {-4, 4}) for (int z : new int[] {11, 17}) build.pillar(x, z, 0, 8, palette);
        for (int y = -1; y <= 3; y++) {
            build.set(-2, y, 20, palette.accent());
            build.set(1, y, 20, palette.accent());
        }
        build.set(-1, 3, 20, palette.accent());
        build.set(0, 3, 20, palette.accent());
        build.set(-1, -1, 20, palette.accent());
        build.set(0, -1, 20, Sanctums.SANCTUM_KEYSTONE.get().defaultBlockState());
        build.air(-1, 0, 20, 0, 2, 20);
        build.set(-1, -1, 19, palette.accent());
        build.set(0, -1, 19, palette.accent());
        build.set(0, 7, 14, palette.light());
        // The opening's corner nearest the start of its axis (the keystone fills it along EAST or SOUTH)
        Direction.Axis axis = build.right.getAxis();
        BlockPos a = build.at(-1, 0, 20), b = build.at(0, 0, 20);
        BlockPos corner = a.get(axis) <= b.get(axis) ? a : b;
        Vec3 returnPos = Vec3.atBottomCenterOf(build.at(0, 0, 16));
        if (build.level.getBlockEntity(build.at(0, -1, 20)) instanceof SanctumKeystoneBlockEntity keystone) {
            keystone.setup(set, spawners, corner, axis, returnPos, build.forward.getOpposite().toYRot());
        }

        shaftAndShrine(build, palette, random, shrineHeight);
    }

    // The spiral stair from the shrine down into the antechamber, round a pillar, in a shaft walled above the rooms;
    // and the shrine over it: a platform on the ground (filled under where the ground dips), four log pillars with
    // lights, a crystal over the opening
    private static void shaftAndShrine(Build build, SanctumPalette palette, RandomSource random, int top)
    {
        // The shaft above the antechamber's ceiling: walls round a 3 x 3 opening
        if (top > 6) {
            build.fill(SHAFT_X - 2, 5, SHAFT_Z - 2, SHAFT_X + 2, top - 1, SHAFT_Z + 2, pos -> palette.wall(random));
            build.air(SHAFT_X - 1, 5, SHAFT_Z - 1, SHAFT_X + 1, top - 1, SHAFT_Z + 1);
        }
        // The pillar and the steps, one block down each, winding round it
        build.fill(SHAFT_X, 0, SHAFT_Z, SHAFT_X, top - 1, SHAFT_Z, palette.pillar());
        int step = 0;
        for (int y = top - 2; y >= 0; y--, step++) {
            int[] cell = STEPS[step % STEPS.length];
            build.set(SHAFT_X + cell[0], y, SHAFT_Z + cell[1], palette.floor());
        }

        // The shrine's platform at the surface, the ground cleared above it and filled below
        int platform = top - 1;
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                boolean opening = Math.abs(x) <= 1 && Math.abs(z) <= 1;
                if (!opening) build.set(SHAFT_X + x, platform, SHAFT_Z + z, (Math.abs(x) == 4 || Math.abs(z) == 4) ? palette.wall() : palette.floor());
                build.air(SHAFT_X + x, platform + 1, SHAFT_Z + z, SHAFT_X + x, platform + 6, SHAFT_Z + z);
                if (opening) continue;
                for (int y = platform - 1; y > platform - 8; y--) {
                    BlockPos below = build.at(SHAFT_X + x, y, SHAFT_Z + z);
                    BlockState there = build.level.getBlockState(below);
                    if (!there.canBeReplaced() && there.getFluidState().isEmpty()) break;
                    build.level.setBlock(below, palette.wall(random), Build.FLAGS);
                }
            }
        }
        for (int x : new int[] {-4, 4}) {
            for (int z : new int[] {-4, 4}) {
                build.fill(SHAFT_X + x, platform + 1, SHAFT_Z + z, SHAFT_X + x, platform + 3, SHAFT_Z + z, palette.pillar());
                build.set(SHAFT_X + x, platform + 4, SHAFT_Z + z, palette.light());
            }
        }
        // A roof frame of wall blocks from pillar to pillar, crossbeams meeting over the opening, the element's crystal
        // where they meet
        for (int i = -3; i <= 3; i++) {
            build.set(SHAFT_X + i, platform + 4, SHAFT_Z - 4, palette.wall());
            build.set(SHAFT_X + i, platform + 4, SHAFT_Z + 4, palette.wall());
            build.set(SHAFT_X - 4, platform + 4, SHAFT_Z + i, palette.wall());
            build.set(SHAFT_X + 4, platform + 4, SHAFT_Z + i, palette.wall());
            build.set(SHAFT_X + i, platform + 4, SHAFT_Z, palette.wall());
            build.set(SHAFT_X, platform + 4, SHAFT_Z + i, palette.wall());
        }
        build.set(SHAFT_X, platform + 4, SHAFT_Z, palette.accent());
    }

    // A chest at the end of an alcove, facing into it: the element's dust, crystals and fruit, sometimes a loot bag
    private static void chest(Build build, int x, int z, Direction facing, ElementalArmorSet set, RandomSource random)
    {
        BlockPos pos = build.at(x, 0, z);
        build.level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), Build.FLAGS);
        if (!(build.level.getBlockEntity(pos) instanceof ChestBlockEntity chest)) return;
        DustElement element = set.element();
        chest.setItem(random.nextInt(27), new ItemStack(DustItemRegistry.DUSTS.get(element).get(), 8 + random.nextInt(17)));
        chest.setItem(random.nextInt(27), new ItemStack(DustItemRegistry.CRYSTALS.get(element).get(), 2 + random.nextInt(4)));
        WoodSet wood = null;
        for (ElementalWood type : ElementalWood.values()) if (type.element().equals(element)) wood = ElementalWoods.set(type);
        if (wood != null) {
            chest.setItem(random.nextInt(27), new ItemStack(wood.fruit().get(), 2 + random.nextInt(3)));
            if (random.nextBoolean()) chest.setItem(random.nextInt(27), new ItemStack(wood.sapling().get()));
        }
        if (random.nextInt(10) == 0) chest.setItem(random.nextInt(27), LootBags.bag(set, 1));
    }
}
