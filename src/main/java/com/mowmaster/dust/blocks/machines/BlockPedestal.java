package com.mowmaster.dust.blocks.machines;

import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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

    public static Block pedestalstoneunlit;
    public static Block pedestalredunlit;
    public static Block pedestalblueunlit;
    public static Block pedestalyellowunlit;
    public static Block pedestalpurpleunlit;
    public static Block pedestalorangeunlit;
    public static Block pedestalgreenunlit;
    public static Block pedestalwhiteunlit;
    public static Block pedestalblackunlit;

    private static double lengthWidth = 0.625;
    private static double height = 0.75;
    private static AxisAlignedBB CUP = new AxisAlignedBB((1-lengthWidth)/2, 0.0D, (1-lengthWidth)/2, 1-((1-lengthWidth)/2), height, 1-((1-lengthWidth)/2));
    private static AxisAlignedBB CDOWN = new AxisAlignedBB((1-lengthWidth)/2, 1-height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2), 1.0D, 1-((1-lengthWidth)/2));
    private static AxisAlignedBB CNORTH = new AxisAlignedBB((1-lengthWidth)/2, 1-((1-lengthWidth)/2), 1.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, 1-height);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB((1-lengthWidth)/2, 1-((1-lengthWidth)/2), 0.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, height);
    private static AxisAlignedBB CWEST = new AxisAlignedBB(1.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, 1-height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2));
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 1-((1-lengthWidth)/2), (1-lengthWidth)/2, height, (1-lengthWidth)/2, 1-((1-lengthWidth)/2));

    public BlockPedestal(String unloc, String registryName, float lightLevel)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setHardness(2);
        this.setResistance(10);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(SoundType.STONE);
        this.setLightLevel(lightLevel);
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

            if(!worldIn.isRemote)
            {
                Item coinInPed = tilePedestal.getCoinOnPedestal().getItem();
                if(coinInPed instanceof ipuBasic)
                {
                    ((ipuBasic) coinInPed).actionOnColideWithBlock(worldIn, tilePedestal, pos, state, entityIn);
                }
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
            TilePedestal pedestal = (TilePedestal)tileentity;


            ItemStack pedestalItemToDrop = new ItemStack(Item.getItemFromBlock(state.getBlock()),1);
            if(pedestal.getPedestalTransferAmount()>0)
            {
                PotionEffect effect = new PotionEffect(PotionRegistry.POTION_VOIDSTORAGE,1 ,pedestal.getPedestalTransferAmount()-1);
                NBTTagCompound cmpd = new NBTTagCompound();
                cmpd.setTag("coineffect",effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
                pedestalItemToDrop.setTagCompound(cmpd);
            }

            if(pedestal.getPedestalTransferSpeed()>0)
            {
                pedestalItemToDrop.addEnchantment(EnchantmentRegistry.enchantmentTransferRate, pedestal.getPedestalTransferSpeed());
            }

            InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(),pedestal.getItemInPedestal());
            InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(),pedestal.getCoinOnPedestal());
            InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(),pedestalItemToDrop);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        ItemStack noStack = ItemStack.EMPTY;
        return noStack.getItem();
    }

    public PotionEffect getCoinEffect(ItemStack stack)
    {
        PotionEffect potionEffect = new PotionEffect(MobEffects.LUCK);


        if(hasCoinEffect(stack))
        {
            potionEffect = PotionEffect.readCustomPotionEffectFromNBT(stack.getTagCompound().getCompoundTag("coineffect"));
        }

        return potionEffect;
    }

    public boolean hasEffect;

    public boolean hasCoinEffect(ItemStack stack)
    {
        hasEffect = false;
        if(stack.hasTagCompound())
        {
            hasEffect = stack.getTagCompound().hasKey("coineffect");
        }
        return hasEffect;
    }

    public int getPotency(ItemStack stack)
    {
        return getCoinEffect(stack).getAmplifier()+1;
    }

    public int intRate = 0;

    public int getRateModifier(Potion effect, ItemStack stack)
    {
        if(hasCoinEffect(stack))
        {
            if(getCoinEffect(stack).getPotion().equals(effect))
            {
                intRate = getPotency(stack);
            }
        }
        else intRate = 0;

        return intRate;
    }

    public int getTransferRateModifier(ItemStack stack)
    {
        int rate = 0;
        if(stack.isItemEnchanted())
        {
            rate = EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.enchantmentTransferRate,stack);
        }
        return rate;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TilePedestal)
        {
            TilePedestal tilePedestal = (TilePedestal) tileentity;

            if(!worldIn.isRemote)
            {
                tilePedestal.setPedestalTransferAmount(getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack));
                tilePedestal.setPedestalTransferSpeed(getTransferRateModifier(stack));
            }
        }
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
                        int availableSpace = tilePedestal.canAcceptItems(playerIn.getHeldItem(hand));
                        if(availableSpace>0)
                        {
                            if (tilePedestal.addItem(playerIn.getHeldItem(EnumHand.MAIN_HAND)))
                            {
                                playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(availableSpace);
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
                    int availableSpace = tilePedestal.canAcceptItems(playerIn.getHeldItem(hand));
                    if(availableSpace>0)
                    {
                        if (tilePedestal.addItem(playerIn.getHeldItem(EnumHand.MAIN_HAND)))
                        {
                            playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(availableSpace);
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

    public int getItemTransferRate(ItemStack stack)
    {
        int itemRate = 4;
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack ))
        {
            case 0:
                itemRate = 4;
                break;
            case 1:
                itemRate=8;
                break;
            case 2:
                itemRate = 16;
                break;
            case 3:
                itemRate = 32;
                break;
            case 4:
                itemRate = 48;
                break;
            case 5:
                itemRate=64;
                break;
            default: itemRate=4;
        }

        return  itemRate;
    }

    public int getOperationSpeed(ItemStack stack)
    {
        int speed = 20;
        switch (getTransferRateModifier(stack))
        {
            case 0:
                speed = 20;//normal speed
                break;
            case 1:
                speed=10;//2x faster
                break;
            case 2:
                speed = 5;//4x faster
                break;
            case 3:
                speed = 3;//6x faster
                break;
            case 4:
                speed = 2;//10x faster
                break;
            case 5:
                speed=1;//20x faster
                break;
            default: speed=20;
        }

        return  speed;
    }

    @Override
    public int hasBookEntry(ItemStack getBook)
    {
        int returner = 0;

        if(getBook.hasTagCompound())
        {
            NBTTagCompound bookList = getBook.getTagCompound();
            if(bookList.hasKey("booklist"))
            {
                String strList = bookList.getString("booklist");
                if(strList.contains("blockpedestal"))
                {
                    returner = 1;
                }
            }
        }

        return  returner;
    }

    @Override
    public boolean addBookEntry(ItemStack getBook)
    {
        boolean returner = false;

        if(getBook.hasTagCompound())
        {
            NBTTagCompound bookList = getBook.getTagCompound();
            if(bookList.hasKey("booklist"))
            {
                String strList = bookList.getString("booklist");
                strList = strList + ",blockpedestal";
                bookList.setString("booklist",strList );
            }
            else
            {
                String strList = "blockpedestal";
                bookList.setString("booklist",strList );
            }


            ItemStack itemIcon = new ItemStack(BlockPedestal.pedestalstone);
            bookList.setTag("blockpedestalIcon",itemIcon.serializeNBT() );
            String strTitle = "Pedestals";
            bookList.setString("blockpedestalTitle",strTitle );
            String strEntry = "Pedestals have a few uses in dust, 1. They can be used to display items. " +
                    "2. You can slot upgrades into them to make them do work for you. " +
                    "3. Finally you can link them together to transport items around your base.";
            bookList.setString("blockpedestalEntry",strEntry );

            getBook.setTagCompound(bookList);
            returner = true;


        }
        else
        {
            NBTTagCompound bookList = new NBTTagCompound();
            String strList = "blockpedestal";
            bookList.setString("booklist",strList );

            ItemStack itemIcon = new ItemStack(BlockPedestal.pedestalstone);
            bookList.setTag("blockpedestalIcon",itemIcon.serializeNBT() );
            String strTitle = "Pedestals";
            bookList.setString("blockpedestalTitle",strTitle );
            String strEntry = "Pedestals have a few uses in dust, 1. They can be used to display items. " +
                    "2. You can slot upgrades into them to make them do work for you. " +
                    "3. Finally you can link them together to transport items around your base.";
            bookList.setString("blockpedestalEntry",strEntry );

            getBook.setTagCompound(bookList);
            returner = true;
        }

        return  returner;
    }



    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        int s2 = getItemTransferRate(stack);
        String s3 = "";

        switch (getOperationSpeed(stack))
        {
            case 1:
                s3 = "20x Faster";
                break;
            case 2:
                s3="10x Faster";
                break;
            case 3:
                s3 = "6x Faster";
                break;
            case 5:
                s3 = "4x Faster";
                break;
            case 10:
                s3 = "2x Faster";
                break;
            case 20:
                s3="Normal Speed";
                break;
            default: s3="Normal Speed";
        }


        String tr = "" + s2 + "";
        String trr = s3;
        tooltip.add("Item Stack Import Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add("Transfer Rate: " + tr);
            }
            else
            {
                tooltip.add("Transfer Rate: 1");
            }

            if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
            {
                tooltip.add("Transfer Speed: " + trr);
            }
            else
            {
                tooltip.add("Transfer Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Transfer Rate: 1");
            tooltip.add("Transfer Speed: Normal Speed");
        }
    }



    public static void Init()
    {
        pedestalstone = new BlockPedestal("pedestal_stone","pedestal_stone",3f);
        pedestalred = new BlockPedestal("pedestal_red","pedestal_red",3f);
        pedestalblue = new BlockPedestal("pedestal_blue","pedestal_blue",3f);
        pedestalyellow = new BlockPedestal("pedestal_yellow","pedestal_yellow",3f);
        pedestalpurple = new BlockPedestal("pedestal_purple","pedestal_purple",3f);
        pedestalorange = new BlockPedestal("pedestal_orange","pedestal_orange",3f);
        pedestalgreen = new BlockPedestal("pedestal_green","pedestal_green",3f);
        pedestalwhite = new BlockPedestal("pedestal_white","pedestal_white",3f);
        pedestalblack = new BlockPedestal("pedestal_black","pedestal_black",3f);

        pedestalstoneunlit = new BlockPedestal("pedestal_stone_unlit","pedestal_stone_unlit",0f);
        pedestalredunlit = new BlockPedestal("pedestal_red_unlit","pedestal_red_unlit",0f);
        pedestalblueunlit = new BlockPedestal("pedestal_blue_unlit","pedestal_blue_unlit",0f);
        pedestalyellowunlit = new BlockPedestal("pedestal_yellow_unlit","pedestal_yellow_unlit",0f);
        pedestalpurpleunlit = new BlockPedestal("pedestal_purple_unlit","pedestal_purple_unlit",0f);
        pedestalorangeunlit = new BlockPedestal("pedestal_orange_unlit","pedestal_orange_unlit",0f);
        pedestalgreenunlit = new BlockPedestal("pedestal_green_unlit","pedestal_green_unlit",0f);
        pedestalwhiteunlit = new BlockPedestal("pedestal_white_unlit","pedestal_white_unlit",0f);
        pedestalblackunlit = new BlockPedestal("pedestal_black_unlit","pedestal_black_unlit",0f);
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

        registerBlock(pedestalstoneunlit);
        registerBlock(pedestalredunlit);
        registerBlock(pedestalblueunlit);
        registerBlock(pedestalyellowunlit);
        registerBlock(pedestalpurpleunlit);
        registerBlock(pedestalorangeunlit);
        registerBlock(pedestalgreenunlit);
        registerBlock(pedestalwhiteunlit);
        registerBlock(pedestalblackunlit);
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

        registerRender(pedestalstoneunlit);
        registerRender(pedestalredunlit);
        registerRender(pedestalblueunlit);
        registerRender(pedestalyellowunlit);
        registerRender(pedestalpurpleunlit);
        registerRender(pedestalorangeunlit);
        registerRender(pedestalgreenunlit);
        registerRender(pedestalwhiteunlit);
        registerRender(pedestalblackunlit);
    }
}
