package com.mowmaster.dust.item.Tools.Axes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemAxe;


public class CrystalAxeWhite extends ItemAxe {

    public CrystalAxeWhite(ToolMaterial material){

        super(material);

    }

    public CrystalAxeWhite(String crystalaxe_white, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_white);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
