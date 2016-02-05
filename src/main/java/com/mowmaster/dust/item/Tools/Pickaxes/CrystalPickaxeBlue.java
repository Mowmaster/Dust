package com.mowmaster.dust.item.Tools.Pickaxes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeBlue extends ItemPickaxe {

    public CrystalPickaxeBlue(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeBlue(String crystalpickaxe_blue, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_blue);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
