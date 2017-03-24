package com.mowmaster.dust.blocks;

import com.mowmaster.dust.blocks.item.IMetaBlockName;
import com.mowmaster.dust.enums.CrystalBlocks;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileRedOre;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.block.BlockPistonBase.getFacingFromEntity;
import static net.minecraft.util.BlockRenderLayer.SOLID;
import static net.minecraft.util.BlockRenderLayer.TRANSLUCENT;


public class BlockRedOre extends BlockDirectional implements IMetaBlockName, ITileEntityProvider
{
    //the string text is what you use in your json file
    public static final PropertyEnum REDSTATE = PropertyEnum.create("state",CrystalBlocks.CrystalOres.class);



    public BlockRedOre(String unloc)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, unloc));
        this.setDefaultState(this.blockState.getBaseState().withProperty(REDSTATE, CrystalBlocks.CrystalOres.ORE));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,new IProperty[]{REDSTATE});
    }

	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)

	{
		return super.onBlockPlaced(worldIn,pos, getFacingFromEntity(pos,placer),hitX,hitY,hitZ,meta,placer);
	}

    @Override
    public int getMetaFromState(IBlockState state)
    {
        CrystalBlocks.CrystalOres ores = (CrystalBlocks.CrystalOres) state.getValue(REDSTATE);
        return ores.getID();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(REDSTATE,CrystalBlocks.CrystalOres.values()[meta]);
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
        switch((CrystalBlocks.CrystalOres)state.getValue(REDSTATE))
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

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileRedOre();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileRedOre();
    }
}