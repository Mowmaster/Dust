package com.mowmaster.dust.effects;

import com.mowmaster.dust.references.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;


public class PotionMagnetism extends Potion
{
    public PotionMagnetism(String potionName, String registryName)
    {
        super(false,1393400);//does it hurt dead mobs?T/F, decimal color or hexadecimal
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        setPotionName(potionName);
        setIconIndex(0,0);
    }

}
