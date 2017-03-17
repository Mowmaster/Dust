package com.mowmaster.dust.recipes;

import com.mowmaster.dust.enums.CrystalItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static com.mowmaster.dust.items.ItemRegistry.crystal;
import static com.mowmaster.dust.items.ItemRegistry.dust;

/**
 * Created by KingMowmaster on 3/1/2017.
 */
public class CraftingRecipes
{
    public static void ICraftingRecipes()
    {
        for(int i =0; i < CrystalItems.DustTypes.values().length; i++)
        {
            GameRegistry.addShapelessRecipe(new ItemStack(dust,1,i),new ItemStack(crystal,1,i));

        }
    }




}