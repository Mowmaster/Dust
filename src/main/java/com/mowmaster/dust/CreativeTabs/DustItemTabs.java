package com.mowmaster.dust.CreativeTabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DustItemTabs extends CreativeModeTab
{
    public DustItemTabs() {
        super("tab_dustitems");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.AMETHYST_SHARD);
    }
}
