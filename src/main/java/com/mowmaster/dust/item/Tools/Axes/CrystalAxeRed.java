package com.mowmaster.dust.item.Tools.Axes;

import net.minecraft.item.ItemAxe;


public class CrystalAxeRed extends ItemAxe {

    public CrystalAxeRed(ToolMaterial material){

        super(material);

    }

    public CrystalAxeRed(String crystalaxe_red, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_red);
    }
}
