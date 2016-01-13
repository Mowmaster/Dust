package com.mowmaster.dust.item.Tools.Swords;

import net.minecraft.item.ItemSword;


public class CrystalSwordBlack extends ItemSword {

    public CrystalSwordBlack(ToolMaterial material){

        super(material);

    }

    public CrystalSwordBlack(String crystalsword_black, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalsword_black);
    }
}
