package com.mowmaster.dust.items.trinkets;

import com.mowmaster.dust.misc.DustyTab;
import net.minecraft.item.ItemPickaxe;

public class ItemCrystalPickaxe extends ItemPickaxe
{
    public ItemCrystalPickaxe(ToolMaterial material)
    {
        super(material);
        this.setUnlocalizedName(material.name().toLowerCase()+"Pickaxe");
        this.setCreativeTab(DustyTab.DUSTTABS);
        this.setRegistryName("crystalPickaxe");
    }
}
