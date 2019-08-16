package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class ipuaImport extends ipuBasic{


    public ipuaImport(){}

    public void upgradeAction(World world, BlockPos posOfPedestal, BlockPos posOfInventory, ItemStack coinInPedestal, int transferRate)
    {
        World worldIn = world;
        BlockPos posPedestal = posOfPedestal;
        BlockPos posInventory = posOfInventory;
        ItemStack coinPedestal = coinInPedestal;
        TileEntity tileEntity = world.getTileEntity(posOfPedestal);
        if (tileEntity instanceof TilePedestal) {
            TilePedestal tilePedestal = (TilePedestal) tileEntity;
            worldIn = tilePedestal.getWorld();
            posPedestal = tilePedestal.getPos();
            posInventory = getPosOfBlockBelow(worldIn,posOfPedestal,1);
            coinPedestal = tilePedestal.getCoinOnPedestal();
        }
        //Only works when block is NOT powered
        if(!world.isBlockPowered(posOfPedestal))
        {
            ItemStack itemFromInv = ItemStack.EMPTY;
            if(worldIn.getTileEntity(posInventory) !=null)
            {
                if(worldIn.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
                {

                    TileEntity invToPullFrom = worldIn.getTileEntity(posInventory);
                    if(invToPullFrom instanceof TilePedestal) {
                        itemFromInv = ItemStack.EMPTY;

                    }
                    else {
                        for(int i =0;i<invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots();i++)
                        {
                            if(!invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i).equals(ItemStack.EMPTY))
                            {
                                itemFromInv = invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                if(getStackInPedestal(worldIn,posPedestal) == ItemStack.EMPTY)
                                {
                                    if(itemFromInv.getCount() > transferRate)
                                    {
                                        ItemStack copyIncoming = itemFromInv.copy();
                                        copyIncoming.setCount(transferRate);
                                        TileEntity pedestalInv = worldIn.getTileEntity(posPedestal);
                                        if(pedestalInv instanceof TilePedestal) {
                                            ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                        }
                                        int counted = itemFromInv.getCount()-transferRate;
                                        itemFromInv.setCount(counted);
                                    }
                                    else
                                    {
                                        ItemStack copyIncoming = itemFromInv.copy();
                                        TileEntity pedestalInv = worldIn.getTileEntity(posPedestal);
                                        if(pedestalInv instanceof TilePedestal) {
                                            ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                        }
                                        itemFromInv.setCount(0);
                                    }
                                }
                                else if(doItemsMatch(getStackInPedestal(worldIn,posPedestal),itemFromInv))
                                {
                                    int leftTillFilled = 64 - getStackInPedestal(worldIn,posPedestal).getCount();
                                    if(leftTillFilled > itemFromInv.getCount())
                                    {
                                        if(itemFromInv.getCount()> transferRate)
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            copyIncoming.setCount(transferRate);
                                            TileEntity pedestalInv = worldIn.getTileEntity(posPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            int counted = itemFromInv.getCount()-transferRate;
                                            itemFromInv.setCount(counted);
                                        }
                                        else
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            TileEntity pedestalInv = worldIn.getTileEntity(posPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            itemFromInv.setCount(0);
                                        }
                                    }
                                    else
                                    {
                                        if(leftTillFilled >= transferRate)
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            copyIncoming.setCount(transferRate);
                                            TileEntity pedestalInv = worldIn.getTileEntity(posPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            int counted = itemFromInv.getCount()-transferRate;
                                            itemFromInv.setCount(counted);
                                        }
                                        else
                                        {
                                            ItemStack copyIncoming = itemFromInv.copy();
                                            copyIncoming.setCount(leftTillFilled);
                                            TileEntity pedestalInv = worldIn.getTileEntity(posPedestal);
                                            if(pedestalInv instanceof TilePedestal) {
                                                ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                            }
                                            int counted = itemFromInv.getCount()-leftTillFilled;
                                            itemFromInv.setCount(counted);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
