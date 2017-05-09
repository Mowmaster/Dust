package com.mowmaster.dust.configtabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;


public class DustyTab
{
    public static final CreativeTabs DUSTTABS = new CreativeTabs("dusttabs") {
        @Override
        public Item getTabIconItem()
        {

            return Items.GLASS_BOTTLE;
        }
    };
}
