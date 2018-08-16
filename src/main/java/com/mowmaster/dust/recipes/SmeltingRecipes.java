package com.mowmaster.dust.recipes;


import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class SmeltingRecipes
{
    public static void init()
    {
        GameRegistry.addSmelting(BlockRegistry.logred, new ItemStack(ItemRegistry.charcoalRed),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logblue, new ItemStack(ItemRegistry.charcoalBlue),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logyellow, new ItemStack(ItemRegistry.charcoalYellow),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logpurple, new ItemStack(ItemRegistry.charcoalPurple),0.7f);
        GameRegistry.addSmelting(BlockRegistry.loggreen, new ItemStack(ItemRegistry.charcoalGreen),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logorange, new ItemStack(ItemRegistry.charcoalOrange),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logwhite, new ItemStack(ItemRegistry.charcoalWhite),0.7f);
        GameRegistry.addSmelting(BlockRegistry.logblack, new ItemStack(ItemRegistry.charcoalBlack),0.7f);

        for(ItemStack stack : OreDictionary.getOres("ancientCoin")){ GameRegistry.addSmelting(stack, new ItemStack(Items.GOLD_INGOT,1), 0.05f); }
    }
}
