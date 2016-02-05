package com.mowmaster.dust.item.Armor;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemArmor;

public class BlackCrystalArmor extends ItemArmor {

    public BlackCrystalArmor(String blackcrystalarmor, ArmorMaterial material, int renderIndex, int armorType)
    {
        super(material, renderIndex, armorType);

        this.setUnlocalizedName(blackcrystalarmor);
        this.setCreativeTab(dustCreativeTabs.dustArmor);
    }

}
