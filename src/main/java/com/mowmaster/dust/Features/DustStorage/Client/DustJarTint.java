package com.mowmaster.dust.Features.DustStorage.Client;

import com.mowmaster.dust.Features.DustStorage.DustJarBlockEntity;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

// The Dust Jar's colours in the world, one tint source per model tintindex (registered in DustClient):
//   0  nothing tinted (white)
//   1  the lid: the colour of the element stored, white with none set
// Colours are 0xAARRGGBB.
public class DustJarTint implements BlockTintSource
{
    public static final int WHITE = 0xFFFFFFFF;

    public static List<BlockTintSource> all()
    {
        return List.of(new Plain(), new DustJarTint());
    }

    // In hand / inventory the block model isn't tinted this way (the item model has its own: DustJarItemTint)
    @Override
    public int color(BlockState state)
    {
        return WHITE;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof DustJarBlockEntity jar && jar.element() != null) return 0xFF000000 | jar.element().color();
        return WHITE;
    }

    private static class Plain implements BlockTintSource
    {
        @Override
        public int color(BlockState state)
        {
            return WHITE;
        }
    }
}
