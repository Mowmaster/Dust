package com.mowmaster.dust.items.trinkets;

import com.mowmaster.dust.misc.DustyTab;
import net.minecraft.item.ItemSword;

public class ItemCrystalSword extends ItemSword
{
    public ItemCrystalSword(ToolMaterial material)
    {
        super(material);
        this.setUnlocalizedName(material.name().toLowerCase()+"Sword");
        this.setCreativeTab(DustyTab.DUSTTABS);
        this.setRegistryName("crystalSword");
    }
}
