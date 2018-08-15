package com.mowmaster.dust.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


public class EnchantmentLastResort extends Enchantment//Uses xp to Keep player alive
{

    public EnchantmentLastResort(Rarity rarityIn, ResourceLocation location) {
        super(Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] {EntityEquipmentSlot.CHEST});
        setName("enchantLastResort");
        setRegistryName(location);
    }

    @Override
    public int getMinEnchantability(int par1){
        return 30;
    }

    @Override
    public int getMaxEnchantability(int par1){
        return 50;
    }

    @Override
    public int getMinLevel(){
        return 1;
    }

    @Override
    public int getMaxLevel(){
        return 5;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack){
        return true;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

}
