package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class ipuaCraftingReStocker extends ipuBasic {

    public ipuaCraftingReStocker()
    {

    }

    private int currentslot = 0;
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
                    if(world.getTileEntity(getPosOfBlockBelow(1)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
                    {
                        TileEntity invToPushTo = world.getTileEntity(getPosOfBlockBelow(1));
                        if(invToPushTo instanceof TilePedestal) {
                            itemToInv = ItemStack.EMPTY;
                        }
                        else {
                            if(hasItem())
                            {

                                //iterates through all slots in chest
                                if(currentslot<=invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots())
                                {
                                    itemToInv = invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot);
                                    //if items in slot has less then max size
                                    if(invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot).getCount() <
                                            invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot).getMaxStackSize())
                                    {
                                        //if stack is not empty (Avoid empty slots)
                                        if(!invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot).equals(ItemStack.EMPTY))
                                        {
                                            //if stacks are equal
                                            if(doItemsMatch(getItemInPedestal(),invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(currentslot)))
                                            {
                                                ItemStack copyPedestal = getItemInPedestal().copy();
                                                copyPedestal.setCount(1);
                                                invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).insertItem(currentslot,copyPedestal,false);
                                                this.removeItem(1);
                                            }
                                        }
                                    }

                                    currentslot++;
                                }
                                if(currentslot>invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots()-1)
                                {
                                    currentslot=0;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
