package com.mowmaster.dust.item.Tools.Swords;

import net.minecraft.item.ItemSword;


public class CrystalSwordGreen extends ItemSword {

    public CrystalSwordGreen(ToolMaterial material){

        super(material);

    }

    public CrystalSwordGreen(String crystalsword_green, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalsword_green);
    }
}
