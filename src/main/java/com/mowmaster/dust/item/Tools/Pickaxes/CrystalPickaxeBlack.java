package com.mowmaster.dust.item.Tools.Pickaxes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeBlack extends ItemPickaxe {

    public CrystalPickaxeBlack(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeBlack(String crystalpickaxe_black, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_black);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
