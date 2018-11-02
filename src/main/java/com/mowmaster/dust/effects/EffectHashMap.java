package com.mowmaster.dust.effects;

import net.minecraft.potion.PotionEffect;

public class EffectHashMap
{

    public int inputColorCode;
    public PotionEffect outputEffect;

    public EffectHashMap(int inputColorCode, PotionEffect outputEffect)
    {
        this.inputColorCode=inputColorCode;
        this.outputEffect=outputEffect;
    }

    public int getInput()
    {
        return inputColorCode;
    }

    public PotionEffect getOutput()
    {
        return outputEffect;
    }

    @Override
    public String toString() {
        return "EffectHashMap [input=" + inputColorCode + ", output=" + outputEffect + "]";
    }
}
