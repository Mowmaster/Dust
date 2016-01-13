package com.mowmaster.dust.item.Tools.Swords;

import net.minecraft.item.ItemSword;


public class CrystalSwordBlue extends ItemSword {

    public CrystalSwordBlue(ToolMaterial material){

        super(material);

    }

    public CrystalSwordBlue(String crystalsword_blue, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalsword_blue);
    }
}
