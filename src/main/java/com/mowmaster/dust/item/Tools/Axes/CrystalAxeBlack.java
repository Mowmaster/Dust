package com.mowmaster.dust.item.Tools.Axes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemAxe;


public class CrystalAxeBlack extends ItemAxe {

    public CrystalAxeBlack(ToolMaterial material){

        super(material);

    }

    public CrystalAxeBlack(String crystalaxe_black, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_black);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
