package com.mowmaster.dust.Features.Spells.Client;

import com.mowmaster.dust.Features.Spells.pylons.CounterPylonBlock;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

// The Blessed and Corrupted Pylons' colours (models/block/pylon/pylon_blessed.json, pylon_corrupted.json), one tint
// source per model tint index:
//   0, 1        the pylon's body and its ring: pale blue-white and gold (blessed), dark red-grey and red (corrupted)
//   the nodes   one per step of the counter (CounterPylonBlock.LEVEL), in order: blessed white, turning blue as the
//               blessing comes; corrupted black, turning red as the corruption does
// The node order is the tint indices in BLESSED_NODES / CORRUPTED_NODES (the corrupted ones left to right along the front).
public class CounterPylonTint implements BlockTintSource
{
    public static final int[] BLESSED_NODES = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    public static final int[] CORRUPTED_NODES = {2, 10, 9, 11, 3};
    public static final int MAX_INDEX = 11;

    private static final int BLESSED_BODY = 0xFFE8F0FF, BLESSED_RING = 0xFFFFD860, BLESSED_OFF = 0xFFFFFFFF, BLESSED_ON = 0xFF3A7BFF;
    private static final int CORRUPTED_BODY = 0xFF4A3434, CORRUPTED_RING = 0xFFB01818, CORRUPTED_OFF = 0xFF1A1A1A, CORRUPTED_ON = 0xFFFF2020;

    private final int index;

    private CounterPylonTint(int index)
    {
        this.index = index;
    }

    // All of them, in tint index order
    public static List<BlockTintSource> all()
    {
        List<BlockTintSource> list = new ArrayList<>();
        for (int i = 0; i <= MAX_INDEX; i++) list.add(new CounterPylonTint(i));
        return list;
    }

    // The colour of tint index `index` at this count (also the item's, at 0)
    public static int colorOf(boolean blessed, int index, int level)
    {
        if (index == 0) return blessed ? BLESSED_BODY : CORRUPTED_BODY;
        if (index == 1) return blessed ? BLESSED_RING : CORRUPTED_RING;
        int[] nodes = blessed ? BLESSED_NODES : CORRUPTED_NODES;
        for (int n = 0; n < nodes.length; n++) {
            if (nodes[n] == index) return n < level ? (blessed ? BLESSED_ON : CORRUPTED_ON) : (blessed ? BLESSED_OFF : CORRUPTED_OFF);
        }
        return 0xFFFFFFFF;
    }

    @Override
    public int color(BlockState state)
    {
        return state.getBlock() instanceof CounterPylonBlock counter ? colorOf(counter.blessed(), index, state.getValue(CounterPylonBlock.LEVEL)) : 0xFFFFFFFF;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos)
    {
        return color(state);
    }
}
