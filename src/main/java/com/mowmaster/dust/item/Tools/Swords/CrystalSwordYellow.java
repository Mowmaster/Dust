package com.mowmaster.dust.item.Tools.Swords;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemSword;


public class CrystalSwordYellow extends ItemSword {

    public CrystalSwordYellow(ToolMaterial material){

        super(material);


    }

    public CrystalSwordYellow(String crystalsword_yellow, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalsword_yellow);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
