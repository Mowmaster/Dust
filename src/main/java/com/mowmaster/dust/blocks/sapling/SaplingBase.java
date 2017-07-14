package com.mowmaster.dust.blocks.sapling;

import com.mowmaster.dust.enums.TreeTypes;
import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.misc.DustyTab;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;


public abstract class SaplingBase extends BlockBush implements IGrowable
{
    private static AxisAlignedBB CUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.9D, 0.75D);


    public SaplingBase(){
        super();
        this.setTickRandomly(true);
        this.setSoundType(SoundType.PLANT);
        setCreativeTab(DustyTab.DUSTBLOCKSTABS);
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

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CUP;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.hasAchievement(AchievementHandler.achievementSapling))
        {
            playerIn.addStat(AchievementHandler.achievementSapling);
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if(placer instanceof EntityPlayer)
        {
            EntityPlayer playerIn = (EntityPlayer) placer;
            if(!playerIn.hasAchievement(AchievementHandler.achievementSapling))
            {
                playerIn.addStat(AchievementHandler.achievementSapling);
            }
        }
    }
}
