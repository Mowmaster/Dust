package com.mowmaster.dust.enchantments;

import com.mowmaster.dust.dust;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class EnchantmentRegistry
{

    public static EnchantmentDigger enchantDigger = new EnchantmentDigger(Enchantment.Rarity.RARE,new ResourceLocation("dust", "enchantDigger"));
    public static EnchantmentVamperic enchantVamperic = new EnchantmentVamperic(Enchantment.Rarity.RARE,new ResourceLocation("dust", "enchantVamperic"));

    public static void Init(){
        //GameRegistry.register(enchantDigger, );
        ForgeRegistries.ENCHANTMENTS.register(enchantDigger);
        ForgeRegistries.ENCHANTMENTS.register(enchantVamperic);


    }




}
