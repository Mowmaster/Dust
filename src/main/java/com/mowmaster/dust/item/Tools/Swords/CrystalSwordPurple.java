package com.mowmaster.dust.item.Tools.Swords;

import net.minecraft.item.ItemSword;


public class CrystalSwordPurple extends ItemSword {

    public CrystalSwordPurple(ToolMaterial material){

        super(material);

    }

    public CrystalSwordPurple(String crystalsword_purple, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalsword_purple);
    }
}
