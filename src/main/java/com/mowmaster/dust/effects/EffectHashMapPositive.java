package com.mowmaster.dust.effects;

import net.minecraft.potion.PotionEffect;

public class EffectHashMapPositive
{

    public int inputColorCode;
    public PotionEffect outputEffect;

    public EffectHashMapPositive(int inputColorCode, PotionEffect outputEffect)
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
        return "EffectHashMapPositive [input=" + inputColorCode + ", output=" + outputEffect + "]";
    }
}
