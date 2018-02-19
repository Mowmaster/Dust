package com.mowmaster.dust.handlers;

import com.mowmaster.dust.blocks.BlockCrystalClusterBasic;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.mowmaster.dust.items.ItemRegistry.crystal;


public class PlaceableCrystals
{
    ItemStack redcrystal = new ItemStack(crystal,1,0);
    ItemStack bluecrystal = new ItemStack(crystal,1,1);
    ItemStack yellowcrystal = new ItemStack(crystal,1,2);
    ItemStack purplecrystal = new ItemStack(crystal,1,3);
    ItemStack greencrystal = new ItemStack(crystal,1,4);
    ItemStack orangecrystal = new ItemStack(crystal,1,5);
    ItemStack whitecrystal = new ItemStack(crystal,1,6);
    ItemStack blackcrystal = new ItemStack(crystal,1,7);

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        World worldIn = event.getWorld();
        EntityPlayer playerIn = event.getEntityPlayer();
        EnumHand hand = event.getHand();
        IBlockState state = worldIn.getBlockState(event.getPos());

        Boolean red = ItemStack.areItemsEqual(playerIn.getHeldItem(hand), redcrystal);
        Boolean blue = ItemStack.areItemsEqual(playerIn.getHeldItem(hand), bluecrystal);
        Boolean yellow = ItemStack.areItemsEqual(playerIn.getHeldItem(hand), yellowcrystal);
        Boolean purple = ItemStack.areItemsEqual(playerIn.getHeldItem(hand), purplecrystal);
        Boolean green = ItemStack.areItemsEqual(playerIn.getHeldItem(hand), greencrystal);
        Boolean orange = ItemStack.areItemsEqual(playerIn.getHeldItem(hand), orangecrystal);
        Boolean white = ItemStack.areItemsEqual(playerIn.getHeldItem(hand), whitecrystal);
        Boolean black = ItemStack.areItemsEqual(playerIn.getHeldItem(hand), blackcrystal);

        if(event.getEntityPlayer().isSneaking())
        {
            if((playerIn.getHeldItem(hand) != null))
            {
                if (!(state.getBlock()instanceof BlockCrystalClusterBasic))
                {
                    if(playerIn.getHeldItem(hand).getItem() instanceof ItemCrystal)
                    {
                        if(event.getFace()==EnumFacing.UP && (red||blue||yellow||purple||green||orange))
                        {
                            spawnBasicCluster(event,0,1,0);
                        }
                        else if(event.getFace()==EnumFacing.DOWN && (red||blue||yellow||purple||green||orange))
                        {
                            spawnBasicCluster(event,0,-1,0);
                        }
                        else if(event.getFace()==EnumFacing.NORTH && (red||blue||yellow||purple||green||orange)) {
                            spawnBasicCluster(event,0,0,-1);
                        }
                        else if(event.getFace()==EnumFacing.SOUTH && (red||blue||yellow||purple||green||orange)) {
                            spawnBasicCluster(event,0,0,1);
                        }
                        else if(event.getFace()==EnumFacing.EAST && (red||blue||yellow||purple||green||orange)) {
                            spawnBasicCluster(event,1,0,0);
                        }
                        else if(event.getFace()==EnumFacing.WEST && (red||blue||yellow||purple||green||orange)) {
                            spawnBasicCluster(event,-1,0,0);
                        }
                    }
                }
            }
        }
    }

    private void spawnBasicCluster(PlayerInteractEvent.RightClickBlock event,int x, int y, int z)
    {
        World worldIn = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer playerIn = event.getEntityPlayer();
        EnumHand hand = event.getHand();
        EnumFacing facing = event.getFace();

        worldIn.setBlockState(pos.add(x,y,z), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
        TileEntity tileentity = worldIn.getTileEntity(pos.add(x,y,z));
        if (tileentity instanceof TileCrystalCluster) {
            if(((TileCrystalCluster) tileentity).getCrystalCount()==0)
            {
                ((TileCrystalCluster) tileentity).addCrystal(playerIn.getHeldItem(hand).getMetadata());
                playerIn.getHeldItem(hand).shrink(1);
            }
        }
    }

}
