package com.mowmaster.dust.blocks;


import com.mojang.authlib.GameProfile;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

public class BlockSpike extends BlockDirectional
{
    private static AxisAlignedBB CUP = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
    private static AxisAlignedBB CDOWN = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    private static AxisAlignedBB CNORTH = new AxisAlignedBB(0.0D, 1.0D, 0.75D, 1.0D, 0.0D, 1.0D);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 0.25D);

    private static AxisAlignedBB CWEST = new AxisAlignedBB(0.75D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 1.0D, 0.0D, 0.25D, 0.0D, 1.0D);

    public BlockSpike(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(3);
        this.setResistance(20);
        this.setSoundType(SoundType.GLASS);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return new ItemStack(BlockRegistry.spike1).getItem();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{FACING});
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(facing.getOpposite()));

        if (iblockstate.getBlock() == this)
        {
            EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);

            if (enumfacing == facing)
            {
                return this.getDefaultState().withProperty(FACING, facing.getOpposite());
            }
        }

        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        if(entityIn instanceof EntityLivingBase)
        {super.canCollideCheck(this.getDefaultState(),true);}
        else {super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, bool);}
    }



    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }



    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();
        state = state.withProperty(FACING, EnumFacing.getFront(meta));
        return state;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (((EnumFacing)state.getValue(FACING)))
        {
            case UP:
            default:
                return CUP;
            case DOWN:
                return CDOWN;
            case NORTH:
                return CNORTH;
            case EAST:
                return CEAST;
            case SOUTH:
                return CSOUTH;
            case WEST:
                return CWEST;
        }
    }
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    public static DamageSource crystalSpikes = new DamageSource("crystal").setMagicDamage();

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {

        if(entityIn instanceof EntityLivingBase) {
            float damage = 0f;
            DamageSource source = DamageSource.WITHER;
            int level = 0;
            if(this.equals(BlockRegistry.spike1)){damage = 1f; source = DamageSource.MAGIC; level = 0;}
            else if(this.equals(BlockRegistry.spike2)){damage = 2f; source = crystalSpikes; level = 1;}
            else if(this.equals(BlockRegistry.spike3)){damage = 3f; source = crystalSpikes; level = 2;}
            else if(this.equals(BlockRegistry.spike4)){damage = 5f; source = crystalSpikes; level = 3;}
            else if(this.equals(BlockRegistry.spike5)){damage = 7f; source = crystalSpikes; level = 4;}
            if(entityIn.fallDistance > 0) {
                damage += entityIn.fallDistance * 1.5f + 2f;
            }
            entityIn.attackEntityFrom(source, damage);
            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, level));
        }
    }



    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        ItemStack spike = new ItemStack(BlockRegistry.spike1);

        if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand),spike))
        {

            if(state.equals(BlockRegistry.spike1.getDefaultState().withProperty(BlockSpike.FACING, facing))){worldIn.setBlockState(pos,BlockRegistry.spike2.getDefaultState().withProperty(BlockSpike.FACING,facing));if(!playerIn.isCreative()) {playerIn.getHeldItem(hand).shrink(1);}}
            else if(state.equals(BlockRegistry.spike2.getDefaultState().withProperty(BlockSpike.FACING, facing))){worldIn.setBlockState(pos,BlockRegistry.spike3.getDefaultState().withProperty(BlockSpike.FACING,facing));if(!playerIn.isCreative()) {playerIn.getHeldItem(hand).shrink(1);}}
            else if(state.equals(BlockRegistry.spike3.getDefaultState().withProperty(BlockSpike.FACING, facing))){worldIn.setBlockState(pos,BlockRegistry.spike4.getDefaultState().withProperty(BlockSpike.FACING,facing));if(!playerIn.isCreative()) {playerIn.getHeldItem(hand).shrink(1);}}
            else if(state.equals(BlockRegistry.spike4.getDefaultState().withProperty(BlockSpike.FACING, facing))){worldIn.setBlockState(pos,BlockRegistry.spike5.getDefaultState().withProperty(BlockSpike.FACING,facing));if(!playerIn.isCreative()) {playerIn.getHeldItem(hand).shrink(1);}}
        }

        return true;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        int drops = 1;
        EnumFacing facing = state.getValue(BlockSpike.FACING);
        if(state.equals(BlockRegistry.spike1.getDefaultState().withProperty(BlockSpike.FACING,facing))) {drops =  1;}
        else if(state.equals(BlockRegistry.spike2.getDefaultState().withProperty(BlockSpike.FACING,facing))) {drops = 2;}
        else if(state.equals(BlockRegistry.spike3.getDefaultState().withProperty(BlockSpike.FACING,facing))) {drops = 3;}
        else if(state.equals(BlockRegistry.spike4.getDefaultState().withProperty(BlockSpike.FACING,facing))) {drops = 4;}
        else if(state.equals(BlockRegistry.spike5.getDefaultState().withProperty(BlockSpike.FACING,facing))) {drops = 5;}

        return drops;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("Spikes damage Entities that touch them");
    }
}
