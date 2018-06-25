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

    public static final PotionType FLIGHT = new PotionType("flight",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_FLIGHT,3600)}).setRegistryName("flight");
    public static final PotionType QUICKNESS = new PotionType("quickness",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_QUICKNESS,3600)}).setRegistryName("quickness");
    public static final PotionType STRONG_QUICKNESS = new PotionType("strong_quickness",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_QUICKNESS,1800,1)}).setRegistryName("strong_quickness");
    public static final PotionType LONG_QUICKNESS = new PotionType("long_quickness",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_QUICKNESS,7200)}).setRegistryName("long_quickness");
    //    public static final PotionType FLIGHT = new PotionType("flight",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_FLIGHT,duration in ticks,amplification, is from a beacon?T/F,show particles T/F)  })

    public static void init(){
        ForgeRegistries.POTIONS.register(POTION_FLIGHT);
        ForgeRegistries.POTIONS.register(POTION_QUICKNESS);
    }



    public static void registerPotionTypes()
    {
        ForgeRegistries.POTION_TYPES.register(FLIGHT);
        PotionHelper.addMix(PotionTypes.AWKWARD, new ItemStack(ItemRegistry.crystal,6).getItem(),FLIGHT);
        ForgeRegistries.POTION_TYPES.register(QUICKNESS);
        ForgeRegistries.POTION_TYPES.register(STRONG_QUICKNESS);
        ForgeRegistries.POTION_TYPES.register(LONG_QUICKNESS);
        PotionHelper.addMix(PotionTypes.STRONG_SWIFTNESS, new ItemStack(ItemRegistry.crystal,1).getItem(),QUICKNESS);
        PotionHelper.addMix(QUICKNESS, new ItemStack(ItemRegistry.crystal,6).getItem(),STRONG_QUICKNESS);
        PotionHelper.addMix(QUICKNESS, new ItemStack(Blocks.REDSTONE_BLOCK).getItem(),LONG_QUICKNESS);
    }

}
