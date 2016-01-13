package com.mowmaster.dust.item.Tools.Pickaxes;

import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeGreen extends ItemPickaxe {

    public CrystalPickaxeGreen(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeGreen(String crystalpickaxe_green, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_green);
    }
}
