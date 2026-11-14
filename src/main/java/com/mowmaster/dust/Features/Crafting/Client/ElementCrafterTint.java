package com.mowmaster.dust.Features.Crafting.Client;

import com.mowmaster.dust.Features.Crafting.ElementCrafterBlockEntity;
import com.mowmaster.dust.Features.Crafting.ElementCrafterLayout;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

// The Element Crafter's colours, one tint source per model tint index:
//   0       the slab (top, rails, bottom)            plain
//   1       the middle columns                       plain
//   2-5     the jar lids: north, east, south, west   the jar's element colour, plain with none set
// The block entity asks for a redraw whenever its jars change, so the lids follow.
public class ElementCrafterTint implements BlockTintSource
{
    private static final int WHITE = 0xFFFFFFFF;
    private static final int FIRST_LID = 2;

    private final int index;

    private ElementCrafterTint(int index)
    {
        this.index = index;
    }

    // All of them, in tint index order, for RegisterColorHandlersEvent.BlockTintSources
    public static List<BlockTintSource> all()
    {
        List<BlockTintSource> list = new ArrayList<>();
        for (int i = 0; i < FIRST_LID + ElementCrafterLayout.JARS; i++) list.add(new ElementCrafterTint(i));
        return list;
    }

    @Override
    public int color(BlockState state)
    {
        return WHITE;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        if (index < FIRST_LID || !(level.getBlockEntity(pos) instanceof ElementCrafterBlockEntity crafter)) return WHITE;
        DustElement element = crafter.jarElement(index - FIRST_LID);
        return element == null ? WHITE : 0xFF000000 | element.color();
    }
}
