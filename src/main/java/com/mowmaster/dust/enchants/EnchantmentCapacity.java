package com.mowmaster.dust.enchants;

import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnchantmentCapacity extends Enchantment
{

    public EnchantmentCapacity() {
        super(Rarity.RARE, EnchantmentRegistry.COINUPGRADE, new EquipmentSlotType[]{
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
            canApply = ((ItemUpgradeBase) coin).canAcceptCapacity();
        }
        return stack.getItem() instanceof ItemUpgradeBase && canApply;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        boolean canApply = false;
        Item coin = stack.getItem();
        if(coin instanceof ItemUpgradeBase)
        {
            canApply = ((ItemUpgradeBase) coin).canAcceptCapacity();
        }

        return canApply && super.canApplyAtEnchantingTable(stack);
    }

    public boolean isAllowedOnBooks() {
        return true;
    }
}

