package com.mowmaster.dust.item.Tools.Axes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemAxe;


public class CrystalAxePurple extends ItemAxe {

    public CrystalAxePurple(ToolMaterial material){

        super(material);

    }

    public CrystalAxePurple(String crystalaxe_purple, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalaxe_purple);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
