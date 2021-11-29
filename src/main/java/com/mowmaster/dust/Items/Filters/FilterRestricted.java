package com.mowmaster.dust.Items.Filters;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.IntStream;

public class FilterRestricted extends BaseFilter{
    public FilterRestricted(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int canAcceptCount(BasePedestalBlockEntity pedestal, ItemStack itemStackIncoming) {
        return super.canAcceptCount(pedestal, itemStackIncoming);
    }
}
