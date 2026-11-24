package com.mowmaster.dust.Features.Trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;

// How readily a block catches and burns, for blocks vanilla's fire table doesn't list (FireBlock keeps its table private,
// and NeoForge's defaults read only that table). A block class implementing this burns like vanilla wood: these defaults
// are more specific than IBlockExtension's, so they win for any Block subclass that implements this.
//   ignite odds  how readily it catches from fire next to it (FireBlock's "ignite"; NeoForge's fire spread speed)
//   burn odds    how readily fire on it burns it away (FireBlock's "burn"; NeoForge's flammability)
// Vanilla's numbers: logs and wood 5/5, planks and their stairs, slabs, fences and gates 5/20, leaves 30/60.
public interface Flammable extends IBlockExtension
{
    int LOG_IGNITE = 5, LOG_BURN = 5;
    int PLANKS_IGNITE = 5, PLANKS_BURN = 20;
    int LEAVES_IGNITE = 30, LEAVES_BURN = 60;

    int igniteOdds();

    int burnOdds();

    @Override
    default int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return burnOdds();
    }

    @Override
    default int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return igniteOdds();
    }
}
