package com.mowmaster.dust.Features.DustStorage.Client;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

// The dust core inside a filled Glass Dust Tube (tintindex 0, registered in DustClient): the colour of the dust that
// last ran through it (DustTubeFlows.colorAt), white if none has been seen. Colours are 0xAARRGGBB.
public class DustTubeTint implements BlockTintSource
{
    @Override
    public int color(BlockState state)
    {
        return 0xFFFFFFFF;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        return 0xFF000000 | DustTubeFlows.colorAt(pos);
    }
}
