package com.mowmaster.dust.effects;

import com.mowmaster.dust.references.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;


public class PotionEnvigoration extends Potion
{
    public PotionEnvigoration(String potionName, String registryName)
    {
        super(false,15667711);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        setPotionName(potionName);
        setIconIndex(4,0);
    }

}
