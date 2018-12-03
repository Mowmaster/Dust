package com.mowmaster.dust.blocks;


import com.mowmaster.dust.items.ItemDust;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileDustBlock;
import com.mowmaster.dust.tiles.TileTrapBlock;
import com.sun.istack.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockDust extends Block implements ITileEntityProvider
{

    public BlockDust(String unloc, String registryName)
    {
        super(Material.SAND);
        this.setUnlocalizedName(unloc);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.setHardness(1);
        this.setResistance(1);
        this.setSoundType(SoundType.SAND);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        return null;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public Block setBlockUnbreakable() {
        return super.setBlockUnbreakable();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote)
        {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileDustBlock) {
                TileDustBlock tileDust = (TileDustBlock) tileEntity;


                if(Block.getBlockFromItem(playerIn.getHeldItemMainhand().getItem()) instanceof BlockDust)
                {
                    if(!tileDust.isBlockFull())
                    {
                        tileDust.addItem(playerIn.getHeldItemMainhand());
                        playerIn.getHeldItemMainhand().shrink(1);

                        return true;
                    }
                }
                else if(playerIn.getHeldItemMainhand().getItem() instanceof ItemSpade)
                {
                    worldIn.spawnEntity(new EntityItem(worldIn,pos.getX()+0.5,pos.getY()+1,pos.getZ()+0.5,tileDust.removeItem()));
                    playerIn.getHeldItemMainhand().damageItem(1,playerIn);
                    if(tileDust.getNextAvailSlot()==0)
                    {
                        worldIn.setBlockToAir(pos);
                    }

                    return true;
                }
                else
                {
                    System.out.println("Next Slot: " + tileDust.getNextAvailSlot());
                    System.out.println("Stacks: " + tileDust.getSlots());
                }

            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileDustBlock();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDustBlock();
    }

}
