package com.mowmaster.dust.blocks.machines;


import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.items.ItemCoin;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.itemPedestalUpgrades.ipuBasic;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
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
import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;



public class BlockPedestal extends BlockBasic implements ITileEntityProvider//, IItemHandler
{
    public static Block pedestalstone;
    public static Block pedestalred;
    public static Block pedestalblue;
    public static Block pedestalyellow;
    public static Block pedestalpurple;
    public static Block pedestalorange;
    public static Block pedestalgreen;
    public static Block pedestalwhite;
    public static Block pedestalblack;
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

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

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
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TilePedestal)
        {
            TilePedestal tilePedestal = (TilePedestal) tileentity;

            int returner = 0;
            if(entityIn instanceof EntityItem)
            {
                ItemStack incomingItem = ((EntityItem) entityIn).getItem();
                if(tilePedestal.getItemInPedestal().isEmpty())
                {
                    tilePedestal.addItem(incomingItem);
                    returner = incomingItem.getCount();
                    if(incomingItem.getCount()-returner>0)
                    {
                        incomingItem.setCount(incomingItem.getCount()-returner);
                    }
                    else entityIn.setDead();
                }
                else if(tilePedestal.doItemsMatch(tilePedestal.getItemInPedestal(),incomingItem))
                {
                    int leftTillFilled = tilePedestal.roomLeftInStack(tilePedestal.getItemInPedestal());
                    if(leftTillFilled>incomingItem.getCount())
                    {

                        tilePedestal.addItem(incomingItem);
                        returner = incomingItem.getCount();
                    }
                    else
                    {
                        ItemStack copyIncoming = incomingItem.copy();
                        copyIncoming.setCount(leftTillFilled);
                        tilePedestal.addItem(copyIncoming);
                        returner = incomingItem.getCount()-leftTillFilled;
                    }

                    if(incomingItem.getCount()-returner>0)
                    {
                        incomingItem.setCount(incomingItem.getCount()-returner);
                    }
                    else entityIn.setDead();
                }
            }
            else if(entityIn instanceof EntityXPOrb)
            {
                EntityXPOrb getOrb = (EntityXPOrb)entityIn;
                int valueXP = getOrb.getXpValue();
                tilePedestal.addExpToPedestal(valueXP);
                getOrb.setDead();
            }
        }
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
                    if(playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemCoin ||
                            playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ipuBasic)
                    {
                        if(tilePedestal.addCoin(playerIn.getHeldItem(EnumHand.MAIN_HAND)))
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
                                playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(stackSize);
                                return true;
                            }
                        }
                        else return true;
                    }
                }

                else if(playerIn.getHeldItemMainhand().getItem().equals(ItemRegistry.crystalWrench))
                {
                    return false;
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

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return super.canConnectRedstone(state, world, pos, side);
    }

    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    private int getRedstoneLevel(World worldIn, BlockPos pos)
    {
        int hasItem=0;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TilePedestal) {
            TilePedestal pedestal = (TilePedestal) tileEntity;
            int counter = pedestal.getItemInPedestal().getCount();
            if(counter<=0) {hasItem=0;}
            else if(counter>0 && counter<=1) {hasItem=1;}
            else if(counter>1 && counter<=4) {hasItem=2;}//1-4
            else if(counter>4 && counter<=9) {hasItem=3;}//4-9
            else if(counter>9 && counter<=14) {hasItem=4;}//9-14
            else if(counter>14 && counter<=19) {hasItem=5;}//14-19
            else if(counter>19 && counter<=24) {hasItem=6;}//19-24
            else if(counter>24 && counter<=29) {hasItem=7;}//24-29
            else if(counter>29 && counter<=34) {hasItem=8;}//29-34
            else if(counter>34 && counter<=39) {hasItem=9;}//34-39
            else if(counter>39 && counter<=44) {hasItem=10;}//39-44
            else if(counter>44 && counter<=49) {hasItem=11;}//44-49
            else if(counter>49 && counter<=54) {hasItem=12;}//49-54
            else if(counter>54 && counter<=59) {hasItem=13;}//54-59
            else if(counter>59 && counter<=63) {hasItem=14;}//59-63
            else if(counter>63) {hasItem=15;}
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

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        boolean hasRequiredUpgrade = false;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof TilePedestal) {
            TilePedestal pedestal = (TilePedestal) tileEntity;
            hasRequiredUpgrade = pedestal.hasUpgrade(ItemRegistry.enchantUpgrade);
        }
        super.randomDisplayTick(stateIn, worldIn, pos, rand);

        if(hasRequiredUpgrade)
        {
            for (int i = -2; i <= 2; ++i)
            {
                for (int j = -2; j <= 2; ++j)
                {
                    if (i > -2 && i < 2 && j == -1)
                    {
                        j = 2;
                    }

                    if (rand.nextInt(16) == 0)
                    {
                        for (int k = 0; k <= 2; ++k)
                        {
                            BlockPos blockpos = pos.add(i, k, j);

                            if (net.minecraftforge.common.ForgeHooks.getEnchantPower(worldIn, blockpos) > 0)
                            {
                                if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2)))
                                {
                                    break;
                                }

                                worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.0D, (double)pos.getZ() + 0.5D, (double)((float)i + rand.nextFloat()) - 0.5D, (double)((float)k - rand.nextFloat() - 1.0F), (double)((float)j + rand.nextFloat()) - 0.5D);
                            }
                        }
                    }
                }
            }
        }
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

    public static void Init()
    {
        pedestalstone = new BlockPedestal("pedestal_stone","pedestal_stone");
        pedestalred = new BlockPedestal("pedestal_red","pedestal_red");
        pedestalblue = new BlockPedestal("pedestal_blue","pedestal_blue");
        pedestalyellow = new BlockPedestal("pedestal_yellow","pedestal_yellow");
        pedestalpurple = new BlockPedestal("pedestal_purple","pedestal_purple");
        pedestalorange = new BlockPedestal("pedestal_orange","pedestal_orange");
        pedestalgreen = new BlockPedestal("pedestal_green","pedestal_green");
        pedestalwhite = new BlockPedestal("pedestal_white","pedestal_white");
        pedestalblack = new BlockPedestal("pedestal_black","pedestal_black");
    }

    public static void Register()
    {
        registerBlock(pedestalstone);
        registerBlock(pedestalred);
        registerBlock(pedestalblue);
        registerBlock(pedestalyellow);
        registerBlock(pedestalpurple);
        registerBlock(pedestalorange);
        registerBlock(pedestalgreen);
        registerBlock(pedestalwhite);
        registerBlock(pedestalblack);
    }

    public static void RegisterRender()
    {
        registerRender(pedestalstone);
        registerRender(pedestalred);
        registerRender(pedestalblue);
        registerRender(pedestalyellow);
        registerRender(pedestalpurple);
        registerRender(pedestalorange);
        registerRender(pedestalgreen);
        registerRender(pedestalwhite);
        registerRender(pedestalblack);
    }
}
