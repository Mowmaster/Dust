package com.mowmaster.dust.blocks;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

import static com.mowmaster.dust.blocks.BlockRegistry.crystalCluster;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class BlockCrystalBase extends BlockDirectional implements ITileEntityProvider
{
    TileCrystalCluster te;
    Boolean switchBounds = false;

    private static AxisAlignedBB FUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.125D, 0.75D);
    private static AxisAlignedBB FDOWN = new AxisAlignedBB(0.25D, 1.0D, 0.25D, 0.75D, 0.875D, 0.75D);
    private static AxisAlignedBB FNORTH = new AxisAlignedBB(0.25D, 0.75D, 0.875D, 0.75D, 0.25D, 1.0D);
    private static AxisAlignedBB FSOUTH = new AxisAlignedBB(0.25D, 0.75D, 0.0D, 0.75D, 0.25D, 0.125D);
    private static AxisAlignedBB FWEST = new AxisAlignedBB(0.875D, 0.75D, 0.25D, 1.0D, 0.25D, 0.75D);
    private static AxisAlignedBB FEAST = new AxisAlignedBB(0.0D, 0.75D, 0.25D, 0.125D, 0.25D, 0.75D);
    private static AxisAlignedBB CUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.9D, 0.75D);
    private static AxisAlignedBB CDOWN = new AxisAlignedBB(0.25D, 1.0D, 0.25D, 0.75D, 0.1D, 0.75D);
    private static AxisAlignedBB CNORTH = new AxisAlignedBB(0.25D, 0.75D, 0.1D, 0.75D, 0.25D, 1.0D);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB(0.25D, 0.75D, 0.0D, 0.75D, 0.25D, 0.9D);
    private static AxisAlignedBB CWEST = new AxisAlignedBB(0.1D, 0.75D, 0.25D, 1.0D, 0.25D, 0.75D);
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 0.75D, 0.25D, 0.9D, 0.25D, 0.75D);

    public BlockCrystalBase(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(20);
        this.setResistance(20);
        this.setLightOpacity(10);
        this.setCreativeTab(DUSTTABS);
        this.setSoundType(SoundType.STONE);
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
        return BlockRenderLayer.CUTOUT;
    }



    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if(this.equals(crystalCluster) && switchBounds==false)
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
        switch (((EnumFacing)state.getValue(FACING)))
        {
            case UP:
            default:
                return FUP;
            case DOWN:
                return FDOWN;
            case NORTH:
                return FNORTH;
            case EAST:
                return FEAST;
            case SOUTH:
                return FSOUTH;
            case WEST:
                return FWEST;
        }
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

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof TileCrystalCluster)
            {
                TileCrystalCluster cluster = (TileCrystalCluster) tileEntity;




                if(!worldIn.isRemote && cluster.getCrystalCount()==0)
                {
                    switchBounds = false;
                }
                else{switchBounds = true;}


                cluster.removeCrystal();

                if(playerIn.isSneaking())
                {
                    cluster.addCrystal();
                }
            }
            return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCrystalCluster();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCrystalCluster();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("[WIP] Will Allow for The creation of Mixed Crystal Clusters");
        tooltip.add("Will replace craftable crystals and have effects at tier 2+");
        tooltip.add("Will be in the Next Beta Release");
    }
}
