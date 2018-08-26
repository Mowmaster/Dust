package com.mowmaster.dust.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


public class EnchantmentSmelt extends Enchantment
{

    public EnchantmentSmelt(Rarity rarityIn, ResourceLocation location) {
        super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
        setName("enchantSmelter");
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

    //NO min max level defaults to 1 only

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack){
        return true;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }
}
