package com.mowmaster.dust.blocks;

import com.mowmaster.dust.configtabs.DustyTab;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

import static com.mowmaster.dust.configtabs.DustyTab.DUSTTABS;

/**
 * Created by KingMowmaster on 3/15/2017.
 */
public class BlockBasic extends Block
{
    public BlockBasic(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(20);
        this.setResistance(20);
        this.setLightOpacity(10);
        this.setCreativeTab(DUSTTABS);
    }

    @Override
    public Item getItemDropped(IBlockState state,Random random,int fortune)
    {
        return Item.getItemFromBlock(this);
    }


}
