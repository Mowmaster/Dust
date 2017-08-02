package com.mowmaster.dust.blocks;

import com.mowmaster.dust.items.ItemRegistry;
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

import static com.mowmaster.dust.items.ItemRegistry.crystal;
import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;


public class BlockCrystalBaseBlock extends BlockDirectional
{
    private static double v3=0.125D;
    private static double v4=0.875D;

    private static AxisAlignedBB BUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, v3, 0.75D);
    private static AxisAlignedBB BDOWN = new AxisAlignedBB(0.25D, 1.0D, 0.25D, 0.75D, v4, 0.75D);
    private static AxisAlignedBB BNORTH = new AxisAlignedBB(0.25D, 0.75D, v4, 0.75D, 0.25D, 1.0D);
    private static AxisAlignedBB BSOUTH = new AxisAlignedBB(0.25D, 0.75D, 0.0D, 0.75D, 0.25D, v3);
    private static AxisAlignedBB BWEST = new AxisAlignedBB(v4, 0.75D, 0.25D, 1.0D, 0.25D, 0.75D);
    private static AxisAlignedBB BEAST = new AxisAlignedBB(0.0D, 0.75D, 0.25D, v3, 0.25D, 0.75D);

    public BlockCrystalBaseBlock(String unloc, String registryName)
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
        switch (((EnumFacing)state.getValue(FACING)))
        {
            case UP:
            default:
                return BUP;
            case DOWN:
                return BDOWN;
            case NORTH:
                return BNORTH;
            case EAST:
                return BEAST;
            case SOUTH:
                return BSOUTH;
            case WEST:
                return BWEST;
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        return true;
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("Turns into the TE when clicked");

    }
}
