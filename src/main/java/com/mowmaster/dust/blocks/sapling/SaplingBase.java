package com.mowmaster.dust.blocks.sapling;

import com.mowmaster.dust.configtabs.DustyTab;
import com.mowmaster.dust.enums.TreeTypes;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;


public abstract class SaplingBase extends BlockBush implements IGrowable
{
    public SaplingBase(){
        super();
        setCreativeTab(DustyTab.DUSTTABS);
    }

    public abstract TreeTypes getTreeType();

    private void updateOrGrow(World world, BlockPos pos, IBlockState state, Random rand){
        if (!(world.getBlockState(pos).getBlock() instanceof SaplingBase)) return;
        if (canGrow(world, pos, world.getBlockState(pos), world.isRemote))
            this.grow(world, rand, pos, world.getBlockState(pos));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand){
        if (worldIn.isRemote) return;
        super.updateTick(worldIn, pos, state, rand);
        updateOrGrow(worldIn, pos, state, rand);
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state){
        TreeGenerator gen = new TreeGenerator(true);
        if (!gen.generate(worldIn, rand, pos))
            worldIn.setBlockState(pos, getDefaultState());
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }
}
