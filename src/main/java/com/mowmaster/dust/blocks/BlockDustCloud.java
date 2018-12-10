package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileDustBlock;
import net.minecraft.block.BlockFalling;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
public class BlockDustCloud extends BlockFalling {

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
        return true;
    }

    //Only until the Crusher is implimented
    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        return Item.getItemFromBlock(this);
    }

    public ItemStack getDustDropped()
    {

        ItemStack stacker = ItemStack.EMPTY;
        if(this.equals(BlockRegistry.redDust)) { stacker = new ItemStack(ItemRegistry.dust,1,0); }
        if(this.equals(BlockRegistry.blueDust)) { stacker = new ItemStack(ItemRegistry.dust,1,1); }
        if(this.equals(BlockRegistry.yellowDust)) { stacker = new ItemStack(ItemRegistry.dust,1,2); }
        if(this.equals(BlockRegistry.purpleDust)) { stacker = new ItemStack(ItemRegistry.dust,1,3); }
        if(this.equals(BlockRegistry.greenDust)) { stacker = new ItemStack(ItemRegistry.dust,1,4); }
        if(this.equals(BlockRegistry.orangeDust)) { stacker = new ItemStack(ItemRegistry.dust,1,5); }
        if(this.equals(BlockRegistry.whiteDust)) { stacker = new ItemStack(ItemRegistry.dust,1,6); }
        if(this.equals(BlockRegistry.blackDust)) { stacker = new ItemStack(ItemRegistry.dust,1,7); }

        if(this.equals(BlockRegistry.blazeDust)) { stacker = new ItemStack(Items.BLAZE_POWDER,1); }
        if(this.equals(BlockRegistry.carbonDust)) { stacker = new ItemStack(Items.COAL,1); }
        if(this.equals(BlockRegistry.ironDust)) { stacker = new ItemStack(Items.IRON_INGOT,1); }
        if(this.equals(BlockRegistry.goldDust)) { stacker = new ItemStack(Items.GOLD_INGOT,1); }
        if(this.equals(BlockRegistry.redstoneDust)) { stacker = new ItemStack(Items.REDSTONE,1); }

        if(this.equals(BlockRegistry.wheatDust)) { stacker = new ItemStack(Items.WHEAT,1); }
        if(this.equals(BlockRegistry.potatoDust)) { stacker = new ItemStack(Items.POTATO,1); }
        if(this.equals(BlockRegistry.sugarDust)) { stacker = new ItemStack(Items.SUGAR,1); }

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
                    ((TileDustBlock) tileentity).addItem(blockToAdd);
                    worldIn.setBlockToAir(pos);
                }
            }
            else
            {

                //Make a new Dust Block
                ItemStack blockToAdd = new ItemStack(this.getDefaultState().getBlock(),1);
                worldIn.setBlockState(pos,BlockRegistry.dustBlock.getDefaultState());
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
}


