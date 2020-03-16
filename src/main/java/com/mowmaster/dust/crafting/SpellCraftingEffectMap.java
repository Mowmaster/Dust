package com.mowmaster.dust.crafting;

import net.minecraft.potion.Effect;

public class SpellCraftingEffectMap
{

    public int colorIn;
    public Effect outputEffect;

    public SpellCraftingEffectMap(int getColor, Effect outputDustEffect)
    {
        this.colorIn=getColor;
        this.outputEffect=outputDustEffect;
    }

    public int getInputColor()
    {
        return colorIn;
    }

    public Effect getOutput()
    {
        return outputEffect;
    }

    @Override
    public String toString() {
        return "DustEffect [input=" + colorIn + ", output=" + outputEffect + "]";
    }
}
