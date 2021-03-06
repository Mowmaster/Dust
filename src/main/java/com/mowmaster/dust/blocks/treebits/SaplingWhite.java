package com.mowmaster.dust.blocks.treebits;

import com.mowmaster.dust.enums.TreeTypes;
import com.mowmaster.dust.references.Reference;
import net.minecraft.util.ResourceLocation;


public class SaplingWhite extends SaplingBase
{

    public SaplingWhite(String unloc, String registryName)
    {
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
    }
    @Override
    public TreeTypes getTreeType()
    {
        return TreeTypes.WHITE;
    }
}
