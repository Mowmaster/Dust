package com.mowmaster.dust.item.Tools.Axes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemAxe;


public class CrystalAxeRed extends ItemAxe {

    public CrystalAxeRed(ToolMaterial material){

        super(material);

    }

    public CrystalAxeRed(String crystalaxe_red, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_red);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
