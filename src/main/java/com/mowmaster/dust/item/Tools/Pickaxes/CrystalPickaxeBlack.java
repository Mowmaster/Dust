package com.mowmaster.dust.item.Tools.Pickaxes;

import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeBlack extends ItemPickaxe {

    public CrystalPickaxeBlack(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeBlack(String crystalpickaxe_black, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_black);
    }
}
