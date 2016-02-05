package com.mowmaster.dust.item.Tools.Pickaxes;

import com.mowmaster.dust.init.dustCreativeTabs;
import net.minecraft.item.ItemPickaxe;


public class CrystalPickaxePurple extends ItemPickaxe {

    public CrystalPickaxePurple(ToolMaterial material){

        super(material);

    }

    public CrystalPickaxePurple(String crystalpickaxe_purple, ToolMaterial material){
        super(material);
        this.setUnlocalizedName(crystalpickaxe_purple);
        this.setCreativeTab(dustCreativeTabs.dustTool);
    }
}
