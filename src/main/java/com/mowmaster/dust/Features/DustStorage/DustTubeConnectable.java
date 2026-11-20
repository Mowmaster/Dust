package com.mowmaster.dust.Features.DustStorage;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

// A block Dust Tubes join on to (DustTubeBlock): the Dust Jar, the Dust Grinder, the alchemical apparatus, the Spell
// Caster. Asked of the block rather than its block entity, so a tube can tell while the block is still being placed.
public interface DustTubeConnectable
{
    // Whether a tube on this side (the side of this block the tube is against) joins on. Default: every side.
    default boolean connectsDustTube(BlockState state, Direction side)
    {
        return true;
    }

    // How far a tube's arm reaches into this block to meet it (TubeJoin.BLOCK: 3 pixels; TubeJoin.NEAR: 2)
    default TubeJoin tubeJoin(BlockState state, Direction side)
    {
        return TubeJoin.BLOCK;
    }
}
