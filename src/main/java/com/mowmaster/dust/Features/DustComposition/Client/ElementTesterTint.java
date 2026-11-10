package com.mowmaster.dust.Features.DustComposition.Client;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlock;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlockEntity;
import com.mowmaster.dust.Features.DustComposition.ExaminerLayout;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

// The Element Tester's colours, one tint source per model tint index (see ExaminerLayout):
//   0-4                    each crystal, in its element's colour
//   5 + crystal * 10 + k   gauge line k of that crystal: the crystal's colour if lit, a dull grey if not
// The block entity reruns this whenever its crystals or item change (it asks for the block to be redrawn).
public class ElementTesterTint implements BlockTintSource
{
    private static final int WHITE = 0xFFFFFFFF;
    private static final int UNLIT = 0xFF3A3A3A;

    private final int index;

    private ElementTesterTint(int index)
    {
        this.index = index;
    }

    // All of them, in tint index order, for RegisterColorHandlersEvent.BlockTintSources
    public static List<BlockTintSource> all()
    {
        List<BlockTintSource> list = new ArrayList<>();
        for (int i = 0; i < ExaminerLayout.MAX_CRYSTALS * (1 + ExaminerLayout.MAX_LINES); i++) list.add(new ElementTesterTint(i));
        return list;
    }

    // As an item (no crystals): plain
    @Override
    public int color(BlockState state)
    {
        return WHITE;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        if (!(level.getBlockEntity(pos) instanceof ElementTesterBlockEntity tester)) return WHITE;
        List<DustElement> crystals = tester.crystals();
        int crystal = index < ExaminerLayout.MAX_CRYSTALS ? index : (index - ExaminerLayout.MAX_CRYSTALS) / ExaminerLayout.MAX_LINES;
        if (crystal >= crystals.size()) return WHITE;
        int color = 0xFF000000 | crystals.get(crystal).color();
        if (index < ExaminerLayout.MAX_CRYSTALS) return color;

        int line = (index - ExaminerLayout.MAX_CRYSTALS) % ExaminerLayout.MAX_LINES;
        ExaminerLayout layout = ExaminerLayout.of(state.getValue(ElementTesterBlock.CRYSTALS));
        if (layout == null) return UNLIT;
        return line < layout.litLines(tester.percent(crystals.get(crystal))) ? color : UNLIT;
    }
}
