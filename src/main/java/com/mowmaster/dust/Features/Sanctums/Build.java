package com.mowmaster.dust.Features.Sanctums;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

// Building in a turned frame: local x to the right, y up, z forward from an origin, facing any horizontal way (the
// sanctums face every way). Shared by the sanctums (worldgen) and the labyrinth (built in a live level).
public final class Build
{
    // Block flags: send to clients, no neighbour updates (nothing around reacts to what's being built)
    public static final int FLAGS = 2;

    public final LevelAccessor level;
    public final BlockPos origin;
    public final Direction forward;
    public final Direction right;

    public Build(LevelAccessor level, BlockPos origin, Direction forward)
    {
        this.level = level;
        this.origin = origin;
        this.forward = forward;
        this.right = forward.getClockWise();
    }

    public BlockPos at(int x, int y, int z)
    {
        return origin.relative(right, x).relative(forward, z).above(y);
    }

    public void set(int x, int y, int z, BlockState state)
    {
        level.setBlock(at(x, y, z), state, FLAGS);
    }

    // Every block in the box (corners inclusive, any order)
    public void fill(int x0, int y0, int z0, int x1, int y1, int z1, Function<BlockPos, BlockState> state)
    {
        for (int x = Math.min(x0, x1); x <= Math.max(x0, x1); x++) {
            for (int y = Math.min(y0, y1); y <= Math.max(y0, y1); y++) {
                for (int z = Math.min(z0, z1); z <= Math.max(z0, z1); z++) {
                    BlockPos pos = at(x, y, z);
                    level.setBlock(pos, state.apply(pos), FLAGS);
                }
            }
        }
    }

    public void fill(int x0, int y0, int z0, int x1, int y1, int z1, BlockState state)
    {
        fill(x0, y0, z0, x1, y1, z1, pos -> state);
    }

    public void air(int x0, int y0, int z0, int x1, int y1, int z1)
    {
        fill(x0, y0, z0, x1, y1, z1, Blocks.AIR.defaultBlockState());
    }

    // A room: its inside (x0..x1, z0..z1, height blocks up from floorY) emptied, with walls, a floor and a ceiling of
    // the palette round it
    public void room(int x0, int z0, int x1, int z1, int floorY, int height, SanctumPalette palette, RandomSource random)
    {
        fill(x0 - 1, floorY - 1, z0 - 1, x1 + 1, floorY + height, z1 + 1, pos -> palette.wall(random));
        fill(x0, floorY - 1, z0, x1, floorY - 1, z1, palette.floor());
        air(x0, floorY, z0, x1, floorY + height - 1, z1);
    }

    // A pillar of the palette's logs from the floor to the ceiling, with a light at its top
    public void pillar(int x, int z, int floorY, int height, SanctumPalette palette)
    {
        fill(x, floorY, z, x, floorY + height - 2, z, palette.pillar());
        set(x, floorY + height - 1, z, palette.light());
    }
}
