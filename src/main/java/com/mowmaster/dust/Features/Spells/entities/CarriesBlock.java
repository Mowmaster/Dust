package com.mowmaster.dust.Features.Spells.entities;

import net.minecraft.world.level.block.state.BlockState;

// An entity drawn as a block (the floating and animated blocks): CarriedBlockRenderer draws whatever this gives
public interface CarriesBlock
{
    BlockState carriedBlock();
}
