package com.mowmaster.dust.effects;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Created by KingMowmaster on 6/22/2018.
 */
public class PotionRegistry
{
    public static final Potion POTION_FLIGHT = new PotionFlight("effect.fly","potionflight");

    public static void init(){
        ForgeRegistries.POTIONS.register(POTION_FLIGHT);
    }
}
