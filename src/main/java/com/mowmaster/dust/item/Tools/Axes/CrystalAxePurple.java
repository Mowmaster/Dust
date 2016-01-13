package com.mowmaster.dust.item.Tools.Axes;

import net.minecraft.item.ItemAxe;


public class CrystalAxePurple extends ItemAxe {

    public CrystalAxePurple(ToolMaterial material){

        super(material);

    }

    public CrystalAxePurple(String crystalaxe_purple, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_purple);
    }
}
