package com.mowmaster.dust.blocks;

import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.enums.CrystalBlocks;

import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.util.BlockRenderLayer.CUTOUT;
import static net.minecraft.util.BlockRenderLayer.TRANSLUCENT;
import static net.minecraftforge.client.ForgeHooksClient.setRenderLayer;


public class BlockYellowOre extends Block implements IMetaBlockName
{

    public static final PropertyEnum YELLOWSTATE = PropertyEnum.create("yellowstate",CrystalBlocks.CrystalOres.class);

    public BlockYellowOre(String unloc)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setDefaultState(this.blockState.getBaseState().withProperty(YELLOWSTATE, CrystalBlocks.CrystalOres.ORE));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{YELLOWSTATE});
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        CrystalBlocks.CrystalOres ores = (CrystalBlocks.CrystalOres) state.getValue(YELLOWSTATE);
        return ores.getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(YELLOWSTATE, CrystalBlocks.CrystalOres.values()[meta]);
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

    @Override
    public BlockRenderLayer getBlockLayer() {
        return TRANSLUCENT;
    }

    //AxisAlignedBB(min x1, mix y1, min z1, max x2, max y2, max z2)
    private static final AxisAlignedBB ORE_BOX = new AxisAlignedBB(
		0.0D, 0.0D, 0.0D,
		1.0D, 1.0D, 1.0D);
    private static AxisAlignedBB CRYSTAL_BOX = new AxisAlignedBB(
		0.25D, 0.0D, 0.25D,
		0.75D, 0.90D, 0.75D);
    private static final AxisAlignedBB BASE_BOX = new AxisAlignedBB(
		0.25D, 0.0D, 0.25D,
		0.75D, 0.125D, 0.75D);
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {

        switch ((CrystalBlocks.CrystalOres)state.getValue(YELLOWSTATE))
        {
            case FIFTH:
            case FOURTH:
            case THIRD:
            case SECOND:
            case FIRST:
                return CRYSTAL_BOX;
            case BASE:
                return BASE_BOX;
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
