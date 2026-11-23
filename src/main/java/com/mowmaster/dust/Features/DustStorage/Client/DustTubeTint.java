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

    // In a guide scene the scene gives the colour (its "tint"); without one, white, not whatever ran through a tube at the
    // same spot in the world
    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        if (level instanceof com.mowmaster.dust.Features.Guide.Client.GuideSceneBlocks) return 0xFFFFFFFF;
        return 0xFF000000 | DustTubeFlows.colorAt(pos);
    }
}
