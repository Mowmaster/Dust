package com.mowmaster.dust.misc;

import com.mowmaster.dust.blocks.buildingblocks.BlockDustBasic;
import com.mowmaster.dust.blocks.buildingblocks.BlockLootBlock;
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

    public static final CreativeTabs DUSTCREATIVE = new CreativeTabs("dustcreative") {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(BlockLootBlock.lootblock);
        }
    };
}
