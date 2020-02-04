package com.mowmaster.dust.items.itemPedestalUpgrades;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class ipuBasicFilter extends ipuBasic
{
    public ipuBasicFilter() {}

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public int getItemEnchantability()
    {
        return 0;
    }

}
