package com.mowmaster.dust.item.Tools.Axes;

import net.minecraft.item.ItemAxe;


public class CrystalAxeGreen extends ItemAxe {

    public CrystalAxeGreen(ToolMaterial material){

        super(material);

    }

    public CrystalAxeGreen(String crystalaxe_green, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_green);
    }
}
