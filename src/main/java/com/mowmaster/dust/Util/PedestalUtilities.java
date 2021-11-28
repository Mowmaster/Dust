package com.mowmaster.dust.Util;

import com.mowmaster.dust.Block.Pedestal.BasePedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PedestalUtilities
{
    public static int getRedstoneLevelPedestal(Level worldIn, BlockPos pos)
    {
        int hasItem=0;
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if(blockEntity instanceof BasePedestalBlockEntity) {
            BasePedestalBlockEntity pedestal = ((BasePedestalBlockEntity) blockEntity);
            ItemStack itemstack = pedestal.getItemInPedestal();
            ItemStack coin = pedestal.getCoinOnPedestal();
            /*if(coin.getItem() instanceof IPedestalUpgrade)
            {
                return ((IPedestalUpgrade)coin.getItem()).getComparatorRedstoneLevel(worldIn,pos);
            }*/
            if(!itemstack.isEmpty())
            {
                float f = (float)itemstack.getCount()/(float)itemstack.getMaxStackSize();
                hasItem = (int)Math.floor(f*14.0F)+1;
            }
        }

        return hasItem;
    }
}
