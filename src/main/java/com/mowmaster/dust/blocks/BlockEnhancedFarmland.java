package com.mowmaster.dust.blocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockEnhancedFarmland extends Block
{

    public BlockEnhancedFarmland(String unloc, String registryName)
    {
        super(Material.GRASS);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(2);
        this.setResistance(10);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.GROUND);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setTickRandomly(true);
        this.setHarvestLevel("shovel",2);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public boolean isFertile(World world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return true;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (!world.isRemote)
        {
            IBlockState plantState = world.getBlockState(pos.up());
            Block toBoost = plantState.getBlock();
            for (int i = 0; i < 3; i++)
            {
                plantState = world.getBlockState(pos.up());
                toBoost = plantState.getBlock();
                if (plantState != null && toBoost != null && toBoost != Blocks.AIR && toBoost instanceof IPlantable)
                {
                    toBoost.updateTick(world, pos.up(), plantState, rand);
                }
            }
        }
    }
}
