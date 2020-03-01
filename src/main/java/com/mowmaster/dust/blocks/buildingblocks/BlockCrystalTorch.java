package com.mowmaster.dust.blocks.buildingblocks;

import com.mowmaster.dust.blocks.blockbasics.BlockBasicDirectional;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
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

public class BlockCrystalTorch extends BlockBasicDirectional
{
    private static AxisAlignedBB CUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.75D, 0.75D);
    private static AxisAlignedBB CDOWN = new AxisAlignedBB(0.25D, 1.0D, 0.25D, 0.75D, 0.5D, 0.75D);

    private static AxisAlignedBB CNORTH = new AxisAlignedBB(0.25D, 0.75D, 0.25D, 0.75D, 0.25D, 1.0D);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB(0.25D, 0.75D, 0.0D, 0.75D, 0.25D, 0.75D);

    private static AxisAlignedBB CWEST = new AxisAlignedBB(0.25D, 0.75D, 0.25D, 1.0D, 0.25D, 0.75D);
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 0.75D, 0.25D, 0.75D, 0.25D, 0.75D);

    public static Block redCrystalTorch;
    public static Block blueCrystalTorch;
    public static Block yellowCrystalTorch;
    public static Block purpleCrystalTorch;
    public static Block greenCrystalTorch;
    public static Block orangeCrystalTorch;
    public static Block whiteCrystalTorch;
    public static Block blackCrystalTorch;

    public BlockCrystalTorch(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(3);
        this.setResistance(5);
        this.setSoundType(SoundType.GLASS);
        this.setLightLevel(3f);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setCreativeTab(DUSTBLOCKSTABS);
    }


    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return super.getItemDropped(state, rand, fortune);
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

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
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

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        if(entityIn instanceof EntityLivingBase)
        {super.canCollideCheck(this.getDefaultState(),true);}
        else {super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, bool);}
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        Random rn = new Random();
        int num = rn.nextInt(5);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);

        if(num<=1)
        {
            worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d0 + (double)d3, d1 + (double)d3, d2 +d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]);
        }
    }

    public static void Init()
    {
        redCrystalTorch = new BlockCrystalTorch("redtorch", "red/redtorch");
        blueCrystalTorch = new BlockCrystalTorch("bluetorch", "blue/bluetorch");
        yellowCrystalTorch = new BlockCrystalTorch("yellowtorch", "yellow/yellowtorch");
        purpleCrystalTorch = new BlockCrystalTorch("purpletorch", "purple/purpletorch");
        greenCrystalTorch = new BlockCrystalTorch("greentorch", "green/greentorch");
        orangeCrystalTorch = new BlockCrystalTorch("orangetorch", "orange/orangetorch");
        whiteCrystalTorch = new BlockCrystalTorch("whitetorch", "white/whitetorch");
        blackCrystalTorch = new BlockCrystalTorch("blacktorch", "black/blacktorch");
    }

    public static void Register()
    {
        registerBlock(redCrystalTorch);
        registerBlock(blueCrystalTorch);
        registerBlock(yellowCrystalTorch);
        registerBlock(purpleCrystalTorch);
        registerBlock(greenCrystalTorch);
        registerBlock(orangeCrystalTorch);
        registerBlock(whiteCrystalTorch);
        registerBlock(blackCrystalTorch);
    }

    public static void RegisterRender()
    {
        registerRender(redCrystalTorch);
        registerRender(blueCrystalTorch);
        registerRender(yellowCrystalTorch);
        registerRender(purpleCrystalTorch);
        registerRender(greenCrystalTorch);
        registerRender(orangeCrystalTorch);
        registerRender(whiteCrystalTorch);
        registerRender(blackCrystalTorch);
    }
}
