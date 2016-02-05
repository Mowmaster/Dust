package com.mowmaster.dust.item.Tools.Swords;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemSword;


public class CrystalSwordWhite extends ItemSword {

    public CrystalSwordWhite(ToolMaterial material){

        super(material);


    }

    public CrystalSwordWhite(String crystalsword_white, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalsword_white);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
