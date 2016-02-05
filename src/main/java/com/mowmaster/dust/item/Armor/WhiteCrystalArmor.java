package com.mowmaster.dust.item.Armor;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemArmor;

public class WhiteCrystalArmor extends ItemArmor {

    public WhiteCrystalArmor(String whitecrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(whitecrystalarmor);
        this.setCreativeTab(dustCreativeTabs.dustArmor);
    }

}
