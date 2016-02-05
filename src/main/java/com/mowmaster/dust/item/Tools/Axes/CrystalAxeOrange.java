package com.mowmaster.dust.item.Tools.Axes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemAxe;


public class CrystalAxeOrange extends ItemAxe {

    public CrystalAxeOrange(ToolMaterial material){

        super(material);

    }

    public CrystalAxeOrange(String crystalaxe_orange, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_orange);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
