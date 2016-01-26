package com.mowmaster.dust.block;

import com.mowmaster.dust.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import java.util.Random;


public class BlockOrePurple extends Block {



    public Item drop;
    public int meta;
    public int least;
    public int most;

    // String unloc = unlocalized name
// Material material = block material
// float hardness = time to mine
// float resistance = anti creeper damage (explosion proofness)
    public BlockOrePurple(String unlocalizedName, Material material, float hardness, float resistance, float lightlevel, Item drop, int meta, int least, int most)
    {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightLevel(lightlevel);
        this.setHarvestLevel("pickaxe", 2);
        this.drop = drop;
        this.meta = meta;
        this.least = least;
        this.most = most;

    }

    /*
    public BlockOreRed(String unlocalizedName, Material mat, Item drop, int least, int most)
    {
        this(unlocalizedName, mat, drop, least, most);
    }

    public BlockOreRed(String unlocalizedName, Material mat, Item drop)
    {
        this(unlocalizedName, mat, drop, 1, 1);
    }
    */


    // , Item drop, int meta, int least, int most
    public BlockOrePurple(String unlocalizedName, float hardness, float resistance, float lightlevel, Item drop, int meta, int least, int most)
    // , drop, meta, least, most
    {
        this(unlocalizedName, Material.rock, hardness, resistance, lightlevel, drop, meta, least, most);
    }

    public BlockOrePurple(String unlocalizedName)
    // , ModItems.ItemCrystalRed, 0, 1, 3
    {
        this(unlocalizedName, 20.0f, 10.0f, 0.4f, ModItems.ItemCrystalPurple, 0, 1, 1);
    }

    @Override
    public Item getItemDropped(IBlockState blockState, Random random, int fortune)
    {
        return this.drop;
    }

    @Override
    public int damageDropped(IBlockState blockState)
    {
        return this.meta;
    }
/*
    @Override
    public int quanityDropped(IBlockState blockState, int fortune, Random random)
    {
        if(this.least >= this.most)
            return this.least;
        return this.least + random.nextInt(this.most - this.least + fortune + 1);
    }
*/
}
