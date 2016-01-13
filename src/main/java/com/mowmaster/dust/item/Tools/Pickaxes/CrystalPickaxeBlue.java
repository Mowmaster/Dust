package com.mowmaster.dust.item.Tools.Pickaxes;

import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxeBlue extends ItemPickaxe {

    public CrystalPickaxeBlue(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxeBlue(String crystalpickaxe_blue, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_blue);
    }
}
