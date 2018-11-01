package com.mowmaster.dust.effects;


import com.google.common.collect.Maps;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

import java.util.Map;

public class EffectGetterNegative
{
    private static final EffectGetterNegative EFFECT_BASE = new EffectGetterNegative();
    private final Map<Integer, EffectHashMapNegative> effectList = Maps.<Integer, EffectHashMapNegative>newHashMap();
    public static EffectGetterNegative instance()
    {
        return EFFECT_BASE;
    }


    private EffectGetterNegative()
    {
        this.addPositiveEffect(1100000000, new PotionEffect(MobEffects.STRENGTH));//100%red
        this.addPositiveEffect(1090000010, new PotionEffect(MobEffects.HEALTH_BOOST));//90% red 10% yellow
        this.addPositiveEffect(1080000020, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addPositiveEffect(1070000030, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addPositiveEffect(1060000040, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addPositiveEffect(1050000050, new PotionEffect(MobEffects.HASTE));//50 r 50y
        this.addPositiveEffect(1040000060, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addPositiveEffect(1030000070, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addPositiveEffect(1020000080, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addPositiveEffect(1010000090, new PotionEffect(MobEffects.HEALTH_BOOST));//10%red 90%yellow
        this.addPositiveEffect(1000000100, new PotionEffect(MobEffects.SATURATION));//100% yellow
        this.addPositiveEffect(1000010090, new PotionEffect(MobEffects.SATURATION));
        this.addPositiveEffect(1000020080, new PotionEffect(MobEffects.SATURATION));
        this.addPositiveEffect(1000030070, new PotionEffect(MobEffects.SATURATION));
        this.addPositiveEffect(1000040060, new PotionEffect(MobEffects.SATURATION));
        this.addPositiveEffect(1000050050, new PotionEffect(MobEffects.REGENERATION));//50y 50b
        this.addPositiveEffect(1000060040, new PotionEffect(MobEffects.SATURATION));
        this.addPositiveEffect(1000070030, new PotionEffect(MobEffects.SATURATION));
        this.addPositiveEffect(1000080020, new PotionEffect(MobEffects.SATURATION));
        this.addPositiveEffect(1000090010, new PotionEffect(MobEffects.SATURATION));
        this.addPositiveEffect(1000100000, new PotionEffect(MobEffects.WATER_BREATHING));//100% Blue
        this.addPositiveEffect(1010090000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addPositiveEffect(1020080000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addPositiveEffect(1030070000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addPositiveEffect(1040060000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addPositiveEffect(1050050000, new PotionEffect(MobEffects.RESISTANCE));//50r 50b
        this.addPositiveEffect(1060040000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addPositiveEffect(1070030000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addPositiveEffect(1080020000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addPositiveEffect(1090010000, new PotionEffect(MobEffects.WATER_BREATHING));
        //this.addPositiveEffect(1100000000, new PotionEffect(MobEffects.STRENGTH));//100%red

        this.addPositiveEffect(1033033033, new PotionEffect(PotionRegistry.POTION_FLIGHT));//3 way split
        this.addPositiveEffect(1025025050, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addPositiveEffect(1025050025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addPositiveEffect(1050025025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
    }


    public void addPositiveEffect(int colorIn, PotionEffect effectOut)
    {
        if(colorIn>=1000000000 && colorIn<=1100000000)
        {
            this.effectList.put(colorIn,new EffectHashMapNegative(colorIn,effectOut));;
        }
        else return;
    }

    public boolean hasPotionEffect(int getColorIn)
    {
        for (Map.Entry<Integer, EffectHashMapNegative> entry : this.effectList.entrySet())
        {
            if (getColorIn == entry.getKey())
            {
                return true;
            }
        }

        return false;
    }

    public PotionEffect getPotionEffect(int getColorIn)
    {
        for (Map.Entry<Integer, EffectHashMapNegative> entry : this.effectList.entrySet())
        {
            if (getColorIn == entry.getKey())
            {
                return entry.getValue().getOutput();
            }
        }

        return null;
    }
}
