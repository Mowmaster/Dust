package com.mowmaster.dust.enchants;

import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnchantmentRange extends Enchantment
{
    public EnchantmentRange() {
        super(Rarity.COMMON, EnchantmentRegistry.COINUPGRADE, new EquipmentSlotType[]{
                EquipmentSlotType.OFFHAND
        });
    }

    public int getMinEnchantability(int level) {
        return 10;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean canApply(ItemStack stack) {
        boolean canApply = false;
        Item coin = stack.getItem();
        if(coin instanceof ItemUpgradeBase)
        {
            canApply = ((ItemUpgradeBase) coin).canAcceptRange();
        }
        return stack.getItem() instanceof ItemUpgradeBase && canApply;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        boolean canApply = false;
        Item coin = stack.getItem();
        if(coin instanceof ItemUpgradeBase)
        {
            canApply = ((ItemUpgradeBase) coin).canAcceptRange();
        }

        return canApply;
    }

    public boolean isAllowedOnBooks() {
        return true;
    }
}

