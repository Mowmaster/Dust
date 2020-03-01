package com.mowmaster.dust.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class EnchantmentCopy extends Enchantment
{
    public EnchantmentCopy() {
        super(Rarity.COMMON, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
        this.setName("fancy_feet");
        this.setRegistryName("fancy_feet");

    }

    @Override
    public int getMaxLevel() {
        return 3;
    }


    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 15;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.FEET;
    }

    /*
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
     */

    @Override
    public boolean canApplyTogether(Enchantment ench)
    {
        return super.canApplyTogether(ench)
                && ench == Enchantments.DEPTH_STRIDER
                && ench == Enchantments.FROST_WALKER
                && ench == Enchantments.FEATHER_FALLING
                && ench == Enchantments.PROTECTION
                && ench == Enchantments.FIRE_PROTECTION
                && ench == Enchantments.THORNS
                && ench == Enchantments.MENDING
                && ench == Enchantments.BLAST_PROTECTION;
    }



    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }
}
