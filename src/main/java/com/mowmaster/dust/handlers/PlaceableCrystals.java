package com.mowmaster.dust.handlers;

import com.mowmaster.dust.blocks.BlockRegistry;
import ibxm.Player;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

import static com.mowmaster.dust.items.ItemRegistry.crystal;


public class PlaceableCrystals
{
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onItemRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        World worldIn = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer playerIn = event.getEntityPlayer();
        EnumHand hand = event.getHand();
        EnumFacing facing = event.getFace();

        if(event.getEntityPlayer().isSneaking())
        {

            if((playerIn.getHeldItem(hand) != null))
            {
                if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), new ItemStack(crystal)) && event.getFace()==EnumFacing.UP) {
                    worldIn.setBlockState(pos.add(0,1,0), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                }
                else if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), new ItemStack(crystal)) && event.getFace()==EnumFacing.DOWN) {
                    worldIn.setBlockState(pos.add(0,-1,0), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                }
                else if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), new ItemStack(crystal)) && event.getFace()==EnumFacing.NORTH) {
                    worldIn.setBlockState(pos.add(0,0,-1), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                }
                else if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), new ItemStack(crystal)) && event.getFace()==EnumFacing.SOUTH) {
                    worldIn.setBlockState(pos.add(0,0,1), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                }
                else if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), new ItemStack(crystal)) && event.getFace()==EnumFacing.EAST) {
                    worldIn.setBlockState(pos.add(1,0,0), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                }
                else if(ItemStack.areItemsEqual(playerIn.getHeldItem(hand), new ItemStack(crystal)) && event.getFace()==EnumFacing.WEST) {
                    worldIn.setBlockState(pos.add(-1,0,0), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                }
            }
        }
    }
}
