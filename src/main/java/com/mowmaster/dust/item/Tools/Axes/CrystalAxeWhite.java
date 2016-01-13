package com.mowmaster.dust.item.Tools.Axes;

import net.minecraft.item.ItemAxe;


public class CrystalAxeWhite extends ItemAxe {

    public CrystalAxeWhite(ToolMaterial material){

        super(material);

    }

    public CrystalAxeWhite(String crystalaxe_white, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_white);
    }
}
