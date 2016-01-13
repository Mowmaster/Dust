package com.mowmaster.dust.item.Tools.Swords;

import net.minecraft.item.ItemSword;


public class CrystalSwordOrange extends ItemSword {

    public CrystalSwordOrange(ToolMaterial material){

        super(material);

    }

    public CrystalSwordOrange(String crystalsword_orange, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalsword_orange);
    }
}
