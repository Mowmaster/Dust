package com.mowmaster.dust.item.Armor;

import net.minecraft.item.ItemArmor;

public class OrangeCrystalArmor extends ItemArmor {

    public OrangeCrystalArmor(String orangecrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(orangecrystalarmor);
    }

}
