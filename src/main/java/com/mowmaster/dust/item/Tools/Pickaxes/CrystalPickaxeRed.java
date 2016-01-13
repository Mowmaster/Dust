package com.mowmaster.dust.item.Tools.Pickaxes;

import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeRed extends ItemPickaxe {

    public CrystalPickaxeRed(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeRed(String crystalpickaxe_red, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_red);
    }
}
