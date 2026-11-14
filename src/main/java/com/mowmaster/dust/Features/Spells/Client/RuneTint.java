package com.mowmaster.dust.Features.Spells.Client;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.blocks.RuneBlockEntity;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

// A rune's glyph (model tint index 0): its spell's element colour
public class RuneTint implements BlockTintSource
{
    private static final int WHITE = 0xFFFFFFFF;

    @Override
    public int color(BlockState state)
    {
        return WHITE;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        if (!(level.getBlockEntity(pos) instanceof RuneBlockEntity rune)) return WHITE;
        DustElement element = rune.element();
        return element == null ? WHITE : 0xFF000000 | element.color();
    }
}
