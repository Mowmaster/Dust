package com.mowmaster.dust.blocks;

import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.enums.CrystalBlocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.block.BlockPistonBase.getFacingFromEntity;
import static net.minecraft.util.BlockRenderLayer.CUTOUT;
import static net.minecraft.util.BlockRenderLayer.SOLID;
import static net.minecraft.util.BlockRenderLayer.TRANSLUCENT;
import static net.minecraftforge.client.ForgeHooksClient.setRenderLayer;


public class BlockPurpleOre extends BlockDirectional implements IMetaBlockName
{
    //the string text is what you use in your json file
    public static final PropertyEnum PURPLESTATE = PropertyEnum.create("purplestate",CrystalBlocks.CrystalOres.class);



    public BlockPurpleOre(String unloc)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setDefaultState(this.blockState.getBaseState().withProperty(PURPLESTATE, CrystalBlocks.CrystalOres.ORE).withProperty(FACING, EnumFacing.UP));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{PURPLESTATE,FACING});
    }

	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)

	{
		return super.onBlockPlaced(worldIn,pos, getFacingFromEntity(pos,placer),hitX,hitY,hitZ,meta,placer);
	}

	public void onBlockPlacedBy(World worldIn,BlockPos pos,IBlockState state,EntityLivingBase placer,ItemStack stack)
	{
        worldIn.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
        EntityPlayer player = (EntityPlayer) placer;
	}

    @Override
    public int getMetaFromState(IBlockState state)
    {
        CrystalBlocks.CrystalOres ores = (CrystalBlocks.CrystalOres) state.getValue(PURPLESTATE);
        return ores.getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        //EnumFacing enumfacing = EnumFacing.UP;
        //).withProperty(FACING, enumfacing
        return this.getDefaultState().withProperty(PURPLESTATE,CrystalBlocks.CrystalOres.values()[meta]);
    }


    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for (int i = 0; i < CrystalBlocks.CrystalOres.values().length; i++)
        {
            list.add(new ItemStack(itemIn,1,i));
        }
    }

    public String getSpecialName(ItemStack stack)
    {
        return CrystalBlocks.CrystalOres.values()[stack.getItemDamage()].getName();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(this),1,getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }


    public BlockRenderLayer getBlockLayer(IBlockState state) {
        switch((CrystalBlocks.CrystalOres)state.getValue(PURPLESTATE))
        {
            case FIFTH:
            case FOURTH:
            case THIRD:
            case SECOND:
            case FIRST:
            case BASE:
                return TRANSLUCENT;
            case ORE:
            default:
                return SOLID ;
        }

    }

    //AxisAlignedBB(min x1, mix y1, min z1, max x2, max y2, max z2)
    private static AxisAlignedBB CDOWN = new AxisAlignedBB(0.25D, 1.0D, 0.25D, 0.75D, 0.1D, 0.75D);
    private static AxisAlignedBB CUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.9D, 0.75D);
    private static AxisAlignedBB CNORTH = new AxisAlignedBB(0.25D, 0.75D, 0.1D, 0.75D, 0.25D, 1.0D);
    private static AxisAlignedBB CSOUTH = new AxisAlignedBB(0.25D, 0.75D, 0.0D, 0.75D, 0.25D, 0.9D);
    private static AxisAlignedBB CWEST = new AxisAlignedBB(0.1D, 0.75D, 0.25D, 1.0D, 0.25D, 0.75D);
    private static AxisAlignedBB CEAST = new AxisAlignedBB(0.0D, 0.75D, 0.25D, 0.9D, 0.25D, 0.75D);

    private static final AxisAlignedBB BUP = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.125D, 0.75D);
    private static final AxisAlignedBB BDOWN = new AxisAlignedBB(0.25D, 0.875D, 0.25D, 0.75D, 1.0D, 0.75D);
    private static final AxisAlignedBB BSOUTH = new AxisAlignedBB(0.25D, 0.75D, 0.125D, 0.75D, 0.25D, 0.0D);
    private static final AxisAlignedBB BWEST = new AxisAlignedBB(0.875D, 0.75D, 0.25D, 1.0D, 0.25D, 0.75D);
    private static final AxisAlignedBB BNORTH = new AxisAlignedBB(0.25D, 0.75D, 0.875D, 0.75D, 0.25D, 1.0D);
    private static final AxisAlignedBB BEAST = new AxisAlignedBB(0.0D, 0.75D, 0.25D, 0.125D, 0.25D, 0.75D);

    private static final AxisAlignedBB ORE_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);


    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing enumFacing = state.getValue(FACING);
        switch ((CrystalBlocks.CrystalOres)state.getValue(PURPLESTATE)) {
            case FIFTH:
            case FOURTH:
            case THIRD:
            case SECOND:
            case FIRST:
                if (enumFacing == EnumFacing.UP) {return CUP;}
                else if (enumFacing == EnumFacing.DOWN) {return CDOWN;}
                else if(enumFacing == EnumFacing.NORTH) {return CNORTH;}
                else if(enumFacing == EnumFacing.EAST) {return CEAST;}
                else if(enumFacing == EnumFacing.SOUTH) {return CSOUTH;}
                else if(enumFacing == EnumFacing.WEST)  {return CWEST;}
            case BASE:
                if (enumFacing == EnumFacing.UP) {return BUP;}
                else if (enumFacing == EnumFacing.DOWN) {return BDOWN;}
                else if(enumFacing == EnumFacing.NORTH) {return BNORTH;}
                else if(enumFacing == EnumFacing.EAST) {return BEAST;}
                else if(enumFacing == EnumFacing.SOUTH) {return BSOUTH;}
                else if(enumFacing == EnumFacing.WEST) {return BWEST;}
            case ORE:
            default:
                return ORE_BOX;
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



}