package com.mowmaster.dust.References;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EffectReference
{
    public static final int DEFAULTCOLOR = 16777215;
    public static final List<Integer> DEFAULTCOLORLIST = Arrays.asList(3,3,3);


    public static MobEffect getIntEffect(int color)
    {
        Map<Integer, MobEffect> EFFECT_FROM_INT = Map.ofEntries(
                Map.entry(2763306, MobEffects.WITHER),//Arrays.asList(0,0,0)
                Map.entry(85,getRandomNegativeEffect()),//Arrays.asList(0,0,1)
                Map.entry(170,getRandomNegativeEffect()),//Arrays.asList(0,0,2)
                Map.entry(255,getRandomNegativeEffect()),//Arrays.asList(0,0,3)
                Map.entry(21760,MobEffects.UNLUCK),//Arrays.asList(0,1,0)
                Map.entry(21845,getRandomNegativeEffect()),//Arrays.asList(0,1,1)
                Map.entry(21930,getRandomNegativeEffect()),//Arrays.asList(0,1,2)
                Map.entry(22015,MobEffects.HERO_OF_THE_VILLAGE),//Arrays.asList(0,1,3)

                Map.entry(43520,getRandomNegativeEffect()),//Arrays.asList(0,2,0)
                Map.entry(43605,MobEffects.INVISIBILITY),//Arrays.asList(0,2,1)
                Map.entry(43690,MobEffects.CONDUIT_POWER),//Arrays.asList(0,2,2)
                Map.entry(43775,MobEffects.WATER_BREATHING),//Arrays.asList(0,2,3)
                Map.entry(65280,getRandomNegativeEffect()),//Arrays.asList(0,3,0)
                Map.entry(65365,MobEffects.JUMP),//Arrays.asList(0,3,1)
                Map.entry(65450,getRandomNegativeEffect()),//Arrays.asList(0,3,2)
                Map.entry(65535,MobEffects.DOLPHINS_GRACE),//Arrays.asList(0,3,3)

                Map.entry(5570560,MobEffects.HARM),//Arrays.asList(1,0,0)
                Map.entry(5570645,MobEffects.NIGHT_VISION),//Arrays.asList(1,0,1)
                Map.entry(5570730,getRandomNegativeEffect()),//Arrays.asList(1,0,2)
                Map.entry(5570815,getRandomNegativeEffect()),//Arrays.asList(1,0,3)
                Map.entry(5592320,MobEffects.CONFUSION),//Arrays.asList(1,1,0)
                Map.entry(5592405,MobEffects.BLINDNESS),//Arrays.asList(1,1,1)
                Map.entry(5592490,getRandomNegativeEffect()),//Arrays.asList(1,1,2)
                Map.entry(5592575,getRandomNegativeEffect()),//Arrays.asList(1,1,3)

                Map.entry(5614080,MobEffects.POISON),//Arrays.asList(1,2,0)
                Map.entry(5614165,getRandomNegativeEffect()),//Arrays.asList(1,2,1)
                Map.entry(5614250,MobEffects.MOVEMENT_SLOWDOWN),//Arrays.asList(1,2,2)
                Map.entry(5614335,getRandomNegativeEffect()),//Arrays.asList(1,2,3)
                Map.entry(5635840,MobEffects.LUCK),//Arrays.asList(1,3,0)
                Map.entry(5635925,getRandomNegativeEffect()),//Arrays.asList(1,3,1)
                Map.entry(5636010,getRandomNegativeEffect()),//Arrays.asList(1,3,2)
                Map.entry(5636095,MobEffects.MOVEMENT_SPEED),//Arrays.asList(1,3,3)

                Map.entry(11141120,getRandomNegativeEffect()),//Arrays.asList(2,0,0)
                Map.entry(11141205,MobEffects.BAD_OMEN),//Arrays.asList(2,0,1)
                Map.entry(11141290,getRandomNegativeEffect()),//Arrays.asList(2,0,2)
                Map.entry(11141375,MobEffects.DAMAGE_RESISTANCE),//Arrays.asList(2,0,3)
                Map.entry(11162880,MobEffects.DIG_SLOWDOWN),//Arrays.asList(2,1,0)
                Map.entry(11162965,getRandomNegativeEffect()),//Arrays.asList(2,1,1)
                Map.entry(11163050,getRandomNegativeEffect()),//Arrays.asList(2,1,2)
                Map.entry(11163135,getRandomNegativeEffect()),//Arrays.asList(2,1,3)

                Map.entry(11184640,MobEffects.HUNGER),//Arrays.asList(2,2,0)
                Map.entry(11184725,getRandomNegativeEffect()),//Arrays.asList(2,2,1)
                Map.entry(11184810,MobEffects.WEAKNESS),//Arrays.asList(2,2,2)
                Map.entry(11184895,getRandomNegativeEffect()),//Arrays.asList(2,2,3)
                Map.entry(11206400,MobEffects.ABSORPTION),//Arrays.asList(2,3,0)
                Map.entry(11206485,getRandomNegativeEffect()),//Arrays.asList(2,3,1)
                Map.entry(11206570,getRandomNegativeEffect()),//Arrays.asList(2,3,2)
                Map.entry(11206655,MobEffects.LEVITATION),//Arrays.asList(2,3,3)

                Map.entry(16711680,MobEffects.DAMAGE_BOOST),//Arrays.asList(3,0,0)
                Map.entry(16711765,getRandomNegativeEffect()),//Arrays.asList(3,0,1)
                Map.entry(16711850,MobEffects.HEALTH_BOOST),//Arrays.asList(3,0,2)
                Map.entry(16711935,getRandomNegativeEffect()),//Arrays.asList(3,0,3)
                Map.entry(16733440,MobEffects.DIG_SPEED),//Arrays.asList(3,1,0)
                Map.entry(16733525,getRandomNegativeEffect()),//Arrays.asList(3,1,1)
                Map.entry(16733610,MobEffects.REGENERATION),//Arrays.asList(3,1,2)
                Map.entry(16733695,MobEffects.HEAL),//Arrays.asList(3,1,3)

                Map.entry(16755200,MobEffects.FIRE_RESISTANCE),//Arrays.asList(3,2,0)
                Map.entry(16755285,getRandomNegativeEffect()),//Arrays.asList(3,2,1)
                Map.entry(16755370,getRandomNegativeEffect()),//Arrays.asList(3,2,2)
                Map.entry(16755455,getRandomNegativeEffect()),//Arrays.asList(3,2,3)
                Map.entry(16776960,MobEffects.SATURATION),//Arrays.asList(3,3,0)
                Map.entry(16777045,MobEffects.GLOWING),//Arrays.asList(3,3,1)
                Map.entry(16777130,MobEffects.SLOW_FALLING),//Arrays.asList(3,3,2)
                Map.entry(16777215,getRandomNegativeEffect())//Arrays.asList(3,3,3)
        );

        return EFFECT_FROM_INT.getOrDefault(color,getRandomNegativeEffect());
    }


    public static MobEffect getRandomNegativeEffect()
    {
        Random rand = new Random();
        Map<Integer, MobEffect> NEGEFFECT = Map.ofEntries(
                Map.entry(0, MobEffects.BAD_OMEN),
                Map.entry(1,MobEffects.BLINDNESS),
                Map.entry(2,MobEffects.GLOWING),
                Map.entry(3,MobEffects.HUNGER),
                Map.entry(4,MobEffects.HARM),
                Map.entry(5,MobEffects.LEVITATION),
                Map.entry(6,MobEffects.DIG_SLOWDOWN),
                Map.entry(7,MobEffects.CONFUSION),
                Map.entry(8,MobEffects.POISON),
                Map.entry(9,MobEffects.MOVEMENT_SLOWDOWN),
                Map.entry(10,MobEffects.UNLUCK),
                Map.entry(11,MobEffects.WEAKNESS),
                Map.entry(12,MobEffects.WITHER)
        );

        return NEGEFFECT.getOrDefault(rand.nextInt(13),MobEffects.HUNGER);
    }
}
