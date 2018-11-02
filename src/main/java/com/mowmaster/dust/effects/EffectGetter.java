package com.mowmaster.dust.effects;


import com.google.common.collect.Maps;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Loader;

import java.util.Map;

public class EffectGetter
{
    private static final EffectGetter EFFECT_BASE = new EffectGetter();
    private final Map<Integer, EffectHashMap> effectList = Maps.<Integer, EffectHashMap>newHashMap();
    public static EffectGetter instance()
    {
        return EFFECT_BASE;
    }
    private boolean immersiveE=false;


    private EffectGetter()
    {
        if(Loader.isModLoaded("immersiveengineering")) {immersiveE = true;}

        this.addSpellEffect(1100000000, new PotionEffect(MobEffects.STRENGTH));//100%red
        this.addSpellEffect(1090000010, new PotionEffect(MobEffects.HEALTH_BOOST));//90% red 10% yellow
        this.addSpellEffect(1080000020, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1070000030, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1060000040, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1050000050, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1040000060, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1030000070, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1020000080, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1010000090, new PotionEffect(MobEffects.HEALTH_BOOST));//10%red 90%yellow
        this.addSpellEffect(1000000100, new PotionEffect(MobEffects.SATURATION));//100% yellow
        this.addSpellEffect(1000010090, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(1000020080, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(1000030070, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(1000040060, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(1000050050, new PotionEffect(MobEffects.SATURATION));//
        this.addSpellEffect(1000060040, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(1000070030, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(1000080020, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(1000090010, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(1000100000, new PotionEffect(MobEffects.WATER_BREATHING));//100% Blue
        this.addSpellEffect(1010090000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(1020080000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(1030070000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(1040060000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(1050050000, new PotionEffect(MobEffects.WATER_BREATHING));//
        this.addSpellEffect(1060040000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(1070030000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(1080020000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(1090010000, new PotionEffect(MobEffects.WATER_BREATHING));
        //this.addSpellEffect(1100000000, new PotionEffect(MobEffects.STRENGTH));//100%red

        this.addSpellEffect(1033033033, new PotionEffect(PotionRegistry.POTION_FLIGHT));//3 way split
        this.addSpellEffect(1025025050, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addSpellEffect(1025050025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addSpellEffect(1050025025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));





        this.addSpellEffect(2100000000, new PotionEffect(MobEffects.STRENGTH));//100%red
        this.addSpellEffect(2090000010, new PotionEffect(MobEffects.HEALTH_BOOST));//90% red 10% yellow
        this.addSpellEffect(2080000020, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(2070000030, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(2060000040, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(2050000050, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(2040000060, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(2030000070, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(2020000080, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(2010000090, new PotionEffect(MobEffects.HEALTH_BOOST));//10%red 90%yellow
        this.addSpellEffect(2000000100, new PotionEffect(MobEffects.SATURATION));//100% yellow
        this.addSpellEffect(2000010090, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(2000020080, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(2000030070, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(2000040060, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(2000050050, new PotionEffect(MobEffects.SATURATION));//
        this.addSpellEffect(2000060040, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(2000070030, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(2000080020, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(2000090010, new PotionEffect(MobEffects.SATURATION));
        this.addSpellEffect(2000100000, new PotionEffect(MobEffects.WATER_BREATHING));//100% Blue
        this.addSpellEffect(2010090000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(2020080000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(2030070000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(2040060000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(2050050000, new PotionEffect(MobEffects.WATER_BREATHING));//
        this.addSpellEffect(2060040000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(2070030000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(2080020000, new PotionEffect(MobEffects.WATER_BREATHING));
        this.addSpellEffect(2090010000, new PotionEffect(MobEffects.WATER_BREATHING));
        //this.addSpellEffect(2100000000, new PotionEffect(MobEffects.STRENGTH));//100%red

        this.addSpellEffect(2033033033, new PotionEffect(PotionRegistry.POTION_FLIGHT));//3 way split
        this.addSpellEffect(2025025050, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addSpellEffect(2025050025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addSpellEffect(2050025025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
    }


    public void addSpellEffect(int colorIn, PotionEffect effectOut)
    {
        if(colorIn>=1000000000 && colorIn<=2100000000)
        {
            this.effectList.put(colorIn,new EffectHashMap(colorIn,effectOut));;
        }
        else return;
    }

    public boolean hasPotionEffect(int getColorIn)
    {
        for (Map.Entry<Integer, EffectHashMap> entry : this.effectList.entrySet())
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
        for (Map.Entry<Integer, EffectHashMap> entry : this.effectList.entrySet())
        {
            if (getColorIn == entry.getKey())
            {
                return entry.getValue().getOutput();
            }
        }

        return null;
    }
}
