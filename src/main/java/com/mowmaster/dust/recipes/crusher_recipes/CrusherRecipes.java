package com.mowmaster.dust.recipes.crusher_recipes;


import com.google.common.collect.Maps;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class CrusherRecipes
{
    private static final CrusherRecipes CRUSHER_RECIPES = new CrusherRecipes();
    private final HashMap<ItemStack,ItemStack> crusherList = new HashMap();
    public static CrusherRecipes instance()
    {
        return CRUSHER_RECIPES;
    }


    private CrusherRecipes()
    {
        this.crusherList.put(new ItemStack(ItemRegistry.crystal,1,0),new ItemStack(BlockRegistry.redDust,8));
        this.crusherList.put(new ItemStack(ItemRegistry.crystal,1,1),new ItemStack(BlockRegistry.blueDust,8));
        this.crusherList.put(new ItemStack(ItemRegistry.crystal,1,2),new ItemStack(BlockRegistry.yellowDust,8));
        this.crusherList.put(new ItemStack(ItemRegistry.crystal,1,3),new ItemStack(BlockRegistry.purpleDust,8));
        this.crusherList.put(new ItemStack(ItemRegistry.crystal,1,4),new ItemStack(BlockRegistry.greenDust,8));
        this.crusherList.put(new ItemStack(ItemRegistry.crystal,1,5),new ItemStack(BlockRegistry.orangeDust,8));
        this.crusherList.put(new ItemStack(ItemRegistry.crystal,1,6),new ItemStack(BlockRegistry.whiteDust,8));
        this.crusherList.put(new ItemStack(ItemRegistry.crystal,1,7),new ItemStack(BlockRegistry.blackDust,8));

        this.crusherList.put(new ItemStack(Blocks.REDSTONE_ORE,1),new ItemStack(BlockRegistry.redstoneDust,4));
        this.crusherList.put(new ItemStack(Blocks.IRON_ORE,1),new ItemStack(BlockRegistry.ironDust,2));
        this.crusherList.put(new ItemStack(Blocks.GOLD_ORE,1),new ItemStack(BlockRegistry.goldDust,2));
        this.crusherList.put(new ItemStack(Blocks.REEDS,1),new ItemStack(BlockRegistry.sugarDust,2));
        this.crusherList.put(new ItemStack(Items.WHEAT,1),new ItemStack(BlockRegistry.wheatDust,1));
        this.crusherList.put(new ItemStack(Items.POTATO,1),new ItemStack(BlockRegistry.potatoDust,1));
        this.crusherList.put(new ItemStack(Items.POISONOUS_POTATO,1),new ItemStack(BlockRegistry.potatoDust,1));
        this.crusherList.put(new ItemStack(Items.BLAZE_ROD,1),new ItemStack(BlockRegistry.blazeDust,3));
    }


    public void addCrusherRecipe(ItemStack inputItem, ItemStack outputBlock)
    {
        this.crusherList.put(inputItem,outputBlock);
    }

    public boolean hasOutput(ItemStack inputItem)
    {
        /*
        if(!crusherList.get(inputItem).isEmpty())
        {
            return true;
        }
         */
        System.out.println(crusherList.get(inputItem).getDisplayName());

        return false;
    }

    public ItemStack getOutput(ItemStack inputItem)
    {
        if(hasOutput(inputItem))
        {
            return crusherList.get(inputItem);
        }

        return ItemStack.EMPTY;
    }
}
