package com.mowmaster.dust.item.Tools.Swords;

import net.minecraft.item.ItemSword;


public class CrystalSwordRed extends ItemSword {

    public CrystalSwordRed(ToolMaterial material){

        super(material);

    }

    public CrystalSwordRed(String crystalsword_red, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalsword_red);
    }
}
