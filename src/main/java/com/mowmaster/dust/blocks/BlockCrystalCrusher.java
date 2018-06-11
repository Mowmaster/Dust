package com.mowmaster.dust.blocks;


import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCrusher;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

public class BlockCrystalCrusher extends BlockContainer
{
    private static AxisAlignedBB bounds = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockCrystalCrusher(String unloc, String registryName)
    {
        super(Material.ROCK);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setCreativeTab(DUSTBLOCKSTABS);
        this.setSoundType(SoundType.STONE);
        setBlockUnbreakable();//May need to remove this later when adding the higher tier ones.
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
        return bounds;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCrystalCrusher();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileCrystalCrusher();
    }
}
