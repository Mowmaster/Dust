package com.mowmaster.dust.Features.DustStorage.Client;

import com.mowmaster.dust.Features.DustStorage.DustPileBlockEntity;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

// A Dust Pile's colour in the world (tintindex 0, registered in DustClient): the mix of the elements in it, weighed by
// how much of each (DustPileBlockEntity.color). Colours are 0xAARRGGBB.
public class DustPileTint implements BlockTintSource
{
    @Override
    public int color(BlockState state)
    {
        return 0xFFFFFFFF;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof DustPileBlockEntity pile) return 0xFF000000 | pile.color();
        return 0xFFFFFFFF;
    }
}
