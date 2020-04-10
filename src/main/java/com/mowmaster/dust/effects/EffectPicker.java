package com.mowmaster.dust.effects;

import com.mowmaster.dust.enums.CrystalTypes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class EffectPicker
{
    static int percentRed;
    static int percentBlue;
    static int percentYellow;
    static int colorToRecipe = 0;
    public static PotionEffect feffect = new PotionEffect(Potion.getPotionFromResourceLocation("bloodmagic:flight"));

    public static PotionEffect getEffectFromInputs(int red, int blue, int yellow, int white, int black, int duration, int potencyCap, Boolean ambient, Boolean showParticles, CrystalTypes.EffectTypes type)
    {

        calcPercentages(red,blue,yellow);
        getPotency(type,white,black,potencyCap);
        getColor(white,black,percentRed,percentBlue,percentYellow);


        if(red>0&&blue>0&&yellow>0)
        {
            if(red==blue&&blue==yellow&&yellow==red)
            {
                getEffect(type,white,black,potencyCap,duration,ambient,showParticles);
            }
            else
            {
                return new PotionEffect(PotionRegistry.POTION_SLOWFALL,duration);
            }
        }
        else
        {
            while(EffectGetter.instance().hasPotionEffect(colorToRecipe)==false)
            {
                findNextColorForEffect();
                getColor(white,black,percentRed,percentBlue,percentYellow);
            }
        }


        while(EffectGetter.instance().hasPotionEffect(colorToRecipe)==false)
        {
            findNextColorForEffect();
            getColor(white,black,percentRed,percentBlue,percentYellow);
        }


        //System.out.println(colorToRecipe);
        return getEffect(type,white,black,potencyCap,duration,ambient,showParticles);
    }

    private static void calcPercentages(int red, int blue, int yellow)
    {
        int totalColor=(red+blue)+yellow;
        percentRed = (int)(100*((double)red/totalColor));
        percentBlue = (int)(100*((double)blue/totalColor));
        percentYellow = (int)(100*((double)yellow/totalColor));

        if(percentRed<=0)
        {
            if(percentBlue>percentYellow)
            {
                while(percentBlue+percentYellow!=100)
                {
                    percentBlue++;
                }
            }
            else
            {
                while(percentBlue+percentYellow!=100)
                {
                    percentYellow++;
                }
            }
        }
        else if(percentBlue<=0)
        {
            if(percentRed>percentYellow)
            {
                while(percentRed+percentYellow!=100)
                {
                    percentRed++;
                }
            }
            else
            {
                while(percentRed+percentYellow!=100)
                {
                    percentYellow++;
                }
            }
        }
        else if(percentYellow<=0)
        {
            if(percentBlue>percentRed)
            {
                while(percentBlue+percentRed!=100)
                {
                    percentBlue++;
                }
            }
            else
            {
                while(percentBlue+percentRed!=100)
                {
                    percentRed++;
                }
            }
        }
    }

    private static int getPotency(CrystalTypes.EffectTypes type, int white, int black, int potencyCap)
    {
        int amp = 0;
        if(type.equals(CrystalTypes.EffectTypes.DUST))
        {
            int potencycount=Math.abs(white-black);
            if(potencycount>=0 && potencycount<64){amp=0;}
            if(potencycount>=64 && potencycount<128){amp=1;}
            if(potencycount>=128 && potencycount<192){amp=2;}
            if(potencycount>=192 && potencycount<256){amp=3;}
            if(potencycount>=256 && potencycount<320){amp=4;}
            if(potencycount>=320 && potencycount<384){amp=5;}
            if(potencycount>=384 && potencycount<448){amp=6;}
            if(potencycount>=448 && potencycount<512){amp=7;}
            if(potencycount>=512 && potencycount<576){amp=8;}
            if(potencycount>=576 && potencycount<640){amp=9;}
            if(potencycount>=640){amp=10;}
        }
        else{
            amp=Math.abs(white-black);
            if(black>0 && white<black)
            {
                amp-=1;
            }
        }

        if(amp>potencyCap)
        {
            amp=potencyCap;
        }

        return amp;
    }

    private static void getColor(int white, int black, int percentRed, int percentBlue, int percentYellow)
    {
        if(white>black || white==black)//positive effects
        {
            colorToRecipe = 1000000000;
        }
        else colorToRecipe = 2000000000;

        colorToRecipe += (percentRed*1000000);
        colorToRecipe += (percentBlue*1000);
        colorToRecipe += (percentYellow);
    }


    private static void findNextColorForEffect()
    {
        if(percentRed<=0)
        {
            if(percentBlue>percentYellow)
            {
                percentBlue++;
                percentYellow--;
            }
            else
            {
                percentBlue--;
                percentYellow++;
            }
        }
        else if(percentBlue<=0)
        {
            if(percentRed>percentYellow)
            {
                percentRed++;
                percentYellow--;
            }
            else
            {
                percentRed--;
                percentYellow++;
            }
        }
        else if(percentYellow<=0)
        {
            if(percentBlue>percentRed)
            {
                percentBlue++;
                percentRed--;
            }
            else
            {
                percentBlue--;
                percentRed++;
            }
        }
    }

    private static PotionEffect getEffect(CrystalTypes.EffectTypes type, int white, int black,int potencyCap, int duration, boolean isAmbient, boolean canShowParticles)
    {
        int time = duration;
        if(EffectGetter.instance().getPotionEffect(colorToRecipe).getPotion().isInstant())
        {
            time = 1;
        }
        else if(EffectGetter.instance().getPotionEffect(colorToRecipe).getPotion().equals(MobEffects.SATURATION))
        {
            time = 20*getPotency(type,white,black,potencyCap);
        }

        PotionEffect effect = new PotionEffect(EffectGetter.instance().getPotionEffect(colorToRecipe).getPotion(), time, getPotency(type,white,black,potencyCap), isAmbient, canShowParticles);
        return effect;
    }
}
