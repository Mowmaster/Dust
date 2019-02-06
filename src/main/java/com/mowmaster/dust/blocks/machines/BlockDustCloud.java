package com.mowmaster.dust.blocks.machines;

import com.mowmaster.dust.blocks.blockbasics.BlockBasicFalling;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileDustBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.misc.DustConfigurationFile.chanceToDust;
import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

//Result of the dust from a crusher
public class BlockDustCloud extends BlockBasicFalling {

    public static Block redDust;
    public static Block blueDust;
    public static Block yellowDust;
    public static Block purpleDust;
    public static Block orangeDust;
    public static Block greenDust;
    public static Block whiteDust;
    public static Block blackDust;
    public static Block blazeDust;
    public static Block carbonDust;
    public static Block redstoneDust;
    public static Block ironDust;
    public static Block goldDust;
    public static Block wheatDust;
    public static Block potatoDust;
    public static Block sugarDust;
    public static Block dustBlock;

    public BlockDustCloud(String unloc, String registryName) {
        super(Material.SAND);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(1);
        this.setResistance(1);
        this.setSoundType(SoundType.SAND);
        this.setTickRandomly(true);
        this.setCreativeTab(DUSTBLOCKSTABS);
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean b)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos)
    {
        return null;
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World par1World, BlockPos pos, Vec3d par5Vec3, Vec3d par6Vec3)
    {
        return null;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.IGNORE;
    }
    @Override
    public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    //Only until the Crusher is implimented
    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        return Item.getItemFromBlock(this);
    }

    public ItemStack getDustDropped()
    {

        ItemStack stacker = ItemStack.EMPTY;
        if(this.equals(redDust)) { stacker = new ItemStack(ItemRegistry.dust,1,0); }
        if(this.equals(blueDust)) { stacker = new ItemStack(ItemRegistry.dust,1,1); }
        if(this.equals(yellowDust)) { stacker = new ItemStack(ItemRegistry.dust,1,2); }
        if(this.equals(purpleDust)) { stacker = new ItemStack(ItemRegistry.dust,1,3); }
        if(this.equals(greenDust)) { stacker = new ItemStack(ItemRegistry.dust,1,4); }
        if(this.equals(orangeDust)) { stacker = new ItemStack(ItemRegistry.dust,1,5); }
        if(this.equals(whiteDust)) { stacker = new ItemStack(ItemRegistry.dust,1,6); }
        if(this.equals(blackDust)) { stacker = new ItemStack(ItemRegistry.dust,1,7); }

        if(this.equals(blazeDust)) { stacker = new ItemStack(Items.BLAZE_POWDER,1); }
        if(this.equals(carbonDust)) { stacker = new ItemStack(Items.COAL,1); }
        if(this.equals(ironDust)) { stacker = new ItemStack(ItemRegistry.dust,1,9); }
        if(this.equals(goldDust)) { stacker = new ItemStack(ItemRegistry.dust,1,10); }
        if(this.equals(redstoneDust)) { stacker = new ItemStack(Items.REDSTONE,1); }

        if(this.equals(wheatDust)) { stacker = new ItemStack(Items.WHEAT,1); }
        if(this.equals(potatoDust)) { stacker = new ItemStack(Items.POTATO,1); }
        if(this.equals(sugarDust)) { stacker = new ItemStack(Items.SUGAR,1); }

        return stacker;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {


        final int CHANCE = chanceToDust;
        int rand = random.nextInt(100);
        if (rand <= CHANCE) {
            if(worldIn.getBlockState(pos.add(0,-1,0)).getBlock() instanceof BlockDustCloud) {}//Do nothing
            else if(worldIn.getBlockState(pos.add(0,-1,0)).getBlock() instanceof BlockDust)
            {
                //add this block to the TE and remove it
                ItemStack blockToAdd = new ItemStack(this.getDefaultState().getBlock(),1);
                TileEntity tileentity = worldIn.getTileEntity(pos.add(0,-1,0));
                if (tileentity instanceof TileDustBlock) {
                    if(((TileDustBlock) tileentity).canAddItem(blockToAdd))
                    {
                        ((TileDustBlock) tileentity).addItem(blockToAdd);
                        worldIn.setBlockToAir(pos);
                    }
                    else
                    {
                        worldIn.setBlockState(pos,dustBlock.getDefaultState());
                        TileEntity tileentity2 = worldIn.getTileEntity(pos);
                        if (tileentity2 instanceof TileDustBlock) {
                            ((TileDustBlock) tileentity).addItem(blockToAdd);
                        }
                    }
                }
            }
            else
            {

                //Make a new Dust Block
                ItemStack blockToAdd = new ItemStack(this.getDefaultState().getBlock(),1);
                worldIn.setBlockState(pos,dustBlock.getDefaultState());
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileDustBlock) {
                    ((TileDustBlock) tileentity).addItem(blockToAdd);
                }
            }
        }
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        worldIn.createExplosion(new EntityItem(worldIn), pos.getX() + 0.5,pos.getY() + 1.0,pos.getZ() + 0.5,3.0F, true);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        if(entityIn instanceof EntityPlayer)
        {super.canCollideCheck(this.getDefaultState(),true);}
        else {super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, bool);}
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add("[WIP] Can only be gotten from Crushed Crystals,");
        tooltip.add("in next beta release.");
    }
