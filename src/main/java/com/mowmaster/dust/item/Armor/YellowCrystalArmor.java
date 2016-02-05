package com.mowmaster.dust.item.Armor;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemArmor;

public class YellowCrystalArmor extends ItemArmor {

    public YellowCrystalArmor(String yellowcrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(yellowcrystalarmor);
        this.setCreativeTab(dustCreativeTabs.dustArmor);
    }

}
