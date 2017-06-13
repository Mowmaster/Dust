package com.mowmaster.dust.blocks.sapling;

import com.mowmaster.dust.enums.TreeTypes;
import com.mowmaster.dust.references.Reference;
import net.minecraft.util.ResourceLocation;


public class SaplingBlack extends SaplingBase
{
    public SaplingBlack(String unloc, String registryName)
    {
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
    }

    @Override
    public TreeTypes getTreeType()
    {
        return TreeTypes.BLACK;
    }
}
