package com.mowmaster.dust.recipes;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;


public class BrewingRecipes
{
    public static void registerPotionRecipes()
    {
        PotionHelper.addMix(PotionTypes.STRONG_SWIFTNESS, new ItemStack(ItemRegistry.crystal,1).getItem(), PotionRegistry.QUICKNESS);
        PotionHelper.addMix(PotionRegistry.QUICKNESS, new ItemStack(ItemRegistry.crystal,6).getItem(),PotionRegistry.STRONG_QUICKNESS);
        PotionHelper.addMix(PotionRegistry.QUICKNESS, new ItemStack(Blocks.REDSTONE_BLOCK).getItem(),PotionRegistry.LONG_QUICKNESS);
    }
}
