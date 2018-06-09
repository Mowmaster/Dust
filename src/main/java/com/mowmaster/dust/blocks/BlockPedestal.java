package com.mowmaster.dust.blocks;


import com.mowmaster.dust.items.ItemCoin;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;



public class BlockPedestal extends BlockDirectional implements ITileEntityProvider
{
    private static double lengthWidth = 0.625;
    private static double height = 0.75;
    private static AxisAlignedBB CUP = new AxisAlignedBB((1-lengthWidth)/2, 0.0D, (1-lengthWidth)/2, 1-((1-lengthWidth)/2), height, 1-((1-lengthWidth)/2));
    private static AxisAlignedBB CDOWN = new AxisAlignedBB((1-lengthWidth)/2, 1-height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2), 1.0D, 1-((1-lengthWidth)/2));
    private static AxisAlignedBB CNORTH = new AxisAlignedBB((1-lengthWidth)/2, 1-((1-lengthWidth)/2), 1.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, 1-height);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB((1-lengthWidth)/2, 1-((1-lengthWidth)/2), 0.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, height);
    private static AxisAlignedBB CWEST = new AxisAlignedBB(1.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, 1-height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2));
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2));

    public BlockPedestal(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setHardness(2);
        this.setResistance(10);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(SoundType.STONE);
        this.setLightLevel(3f);
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



    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TilePedestal)
        {
            if(!worldIn.isRemote)
            {
                TilePedestal pedestal = (TilePedestal) tileEntity;
                if(pedestal.hasItem()){worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,pedestal.getItemInPedestal()));}
                if(pedestal.hasCoin()){worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,pedestal.getCoinOnPedestal()));}
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TilePedestal)
        {
            if(!worldIn.isRemote)
            {
                TilePedestal pedestal = (TilePedestal) tileEntity;
                if(pedestal.hasItem()){worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,pedestal.getItemInPedestal()));}
                if(pedestal.hasCoin()){worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,pedestal.getCoinOnPedestal()));}
            }
        }
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

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TilePedestal) {
                TilePedestal tilePedestal = (TilePedestal) tileEntity;

                if(playerIn.isSneaking())
                {
                    if (playerIn.getHeldItemMainhand().isEmpty())
                    {
                        if (tilePedestal.hasCoin()) {
                            playerIn.inventory.addItemStackToInventory(tilePedestal.removeCoin());
                        }
                    }
                }

                else if (playerIn.getHeldItemMainhand().isEmpty()) {
                    if (tilePedestal.hasItem()) {
                        playerIn.inventory.addItemStackToInventory(tilePedestal.removeItem());
                    }
                }
                else if (!tilePedestal.hasItem()) {
                    if (tilePedestal.addItem(playerIn.getHeldItem(EnumHand.MAIN_HAND))) {
                        playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
                        return true;
                    }
                }
                else if(!tilePedestal.hasCoin())
                {
                    if(playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemCoin)
                    {
                        if(tilePedestal.addCoin(playerIn.getHeldItem(EnumHand.MAIN_HAND)))
                        {
                            playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
                        }
                    }
                }
            }
        }
        return true;
    }

    private int getRedstoneLevel(World worldIn, BlockPos pos)
    {
        int hasItem=0;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TilePedestal) {
            TilePedestal pedestal = (TilePedestal) tileEntity;
            if(pedestal.hasItem())
            {
                hasItem=15;
            }
            else hasItem=0;
        }
        return hasItem;
    }




    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }


    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return getRedstoneLevel(worldIn,pos);
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
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePedestal();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePedestal();
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("Used To Display Items");
    }
}
