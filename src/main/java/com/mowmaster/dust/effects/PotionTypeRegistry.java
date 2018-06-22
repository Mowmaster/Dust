package com.mowmaster.dust.effects;

import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class PotionTypeRegistry
{
    public static final PotionType FLIGHT = new PotionType("flight",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_FLIGHT,2400)}).setRegistryName("flight");
    //    public static final PotionType FLIGHT = new PotionType("flight",new PotionEffect[]{new PotionEffect(PotionRegistry.POTION_FLIGHT,duration in ticks,amplification, is from a beacon?T/F,show particles T/F)  })

    public static void registerPotionTypes()
    {
        ForgeRegistries.POTION_TYPES.register(FLIGHT);
        PotionHelper.addMix(PotionTypes.AWKWARD, new ItemStack(ItemRegistry.crystal,6).getItem(),FLIGHT);
    }
}
