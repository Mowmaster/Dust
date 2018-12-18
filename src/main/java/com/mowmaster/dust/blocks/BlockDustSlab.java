package com.mowmaster.dust.blocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;


import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;


public class BlockDustSlab extends BlockBasicDirectional
{
    public static Block redstoneslabs;
    public static Block redbrickslabs;
    public static Block redbrickslabs2;
    public static Block redplankslabs;
    public static Block bluestoneslabs;
    public static Block bluebrickslabs;
    public static Block bluebrickslabs2;
    public static Block blueplankslabs;
    public static Block yellowstoneslabs;
    public static Block yellowbrickslabs;
    public static Block yellowbrickslabs2;
    public static Block yellowplankslabs;
    public static Block purplestoneslabs;
    public static Block purplebrickslabs;
    public static Block purplebrickslabs2;
    public static Block purpleplankslabs;
    public static Block orangestoneslabs;
    public static Block orangebrickslabs;
    public static Block orangebrickslabs2;
    public static Block orangeplankslabs;
    public static Block greenstoneslabs;
    public static Block greenbrickslabs;
    public static Block greenbrickslabs2;
    public static Block greenplankslabs;
    public static Block whitestoneslabs;
    public static Block whitebrickslabs;
    public static Block whitebrickslabs2;
    public static Block whiteplankslabs;
    public static Block blackstoneslabs;
    public static Block blackbrickslabs;
    public static Block blackbrickslabs2;
    public static Block blackplankslabs;

    public BlockDustSlab(String unloc, String registryName, Material material, SoundType soundType, int hardness, int resistance, int lightopacity)
    {
        super(material);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightOpacity(lightopacity);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setSoundType(soundType);
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.useNeighborBrightness = true;
    }

