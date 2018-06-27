package com.mowmaster.dust.blocks;

import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static com.mowmaster.dust.blocks.BlockLeaf.LEAVES;
import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockLog extends Block
{
    //Covers all crystals
    //Using BlockEndRod as an "example"
    public static final PropertyDirection FACING = PropertyDirection.create("facing");


    public BlockLog(String unloc, String registryName)
    {
        super(Material.WOOD);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(2);
        this.setResistance(10);
        this.setLightLevel(5);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setHarvestLevel("axe",1);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return Item.getItemFromBlock(this);
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
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(facing));

        if (iblockstate.getBlock() == this)
        {
            EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);

            if (enumfacing == facing)
            {
                return this.getDefaultState().withProperty(FACING, facing);
            }
        }

        return this.getDefaultState().withProperty(FACING, facing);
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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,3.0F, true);
    }

    public void getDust(World worldIn, BlockPos pos)
    {

        Random rn = new Random();
        int drop = rn.nextInt(10);
        IBlockState state = worldIn.getBlockState(pos);
        if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.RED)))
        {
            if(drop==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1,0)));}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.BLUE)))
        {
            if(drop==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1,1)));}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.YELLOW)))
        {
            if(drop==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1,2)));}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.PURPLE)))
        {
            if(drop==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1,3)));}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.GREEN)))
        {
            if(drop==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1,4)));}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.ORANGE)))
        {
            if(drop==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1,5)));}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.WHITE)))
        {
            if(drop==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1,6)));}
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.BLACK)))
        {
            if(drop==0) {worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(ItemRegistry.dust, 1,7)));}
        }
    }

    public void getSapling(World worldIn, BlockPos pos)
    {

        Random rn = new Random();
        int drop = rn.nextInt(30);
        Item dropped;
        dropped = null;
        IBlockState state = worldIn.getBlockState(pos);
        if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.RED)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingred);}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(dropped, 1)));
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.BLUE)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingblue);}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(dropped, 1)));
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.YELLOW)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingyellow);}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(dropped, 1)));
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.PURPLE)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingpurple);}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(dropped, 1)));
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.ORANGE)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingorange);}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(dropped, 1)));
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.GREEN)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplinggreen);}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(dropped, 1)));
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.WHITE)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingwhite);}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(dropped, 1)));
        }
        else if(state.equals(BlockRegistry.leaf.getDefaultState().withProperty(LEAVES, CrystalBlocks.CrystalLeaves.BLACK)))
        {
            if(drop != 0) {dropped = null;}
            else if(drop==0) {dropped = Item.getItemFromBlock(BlockRegistry.saplingblack);}
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(dropped, 1)));
        }
    }

    public void breakLeaves(World worldIn, BlockPos pos)
    {
        if(!worldIn.isRemote)
        {
            getSapling(worldIn,pos);
            getDust(worldIn,pos);

            if(worldIn.getBlockState(pos).getBlock() instanceof BlockLog)
            {
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.4, pos.getY() + 1.0, pos.getZ() + 0.4, new ItemStack(this, 1)));
            }
            worldIn.setBlockToAir(pos);
        }

    }



    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        int zmin = -3;
        int zmax = +3;
        int xmin = -3;
        int xmax = +3;
        int ymin = 0;
        int ymax = +3;
        if(worldIn.getBlockState(pos.add(0,1,0)).getBlock() instanceof BlockLeaf)
        {

            for(int c=zmin;c<=zmax;c++) {
                for (int a = xmin; a <= xmax; a++)
                    for (int b = ymin; b <= ymax; b++)
                    {
                        if(worldIn.getBlockState(pos.add(a,0,c)).getBlock() instanceof BlockLeaf)
                        {

                            breakLeaves(worldIn,pos.add(a,b,c));
                            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.5F, 1.0F);
                        }
                    }

            }

        }
        else if(worldIn.getBlockState(pos.add(-1,0,1)).getBlock()instanceof BlockLeaf ||
                worldIn.getBlockState(pos.add(0,0,1)).getBlock()instanceof BlockLeaf ||
                worldIn.getBlockState(pos.add(1,0,1)).getBlock()instanceof BlockLeaf ||
                worldIn.getBlockState(pos.add(-1,0,0)).getBlock()instanceof BlockLeaf ||
                worldIn.getBlockState(pos.add(1,0,0)).getBlock()instanceof BlockLeaf ||
                worldIn.getBlockState(pos.add(-1,0,-1)).getBlock()instanceof BlockLeaf ||
                worldIn.getBlockState(pos.add(0,0,-1)).getBlock()instanceof BlockLeaf ||
                worldIn.getBlockState(pos.add(1,0,-1)).getBlock()instanceof BlockLeaf)
        {

            for(int c=zmin;c<=zmax;c++) {
                for (int a = xmin; a <= xmax; a++)
                {
                    if(worldIn.getBlockState(pos.add(a,0,c)).getBlock() instanceof BlockLeaf)
                    {
                        breakLeaves(worldIn,pos.add(a,0,c));
                        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 0.5F, 1.0F);
                    }
                }
            }
        }
    }
}