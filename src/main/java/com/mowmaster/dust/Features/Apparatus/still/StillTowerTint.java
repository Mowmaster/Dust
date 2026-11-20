package com.mowmaster.dust.Features.Apparatus.still;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

// The Still Tower's colours in the world, one tint source per model tintindex (registered in DustClient):
//   0-2  nothing tinted (white): the barrel, straps and inside
//   3    the window (element_tint): the colour of the element the tower is set to while the still is boiling; black
//        with none, or while it isn't
// Colours are 0xAARRGGBB.
public class StillTowerTint implements BlockTintSource
{
    public static final int WHITE = 0xFFFFFFFF;
    public static final int BLACK = 0xFF000000;

    public static List<BlockTintSource> all()
    {
        return List.of(new Plain(), new Plain(), new Plain(), new StillTowerTint());
    }

    // In hand / inventory: black, as an empty tower
    @Override
    public int color(BlockState state)
    {
        return BLACK;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        if (level.getBlockEntity(pos) instanceof StillTowerBlockEntity tower && tower.isActive() && tower.element() != null) return 0xFF000000 | tower.element().color();
        return BLACK;
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