/*
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
            if(entityIn instanceof EntityPlayer)
            {
                EntityPlayer playerIn = (EntityPlayer) entityIn;
                if(playerIn.hasAchievement(AchievementHandler.achievementDust))
                {
                    playerIn.addStat(AchievementHandler.achievementDust);
                }
            }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if(playerIn.hasAchievement(AchievementHandler.achievementDust))
        {
            playerIn.addStat(AchievementHandler.achievementDust);
        }
    }
    */

    public static void Init()
    {
        redDust = new BlockDustCloud("reddust","red/reddust");
        blueDust = new BlockDustCloud("bluedust","blue/bluedust");
        yellowDust = new BlockDustCloud("yellowdust","yellow/yellowdust");
        purpleDust = new BlockDustCloud("purpledust","purple/purpledust");
        greenDust = new BlockDustCloud("greendust","green/greendust");
        orangeDust = new BlockDustCloud("orangedust","orange/orangedust");
        whiteDust = new BlockDustCloud("whitedust","white/whitedust");
        blackDust = new BlockDustCloud("blackdust","black/blackdust");
        blazeDust = new BlockDustCloud("blazedust","dust/blazedust");
        carbonDust = new BlockDustCloud("carbondust","dust/carbondust");
        ironDust = new BlockDustCloud("irondust","dust/irondust");
        goldDust = new BlockDustCloud("golddust","dust/golddust");
        redstoneDust = new BlockDustCloud("redstonedust","dust/redstonedust");
        wheatDust = new BlockDustCloud("wheatdust","dust/wheatdust");
        potatoDust = new BlockDustCloud("potatodust","dust/potatodust");
        sugarDust = new BlockDustCloud("sugardust","dust/sugardust");

        dustBlock = new BlockDust("blockdust","blockdust");
    }

    public static void Register()
    {
        registerBlock(redDust);
        registerBlock(blueDust);
        registerBlock(yellowDust);
        registerBlock(purpleDust);
        registerBlock(orangeDust);
        registerBlock(greenDust);
        registerBlock(whiteDust);
        registerBlock(blackDust);
        registerBlock(blazeDust);
        registerBlock(carbonDust);
        registerBlock(ironDust);
        registerBlock(goldDust);
        registerBlock(redstoneDust);
        registerBlock(wheatDust);
        registerBlock(potatoDust);
        registerBlock(sugarDust);
        registerBlock(dustBlock);
    }

    public static void RegisterRender()
    {
        registerRender(redDust);
        registerRender(blueDust);
        registerRender(yellowDust);
        registerRender(purpleDust);
        registerRender(orangeDust);
        registerRender(greenDust);
        registerRender(whiteDust);
        registerRender(blackDust);
        registerRender(blazeDust);
        registerRender(carbonDust);
        registerRender(ironDust);
        registerRender(goldDust);
        registerRender(redstoneDust);
        registerRender(wheatDust);
        registerRender(potatoDust);
        registerRender(sugarDust);
        registerRender(dustBlock);
    }
}


