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

    // The one element this block holds or gives out (a jar set to one, a Still Tower), or null: a tube network set to
    // another element won't join on to it (DustTubeBlock)
    default com.mowmaster.dust.Features.DustComposition.@org.jspecify.annotations.Nullable DustElement tubeElement(net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos)
    {
        return null;
    }

    // How far in from the block's edge its body is on this side (pixels), for tubes and other things fixed to it
    // (AttachInsets). By default worked out from its shape; a block can say, measured off its model.
    default double attachInset(BlockState state, Direction side)
    {
        return AttachInsets.fromShape(state, side);
    }

    // How a tube's arm reaches into this block to meet it: as far as its body is in (TubeJoin.reaching: flush, 2 pixels
    // or 3)
    default TubeJoin tubeJoin(BlockState state, Direction side)
    {
        return TubeJoin.reaching(attachInset(state, side));
    }
}
