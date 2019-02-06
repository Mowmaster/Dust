package com.mowmaster.dust.recipes;


import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.treebits.BlockDustLog;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class SmeltingRecipes
{
    public static void init()
    {
        GameRegistry.addSmelting(BlockDustLog.logred, new ItemStack(ItemRegistry.charcoalRed),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logblue, new ItemStack(ItemRegistry.charcoalBlue),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logyellow, new ItemStack(ItemRegistry.charcoalYellow),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logpurple, new ItemStack(ItemRegistry.charcoalPurple),0.7f);
        GameRegistry.addSmelting(BlockDustLog.loggreen, new ItemStack(ItemRegistry.charcoalGreen),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logorange, new ItemStack(ItemRegistry.charcoalOrange),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logwhite, new ItemStack(ItemRegistry.charcoalWhite),0.7f);
        GameRegistry.addSmelting(BlockDustLog.logblack, new ItemStack(ItemRegistry.charcoalBlack),0.7f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust,1,9),new ItemStack(Items.IRON_INGOT,1),0.35f);
        GameRegistry.addSmelting(new ItemStack(ItemRegistry.dust,1,10),new ItemStack(Items.GOLD_INGOT,1),0.35f);

        for(ItemStack stack : OreDictionary.getOres("ancientCoin")){ GameRegistry.addSmelting(stack, new ItemStack(Items.GOLD_INGOT,1), 0.05f); }
    }
}
