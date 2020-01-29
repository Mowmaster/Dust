package com.mowmaster.dust.items.itemPedestalUpgrades;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class ipuBasicExpUpgrade extends ipuBasic
{
    private int maxXP;
    public ipuBasicExpUpgrade() {}

    public void setMaxXP(int value)
    {
        maxXP = value;
    }

    public int getMaxXP()
    {
        return maxXP;
    }
}
