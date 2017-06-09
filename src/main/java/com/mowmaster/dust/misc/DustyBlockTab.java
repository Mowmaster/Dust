package com.mowmaster.dust.misc;

import com.mowmaster.dust.blocks.BlockRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Created by KingMowmaster on 6/8/2017.
 */
public class DustyBlockTab
{
    public static final CreativeTabs DUSTBLOCKSTABS = new CreativeTabs("dustblockstabs") {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(BlockRegistry.redbricks);
        }
    };
}
