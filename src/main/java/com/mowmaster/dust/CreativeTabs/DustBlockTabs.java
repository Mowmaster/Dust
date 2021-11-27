package com.mowmaster.dust.CreativeTabs;

import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DustBlockTabs extends CreativeModeTab
{
    public DustBlockTabs() {
        super("tab_dustblocks");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get());
    }

    public static final DustBlockTabs TAB_BLOCKS = new DustBlockTabs() {};
}
