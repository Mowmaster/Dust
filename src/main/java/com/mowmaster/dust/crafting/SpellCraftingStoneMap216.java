package com.mowmaster.dust.crafting;

import net.minecraft.item.ItemStack;

public class SpellCraftingStoneMap216
{

    public int colorIn;
    public ItemStack outputBlock;

    public SpellCraftingStoneMap216(int getColor, ItemStack outputDustBlock)
    {
        this.colorIn=getColor;
        this.outputBlock=outputDustBlock;
    }

    public int getInputColor()
    {
        return colorIn;
    }

    public ItemStack getOutput()
    {
        return outputBlock;
    }

    @Override
    public String toString() {
        return "DustStone [input=" + colorIn + ", output=" + outputBlock + "]";
    }
}
