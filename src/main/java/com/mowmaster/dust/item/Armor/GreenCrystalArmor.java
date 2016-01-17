package com.mowmaster.dust.item.Armor;

import net.minecraft.item.ItemArmor;

public class GreenCrystalArmor extends ItemArmor {

    public GreenCrystalArmor(String greencrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(greencrystalarmor);
    }

}
