package com.mowmaster.dust.misc;

import com.mowmaster.dust.blocks.BlockRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;


public class DustyTab
{
    public static final CreativeTabs DUSTTABS = new CreativeTabs("dusttabs") {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(BlockRegistry.redCrystalFive);
        }
    };
}
