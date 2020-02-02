package com.mowmaster.dust.blocks.machines;


import com.mowmaster.dust.blocks.blockbasics.BlockBasic;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileTrapBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTrap extends BlockBasic implements ITileEntityProvider
{
    public static Block blockTrap;
    public static Block blockTrapMob;

    public BlockTrap(String unloc, String registryName)
    {
        super(Material.GROUND);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(20);
        this.setResistance(20);
        this.setSoundType(SoundType.GROUND);
        this.setBlockUnbreakable();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        return null;
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
        if(entityIn instanceof EntityLivingBase)
        {super.canCollideCheck(this.getDefaultState(),true);}
        else {super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, bool);}
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return null;
    }

    private PotionEffect getEffect(World world, BlockPos pos)
    {
        PotionEffect effect = null;
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileTrapBlock) {
            TileTrapBlock trapBlock = (TileTrapBlock) tileEntity;
            effect=trapBlock.getTrapEffect();
        }
        return effect;

    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if(state.getBlock().equals(BlockTrap.blockTrap))
        {
            if(entityIn instanceof EntityPlayer)
            {
                ((EntityLivingBase) entityIn).addPotionEffect(getEffect(worldIn,pos));
                worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.BLOCKS, 0.4F, 0.6F);
                worldIn.setBlockToAir(pos);
            }
        }
        else if(state.getBlock().equals(BlockTrap.blockTrapMob))
        {
            if(entityIn instanceof EntityCreature)
            {
                ((EntityLivingBase) entityIn).addPotionEffect(getEffect(worldIn,pos));
                worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.BLOCKS, 0.4F, 0.6F);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        Random rn = new Random();
        int num = rn.nextInt(5);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.15D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);

        if(num<=1)
        {
            worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d0 + (double)d3, d1 + (double)d3, d2 +d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("This Block isnt useable by itsself normally...");
        tooltip.add("Please report the method used to obtain this block (outside of cheat mode or commands) to the mod author.");
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTrapBlock();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileTrapBlock();
    }


    public static void Init()
    {

        blockTrap = new BlockTrap("blocktrap","blocktrap");
        blockTrapMob = new BlockTrap("blocktrapmob","blocktrapmob");
    }

    public static void Register()
    {
        registerBlock(blockTrap);
        registerBlock(blockTrapMob);
    }

    public static void RegisterRender()
    {
        registerRender(blockTrap);
        registerRender(blockTrapMob);
    }
}
