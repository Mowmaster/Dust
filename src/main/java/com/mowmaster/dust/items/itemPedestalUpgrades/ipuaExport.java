package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class ipuaExport extends ipuBasic {

    public ipuaExport()
    {

    }

    public void upgradeAction(World world, BlockPos posOfPedestal, int summonRange, int summonCount)
    {
        //Range comes from enchant
        //ammount comes from...speed?

        if(!world.isRemote)
        {
            if(!world.isBlockPowered(posOfPedestal))
            {
                ItemStack itemToInv = ItemStack.EMPTY;
                if(world.getTileEntity(getPosOfBlockBelow(1)) !=null)
                {
                    if(world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))
                    {
                        TileEntity invToPushTo = world.getTileEntity(getPosOfBlockBelow(1));
                        if(invToPushTo instanceof TilePedestal) {
                            itemToInv = ItemStack.EMPTY;
                        }
                        else {
                            if(hasItem())
                            {
                                for(int i =0;i<invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots();i++)
                                {
                                    itemToInv = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                                    if(itemToInv.equals(ItemStack.EMPTY))
                                    {
                                        ItemStack copyPedestal = getItemInPedestal().copy();
                                        invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).insertItem(i,copyPedestal,false);
                                        removeItem();
                                    }
                                    else
                                    {
                                        if(doItemsMatch(itemToInv))
                                        {
                                            int leftTillFilled = roomLeftInStack(itemToInv);
                                            if(leftTillFilled>getItemInPedestal().getCount())
                                            {
                                                ItemStack getcountFromPedestal = getItemInPedestal().copy();
                                                invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).insertItem(i,getcountFromPedestal,false);
                                                removeItem();
                                            }
                                            else
                                            {
                                                ItemStack copyIncoming = getItemInPedestal().copy();
                                                copyIncoming.setCount(leftTillFilled);
                                                invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).insertItem(i,copyIncoming,false);
                                                item.getStackInSlot(0).shrink(leftTillFilled);
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

}
