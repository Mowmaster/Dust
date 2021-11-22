package com.mowmaster.dust.CreativeTabs;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class DustBlockTabs extends CreativeModeTab
{
    public DustBlockTabs(String label) {
        super("tab_dustblocks");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Blocks.AMETHYST_BLOCK);
    }
}
