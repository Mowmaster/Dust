package com.mowmaster.dust.blocks;


import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;




public class BlockLeaf  extends Block implements IMetaBlockName
{
    public static final PropertyEnum LEAVES = PropertyEnum.create("leaves",CrystalBlocks.CrystalLeaves.class);
    protected boolean leavesFancy;



    public BlockLeaf(String unloc)
    {
        super(Material.LEAVES);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.RED));
        this.setHardness(0.001F);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(DUSTBLOCKSTABS);
    }


    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{LEAVES});
    }


    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        int blockmeta = placer.getHeldItem(EnumHand.MAIN_HAND).getMetadata();
        return getStateFromMeta(blockmeta);
    }



    @Override
    public int getMetaFromState(IBlockState state)
    {
        CrystalBlocks.CrystalLeaves leaves = (CrystalBlocks.CrystalLeaves) state.getValue(LEAVES);
        return leaves.getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LEAVES,CrystalBlocks.CrystalLeaves.values()[meta]);
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < CrystalBlocks.CrystalLeaves.values().length; i++)
        {
            list.add(new ItemStack(itemIn,1,i));
        }
    }

    public String getSpecialName(ItemStack stack)
    {
        return CrystalBlocks.CrystalLeaves.values()[stack.getItemDamage()].getName();
    }


    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(this),1,getMetaFromState(world.getBlockState(pos)));
    }

    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        int meta = this.getMetaFromState(state);
        Random rn = new Random();
        int drop = rn.nextInt(10);
        if(!worldIn.isRemote)
        {
            if(drop == 0)
            {
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1, meta)));
            }
        }

    }
    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,1.0F, true);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        Random rn = new Random();
        int drop = rn.nextInt(30);
        Item dropped;
        dropped = null;
        if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.RED)))
        {

            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingred);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.BLUE)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingblue);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.YELLOW)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingyellow);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.PURPLE)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingpurple);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.ORANGE)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingorange);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.GREEN)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplinggreen);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.WHITE)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingwhite);}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.BLACK)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingblack);}
        }
        return dropped;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }



    /*
    public boolean isOpaqueCube(IBlockState state)
    {
        return !this.leavesFancy;
    }

    @SideOnly(Side.CLIENT)
    public void setGraphicsLevel(boolean fancy)
    {
        this.leavesFancy = fancy;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return this.leavesFancy ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return !this.leavesFancy && blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
     */
}