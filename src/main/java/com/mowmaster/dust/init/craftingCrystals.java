package com.mowmaster.dust.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class craftingCrystals {

    public static void init(){


        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalRed, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalBlue, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 4));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalYellow, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 11));

    }
}
