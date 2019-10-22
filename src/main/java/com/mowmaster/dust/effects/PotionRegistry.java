package com.mowmaster.dust.effects;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
    public static final Potion POTION_WATERQUICKNESS = new PotionBase("effect.waterquickness","potionwaterquickness",false,8376831,6,0);
    public static final Potion POTION_PETRIFIED = new PotionBase("effect.petrified","potionpetrified",false,10525595,7,0);
    public static final Potion POTION_SLOWFALL = new PotionBase("effect.slowfall","potionslowfall",false,16248815,8,0);

    public static final Potion POTION_HARVESTER = new PotionBase("effect.harvester","potionharvester",false,16248815,9,0);
    public static final Potion POTION_GROWER = new PotionBase("effect.grower","potiongrower",false,16248815,10,0);
    public static final Potion POTION_TILLER = new PotionBase("effect.tiller","potiontiller",false,11898433,11,0);
    public static final Potion POTION_PLANTER = new PotionBase("effect.planter","potionplanter",false,11898800,12,0);
    //Make a planter effect that uses items off of the ground on tilled soil, might also place blocks but whatever xD
    //public static final Potion POTION_VOIDSTORAGE = new PotionBase("effect.voidstorage","potionvoidstorage",false,4391011,13,0);

    public static final PotionType FLIGHT = new PotionType("flight",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_FLIGHT,3600)}).setRegistryName("flight");
    public static final PotionType QUICKNESS = new PotionType("quickness",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_QUICKNESS,3600)}).setRegistryName("quickness");
    public static final PotionType WATERQUICKNESS = new PotionType("waterquickness",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_WATERQUICKNESS,3600)}).setRegistryName("waterquickness");

    public static final PotionType DROWNING = new PotionType("drowning",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_DROWNING,3600)}).setRegistryName("drowning");
    public static final PotionType MAGNETISM = new PotionType("magnetism",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_MAGNETISM,3600)}).setRegistryName("magnetism");

    public static final PotionType ENVIGORATION = new PotionType("envigoration",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_ENVIGORATION,3600)}).setRegistryName("envigoration");
    public static final PotionType STEPASSIST = new PotionType("stepassist",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_STEPASSIST,3600)}).setRegistryName("stepassist");

    public static final PotionType PETRIFIED = new PotionType("petrified",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_PETRIFIED,3600)}).setRegistryName("petrified");
    public static final PotionType SLOWFALL = new PotionType("slowfall",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_SLOWFALL,3600)}).setRegistryName("slowfall");
    public static final PotionType HARVESTER = new PotionType("harvester",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_HARVESTER,3600)}).setRegistryName("harvester");
    public static final PotionType GROWER = new PotionType("grower",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_GROWER,3600)}).setRegistryName("grower");
    public static final PotionType TILLER = new PotionType("tiller",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_TILLER,3600)}).setRegistryName("tiller");
    public static final PotionType PLANTER = new PotionType("planter",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_PLANTER,3600)}).setRegistryName("planter");
    //public static final PotionType VOIDSTORAGE = new PotionType("voidstorage",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_VOIDSTORAGE,3600)}).setRegistryName("voidstorage");



    public static void init(){
        ForgeRegistries.POTIONS.register(POTION_FLIGHT);
        ForgeRegistries.POTIONS.register(POTION_QUICKNESS);
        ForgeRegistries.POTIONS.register(POTION_WATERQUICKNESS);
        ForgeRegistries.POTIONS.register(POTION_DROWNING);
        ForgeRegistries.POTIONS.register(POTION_ENVIGORATION);
        ForgeRegistries.POTIONS.register(POTION_MAGNETISM);
        ForgeRegistries.POTIONS.register(POTION_STEPASSIST);
        ForgeRegistries.POTIONS.register(POTION_PETRIFIED);
        ForgeRegistries.POTIONS.register(POTION_SLOWFALL);
        ForgeRegistries.POTIONS.register(POTION_HARVESTER);
        ForgeRegistries.POTIONS.register(POTION_GROWER);
        ForgeRegistries.POTIONS.register(POTION_TILLER);
        ForgeRegistries.POTIONS.register(POTION_PLANTER);
        //ForgeRegistries.POTIONS.register(POTION_VOIDSTORAGE);
    }



    public static void registerPotionTypes()
    {
        ForgeRegistries.POTION_TYPES.register(FLIGHT);
        ForgeRegistries.POTION_TYPES.register(QUICKNESS);
        ForgeRegistries.POTION_TYPES.register(WATERQUICKNESS);
        ForgeRegistries.POTION_TYPES.register(DROWNING);
        ForgeRegistries.POTION_TYPES.register(ENVIGORATION);
        ForgeRegistries.POTION_TYPES.register(MAGNETISM);
        ForgeRegistries.POTION_TYPES.register(STEPASSIST);
        ForgeRegistries.POTION_TYPES.register(PETRIFIED);
        ForgeRegistries.POTION_TYPES.register(SLOWFALL);
        ForgeRegistries.POTION_TYPES.register(HARVESTER);
        ForgeRegistries.POTION_TYPES.register(GROWER);
        ForgeRegistries.POTION_TYPES.register(TILLER);
        ForgeRegistries.POTION_TYPES.register(PLANTER);
        //ForgeRegistries.POTION_TYPES.register(VOIDSTORAGE);
    }

}
