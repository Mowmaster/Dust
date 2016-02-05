package com.mowmaster.dust.item.Tools.Axes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemAxe;


public class CrystalAxeYellow extends ItemAxe {

    public CrystalAxeYellow(ToolMaterial material){

        super(material);

    }

    public CrystalAxeYellow(String crystalaxe_yellow, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_yellow);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
