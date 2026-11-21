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
//   Still Pot x 1+      take what's boiled (hoppers, or right-click their tops); each holds one solid element as dust
//                       (StillPotBlockEntity.POT_CAPACITY), let out through Dust Chutes on its sides; the gaseous and
//                       aqueous elements rise into the towers
//   Burnable Firebox    burns furnace fuel (an Alchemical Soot for each piece: StillFireboxBlockEntity); its heat
//                       reaches HEAT_REACH blocks up (the pots and towers)
// The bottom pot (the one on the firebox: StillPotBlockEntity) runs it all; this reads the stack.
public final class StillLayout
{
    // How many blocks above the firebox its heat reaches
    public static final int HEAT_REACH = 5;
    // The most towers a still has (any more are past the heat, and don't catch anything)
    public static final int MAX_TOWERS = 16;

    private StillLayout() {}

    // The stack above the bottom pot: the pots over it, its towers in order, bottom up, and the top over them (null: no
    // top, it isn't a still)
    public record Stack(List<BlockPos> pots, List<BlockPos> towers, @Nullable BlockPos top) {}

    public static Stack above(Level level, BlockPos pot)
    {
        List<BlockPos> pots = new ArrayList<>();
        List<BlockPos> towers = new ArrayList<>();
        BlockPos at = pot.above();
        for (int i = 0; i <= MAX_TOWERS && level.getBlockState(at).getBlock() instanceof StillPotBlock; i++) {
            pots.add(at.immutable());
            at = at.above();
        }
        for (int i = 0; i <= MAX_TOWERS; i++) {
            BlockState state = level.getBlockState(at);
            if (state.getBlock() instanceof StillTowerBlock) {
                towers.add(at.immutable());
                at = at.above();
                continue;
            }
            return new Stack(pots, towers, state.getBlock() instanceof StillTopBlock ? at.immutable() : null);
        }
        return new Stack(pots, towers, null);
    }

    // The bottom pot of the pots stacked with this one (the one on the firebox, or the lowest if there's no firebox)
    public static BlockPos bottomPot(Level level, BlockPos pot)
    {
        BlockPos at = pot;
        for (int i = 0; i <= MAX_TOWERS && level.getBlockState(at.below()).getBlock() instanceof StillPotBlock; i++) at = at.below();
        return at;
    }

    // Whether the firebox under the pot heats this block (it's within HEAT_REACH above the firebox)
    public static boolean heated(BlockPos pot, BlockPos block)
    {
        return block.getY() - (pot.getY() - 1) <= HEAT_REACH;
    }
}
