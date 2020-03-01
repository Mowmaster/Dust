package com.mowmaster.dust.enchantments;

import com.mowmaster.dust.items.itemPedestalUpgrades.*;
import net.minecraft.enchantment.*;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;


public class EnchantmentUpgradeRange extends Enchantment
{
    public EnchantmentUpgradeRange() {
        super(Rarity.COMMON, EnumEnchantmentType.ALL, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        this.setName("upgraderange");
        this.setRegistryName("upgraderange");
    }


    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 25;
    }


    @Override
    public boolean isAllowedOnBooks() {
        return super.isAllowedOnBooks();
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if(stack.getItem() instanceof ipuChopper
                || stack.getItem() instanceof ipuDropper
                || stack.getItem() instanceof ipuExpDropper
                || stack.getItem() instanceof ipuPlacer
                || stack.getItem() instanceof ipuBreaker
                || stack.getItem() instanceof ipuBasicEffect
                || stack.getItem() instanceof ipuExpCollector)
        {
            return super.canApply(stack);
        }
        return false;
    }

    @Override
    public boolean canApplyTogether(Enchantment ench)
    {
        return super.canApplyTogether(ench)
                //&& ench == Enchantments.MENDING
                && ench == Enchantments.SILK_TOUCH || ench == Enchantments.MENDING || ench == Enchantments.FORTUNE || ench == EnchantmentRegistry.enchantmentTransferRate;
                //&& ench == Enchantments.FORTUNE;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if(stack.getItem() instanceof ipuBasic)
        {
            return super.canApplyAtEnchantingTable(stack);
        }
        else return false;
    }
}
