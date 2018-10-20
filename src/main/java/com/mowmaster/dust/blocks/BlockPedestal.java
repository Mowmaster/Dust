package com.mowmaster.dust.blocks;


import com.mowmaster.dust.items.ItemCoin;
import com.mowmaster.dust.items.ItemCrystalWrench;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalFurnace;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;



public class BlockPedestal extends BlockDirectional implements ITileEntityProvider//, IItemHandler
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

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TilePedestal)
        {
            InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(),((TilePedestal) tileentity).getItemInPedestal());
            InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(),((TilePedestal) tileentity).getCoinOnPedestal());
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
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
                else if(!tilePedestal.hasCoin())
                {
                    if(playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemCoin)
                    {
                        if(tilePedestal.addCoin(new ItemStack(playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem(),1)))
                        {
                            playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
                        }
                    }
                    else if (playerIn.getHeldItemMainhand().isEmpty()) {
                        if (tilePedestal.hasItem()) {
                            playerIn.inventory.addItemStackToInventory(tilePedestal.removeItem());
                        }
                    }
                    else
                    {
                        int stackSize = tilePedestal.getMaxStackSize()-tilePedestal.getItemInPedestal().getCount();
                        if(tilePedestal.doItemsMatch(playerIn.getHeldItem(hand)))
                        {
                            if (tilePedestal.addItem(playerIn.getHeldItem(EnumHand.MAIN_HAND)))
                            {
                                System.out.println("");
                                playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(stackSize);
                                return true;
                            }
                        }
                        else return true;
                    }
                }

                else if (playerIn.getHeldItemMainhand().isEmpty()) {
                    if (tilePedestal.hasItem()) {
                        playerIn.inventory.addItemStackToInventory(tilePedestal.removeItem());
                    }
                }
                else
                {
                    int stackSize = tilePedestal.getMaxStackSize()-tilePedestal.getItemInPedestal().getCount();
                    if(tilePedestal.doItemsMatch(playerIn.getHeldItem(hand)))
                    {
                        if (tilePedestal.addItem(playerIn.getHeldItem(EnumHand.MAIN_HAND)))
                        {
                            System.out.println("");
                            playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(stackSize);
                            return true;
                        }
                    }
                    else return true;
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
            int counter = pedestal.getItemInPedestal().getCount();
            if(counter<=0) {hasItem=0;}
            else if(counter>0 && counter<=5) {hasItem=1;}
            else if(counter>5 && counter<=9) {hasItem=2;}
            else if(counter>9 && counter<=13) {hasItem=3;}
            else if(counter>13 && counter<=17) {hasItem=4;}
            else if(counter>17 && counter<=21) {hasItem=5;}
            else if(counter>21 && counter<=25) {hasItem=6;}
            else if(counter>25 && counter<=29) {hasItem=7;}
            else if(counter>29 && counter<=33) {hasItem=8;}
            else if(counter>33 && counter<=37) {hasItem=9;}
            else if(counter>37 && counter<=41) {hasItem=10;}
            else if(counter>41 && counter<=45) {hasItem=11;}
            else if(counter>45 && counter<=49) {hasItem=12;}
            else if(counter>49 && counter<=53) {hasItem=13;}
            else if(counter>53 && counter<=57) {hasItem=14;}
            else if(counter>57) {hasItem=15;}
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


    /*
    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {

        return getStackInSlot(0);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return extractItem(0,amount,false);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(stack.getItem().equals(Items.WHEAT))
        {
            return insertItem(0, stack.copy(), false);
        }
        else return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public int getSlots() {
        return 0;
    }
     */
}
