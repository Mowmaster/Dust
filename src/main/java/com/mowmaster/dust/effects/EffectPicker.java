package com.mowmaster.dust.effects;

import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.enums.CrystalTypes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.registries.GameData;

public class EffectPicker
{
    static int percentRed;
    static int percentBlue;
    static int percentYellow;
    static int colorToRecipe = 0;
    static int amp = 0;

    private static Boolean immersiveE = false;
    public static PotionEffect getEffectFromInputs(int red, int blue, int yellow, int white, int black, int duration, int potencyCap, Boolean ambient, Boolean showParticles, CrystalTypes.EffectTypes type)
    {
        calcPercentages(red,blue,yellow);
        getPotency(type,white,black,potencyCap);
        getColor(white,black,percentRed,percentBlue,percentYellow);

        while(EffectGetter.instance().hasPotionEffect(colorToRecipe)==false)
        {
            findNextColorForEffect();
            getColor(white,black,percentRed,percentBlue,percentYellow);
        }

        System.out.println(colorToRecipe);
        return getEffect(duration,ambient,showParticles);
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

    private static void getPotency(CrystalTypes.EffectTypes type, int white, int black, int potencyCap)
    {
        int amp = 0;
        if(type.equals(CrystalTypes.EffectTypes.DUST))
        {
            int potencycount=Math.abs(white-black);
            if(potencycount>=0 && potencycount<10){amp=0;}
            if(potencycount>=10 && potencycount<30){amp=1;}
            if(potencycount>=30 && potencycount<60){amp=2;}
            if(potencycount>=60 && potencycount<100){amp=3;}
            if(potencycount>=100 && potencycount<150){amp=4;}
            if(potencycount>=150 && potencycount<210){amp=5;}
            if(potencycount>=210 && potencycount<280){amp=6;}
            if(potencycount>=280 && potencycount<360){amp=7;}
            if(potencycount>=360 && potencycount<450){amp=8;}
            if(potencycount>=450 && potencycount<550){amp=9;}
            if(potencycount>=550){amp=10;}
        }
        else{amp=Math.abs(white-black);}

        if(amp>potencyCap)
        {
            amp=potencyCap;
        }
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

    private static PotionEffect getEffect(int duration, boolean isAmbient, boolean canShowParticles)
    {
        PotionEffect effect = new PotionEffect(EffectGetter.instance().getPotionEffect(colorToRecipe).getPotion(), duration, amp, isAmbient, canShowParticles);
        return effect;
    }


    /*
if(red>=1 && blue==0 && yellow==0){effect=new PotionEffect(MobEffects.STRENGTH ,duration, amp, false, true);}
            else if(blue>=1 && red==0 && yellow==0){effect=new PotionEffect(MobEffects.WATER_BREATHING ,duration, amp, false, true);}
            else if(yellow>=1 && blue==0 && red==0){effect=new PotionEffect(MobEffects.SATURATION ,duration, amp, false, true);}
            else if(blue==0)
            {
                if(yellow!=0 && red!=0)
                {
                    if(red==yellow){effect=new PotionEffect(MobEffects.HASTE,duration, amp, false, true);}//orange
                    else if(red>yellow)
                    {
                        if(percentRed>=50.0 && percentRed<60.0){effect=new PotionEffect(PotionRegistry.POTION_TILLER ,duration, amp, false, true);}
                        if(percentRed>=60.0 && percentRed<70.0){effect=new PotionEffect(MobEffects.JUMP_BOOST ,duration, amp, false, true);}
                        if(percentRed>=70.0 && percentRed<80.0){effect=new PotionEffect(MobEffects.JUMP_BOOST ,duration, amp, false, true);}//red-orange
                        if(percentRed>=80.0 && percentRed<90.0){effect=new PotionEffect(MobEffects.JUMP_BOOST ,duration, amp, false, true);}
                        if(percentRed>=90.0 && percentRed<100.0){effect=new PotionEffect(MobEffects.HEALTH_BOOST ,2*duration, amp, false, true);}
                    }
                    else if(yellow>red)
                    {
                        if(percentYellow>=50.0 && percentYellow<60.0){effect=new PotionEffect(PotionRegistry.POTION_HARVESTER ,duration, amp, false, true);}
                        if(percentYellow>=60.0 && percentYellow<70.0){effect=new PotionEffect(MobEffects.NIGHT_VISION ,duration, amp, false, true);}
                        if(percentYellow>=70.0 && percentYellow<80.0){effect=new PotionEffect(MobEffects.NIGHT_VISION ,duration, amp, false, true);}//yellow-orange
                        if(percentYellow>=80.0 && percentYellow<90.0){effect=new PotionEffect(MobEffects.NIGHT_VISION ,duration, amp, false, true);}
                        if(percentYellow>=90.0 && percentYellow<100.0){effect=new PotionEffect(MobEffects.ABSORPTION ,2*duration, amp, false, true);}
                    }
                }
            }
            else if(red==0)
            {
                if(yellow!=0 && blue!=0)
                {
                    if(blue==yellow){effect=new PotionEffect(MobEffects.REGENERATION,duration, amp, false, true);}//green
                    else if(blue>yellow)
                    {
                        if(percentBlue>=50.0 && percentBlue<60.0){effect=new PotionEffect(PotionRegistry.POTION_PLANTER,duration, amp, false, true);}
                        if(percentBlue>=60.0 && percentBlue<70.0){effect=new PotionEffect(PotionRegistry.POTION_STEPASSIST,duration, amp, false, true);}
                        if(percentBlue>=70.0 && percentBlue<80.0){effect=new PotionEffect(MobEffects.SPEED,duration, amp, false, true);}//Blue-green
                        if(percentBlue>=80.0 && percentBlue<90.0){effect=new PotionEffect(MobEffects.SPEED,duration, amp, false, true);}
                        if(percentBlue>=90.0 && percentBlue<100.0){effect=new PotionEffect(PotionRegistry.POTION_QUICKNESS,duration, amp, false, true);}
                    }
                    else if(yellow>blue)
                    {
                        if(percentYellow>=50.0 && percentYellow<60.0){effect=new PotionEffect(PotionRegistry.POTION_GROWER,duration, amp, false, true);}
                        if(percentYellow>=60.0 && percentYellow<70.0){effect=new PotionEffect(MobEffects.INVISIBILITY,duration, amp, false, true);}
                        if(percentYellow>=70.0 && percentYellow<80.0){effect=new PotionEffect(MobEffects.INVISIBILITY,duration, amp, false, true);}//Yellow-green
                        if(percentYellow>=80.0 && percentYellow<90.0){effect=new PotionEffect(MobEffects.INVISIBILITY,duration, amp, false, true);}
                        if(percentYellow>=90.0 && percentYellow<100.0){effect=new PotionEffect(MobEffects.INVISIBILITY,duration, amp, false, true);}
                    }
                }
            }
            else if(yellow==0)
            {
                if(red!=0 && blue!=0)
                {
                    if(blue==red){effect=new PotionEffect(MobEffects.RESISTANCE,duration, amp, false, true);}//purple
                    else if(blue>red)
                    {
                        if(percentBlue>=50.0 && percentBlue<60.0){effect=new PotionEffect(PotionRegistry.POTION_WATERQUICKNESS ,duration, amp, false, true);}
                        if(percentBlue>=60.0 && percentBlue<70.0){effect=new PotionEffect(MobEffects.FIRE_RESISTANCE ,duration, amp, false, true);}
                        if(percentBlue>=70.0 && percentBlue<80.0){effect=new PotionEffect(MobEffects.FIRE_RESISTANCE ,duration, amp, false, true);}//Blue-Purple
                        if(percentBlue>=80.0 && percentBlue<90.0){effect=new PotionEffect(MobEffects.FIRE_RESISTANCE ,duration, amp, false, true);}
                        if(percentBlue>=90.0 && percentBlue<100.0){effect=new PotionEffect(MobEffects.LUCK ,duration, amp, false, true);}
                    }
                    else if(red>blue)
                    {
                        if(percentRed>=50.0 && percentRed<60.0){effect=new PotionEffect(PotionRegistry.POTION_MAGNETISM ,duration, amp, false, true);}
                        if(percentRed>=60.0 && percentRed<70.0){effect=new PotionEffect(PotionRegistry.POTION_MAGNETISM ,duration, amp, false, true);}
                        if(percentRed>=70.0 && percentRed<80.0){effect=new PotionEffect(PotionRegistry.POTION_MAGNETISM ,duration, amp, false, true);}//Red-Purple also should be invigoration
                        if(percentRed>=80.0 && percentRed<90.0){effect=new PotionEffect(MobEffects.INSTANT_HEALTH ,1, amp, false, true);}
                        if(percentRed>=90.0 && percentRed<100.0){effect=new PotionEffect(MobEffects.INSTANT_HEALTH ,1, amp, false, true);}
                    }
                }
            }
            else if((yellow==blue && blue==red) ){effect=new PotionEffect(PotionRegistry.POTION_FLIGHT,duration, amp, false, true);}
            else if(!(yellow==blue && blue==red) && red>0 && blue>0 && yellow >0){effect=new PotionEffect(PotionRegistry.POTION_SLOWFALL,duration, amp, false, true);}
        }
        else//negative effects
        {



            if(amp>potencyCap)
            {
                amp=potencyCap;
            }

            if(red>=1 && blue==0 && yellow==0){effect=new PotionEffect(MobEffects.WEAKNESS ,duration, amp, false, true);}
            else if(blue>=1 && red==0 && yellow==0){effect=new PotionEffect(PotionRegistry.POTION_DROWNING ,duration, amp, false, true);}
            else if(yellow>=1 && blue==0 && red==0){effect=new PotionEffect(MobEffects.HUNGER ,duration, amp, false, true);}
            else if(blue==0)
            {
                if(yellow!=0 && red!=0)
                {
                    if(red==yellow){effect=new PotionEffect(MobEffects.MINING_FATIGUE,duration, amp, false, true);}//orange
                    else if(red>yellow)
                    {
                        if(percentRed>=50.0 && percentRed<60.0){effect=new PotionEffect(MobEffects.JUMP_BOOST ,duration, 250, false, true);}
                        if(percentRed>=60.0 && percentRed<70.0){effect=new PotionEffect(MobEffects.JUMP_BOOST ,duration, 250, false, true);}
                        if(percentRed>=70.0 && percentRed<80.0){effect=new PotionEffect(MobEffects.JUMP_BOOST ,duration, 250, false, true);}//red-orange
                        if(percentRed>=80.0 && percentRed<90.0){effect=new PotionEffect(MobEffects.JUMP_BOOST ,duration, 250, false, true);}
                        if(percentRed>=90.0 && percentRed<100.0){effect=new PotionEffect(MobEffects.JUMP_BOOST ,duration, 250, false, true);}
                    }
                    else if(yellow>red)
                    {
                        if(percentYellow>=50.0 && percentYellow<60.0){effect=new PotionEffect(MobEffects.BLINDNESS ,duration, amp, false, true);}
                        if(percentYellow>=60.0 && percentYellow<70.0){effect=new PotionEffect(MobEffects.BLINDNESS ,duration, amp, false, true);}
                        if(percentYellow>=70.0 && percentYellow<80.0){effect=new PotionEffect(MobEffects.BLINDNESS ,duration, amp, false, true);}//yellow-orange
                        if(percentYellow>=80.0 && percentYellow<90.0){effect=new PotionEffect(MobEffects.BLINDNESS ,duration, amp, false, true);}
                        if(percentYellow>=90.0 && percentYellow<100.0){effect=new PotionEffect(MobEffects.BLINDNESS ,duration, amp, false, true);}
                    }
                }
            }
            else if(red==0)
            {
                if(yellow!=0 && blue!=0)
                {
                    if(blue==yellow){effect=new PotionEffect(MobEffects.WITHER,duration, amp, false, true);}//green
                    else if(blue>yellow)
                    {
                        if(percentBlue>=50.0 && percentBlue<60.0){effect=new PotionEffect(MobEffects.SLOWNESS,duration, amp, false, true);}
                        if(percentBlue>=60.0 && percentBlue<70.0){effect=new PotionEffect(MobEffects.SLOWNESS,duration, amp, false, true);}
                        if(percentBlue>=70.0 && percentBlue<80.0){if(immersiveE){effect=new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:slippery"),duration, amp, false, true);}
                        else effect=new PotionEffect(MobEffects.SLOWNESS,duration, amp, false, true);}//Blue-green
                        if(percentBlue>=80.0 && percentBlue<90.0){if(immersiveE){effect=new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:sticky"),duration, amp, false, true);}
                        else effect=new PotionEffect(MobEffects.SLOWNESS,duration, amp, false, true);}
                        if(percentBlue>=90.0 && percentBlue<100.0){effect=new PotionEffect(PotionRegistry.POTION_PETRIFIED,duration, amp, false, true);}
                    }
                    else if(yellow>blue)
                    {
                        if(percentYellow>=50.0 && percentYellow<60.0){effect=new PotionEffect(MobEffects.GLOWING,duration, amp, false, true);}
                        if(percentYellow>=60.0 && percentYellow<70.0){effect=new PotionEffect(MobEffects.GLOWING,duration, amp, false, true);}
                        if(percentYellow>=70.0 && percentYellow<80.0){effect=new PotionEffect(MobEffects.GLOWING,duration, amp, false, true);}//Yellow-green
                        if(percentYellow>=80.0 && percentYellow<90.0){if(immersiveE){effect=new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:stunned"),duration, amp, false, true);}
                        else effect=new PotionEffect(MobEffects.GLOWING,duration, amp, false, true);}
                        if(percentYellow>=90.0 && percentYellow<100.0){if(immersiveE){effect=new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:flashed"),duration, amp, false, true);}
                        else effect=new PotionEffect(MobEffects.GLOWING,duration, amp, false, true);}
                    }
                }
            }

            else if(yellow==0)
            {
                if(red!=0 && blue!=0)
                {
                    if(blue==red){effect=new PotionEffect(MobEffects.POISON,duration, amp, false, true);}//purple
                    else if(blue>red)
                    {
                        if(percentBlue>=50.0 && percentBlue<60.0){effect=new PotionEffect(PotionRegistry.POTION_ENVIGORATION ,duration, amp, false, true);}
                        if(percentBlue>=60.0 && percentBlue<70.0){effect=new PotionEffect(PotionRegistry.POTION_ENVIGORATION ,duration, amp, false, true);}
                        if(percentBlue>=70.0 && percentBlue<80.0){
                            if(immersiveE){effect=new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:flammable"),duration, amp, false, true);}
                            else effect=new PotionEffect(PotionRegistry.POTION_ENVIGORATION ,duration, amp, false, true);}//Blue-Purple also should be flamibility
                        if(percentBlue>=80.0 && percentBlue<90.0){effect=new PotionEffect(PotionRegistry.POTION_ENVIGORATION ,duration, amp, false, true);}
                        if(percentBlue>=90.0 && percentBlue<100.0){effect=new PotionEffect(MobEffects.UNLUCK ,duration, amp, false, true);}
                    }
                    else if(red>blue)
                    {
                        if(percentRed>=50.0 && percentRed<60.0){effect=new PotionEffect(MobEffects.NAUSEA ,duration, amp, false, true);}
                        if(percentRed>=60.0 && percentRed<70.0){effect=new PotionEffect(MobEffects.NAUSEA ,duration, amp, false, true);}
                        if(percentRed>=70.0 && percentRed<80.0){if(immersiveE){effect=new PotionEffect(Potion.getPotionFromResourceLocation("immersiveengineering:conductive"),duration, amp, false, true);}
                        else effect=new PotionEffect(MobEffects.NAUSEA ,duration, amp, false, true);}//Red-Purple
                        if(percentRed>=80.0 && percentRed<90.0){effect=new PotionEffect(MobEffects.INSTANT_DAMAGE ,1, amp, false, true);}
                        if(percentRed>=90.0 && percentRed<100.0){effect=new PotionEffect(MobEffects.INSTANT_DAMAGE ,1, amp, false, true);}
                    }
                }
            }
            else if((yellow==blue && blue==red) || red>0 && blue>0 && yellow >0 ){effect=new PotionEffect(MobEffects.LEVITATION,duration, amp, false, true);}//Gravity
     */
}
