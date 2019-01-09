package com.mowmaster.dust.enchantments;

import com.mowmaster.dust.items.ItemCoin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;


public class EnchantmentUpgradeRange extends Enchantment
{
    public EnchantmentUpgradeRange() {
        super(Rarity.COMMON, EnumEnchantmentType.ALL, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        this.setName("rangeupgrade");
        this.setRegistryName("rangeupgrade");
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
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ItemCoin;
    }

    @Override
    public boolean canApplyTogether(Enchantment ench)
    {
        return super.canApplyTogether(ench)
                && ench == Enchantments.MENDING;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {

        return canApply(stack);
    }
}
