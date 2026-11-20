package com.mowmaster.dust.Features.Apparatus.still;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// The Still (it replaces the Alembic), one block on another:
//   Still Top           the cap: without it nothing boils. What leaves through it (elements no tower takes, dust towers
//                       can't hold) goes into the chunk's aura as smoke in its colour; while the still boils it puffs
//                       white smoke
//   Still Tower x 0-4   one element each, caught as dust (up to TowerBlockEntity.CAPACITY) and poured into the Glass
//                       Dust Tubes joined on to it
//   Still Pot           takes what's boiled (hoppers, or right-click its top); its solids come out as Alchemical Soot,
//                       pushed into containers beside it; its gaseous and aqueous elements rise into the towers
//   Burnable Firebox    burns furnace fuel; its heat reaches HEAT_REACH blocks up (the pot and four towers)
// The pot (StillPotBlockEntity) runs it all; this reads the stack.
public final class StillLayout
{
    // How many blocks above the firebox its heat reaches
    public static final int HEAT_REACH = 5;
    // The most towers a still has (any more are past the heat, and don't catch anything)
    public static final int MAX_TOWERS = 16;

    private StillLayout() {}

    // The stack above a pot: its towers in order, bottom up, and the top over them (null: no top, it isn't a still)
    public record Stack(List<BlockPos> towers, @Nullable BlockPos top) {}

    public static Stack above(Level level, BlockPos pot)
    {
        List<BlockPos> towers = new ArrayList<>();
        BlockPos at = pot.above();
        for (int i = 0; i <= MAX_TOWERS; i++) {
            BlockState state = level.getBlockState(at);
            if (state.getBlock() instanceof StillTowerBlock) {
                towers.add(at.immutable());
                at = at.above();
                continue;
            }
            return new Stack(towers, state.getBlock() instanceof StillTopBlock ? at.immutable() : null);
        }
        return new Stack(towers, null);
    }

    // Whether the firebox under the pot heats this block (it's within HEAT_REACH above the firebox)
    public static boolean heated(BlockPos pot, BlockPos block)
    {
        return block.getY() - (pot.getY() - 1) <= HEAT_REACH;
    }
}
