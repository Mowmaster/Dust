package com.mowmaster.dust.handlers;

import com.mowmaster.dust.blocks.BlockCrystalBase;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.enums.CrystalItems;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import ibxm.Player;
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
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;

import javax.annotation.Nonnull;

import static com.mowmaster.dust.items.ItemRegistry.crystal;


public class PlaceableCrystals
{
    ItemStack redcrystal = new ItemStack(crystal,1,0);
    ItemStack bluecrystal = new ItemStack(crystal,1,1);
    ItemStack yellowcrystal = new ItemStack(crystal,1,2);
    ItemStack purplecrystal = new ItemStack(crystal,1,3);
    ItemStack orangecrystal = new ItemStack(crystal,1,4);
    ItemStack greencrystal = new ItemStack(crystal,1,5);
    ItemStack whitecrystal = new ItemStack(crystal,1,6);
    ItemStack blackcrystal = new ItemStack(crystal,1,7);

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        World worldIn = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer playerIn = event.getEntityPlayer();
        EnumHand hand = event.getHand();
        EnumFacing facing = event.getFace();
        IBlockState state = worldIn.getBlockState(event.getPos());

            if(event.getEntityPlayer().isSneaking())
            {

//Case through the facings and then output the position add based on that???
                if((playerIn.getHeldItem(hand) != null))
                {
                    if (!(state.getBlock()instanceof BlockCrystalBase))
                    {
                        if((ItemStack.areItemsEqual(playerIn.getHeldItem(hand), redcrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), bluecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), yellowcrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), purplecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), orangecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), greencrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), whitecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), blackcrystal) )
                                && event.getFace()==EnumFacing.UP) {

                            worldIn.setBlockState(pos.add(0,1,0), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                            playerIn.getHeldItem(hand).shrink(1);
                            TileEntity tileentity = worldIn.getTileEntity(pos.add(0,1,0));
                            if (tileentity instanceof TileCrystalCluster) {
                                ((TileCrystalCluster) tileentity).addCrystal(playerIn.getHeldItem(hand).getMetadata());
                            }
                        }
                        else if((ItemStack.areItemsEqual(playerIn.getHeldItem(hand), redcrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), bluecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), yellowcrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), purplecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), orangecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), greencrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), whitecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), blackcrystal) )
                                && event.getFace()==EnumFacing.DOWN) {
                            worldIn.setBlockState(pos.add(0,-1,0), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                            playerIn.getHeldItem(hand).shrink(1);
                            TileEntity tileentity = worldIn.getTileEntity(pos.add(0,-1,0));
                            if (tileentity instanceof TileCrystalCluster) {
                                ((TileCrystalCluster) tileentity).addCrystal(playerIn.getHeldItem(hand).getMetadata());
                            }
                        }
                        else if((ItemStack.areItemsEqual(playerIn.getHeldItem(hand), redcrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), bluecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), yellowcrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), purplecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), orangecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), greencrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), whitecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), blackcrystal) )
                                && event.getFace()==EnumFacing.NORTH) {
                            worldIn.setBlockState(pos.add(0,0,-1), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                            playerIn.getHeldItem(hand).shrink(1);
                            TileEntity tileentity = worldIn.getTileEntity(pos.add(0,0,-1));
                            if (tileentity instanceof TileCrystalCluster) {
                                ((TileCrystalCluster) tileentity).addCrystal(playerIn.getHeldItem(hand).getMetadata());
                            }
                        }
                        else if((ItemStack.areItemsEqual(playerIn.getHeldItem(hand), redcrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), bluecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), yellowcrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), purplecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), orangecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), greencrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), whitecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), blackcrystal) )
                                && event.getFace()==EnumFacing.SOUTH) {
                            worldIn.setBlockState(pos.add(0,0,1), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                            playerIn.getHeldItem(hand).shrink(1);
                            TileEntity tileentity = worldIn.getTileEntity(pos.add(0,0,1));
                            if (tileentity instanceof TileCrystalCluster) {
                                ((TileCrystalCluster) tileentity).addCrystal(playerIn.getHeldItem(hand).getMetadata());
                            }
                        }
                        else if((ItemStack.areItemsEqual(playerIn.getHeldItem(hand), redcrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), bluecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), yellowcrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), purplecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), orangecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), greencrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), whitecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), blackcrystal) )
                                && event.getFace()==EnumFacing.EAST) {
                            worldIn.setBlockState(pos.add(1,0,0), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                            playerIn.getHeldItem(hand).shrink(1);
                            TileEntity tileentity = worldIn.getTileEntity(pos.add(1,0,0));
                            if (tileentity instanceof TileCrystalCluster) {
                                ((TileCrystalCluster) tileentity).addCrystal(playerIn.getHeldItem(hand).getMetadata());
                            }
                        }
                        else if((ItemStack.areItemsEqual(playerIn.getHeldItem(hand), redcrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), bluecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), yellowcrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), purplecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), orangecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), greencrystal)
                                || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), whitecrystal) || ItemStack.areItemsEqual(playerIn.getHeldItem(hand), blackcrystal) )
                                && event.getFace()==EnumFacing.WEST) {
                            worldIn.setBlockState(pos.add(-1,0,0), BlockRegistry.crystalCluster.getDefaultState().withProperty(BlockDirectional.FACING, facing));
                            playerIn.getHeldItem(hand).shrink(1);
                            TileEntity tileentity = worldIn.getTileEntity(pos.add(-1,0,0));
                            if (tileentity instanceof TileCrystalCluster) {
                                ((TileCrystalCluster) tileentity).addCrystal(playerIn.getHeldItem(hand).getMetadata());
                            }
                        }
                    }

                }
            }
        }


}
