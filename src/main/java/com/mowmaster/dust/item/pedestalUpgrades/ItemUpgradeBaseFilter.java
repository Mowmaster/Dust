package com.mowmaster.dust.item.pedestalUpgrades;

import com.mowmaster.dust.dust;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class ItemUpgradeBaseFilter extends ItemUpgradeBase {

    public ItemUpgradeBaseFilter(Properties builder) {super(builder.group(dust.itemGroup));}

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
