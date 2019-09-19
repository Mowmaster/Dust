package com.mowmaster.dust.crafting;

import net.minecraft.item.ItemStack;

public class SpellCraftingStoneMap
{

    public ItemStack inputItem;
    public ItemStack outputBlock;

    public SpellCraftingStoneMap(ItemStack inputInsertedItem, ItemStack outputDustBlock)
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