    //Covers all crystals
    //Using BlockEndRod as an "example"
    private static AxisAlignedBB CDOWN = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    private static AxisAlignedBB CUP = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    private static AxisAlignedBB CNORTH = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);
    private static AxisAlignedBB CWEST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return null;
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
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    public static void Init()
    {
        redstoneslabs = new BlockDustSlab("redstoneslabs", "ancient/redstoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        redbrickslabs = new BlockDustSlab("redbrickslabs", "ancient/redbrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        redbrickslabs2 = new BlockDustSlab("redbrickslabs2", "ancient/redbrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        redplankslabs = new BlockDustSlab("redplankslabs", "ancient/redplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        bluestoneslabs = new BlockDustSlab("bluestoneslabs", "ancient/bluestoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluebrickslabs = new BlockDustSlab("bluebrickslabs", "ancient/bluebrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        bluebrickslabs2 = new BlockDustSlab("bluebrickslabs2", "ancient/bluebrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        blueplankslabs = new BlockDustSlab("blueplankslabs", "ancient/blueplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        yellowstoneslabs = new BlockDustSlab("yellowstoneslabs", "ancient/yellowstoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowbrickslabs = new BlockDustSlab("yellowbrickslabs", "ancient/yellowbrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowbrickslabs2 = new BlockDustSlab("yellowbrickslabs2", "ancient/yellowbrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        yellowplankslabs = new BlockDustSlab("yellowplankslabs", "ancient/yellowplankslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplestoneslabs = new BlockDustSlab("purplestoneslabs", "ancient/purplestoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplebrickslabs = new BlockDustSlab("purplebrickslabs", "ancient/purplebrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        purplebrickslabs2 = new BlockDustSlab("purplebrickslabs2", "ancient/purplebrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        purpleplankslabs = new BlockDustSlab("purpleplankslabs", "ancient/purpleplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        orangestoneslabs = new BlockDustSlab("orangestoneslabs", "ancient/orangestoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangebrickslabs = new BlockDustSlab("orangebrickslabs", "ancient/orangebrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangebrickslabs2 = new BlockDustSlab("orangebrickslabs2", "ancient/orangebrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        orangeplankslabs = new BlockDustSlab("orangeplankslabs", "ancient/orangeplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        greenstoneslabs = new BlockDustSlab("greenstoneslabs", "ancient/greenstoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        greenbrickslabs = new BlockDustSlab("greenbrickslabs", "ancient/greenbrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        greenbrickslabs2 = new BlockDustSlab("greenbrickslabs2", "ancient/greenbrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        greenplankslabs = new BlockDustSlab("greenplankslabs", "ancient/greenplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        whitestoneslabs = new BlockDustSlab("whitestoneslabs", "ancient/whitestoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitebrickslabs = new BlockDustSlab("whitebrickslabs", "ancient/whitebrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        whitebrickslabs2 = new BlockDustSlab("whitebrickslabs2", "ancient/whitebrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        whiteplankslabs = new BlockDustSlab("whiteplankslabs", "ancient/whiteplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
        blackstoneslabs = new BlockDustSlab("blackstoneslabs", "ancient/blackstoneslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackbrickslabs = new BlockDustSlab("blackbrickslabs", "ancient/blackbrickslabs",Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackbrickslabs2 = new BlockDustSlab("blackbrickslabs2", "ancient/blackbrickslabs2",Material.ROCK, SoundType.STONE, 3, 20, 10);
        blackplankslabs = new BlockDustSlab("blackplankslabs", "ancient/blackplankslabs",Material.WOOD, SoundType.WOOD, 2, 10, 10);
    }

    public static void Register()
    {
        registerBlock(redstoneslabs);
        registerBlock(redbrickslabs);
        registerBlock(redbrickslabs2);
        registerBlock(redplankslabs);
        registerBlock(bluestoneslabs);
        registerBlock(bluebrickslabs);
        registerBlock(bluebrickslabs2);
        registerBlock(blueplankslabs);
        registerBlock(yellowstoneslabs);
        registerBlock(yellowbrickslabs);
        registerBlock(yellowbrickslabs2);
        registerBlock(yellowplankslabs);
        registerBlock(purplestoneslabs);
        registerBlock(purplebrickslabs);
        registerBlock(purplebrickslabs2);
        registerBlock(purpleplankslabs);
        registerBlock(orangestoneslabs);
        registerBlock(orangebrickslabs);
        registerBlock(orangebrickslabs2);
        registerBlock(orangeplankslabs);
        registerBlock(greenstoneslabs);
        registerBlock(greenbrickslabs);
        registerBlock(greenbrickslabs2);
        registerBlock(greenplankslabs);
        registerBlock(whitestoneslabs);
        registerBlock(whitebrickslabs);
        registerBlock(whitebrickslabs2);
        registerBlock(whiteplankslabs);
        registerBlock(blackstoneslabs);
        registerBlock(blackbrickslabs);
        registerBlock(blackbrickslabs2);
        registerBlock(blackplankslabs);
    }

    public static void RegisterRender()
    {
        registerRenderAncient(redstoneslabs);
        registerRenderAncient(redbrickslabs);
        registerRenderAncient(redbrickslabs2);
        registerRenderAncient(redplankslabs);
        registerRenderAncient(bluestoneslabs);
        registerRenderAncient(bluebrickslabs);
        registerRenderAncient(bluebrickslabs2);
        registerRenderAncient(blueplankslabs);
        registerRenderAncient(yellowstoneslabs);
        registerRenderAncient(yellowbrickslabs);
        registerRenderAncient(yellowbrickslabs2);
        registerRenderAncient(yellowplankslabs);
        registerRenderAncient(purplestoneslabs);
        registerRenderAncient(purplebrickslabs);
        registerRenderAncient(purplebrickslabs2);
        registerRenderAncient(purpleplankslabs);
        registerRenderAncient(orangestoneslabs);
        registerRenderAncient(orangebrickslabs);
        registerRenderAncient(orangebrickslabs2);
        registerRenderAncient(orangeplankslabs);
        registerRenderAncient(greenstoneslabs);
        registerRenderAncient(greenbrickslabs);
        registerRenderAncient(greenbrickslabs2);
        registerRenderAncient(greenplankslabs);
        registerRenderAncient(whitestoneslabs);
        registerRenderAncient(whitebrickslabs);
        registerRenderAncient(whitebrickslabs2);
        registerRenderAncient(whiteplankslabs);
        registerRenderAncient(blackstoneslabs);
        registerRenderAncient(blackbrickslabs);
        registerRenderAncient(blackbrickslabs2);
        registerRenderAncient(blackplankslabs);
    }
}
