package com.mowmaster.dust.item.Armor;

import net.minecraft.item.ItemArmor;

public class BlueCrystalArmor extends ItemArmor {

    public BlueCrystalArmor(String bluecrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(bluecrystalarmor);
    }

}
