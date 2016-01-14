package com.mowmaster.dust.item.Armor;

import net.minecraft.item.ItemArmor;

public class RedCrystalArmor extends ItemArmor {

    public RedCrystalArmor(String redcrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(redcrystalarmor);
    }

}
