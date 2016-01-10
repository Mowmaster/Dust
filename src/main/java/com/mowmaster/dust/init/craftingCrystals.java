package com.mowmaster.dust.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by KingMowmaster on 1/10/2016.
 */
public class craftingCrystals {

    public static void init(){


        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ItemCrystalRed, 1), new ItemStack(Items.iron_ingot), new ItemStack(Items.dye, 1, 1));

    }
}
