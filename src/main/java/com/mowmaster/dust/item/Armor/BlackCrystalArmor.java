package com.mowmaster.dust.item.Armor;

import net.minecraft.item.ItemArmor;

public class BlackCrystalArmor extends ItemArmor {

    public BlackCrystalArmor(String blackcrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(blackcrystalarmor);
    }

}
