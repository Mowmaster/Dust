package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExportRestock extends ipuBasic
{
    public int transferRate = 0;

    public ipuExportRestock(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    public int getTransferRate(ItemStack stack)
    {
        switch (getRateModifier(PotionRegistry.POTION_VOIDSTORAGE,stack))
        {
            case 0:
                transferRate = 1;
                break;
            case 1:
                transferRate=4;
                break;
            case 2:
                transferRate = 8;
                break;
            case 3:
                transferRate = 16;
                break;
            case 4:
                transferRate = 32;
                break;
            case 5:
                transferRate=64;
                break;
            default: transferRate=1;
        }

        return  transferRate;
    }

    public int getSlotNumberNext(int currentSlotNumber, int range, ItemStack inPedestal, ItemStack inInventory)
    {
        int slotToReturn=-1;
        //We just used current slot so add one and start there with finding the next slot
        int slots = currentSlotNumber+1;
        int ranger = range;
        if(slots>=ranger)
        {
            slots=0;
        }

        for(int i=slots;i<ranger;i++)
        {
            if(doItemsMatch(inPedestal,inInventory ))
            {
                slotToReturn=i;
                break;
            }
        }

        //If above loop fails then try it from the beginning
        if(slotToReturn == -1)
        {
            for(int i=0;i<ranger;i++)
            {
                if(doItemsMatch(inPedestal,inInventory ))
                {
                    slotToReturn=i;
                    break;
                }
            }
        }

        //if all else fails return -1 (next time it will start the loop at 0)
        return slots;
    }


//                          impTicker,this.world,   getItemInPedestal(),      getCoinOnPedestal(),     this.getPos()
    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getOperationSpeed(coinInPedestal);

        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world,pedestalPos,coinInPedestal);
            }
        }
    }

    //Upgrade checks each slot and inserts if it can
    //only inserts into slots with items, will not fill blank slots
    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        int i = getIntValueFromPedestal(world,posOfPedestal );
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int upgradeTransferRate = getTransferRate(coinInPedestal);
        ItemStack itemFromPedestal = ItemStack.EMPTY;
        //Checks to make sure a TE exists
        if(world.getTileEntity(posInventory) !=null)
        {
            //Gets inventory TE then makes sure its not a pedestal
            TileEntity invToPushTo = world.getTileEntity(posInventory);
            if(invToPushTo instanceof TilePedestal) {
                itemFromPedestal = ItemStack.EMPTY;
            }
            else {
                itemFromPedestal = getStackInPedestal(world,posOfPedestal);
                //IF pedestal is empty and has nothing to transfer then dont do anything
                if(!itemFromPedestal.isEmpty() && !itemFromPedestal.equals(ItemStack.EMPTY))
                {
                    if(invToPushTo.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)))
                    {
                        IItemHandler handler = (IItemHandler) invToPushTo.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));

                        //gets next empty or partially filled matching slot
                        if(handler != null)
                        {
                            ItemStack itemFromInventory = ItemStack.EMPTY;
                            itemFromPedestal = getStackInPedestal(world,posOfPedestal).copy();
                            if(i < handler.getSlots() && handler.getStackInSlot(i) != null)
                            {
                                itemFromInventory = handler.getStackInSlot(i);
                            }

                            if(i>=0 && i < handler.getSlots())
                            {
                                if(handler.isItemValid(i, itemFromPedestal))
                                {
                                    int spaceInInventoryStack = handler.getSlotLimit(i) - itemFromInventory.getCount();

                                    //if inv slot is empty it should be able to handle as much as we can give it
                                    int allowedTransferRate = upgradeTransferRate;
                                    //checks allowed slot size amount and sets it if its lower then transfer rate
                                    if(handler.getSlotLimit(i) <= allowedTransferRate) allowedTransferRate = handler.getSlotLimit(i);
                                    //never have to check to see if pedestal and stack match because the slot checker does it for us
                                    //if our transfer rate is bigger then what can go in the slot if its partially full we set the transfer size to what can fit
                                    //Otherwise if space is bigger then rate we know it can accept as much as we're putting in
                                    if(allowedTransferRate> spaceInInventoryStack) allowedTransferRate = spaceInInventoryStack;
                                    //IF items in pedestal are less then the allowed transfer amount then set it as the amount
                                    if(allowedTransferRate > itemFromPedestal.getCount()) allowedTransferRate = itemFromPedestal.getCount();



                                    //After all calculations for transfer rate, set stack size to transfer and transfer the items
                                    int slotnext = getSlotNumberNext(i,handler.getSlots(),itemFromPedestal,itemFromInventory);
                                    if(doItemsMatch(itemFromPedestal,itemFromInventory ))
                                    {
                                        itemFromPedestal.setCount(allowedTransferRate);
                                        if(handler.insertItem(i,itemFromPedestal,true ).equals(ItemStack.EMPTY)){
                                            removeFromPedestal(world,posOfPedestal ,allowedTransferRate);
                                            handler.insertItem(i,itemFromPedestal,false );
                                            setIntValueToPedestal(world,posOfPedestal ,slotnext);
                                        }
                                    }
                                    else
                                    {
                                        setIntValueToPedestal(world,posOfPedestal ,slotnext);
                                    }

                                }
                            }
                            else
                            {
                                int slotnext = getSlotNumberNext(i,handler.getSlots(),itemFromPedestal,itemFromInventory);
                                setIntValueToPedestal(world,posOfPedestal ,slotnext);
                            }
                        }
                    }
                }
            }
        }


    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String tr = "" + getTransferRate(stack) + "";
        String s5 = getOperationSpeedString(stack);

        tooltip.add(TextFormatting.GOLD + "Item Restock Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add(TextFormatting.GRAY + "Transfer Rate: " + tr);
            }
            else
            {
                tooltip.add(TextFormatting.GRAY + "Transfer Rate: 1");
            }

            if(stack.isItemEnchanted() && getOperationSpeed(stack) >0)
            {
                tooltip.add(TextFormatting.RED + "Operational Speed: " + s5);
            }
            else
            {
                tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add(TextFormatting.GRAY + "Transfer Rate: 1");
            tooltip.add(TextFormatting.RED + "Operational Speed: Normal Speed");
        }
    }

}
