package com.mowmaster.dust.item.Tools.Axes;

import net.minecraft.item.ItemAxe;


public class CrystalAxeBlack extends ItemAxe {

    public CrystalAxeBlack(ToolMaterial material){

        super(material);

    }

    public CrystalAxeBlack(String crystalaxe_black, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_black);
    }
}
