package com.mowmaster.dust.Items.Filters;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.IntStream;

public class FilterItem extends BaseFilter{
    public FilterItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int canAcceptCount(BasePedestalBlockEntity pedestal, Level world, BlockPos pos, ItemStack itemInPedestal, ItemStack itemStackIncoming) {

        ItemStack filter = pedestal.getFilterInPedestal();
        List<ItemStack> stackCurrent = readFilterQueueFromNBT(filter,getFilterMode(filter));
        int range = stackCurrent.size();

        ItemStack itemFromInv = ItemStack.EMPTY;
        itemFromInv = IntStream.range(0,range)//Int Range
                .mapToObj((stackCurrent)::get)//Function being applied to each interval
                .filter(itemStack -> itemStack.getItem() instanceof FilterRestricted)
                .findFirst().orElse(ItemStack.EMPTY);
        if(itemInPedestal.isEmpty())
        {
            List<ItemStack> stackCurrentRestricted = readFilterQueueFromNBT(itemFromInv,getFilterMode(itemFromInv));
            int rangeRestricted = stackCurrentRestricted.size();
            int count = 0;
            int maxIncomming = itemStackIncoming.getMaxStackSize();
            for(int i=0;i<rangeRestricted;i++)
            {
                count +=stackCurrent.get(i).getCount();
                if(count>=maxIncomming)break;
            }

            return (count>0)?((count>maxIncomming)?(maxIncomming):(count)):(1);
        }

        return 0;
    }

    @Override
    public boolean canAcceptItem(BasePedestalBlockEntity pedestal, ItemStack itemStackIn) {
        boolean filterBool=getFilterType(pedestal.getFilterInPedestal());
        boolean returner = filterBool;

        ItemStack filter = pedestal.getFilterInPedestal();
        List<ItemStack> stackCurrent = readFilterQueueFromNBT(filter,getFilterMode(filter));
        int range = stackCurrent.size();

        ItemStack itemFromInv = ItemStack.EMPTY;
        itemFromInv = IntStream.range(0,range)//Int Range
                .mapToObj((stackCurrent)::get)//Function being applied to each interval
                .filter(itemStack -> itemStack.getItem().equals(itemStackIn.getItem()))
                .findFirst().orElse(ItemStack.EMPTY);

        if(!itemFromInv.isEmpty())
        {
            returner = !filterBool;
        }

        return returner;
    }
}
