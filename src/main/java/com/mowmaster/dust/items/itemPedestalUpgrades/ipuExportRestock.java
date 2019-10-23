package com.mowmaster.dust.items.itemPedestalUpgrades;

import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuExportRestock extends ipuBasic
{
    public int transferRate = 0;
    public int transferSpeed = 0;

    public ipuExportRestock(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if(stack.getItem().equals(ItemRegistry.importUpgrade))
        {
            return super.isBookEnchantable(stack, book);
        }

        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
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

    public int getTransferSpeed(ItemStack stack)
    {
        switch (getTransferRateModifier(stack))
        {
            case 0:
                transferSpeed = 20;//normal speed
                break;
            case 1:
                transferSpeed=10;//2x faster
                break;
            case 2:
                transferSpeed = 5;//4x faster
                break;
            case 3:
                transferSpeed = 3;//6x faster
                break;
            case 4:
                transferSpeed = 2;//10x faster
                break;
            case 5:
                transferSpeed=1;//20x faster
                break;
            default: transferSpeed=20;
        }

        return  transferSpeed;
    }

    public int getSlotNumberNext(int currentSlotNumber, int range)
    {   int slots = currentSlotNumber;
        if((range-1)<=currentSlotNumber)
        {
            slots = 0;
        }
        else
        {
            slots = (currentSlotNumber+1);
        }
        return slots;
    }


//                          impTicker,this.world,   getItemInPedestal(),      getCoinOnPedestal(),     this.getPos()
    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getTransferSpeed(coinInPedestal);
        if (tick%speed == 0) {
            upgradeAction(world,pedestalPos,coinInPedestal);
        }
    }

    //Upgrade checks each slot and inserts if it can
    //only inserts into slots with items, will not fill blank slots
    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {

        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int upgradeTransferRate = getTransferRate(coinInPedestal);
        int i = getIntValueFromPedestal(world,posOfPedestal );
        ItemStack itemFromPedestal = ItemStack.EMPTY;
        IInventory inventory = null;


            //Checks to make sure a TE exists
            if(world.getTileEntity(posInventory) !=null)
            {
                if(world.getTileEntity(posInventory) instanceof IInventory)
                {
                    inventory = (IInventory) world.getTileEntity(posInventory);
                }
                //Checks to make sure TE is an inventory and can be pulled from facing side
                if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)))
                {
                    //Gets inventory TE then makes sure its not a pedestal
                    TileEntity invToPushInto = world.getTileEntity(posInventory);
                    if(invToPushInto instanceof TilePedestal) {
                        itemFromPedestal = ItemStack.EMPTY;

                    }
                    else {

                        if(!world.isBlockPowered(posOfPedestal))
                        {
                            if(world.getTileEntity(posInventory) !=null)
                            {
                                if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)))
                                {
                                    TileEntity invToPushTo = world.getTileEntity(posInventory);
                                    if(invToPushTo instanceof TilePedestal) {
                                        itemFromPedestal = ItemStack.EMPTY;
                                    }
                                    else {
                                        itemFromPedestal = getStackInPedestal(world,posOfPedestal);
                                        if(!itemFromPedestal.isEmpty())
                                        {
                                            //get slot from pedestal
                                            if(i>=0)
                                            {
                                                ItemStack stackInSlotI = invToPushInto.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).getStackInSlot(i);
                                                //Just checks to see if item can be inserted into slot(doesnt check stack size or if they match???)
                                                if(canInsertItemInSlot(inventory,itemFromPedestal,i,getPedestalFacing(world, posOfPedestal)))
                                                {
                                                    itemFromPedestal = getStackInPedestal(world,posOfPedestal).copy();
                                                    //if Slot is Empty

                                                    if(doItemsMatch(itemFromPedestal,stackInSlotI))
                                                    {
                                                        //if stacks match, then we need to add to stack but not over fill it
                                                        //check if pedestal can send full transfer
                                                        int transfersize = upgradeTransferRate;
                                                        if(transfersize >= itemFromPedestal.getCount())
                                                        {
                                                            transfersize = itemFromPedestal.getCount();
                                                        }
                                                        //check if selected stack can hold a full transfer
                                                        if (stackInSlotI.getMaxStackSize() >= (transfersize+stackInSlotI.getCount()))
                                                        {
                                                            if(itemFromPedestal.getCount()>=transfersize)
                                                            {
                                                                itemFromPedestal.setCount(transfersize);
                                                            }
                                                            removeFromPedestal(world,posOfPedestal ,itemFromPedestal.getCount() );
                                                            invToPushInto.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).insertItem(i,itemFromPedestal,false );

                                                            int slotnext = getSlotNumberNext(i,invToPushInto.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).getSlots());
                                                            setIntValueToPedestal(world,posOfPedestal ,slotnext);
                                                        }
                                                        //if it cant handle a full transfer we need to modify transfer size
                                                        else
                                                        {

                                                            transfersize = stackInSlotI.getMaxStackSize()-stackInSlotI.getCount();
                                                            if(itemFromPedestal.getCount()>=transfersize)
                                                            {
                                                                itemFromPedestal.setCount(transfersize);
                                                            }
                                                            removeFromPedestal(world,posOfPedestal ,itemFromPedestal.getCount() );
                                                            invToPushInto.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).insertItem(i,itemFromPedestal,false );

                                                            int slotnext = getSlotNumberNext(i,invToPushInto.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).getSlots());
                                                            setIntValueToPedestal(world,posOfPedestal ,slotnext);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        int slotnext = getSlotNumberNext(i,invToPushInto.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).getSlots());
                                                        setIntValueToPedestal(world,posOfPedestal ,slotnext);
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getTransferRate(stack);
        String s3 = "";

        switch (getTransferSpeed(stack))
            {
            case 1:
                s3 = "20x Faster";
                break;
            case 2:
                s3="10x Faster";
                break;
            case 3:
                s3 = "6x Faster";
                break;
            case 5:
                s3 = "4x Faster";
                break;
            case 10:
                s3 = "2x Faster";
                break;
            case 20:
                s3="Normal Speed";
                break;
            default: s3="Normal Speed";
        }


        String tr = "" + s2 + "";
        String trr = s3;
        tooltip.add("Item Restock Upgrade");
        if(stack.hasTagCompound())
        {
            if(stack.getTagCompound().hasKey("coineffect"))
            {
                tooltip.add("Transfer Rate: " + tr);
            }
            else
            {
                tooltip.add("Transfer Rate: 1");
            }

            if(stack.isItemEnchanted() && getTransferRate(stack) >0)
            {
                tooltip.add("Transfer Speed: " + trr);
            }
            else
            {
                tooltip.add("Transfer Speed: Normal Speed");
            }
        }
        else
        {
            tooltip.add("Transfer Rate: 1");
            tooltip.add("Transfer Speed: Normal Speed");
        }
    }

}
