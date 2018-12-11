package com.mowmaster.dust.effects;


import com.google.common.collect.Maps;
import net.minecraft.init.MobEffects;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
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
      //this.addSpellEffect(xRRRBBBYYY, new PotionEffect(MobEffects.STRENGTH));x=1 for good effect x=2 for bad effect otherwise its 0-100% of the mix is which of the three colors
        this.addSpellEffect(1100000000, new PotionEffect(MobEffects.STRENGTH));//100%red
        this.addSpellEffect(1099001000, new PotionEffect(MobEffects.INSTANT_HEALTH));
        this.addSpellEffect(1090000010, new PotionEffect(MobEffects.HEALTH_BOOST));
        this.addSpellEffect(1075000025, new PotionEffect(MobEffects.JUMP_BOOST));//red-orange
        this.addSpellEffect(1075025000, new PotionEffect(PotionRegistry.POTION_MAGNETISM));//Red - Purple
        this.addSpellEffect(1060000040, new PotionEffect(PotionRegistry.POTION_TILLER));
        this.addSpellEffect(1050000050, new PotionEffect(MobEffects.HASTE));//orange 50/50
        this.addSpellEffect(1000000100, new PotionEffect(MobEffects.SATURATION));//100% yellow
        this.addSpellEffect(1010000090, new PotionEffect(MobEffects.ABSORPTION));//100% yellow
        this.addSpellEffect(1025000075, new PotionEffect(MobEffects.NIGHT_VISION));//yellow-orange
        this.addSpellEffect(1000025075, new PotionEffect(MobEffects.INVISIBILITY));//green 50/50
        this.addSpellEffect(1040000060, new PotionEffect(PotionRegistry.POTION_HARVESTER));
        this.addSpellEffect(1000040060, new PotionEffect(PotionRegistry.POTION_GROWER));
        this.addSpellEffect(1000050050, new PotionEffect(MobEffects.REGENERATION));//green 50/50
        this.addSpellEffect(1000100000, new PotionEffect(MobEffects.WATER_BREATHING));//100% Blue
        this.addSpellEffect(1000090010, new PotionEffect(MobEffects.SPEED));
        this.addSpellEffect(1005095000, new PotionEffect(MobEffects.LUCK));
        this.addSpellEffect(1000075025, new PotionEffect(PotionRegistry.POTION_QUICKNESS));//Blue - Green
        this.addSpellEffect(1025075000, new PotionEffect(MobEffects.FIRE_RESISTANCE));//Blue - Purple
        this.addSpellEffect(1000070030, new PotionEffect(PotionRegistry.POTION_STEPASSIST));
        this.addSpellEffect(1000060040, new PotionEffect(PotionRegistry.POTION_PLANTER));
        this.addSpellEffect(1040060000, new PotionEffect(PotionRegistry.POTION_WATERQUICKNESS));
        this.addSpellEffect(1050050000, new PotionEffect(MobEffects.RESISTANCE));//purple 50/50

        this.addSpellEffect(1033033033, new PotionEffect(PotionRegistry.POTION_FLIGHT));//3 way split
        this.addSpellEffect(1025025050, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addSpellEffect(1025050025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));
        this.addSpellEffect(1050025025, new PotionEffect(PotionRegistry.POTION_SLOWFALL));




        this.addSpellEffect(2100000000, new PotionEffect(MobEffects.WEAKNESS));//100%red
        this.addSpellEffect(2099001000, new PotionEffect(MobEffects.INSTANT_DAMAGE));
        this.addSpellEffect(2075000025, new PotionEffect(MobEffects.JUMP_BOOST,1,250));//red-orange
        this.addSpellEffect(2050000050, new PotionEffect(MobEffects.MINING_FATIGUE));//orange 50/50
        this.addSpellEffect(2000000100, new PotionEffect(MobEffects.HUNGER));//100% yellow
        this.addSpellEffect(2025000075, new PotionEffect(MobEffects.BLINDNESS));//yellow-orange
        this.addSpellEffect(2000025075, new PotionEffect(MobEffects.GLOWING));
        this.addSpellEffect(2000050050, new PotionEffect(MobEffects.WITHER));//green 50/50
        this.addSpellEffect(2000100000, new PotionEffect(PotionRegistry.POTION_DROWNING));//100% Blue
        this.addSpellEffect(2000090010, new PotionEffect(MobEffects.SLOWNESS));
        this.addSpellEffect(2005095000, new PotionEffect(MobEffects.UNLUCK));
        if(immersiveE){this.addSpellEffect(2000075025, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:slippery")));//Blue - Green
            this.addSpellEffect(2025075000, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:flammable")));//Blue - Purple
            this.addSpellEffect(2040060000, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:conductive")));
            this.addSpellEffect(2000070030, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:sticky")));
            this.addSpellEffect(2000060040, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:stunned")));
            this.addSpellEffect(2000055045, new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:flashed")));}
        this.addSpellEffect(2050050000, new PotionEffect(MobEffects.POISON));//purple 50/50
        this.addSpellEffect(2033033033, new PotionEffect(MobEffects.LEVITATION));
        this.addSpellEffect(2025025050, new PotionEffect(PotionRegistry.POTION_PETRIFIED));
        this.addSpellEffect(2025050025, new PotionEffect(PotionRegistry.POTION_PETRIFIED));
        this.addSpellEffect(2050025025, new PotionEffect(PotionRegistry.POTION_PETRIFIED));
    }


    public void addSpellEffect(int colorIn, PotionEffect effectOut)
    {
        if(colorIn>=1000000000)
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
