package com.mowmaster.dust.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class EnchantmentRegistry
{

    public static EnchantmentDigger enchantDigger = new EnchantmentDigger(Enchantment.Rarity.RARE);

    public static void Init(){
        //GameRegistry.register(enchantDigger, new ResourceLocation("dust", "enchantDigger"));

    }




}
