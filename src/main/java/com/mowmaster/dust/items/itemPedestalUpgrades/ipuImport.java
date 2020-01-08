package com.mowmaster.dust.items.itemPedestalUpgrades;


import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

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
        return super.isBookEnchantable(stack, book);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
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

    private int getNextSlotWithItems(TileEntity invBeingChecked, EnumFacing sideSlot, ItemStack stackInPedestal)
    {
        int slot = -1;

        if(invBeingChecked.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,sideSlot)) {
            IItemHandlerModifiable handler = (IItemHandlerModifiable) invBeingChecked.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideSlot);
            int range = handler.getSlots();
            for(int i=0;i<range;i++)
            {
                ItemStack stackInSlot = handler.getStackInSlot(i);
                //find a slot with items
                if(!stackInSlot.isEmpty())
                {
                    //check if it could pull the item out or not
                    if(!handler.extractItem(i,1 ,true ).equals(ItemStack.EMPTY))
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
            }
        }

        return slot;
    }

    public void updateAction(int tick, World world, ItemStack itemInPedestal, ItemStack coinInPedestal,BlockPos pedestalPos)
    {
        int speed = getTransferRate(coinInPedestal);

        if(!world.isBlockPowered(pedestalPos))
        {
            if (tick%speed == 0) {
                upgradeAction(world,pedestalPos,coinInPedestal);
            }
        }
    }

    public void upgradeAction(World world, BlockPos posOfPedestal, ItemStack coinInPedestal)
    {
        BlockPos posInventory = getPosOfBlockBelow(world,posOfPedestal,1);
        int transferRate = getItemTransferRate(coinInPedestal);

            ItemStack itemFromInv = ItemStack.EMPTY;
            if(world.getTileEntity(posInventory) !=null)
            {
                if(world.getTileEntity(posInventory).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal)))
                {
                    IItemHandlerModifiable handler = (IItemHandlerModifiable) world.getTileEntity(posInventory).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getPedestalFacing(world, posOfPedestal));
                    TileEntity invToPullFrom = world.getTileEntity(posInventory);
                    if(invToPullFrom instanceof TilePedestal) {
                        itemFromInv = ItemStack.EMPTY;

                    }
                    else {
                        if(handler != null)
                        {
                            int i = getNextSlotWithItems(invToPullFrom,getPedestalFacing(world, posOfPedestal),getStackInPedestal(world,posOfPedestal));
                            if(i>=0)
                            {
                                int maxInSlot = handler.getSlotLimit(i);
                                itemFromInv = handler.getStackInSlot(i);
                                ItemStack itemFromPedestal = getStackInPedestal(world,posOfPedestal);
                                if(handler.getStackInSlot(i) != null && !handler.getStackInSlot(i).isEmpty() && handler.getStackInSlot(i).getItem() != Items.AIR)
                                {
                                    int roomLeftInPedestal = 64-itemFromPedestal.getCount();
                                    if(itemFromPedestal.isEmpty() || itemFromPedestal.equals(ItemStack.EMPTY)) roomLeftInPedestal = 64;
                                    int itemCountInInv = itemFromInv.getCount();
                                    int allowedTransferRate = transferRate;
                                    //Checks to see if pedestal can accept as many items as transferRate IF NOT it sets the new rate to what it can accept
                                    if(roomLeftInPedestal < transferRate) allowedTransferRate = roomLeftInPedestal;
                                    //Checks to see how many items are left in the slot IF ITS UNDER the allowedTransferRate then sent the max rate to that.
                                    if(itemCountInInv < allowedTransferRate) allowedTransferRate = itemCountInInv;

                                    ItemStack copyIncoming = itemFromInv.copy();
                                    copyIncoming.setCount(allowedTransferRate);
                                    TileEntity pedestalInv = world.getTileEntity(posOfPedestal);
                                    if(pedestalInv instanceof TilePedestal) {
                                        handler.extractItem(i,allowedTransferRate ,false );
                                        ((TilePedestal) pedestalInv).addItem(copyIncoming);
                                    }
                                }
                            }
                        }
                    }
                }
            }

    }

    @Override
    public void actionOnColideWithBlock(World world, TilePedestal tilePedestal, BlockPos posPedestal, IBlockState state, Entity entityIn)
    {
        if(entityIn instanceof EntityItem)
        {
            ItemStack getItemStack = ((EntityItem) entityIn).getItem();
            ItemStack itemFromPedestal = getStackInPedestal(world,posPedestal);
            if(itemFromPedestal.isEmpty())
            {
                TileEntity pedestalInv = world.getTileEntity(posPedestal);
                if(pedestalInv instanceof TilePedestal) {
                    entityIn.setDead();
                    ((TilePedestal) pedestalInv).addItem(getItemStack);
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
        tooltip.add(TextFormatting.GOLD + "Item Stack Import Upgrade");
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
