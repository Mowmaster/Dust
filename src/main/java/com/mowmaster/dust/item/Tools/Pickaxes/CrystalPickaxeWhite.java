package com.mowmaster.dust.item.Tools.Pickaxes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeWhite extends ItemPickaxe {

    public CrystalPickaxeWhite(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeWhite(String crystalpickaxe_white, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_white);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
