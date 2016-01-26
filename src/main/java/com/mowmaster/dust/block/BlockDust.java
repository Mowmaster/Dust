package com.mowmaster.dust.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockDust extends Block
{

// String unloc = unlocalized name
// Material material = block material
// float hardness = time to mine
// float resistance = anti creeper damage (explosion proofness)
    public BlockDust(String unlocalizedName, Material material, float hardness, float resistance, float lightlevel)
    {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightLevel(lightlevel);


    }

    public BlockDust(String unlocalizedName, float hardness, float resistance, float lightlevel)
    {
        this(unlocalizedName, Material.rock, hardness, resistance, lightlevel);
    }

    public BlockDust(String unlocalizedName)
    {
        this(unlocalizedName, 2.0f, 10.0f, 0.0f);
    }


}
