package com.mowmaster.dust.enchants;

import com.mowmaster.dust.item.pedestalUpgrades.ItemUpgradeBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.system.CallbackI;

public class EnchantmentOperationSpeed extends Enchantment
{

    public EnchantmentOperationSpeed() {
        super(Rarity.COMMON, EnchantmentRegistry.COINUPGRADE, new EquipmentSlotType[]{
                EquipmentSlotType.MAINHAND
        });
    }

    public int getMinEnchantability(int level) {
        return level * 5;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean canApply(ItemStack stack) {
        boolean canApply = false;
        Item coin = stack.getItem();
        if(coin instanceof ItemUpgradeBase)
        {
            canApply = ((ItemUpgradeBase) coin).canAcceptOpSpeed();
        }
        return stack.getItem() instanceof ItemUpgradeBase && canApply || super.canApply(stack);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        boolean canApply = false;
        Item coin = stack.getItem();
        if(coin instanceof ItemUpgradeBase)
        {
            canApply = ((ItemUpgradeBase) coin).canAcceptOpSpeed();
        }

        return canApply && super.canApplyAtEnchantingTable(stack);
    }

    public boolean isAllowedOnBooks() {
        return true;
    }
}

