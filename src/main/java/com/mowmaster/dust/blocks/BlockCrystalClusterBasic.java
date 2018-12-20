package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class BlockCrystalClusterBasic extends BlockBasicDirectional implements ITileEntityProvider
{
    public static Block crystalCluster;
    private static double v1=0.9D;
    private static double v2=0.1D;

    private static AxisAlignedBB CUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, v1, 0.75D);
    private static AxisAlignedBB CDOWN = new AxisAlignedBB(0.25D, 1.0D, 0.25D, 0.75D, v2, 0.75D);
    private static AxisAlignedBB CNORTH = new AxisAlignedBB(0.25D, 0.75D, v2, 0.75D, 0.25D, 1.0D);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB(0.25D, 0.75D, 0.0D, 0.75D, 0.25D, v1);
    private static AxisAlignedBB CWEST = new AxisAlignedBB(v2, 0.75D, 0.25D, 1.0D, 0.25D, 0.75D);
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 0.75D, 0.25D, v1, 0.25D, 0.75D);



    public BlockCrystalClusterBasic(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(3);
        this.setResistance(20);
        this.setCreativeTab(DUSTTABS);
        this.setSoundType(SoundType.STONE);
        this.setBlockUnbreakable();
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

    private void checkThenAddCrystal(EntityPlayer playerIn,EnumHand hand, TileCrystalCluster cluster, int metaCrystalAllowed)
    {
        if(playerIn.getHeldItem(hand).getItem() instanceof ItemCrystal)
        {
            if (playerIn.getHeldItem(hand).getMetadata()==metaCrystalAllowed)//restricts basic cluster to 3 primaries
            {
                if(cluster.addCrystal(playerIn.getHeldItem(hand).getMetadata()))
                {
                    if(!playerIn.isCreative())
                    {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(!worldIn.isRemote)
        {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileCrystalCluster) {
                TileCrystalCluster cluster = (TileCrystalCluster) tileEntity;

                if (playerIn.isSneaking())
                {

                    if (playerIn.getHeldItem(hand).isEmpty())
                    {
                        playerIn.inventory.addItemStackToInventory(cluster.removeCrystal(cluster));
                        return true;
                    }
                    else if (!playerIn.inventory.addItemStackToInventory(cluster.removeCrystal(cluster)))
                    {
                        playerIn.dropItem(cluster.removeCrystal(cluster), false);
                        return true;
                    }

                }


                checkThenAddCrystal(playerIn,hand,cluster,0);
                checkThenAddCrystal(playerIn,hand,cluster,1);
                checkThenAddCrystal(playerIn,hand,cluster,2);
                checkThenAddCrystal(playerIn,hand,cluster,3);
                checkThenAddCrystal(playerIn,hand,cluster,4);
                checkThenAddCrystal(playerIn,hand,cluster,5);
                checkThenAddCrystal(playerIn,hand,cluster,6);
                checkThenAddCrystal(playerIn,hand,cluster,7);

                ItemStack blockGlowstone = new ItemStack(Blocks.GLOWSTONE,1);
                if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), blockGlowstone))
                {
                    if(cluster.addGlowstone()) {if(!playerIn.isCreative())playerIn.getHeldItem(hand).shrink(1);}
                }
            }
        }

        return true;
    }


    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCrystalCluster) {
            TileCrystalCluster cluster = (TileCrystalCluster) tileEntity;

            if(cluster.getLight()==true)
            {
                return 15;
            }
        }
        return 0;

    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        this.getLightValue(state,worldIn,pos);

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
        tooltip.add("[WIP] Combine Crystals To Create Effects");

    }


    public static void Init()
    {
        crystalCluster = new BlockCrystalClusterBasic("crystalcluster", "crystalcluster");
    }

    public static void Register()
    {
        registerBlock(crystalCluster);
    }

    public static void RegisterRender()
    {
        registerRender(crystalCluster);
    }
}
