package com.mowmaster.dust.blocks;


import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCrusher;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

import java.util.Random;

import static com.mowmaster.dust.misc.DustyTab.DUSTBLOCKSTABS;

public class BlockCrystalCrusher extends Block implements ITileEntityProvider
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

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockRegistry.crystalcrusher);
    }

    /*
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
     */

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileCrystalCrusher) {
                TileCrystalCrusher tileCrusher = (TileCrystalCrusher) tileEntity;

                //adding crystals
                if(tileCrusher.canAddItem(0,playerIn.getHeldItemMainhand()))
                {
                    int counter = tileCrusher.addItem(0,playerIn.getHeldItemMainhand(),true);
                    tileCrusher.addItem(0,playerIn.getHeldItemMainhand(),false);
                    playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(counter);
                    return true;
                }
                //adding fuels
                if(tileCrusher.canAddItem(1,playerIn.getHeldItemMainhand()))
                {
                    int counter = tileCrusher.addItem(1,playerIn.getHeldItemMainhand(),true);
                    tileCrusher.addItem(1,playerIn.getHeldItemMainhand(),false);
                    playerIn.getHeldItem(EnumHand.MAIN_HAND).shrink(counter);
                    return true;
                }
                //Debugging
                if(playerIn.getHeldItemMainhand().isEmpty())
                {
                    System.out.println(tileCrusher.getStackInSlot(1).getCount());
                }
            }
        }

        return false;
    }

    /*
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileCrystalCrusher te = (TileCrystalCrusher)worldIn.getTileEntity(pos);
        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null);
        for(int slot = 0; slot <handler.getSlots() -1; slot++){
            ItemStack stack = handler.getStackInSlot(slot);
            InventoryHelper.spawnItemStack(worldIn,pos.getX(),pos.getY(),pos.getZ(),stack);
        }
        super.breakBlock(worldIn, pos, state);
    }
     */

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
