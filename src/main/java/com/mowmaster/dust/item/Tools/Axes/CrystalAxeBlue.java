package com.mowmaster.dust.item.Tools.Axes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemAxe;


public class CrystalAxeBlue extends ItemAxe {

    public CrystalAxeBlue(ToolMaterial material){

        super(material);

    }

    public CrystalAxeBlue(String crystalaxe_blue, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_blue);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
