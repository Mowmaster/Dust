package com.mowmaster.dust.CreativeTabs;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DustItemTabs extends CreativeModeTab
{
    public DustItemTabs() {
        super("tab_dustitems");
    }

    public static final DustItemTabs TAB_ITEMS = new DustItemTabs() {};

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(DeferredRegisterItems.COLOR_APPLICATOR.get());
    }
}
