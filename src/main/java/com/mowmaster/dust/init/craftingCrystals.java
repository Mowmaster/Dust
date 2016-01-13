package com.mowmaster.dust.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class craftingCrystals {

    public static void init(){


        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalRed, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalBlue, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 4));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalYellow, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 11));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalGreen, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalOrange, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 14));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalPurple, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 5));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalBlack, 1), new ItemStack(Items.diamond), new ItemStack(Items.dye, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalWhite, 1), new ItemStack(Items.diamond), new ItemStack(Items.dye, 1, 15));

    }
}
