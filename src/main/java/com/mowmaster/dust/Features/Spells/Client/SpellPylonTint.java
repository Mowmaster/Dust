package com.mowmaster.dust.Features.Spells.Client;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.pylons.SpellPylonBlockEntity;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

// The Spell Pylon's body (model tint index 0): its element's colour once a crystal has set it, plain before. The
// block entity asks for a redraw when the element is set.
public class SpellPylonTint implements BlockTintSource
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
        if (!(level.getBlockEntity(pos) instanceof SpellPylonBlockEntity pylon)) return WHITE;
        DustElement element = pylon.element();
        return element == null ? WHITE : 0xFF000000 | element.color();
    }
}
