package com.mowmaster.dust.items.itemPedestalUpgrades;


import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.mowmaster.dust.misc.DustyTab.DUSTTABS;

public class ipuImport extends ipuBasic
{
    public int transferRate = 0;
    public int transferSpeed = 0;

    public ipuImport(String unlocName, String registryName)
    {
        this.setUnlocalizedName(unlocName);
        this.setRegistryName(new ResourceLocation(Reference.MODID, registryName));
        this.maxStackSize = 64;
        this.setCreativeTab(DUSTTABS);
        this.isFilter=false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public int getItemTransferRate(ItemStack stack)
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

    public int getTransferRate(ItemStack stack)
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

    private int getNextSlotWithItems(int range, TileEntity invBeingChecked, EnumFacing sideSlot, ItemStack stackInPedestal)
    {
        int slot = -1;
        for(int i=0;i<range;i++)
        {
            ItemStack stackInSlot = invBeingChecked.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,sideSlot).getStackInSlot(i);
            //find a slot with items
            if(!stackInSlot.isEmpty())
            {
                //If pedestal is empty accept any items
                if(stackInPedestal.isEmpty())
                {
                    slot=i;
                    break;
                }
                //if stack in pedestal matches items in slot
                else if(doItemsMatch(stackInPedestal,stackInSlot))
                {
                    slot=i;
                    break;
                }
            }
        }
        return slot;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getTransferRate(coinInPedestal);
        if (tick%speed == 0) {
            upgradeAction(world,pedestalPos,coinInPedestal);
        }
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int transferRate = getItemTransferRate(coinInPedestal);

            ItemStack itemFromInv = ItemStack.EMPTY;
            IInventory inventory = null;
            if(world.getTileEntity(posInventory) !=null)
            {
                if(world.getTileEntity(posInventory) instanceof IInventory)
                {
                    inventory = (IInventory) world.getTileEntity(posInventory);
                }
                if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)))
                {

                    TileEntity invToPullFrom = world.getTileEntity(posInventory);
                    if(invToPullFrom instanceof TilePedestal) {
                        itemFromInv = ItemStack.EMPTY;

                    }
                    else {
                        int[] slots = getSlotsForSide(world, posOfPedestal, inventory);

                        if(slots.length==0)
                        {
                            int i = getNextSlotWithItems(invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).getSlots(),invToPullFrom,getPedestalFacing(world, posOfPedestal),getStackInPedestal(world,posOfPedestal));
                            if(i>=0)
                            {
                                if(!invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).getStackInSlot(i).equals(ItemStack.EMPTY))
                                {
                                    itemFromInv = invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).getStackInSlot(i);
                                    int maxInSlot = invToPullFrom.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,getPedestalFacing(world, posOfPedestal)).getSlotLimit(i);
                                    if(canExtractItemFromSlot(inventory, getStackInPedestal(world,posOfPedestal ),i,getPedestalFacing(world,posOfPedestal )))
                                    {
                                        if(getStackInPedestal(world,posOfPedestal) == ItemStack.EMPTY)
                                        {
                                            if(itemFromInv.getCount() > transferRate)
                                            {
                                                ItemStack copyIncoming = itemFromInv.copy();
                                                copyIncoming.setCount(transferRate);
                                                TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                                if(pedestalInv instanceof TilePedestal) {
                                                    ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                                }
                                                int counted = itemFromInv.getCount()-transferRate;
                                                itemFromInv.setCount(counted);
                                            }
                                            else
                                            {
                                                ItemStack copyIncoming = itemFromInv.copy();
                                                TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                                if(pedestalInv instanceof TilePedestal) {
                                                    ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                                }
                                                itemFromInv.setCount(0);
                                            }
                                        }
                                        else if(doItemsMatch(getStackInPedestal(world,posOfPedestal),itemFromInv))
                                        {
                                            int leftTillFilled = 64 - getStackInPedestal(world,posOfPedestal).getCount();
                                            if(leftTillFilled > itemFromInv.getCount())
                                            {
                                                if(itemFromInv.getCount()> transferRate)
                                                {
                                                    ItemStack copyIncoming = itemFromInv.copy();
                                                    copyIncoming.setCount(transferRate);
                                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                                    if(pedestalInv instanceof TilePedestal) {
                                                        ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                                    }
                                                    int counted = itemFromInv.getCount()-transferRate;
                                                    itemFromInv.setCount(counted);
                                                }
                                                else
                                                {
                                                    ItemStack copyIncoming = itemFromInv.copy();
                                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
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
                                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
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
                                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
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
                        else
                        {
                            for(int j=0;j<slots.length;j++)
                            {
                                //System.out.println("J: "+j+" J=="+slots[j]);
                                //System.out.println("Item In Slot: "+inventory.getStackInSlot(slots[j]));
                                if(!inventory.getStackInSlot(slots[j]).isEmpty())
                                {
                                    itemFromInv = inventory.getStackInSlot(slots[j]);
                                    if(canExtractItemFromSlot(inventory, getStackInPedestal(world,posOfPedestal ),slots[j],getPedestalFacing(world,posOfPedestal )))
                                    {
                                        if(getStackInPedestal(world,posOfPedestal) == ItemStack.EMPTY)
                                        {
                                            if(itemFromInv.getCount() > transferRate)
                                            {
                                                ItemStack copyIncoming = itemFromInv.copy();
                                                copyIncoming.setCount(transferRate);
                                                TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                                if(pedestalInv instanceof TilePedestal) {
                                                    ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                                }
                                                int counted = itemFromInv.getCount()-transferRate;
                                                itemFromInv.setCount(counted);
                                            }
                                            else
                                            {
                                                ItemStack copyIncoming = itemFromInv.copy();
                                                TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                                if(pedestalInv instanceof TilePedestal) {
                                                    ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                                }
                                                itemFromInv.setCount(0);
                                            }
                                        }
                                        else if(doItemsMatch(getStackInPedestal(world,posOfPedestal),itemFromInv))
                                        {
                                            int leftTillFilled = 64 - getStackInPedestal(world,posOfPedestal).getCount();
                                            if(leftTillFilled > itemFromInv.getCount())
                                            {
                                                if(itemFromInv.getCount()> transferRate)
                                                {
                                                    ItemStack copyIncoming = itemFromInv.copy();
                                                    copyIncoming.setCount(transferRate);
                                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                                    if(pedestalInv instanceof TilePedestal) {
                                                        ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                                    }
                                                    int counted = itemFromInv.getCount()-transferRate;
                                                    itemFromInv.setCount(counted);
                                                }
                                                else
                                                {
                                                    ItemStack copyIncoming = itemFromInv.copy();
                                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
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
                                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
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
                                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int s2 = getItemTransferRate(stack);
        String s3 = "";

        switch (getTransferRate(stack))
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
        tooltip.add("Item Stack Import Upgrade");
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
