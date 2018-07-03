package com.mowmaster.dust.handlers;

import com.mowmaster.dust.blocks.BlockCrystalClusterBasic;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.items.ItemScroll;
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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.mowmaster.dust.items.ItemRegistry.crystal;
import static sun.audio.AudioPlayer.player;


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

    @SubscribeEvent(priority = EventPriority.LOWEST)
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        World worldIn = event.getWorld();
        EntityPlayer playerIn = event.getEntityPlayer();
        EnumHand hand = event.getHand();
        IBlockState state = worldIn.getBlockState(event.getPos());

        if(playerIn.getHeldItemMainhand().getItem() instanceof ItemScroll)
        {
            playerIn.setHeldItem(EnumHand.MAIN_HAND,picker());
        }
    }

    List<String> scrolls = Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");

    public int pickernum;
    public ItemStack picker()
    {
        Random rn = new Random();
        pickernum = rn.nextInt(26);
        scrolls.get(pickernum);
        ItemStack thisScroll = ItemStack.EMPTY;

        switch (scrolls.get(pickernum))
        {
            case "a":
                return thisScroll = new ItemStack(ItemRegistry.scrollA,1);
            case "b":
                return thisScroll = new ItemStack(ItemRegistry.scrollB,1);
            case "c":
                return thisScroll = new ItemStack(ItemRegistry.scrollC,1);
            case "d":
                return thisScroll = new ItemStack(ItemRegistry.scrollD,1);
            case "e":
                return thisScroll = new ItemStack(ItemRegistry.scrollE,1);
            case "f":
                return thisScroll = new ItemStack(ItemRegistry.scrollF,1);
            case "g":
                return thisScroll = new ItemStack(ItemRegistry.scrollG,1);
            case "h":
                return thisScroll = new ItemStack(ItemRegistry.scrollH,1);
            case "i":
                return thisScroll = new ItemStack(ItemRegistry.scrollI,1);
            case "j":
                return thisScroll = new ItemStack(ItemRegistry.scrollJ,1);
            case "k":
                return thisScroll = new ItemStack(ItemRegistry.scrollK,1);
            case "l":
                return thisScroll = new ItemStack(ItemRegistry.scrollL,1);
            case "m":
                return thisScroll = new ItemStack(ItemRegistry.scrollM,1);
            case "n":
                return thisScroll = new ItemStack(ItemRegistry.scrollN,1);
            case "o":
                return thisScroll = new ItemStack(ItemRegistry.scrollO,1);
            case "p":
                return thisScroll = new ItemStack(ItemRegistry.scrollP,1);
            case "q":
                return thisScroll = new ItemStack(ItemRegistry.scrollQ,1);
            case "r":
                return thisScroll = new ItemStack(ItemRegistry.scrollR,1);
            case "s":
                return thisScroll = new ItemStack(ItemRegistry.scrollS,1);
            case "t":
                return thisScroll = new ItemStack(ItemRegistry.scrollT,1);
            case "u":
                return thisScroll = new ItemStack(ItemRegistry.scrollU,1);
            case "v":
                return thisScroll = new ItemStack(ItemRegistry.scrollV,1);
            case "w":
                return thisScroll = new ItemStack(ItemRegistry.scrollW,1);
            case "x":
                return thisScroll = new ItemStack(ItemRegistry.scrollX,1);
            case "y":
                return thisScroll = new ItemStack(ItemRegistry.scrollY,1);
            case "z":
                return thisScroll = new ItemStack(ItemRegistry.scrollZ,1);
        }
        return thisScroll;
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
