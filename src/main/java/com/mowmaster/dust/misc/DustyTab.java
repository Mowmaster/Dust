package com.mowmaster.dust.misc;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasic;
import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasicMeta;
import com.mowmaster.dust.blocks.crystal.BlockCrystal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;


public class DustyTab
{
    public static final CreativeTabs DUSTTABS = new CreativeTabs("dusttabs") {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(BlockCrystal.redCrystalFive);
        }
    };

    public static final CreativeTabs DUSTBLOCKSTABS = new CreativeTabs("dustblockstabs") {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(BlockDustBasic.darksoilbase);
        }
    };
}
