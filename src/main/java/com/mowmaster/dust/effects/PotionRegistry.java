package com.mowmaster.dust.effects;

import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PotionRegistry
{
    public static final Potion POTION_FLIGHT = new PotionFlight("effect.fly","potionflight");
    public static final Potion POTION_QUICKNESS = new PotionQuickness("effect.quickness","potionquickness");
    public static final Potion POTION_DROWNING = new PotionDrowning("effect.drowning","potiondrowning");
    public static final Potion POTION_ENVIGORATION = new PotionEnvigoration("effect.envigoration","potionenvigoration");
    public static final Potion POTION_MAGNETISM = new PotionMagnetism("effect.magnetism","potionmagnetism");
    public static final Potion POTION_STEPASSIST = new PotionBase("effect.stepassist","potionstepassist",false,3200511,5,0);

    public static final PotionType FLIGHT = new PotionType("flight",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_FLIGHT,3600)}).setRegistryName("flight");
    public static final PotionType QUICKNESS = new PotionType("quickness",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_QUICKNESS,3600)}).setRegistryName("quickness");

    public static final PotionType DROWNING = new PotionType("drowning",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_DROWNING,3600)}).setRegistryName("drowning");
    public static final PotionType MAGNETISM = new PotionType("magnetism",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_MAGNETISM,3600)}).setRegistryName("magnetism");

    public static final PotionType ENVIGORATION = new PotionType("envigoration",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_ENVIGORATION,3600)}).setRegistryName("envigoration");
    public static final PotionType STEPASSIST = new PotionType("stepassist",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_STEPASSIST,3600)}).setRegistryName("stepassist");


    public static void init(){
        ForgeRegistries.POTIONS.register(POTION_FLIGHT);
        ForgeRegistries.POTIONS.register(POTION_QUICKNESS);
        ForgeRegistries.POTIONS.register(POTION_DROWNING);
        ForgeRegistries.POTIONS.register(POTION_ENVIGORATION);
        ForgeRegistries.POTIONS.register(POTION_MAGNETISM);
        ForgeRegistries.POTIONS.register(POTION_STEPASSIST);
    }



    public static void registerPotionTypes()
    {
        ForgeRegistries.POTION_TYPES.register(FLIGHT);
        ForgeRegistries.POTION_TYPES.register(QUICKNESS);
        ForgeRegistries.POTION_TYPES.register(DROWNING);
        ForgeRegistries.POTION_TYPES.register(ENVIGORATION);
        ForgeRegistries.POTION_TYPES.register(MAGNETISM);
        ForgeRegistries.POTION_TYPES.register(STEPASSIST);
    }

}
