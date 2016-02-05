package com.mowmaster.dust.item.Tools.Pickaxes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeOrange extends ItemPickaxe {

    public CrystalPickaxeOrange(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeOrange(String crystalpickaxe_orange, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_orange);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
