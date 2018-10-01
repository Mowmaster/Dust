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

    private static Boolean immersiveE = false;
    public static PotionEffect getEffectFromInputs(int red, int blue, int yellow, int white, int black, int duration, int potencyCap, Boolean ambient, Boolean showParticles, CrystalTypes.EffectTypes type)
    {
        if(Loader.isModLoaded("immersiveengineering")) {immersiveE = true;}

        PotionEffect effect=new PotionEffect(MobEffects.LUCK);
        double totalColor=(red+blue)+yellow;
        double percentRed= 100*((double)red/totalColor);
        double percentBlue= 100*((double)blue/totalColor);
        double percentYellow= 100*((double)yellow/totalColor);

        int amp = 0;
        if(white>black || white==black)//positive effects
        {
            if(type.equals(CrystalTypes.EffectTypes.DUST))
            {
                int whitecount=Math.abs(white-black);
                if(whitecount>=0 && whitecount<10){amp=0;}
                if(whitecount>=10 && whitecount<30){amp=1;}
                if(whitecount>=30 && whitecount<60){amp=2;}
                if(whitecount>=60 && whitecount<100){amp=3;}
                if(whitecount>=100 && whitecount<150){amp=4;}
                if(whitecount>=150 && whitecount<210){amp=5;}
                if(whitecount>=210 && whitecount<280){amp=6;}
                if(whitecount>=280 && whitecount<360){amp=7;}
                if(whitecount>=360 && whitecount<450){amp=8;}
                if(whitecount>=450 && whitecount<550){amp=9;}
                if(whitecount>=550){amp=10;}
            }
            else{amp=Math.abs(white-black);}

            if(amp>potencyCap)
            {
                amp=potencyCap;
            }

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

            if(type.equals(CrystalTypes.EffectTypes.DUST))
            {
                int blackcount=Math.abs(black-white);
                if(blackcount>=1 && blackcount<10){amp=0;}
                if(blackcount>=10 && blackcount<30){amp=1;}
                if(blackcount>=30 && blackcount<60){amp=2;}
                if(blackcount>=60 && blackcount<100){amp=3;}
                if(blackcount>=100 && blackcount<150){amp=4;}
                if(blackcount>=150 && blackcount<210){amp=5;}
                if(blackcount>=210 && blackcount<280){amp=6;}
                if(blackcount>=280 && blackcount<360){amp=7;}
                if(blackcount>=360 && blackcount<450){amp=8;}
                if(blackcount>=450 && blackcount<550){amp=9;}
                if(blackcount>=550){amp=10;}
            }
            else{amp=Math.abs((black-white)-1);}

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


        }

        return effect;
    }


    /*

     */
}
