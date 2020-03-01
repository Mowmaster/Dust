package com.mowmaster.dust.recipes.crusher_recipes;

import net.minecraft.item.ItemStack;

public class CrusherHashMap
{

    public ItemStack inputItem;
    public ItemStack outputBlock;

    public CrusherHashMap(ItemStack inputInsertedItem, ItemStack outputDustBlock)
    {
        this.inputItem=inputInsertedItem;
        this.outputBlock=outputDustBlock;
    }

    public ItemStack getInput()
    {
        return inputItem;
    }

    public ItemStack getOutput()
    {
        return outputBlock;
    }

    @Override
    public String toString() {
        return "CrystalCrusher [input=" + inputItem + ", output=" + outputBlock + "]";
    }
}
