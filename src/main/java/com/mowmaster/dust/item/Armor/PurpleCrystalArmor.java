package com.mowmaster.dust.item.Armor;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemArmor;

public class PurpleCrystalArmor extends ItemArmor {

    public PurpleCrystalArmor(String purplecrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(purplecrystalarmor);
        this.setCreativeTab(dustCreativeTabs.dustArmor);
    }

}
