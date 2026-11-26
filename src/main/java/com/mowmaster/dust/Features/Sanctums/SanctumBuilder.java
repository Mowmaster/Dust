package com.mowmaster.dust.Features.Sanctums;

import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.Bosses.LootBags;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

// What a sanctum is built of (SanctumStructure places them; SanctumPiece calls this once for each chunk it covers, with
// the Build clipped to that chunk): a small shrine on the surface over a spiral stair down to rooms 14 blocks
// underground, of the element's palette (SanctumPalette).
//
//          [ portal chamber ]        the portal in its far wall (opens when every spawner is beaten), keystone below
//                 |
//   [alcove]-[  great hall  ]-[alcove]   two Arcane Spawners in the hall; a chest in each alcove
//                 |
//          [ antechamber  ]          one Arcane Spawner; the spiral stair comes down in its corner
//                 ^ shrine above
//
// Local coordinates: x to the right, z forward (towards the portal), y up from the rooms' floor. Everything fits in
// BOUNDS_X / BOUNDS_Z / BOUNDS_BELOW / BOUNDS_ABOVE round the origin (SanctumPiece's bounding box).
public final class SanctumBuilder
{
    // How far below the surface the rooms' floor is
    public static final int DEPTH = 14;
    // The shrine and its stair: the shaft's middle (the stair winds round it)
    public static final int SHAFT_X = -4, SHAFT_Z = -14;
    // What it covers, round its origin (local): x and z from -BOUNDS_X / BOUNDS_Z_BACK to BOUNDS_X / BOUNDS_Z_FRONT,
    // y from BOUNDS_BELOW to the shrine's surface plus BOUNDS_ABOVE
    public static final int BOUNDS_X = 14, BOUNDS_Z_BACK = 19, BOUNDS_Z_FRONT = 21, BOUNDS_BELOW = -2, BOUNDS_ABOVE = 7;
    private static final int[][] STEPS = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

    private SanctumBuilder() {}

    // shrineHeight: the surface at the shrine, above the rooms' floor
    public static void build(Build build, ElementalArmorSet set, RandomSource random, int shrineHeight)
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
        BlockPos keystonePos = build.at(0, -1, 20);
        if (build.inside(keystonePos) && build.level.getBlockEntity(keystonePos) instanceof SanctumKeystoneBlockEntity keystone) {
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
                    if (!build.inside(below)) break;
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
        if (!build.inside(pos)) return;
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
