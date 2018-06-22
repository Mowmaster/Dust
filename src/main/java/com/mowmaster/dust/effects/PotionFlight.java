package com.mowmaster.dust.effects;

import com.mowmaster.dust.references.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

/**
 * Created by KingMowmaster on 6/22/2018.
 */
public class PotionFlight extends Potion
{
    public PotionFlight(String potionName, String registryName)
    {
        super(false,8376831);//does it hurt dead mobs?T/F, decimal color or hexadecimal
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        setPotionName(potionName);//"effect.fly"
        setIconIndex(3,2);
    }

}
