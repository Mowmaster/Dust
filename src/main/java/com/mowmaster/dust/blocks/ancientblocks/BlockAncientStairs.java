package com.mowmaster.dust.blocks.ancientblocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;




public class BlockAncientStairs extends BlockStairs
{
    public BlockAncientStairs(String unloc, String registryName, IBlockState modelState)
    {
        super (modelState);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(20);
        this.setResistance(20);
        this.setLightOpacity(10);
        this.setCreativeTab(DUSTTABS);
    }
}
