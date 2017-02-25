package com.mowmaster.dust.items;

import com.mowmaster.dust.references.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import static com.mowmaster.dust.configtabs.DustyTab.DUSTTABS;

/**
 * Created by KingMowmaster on 2/25/2017.
 */
public class ItemMod extends Item
{
    public ItemMod(String unlocName,String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setCreativeTab(DUSTTABS);
    }
}
