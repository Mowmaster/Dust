package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.blocks.machines.BlockPedestal;
import com.mowmaster.dust.items.itemPedestalUpgrades.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TilePedestal extends TileEntityBase implements ITickable, ICapabilityProvider
{
    private ItemStackHandler item;
    private ItemStackHandler coin;
    private static final int[] SLOTS_ALLSIDES = new int[] {0};
    public ItemStack display = ItemStack.EMPTY;

    private int storedValueForUpgrades = 0;
    private int intTransferAmount = 4;
    private int intTransferSpeed = 20;
    private int intTransferRange = 8;
    private final List<BlockPos> storedLocations = new ArrayList<BlockPos>();

    public TilePedestal()
    {
        this.item = new ItemStackHandler(1);
        this.coin = new ItemStackHandler(1);
    }

    public int getNumberOfStoredLocations() {return storedLocations.size();}
    public boolean storeNewLocation(BlockPos pos)
    {
        boolean returner = false;
        if(getNumberOfStoredLocations() < 8)
        {
            storedLocations.add(pos);
            returner=true;
        }

        return returner;
    }

    public BlockPos getStoredPositionAt(int index)
    {
        BlockPos sendToPos = getPos();
        if(index<getNumberOfStoredLocations())
        {
            sendToPos = storedLocations.get(index);
        }

        return sendToPos;
    }

    public boolean removeLocation(BlockPos pos)
    {
        boolean returner = false;
        if(getNumberOfStoredLocations() >= 1)
        {
            storedLocations.remove(pos);
            returner=true;
        }

        return returner;
    }

    public boolean isAlreadyLinked(BlockPos pos) {
        return storedLocations.contains(pos);
    }

    public String debugLocationList()
    {
        String lists = "";
        for(int i=0;i<getNumberOfStoredLocations();i++)
        {
            lists = lists + storedLocations.get(i).toString() + ", ";
        }
        return lists;
    }

    public int getStoredValueForUpgrades()
    {
        return storedValueForUpgrades;
    }
    public void setStoredValueForUpgrades(int value)
    {
        storedValueForUpgrades = value;
    }
    public int getPedestalTransferAmount(){return intTransferAmount;}
    public void setPedestalTransferAmount(int transferAmount){intTransferAmount = transferAmount;}
    public int getPedestalTransferSpeed(){return intTransferSpeed;}
    public void setPedestalTransferSpeed(int transferSpeed){intTransferSpeed = transferSpeed;}
    public int getPedestalTransferRange(){return intTransferRange;}
    public void setPedestalTransferRange(int transferSpeed){intTransferRange = transferSpeed;}
    public int spaceInPedestal()
    {
        int space = 0;

        if(getItemInPedestal().isEmpty() || getItemInPedestal().equals(ItemStack.EMPTY))
        {
            space = 64;
        }
        else
        {
            space = (getMaxStackSize() - getItemInPedestal().getCount());
        }

        return space;
    }

    public boolean hasItem()
    {
        if(item.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else  return true;
    }
    public boolean hasCoin()
    {
        if(coin.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else  return true;
    }
    public ItemStack getItemInPedestal()
    {
        if(hasItem())
        {
            return item.getStackInSlot(0);
        }
        else return ItemStack.EMPTY;
    }
    public ItemStack getCoinOnPedestal()
    {
        if(hasCoin())
        {
            return coin.getStackInSlot(0);
        }
        else return ItemStack.EMPTY;
    }
    public ItemStack getDisplay() {return display;}


    public ItemStack removeItem(int numToRemove) {
        ItemStack stack = item.extractItem(0,numToRemove,false);
        updateBlock();
        return stack;
    }

    public ItemStack removeItem() {
        ItemStack stack = item.extractItem(0,item.getStackInSlot(0).getCount(),false);
        updateBlock();
        return stack;
    }

    public ItemStack removeCoin() {
        ItemStack stack = coin.extractItem(0,coin.getStackInSlot(0).getCount(),false);
        setStoredValueForUpgrades(0);
        updateBlock();
        return stack;
    }

    public int getItemTransferRate()
    {
        int itemRate = 4;
        switch (intTransferAmount)
        {
            case 0:
                itemRate = 4;
                break;
            case 1:
                itemRate=8;
                break;
            case 2:
                itemRate = 16;
                break;
            case 3:
                itemRate = 32;
                break;
            case 4:
                itemRate = 48;
                break;
            case 5:
                itemRate=64;
                break;
            default: itemRate=4;
        }

        return  itemRate;
    }

    public int getOperationSpeed()
    {
        int speed = 20;
        switch (intTransferSpeed)
        {
            case 0:
                speed = 20;//normal speed
                break;
            case 1:
                speed=10;//2x faster
                break;
            case 2:
                speed = 5;//4x faster
                break;
            case 3:
                speed = 3;//6x faster
                break;
            case 4:
                speed = 2;//10x faster
                break;
            case 5:
                speed=1;//20x faster
                break;
            default: speed=20;
        }

        return  speed;
    }

    public int getMaxStackSize(){return 64;}

    public boolean addItem(ItemStack itemFromBlock)
    {
        if(hasItem())
        {
            if(doItemsMatch(itemFromBlock))
            {
                item.insertItem(0, itemFromBlock.copy(), false);
            }
        }
        else {item.insertItem(0, itemFromBlock.copy(), false);}
        updateBlock();
        return true;
    }

    public boolean addCoin(ItemStack coinFromBlock)
    {
        ItemStack itemFromBlock = coinFromBlock.copy();
        itemFromBlock.setCount(1);
        if(hasCoin()){} else coin.insertItem(0,itemFromBlock,false);
        setStoredValueForUpgrades(0);
        updateBlock();
        return true;
    }

    public boolean doItemsMatch(ItemStack itemStackIn)
    {
        if(hasItem())
        {
            if(itemStackIn.getHasSubtypes())
            {
                if(itemStackIn.getItem().equals(item.getStackInSlot(0).getItem()) && itemStackIn.getMetadata()==item.getStackInSlot(0).getMetadata())
                {
                    return true;
                }
                else return false;
            }
            else if(itemStackIn.hasTagCompound())
            {
                NBTTagCompound itemIn = itemStackIn.getTagCompound();
                NBTTagCompound itemStored = item.getStackInSlot(0).getTagCompound();
                if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(item.getStackInSlot(0).getItem()))
                {
                    return true;
                }
                else return false;
            }
            else
            {
                if(itemStackIn.getItem().equals(item.getStackInSlot(0).getItem()))
                {
                    return true;
                }
            }
        }
        else{return true;}


        return false;
    }


    public boolean isSamePedestal(BlockPos pedestalToBeLinked)
    {
        BlockPos thisPedestal = this.getPos();

        if(thisPedestal.equals(pedestalToBeLinked))
        {
            return true;
        }

        return false;
    }

    //Checks when linking pedestals if the two being linked are 1.on the same network 2. if one is neutral thus meaning they can link
    public boolean canLinkToPedestalNetwork(BlockPos pedestalToBeLinked)
    {
        //Check to see if pedestal to be linked is a block pedestal
        if(world.getBlockState(pedestalToBeLinked).getBlock() instanceof BlockPedestal)
        {
            //checks to see if pedestal to be linked and this one are the same
            if(world.getBlockState(pedestalToBeLinked).getBlock().equals(world.getBlockState(this.getPos()).getBlock()))
            {
                return true;
            }
            //if they arnt then check if one is neutral
            else if(world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockPedestal.pedestalstone) ||
                    world.getBlockState(this.getPos()).getBlock().equals(BlockPedestal.pedestalstone))
            {
                return true;
            }
            else return false;
        }

        return false;
    }

    //Returns items available to be insert, 0 if false
    public int canAcceptItems(ItemStack itemsIncoming)
    {
        int canAccept = 0;

        if(getItemInPedestal().isEmpty() || getItemInPedestal().equals(ItemStack.EMPTY))
        {
            canAccept = 64;
        }
        else
        {
            if(doItemsMatch(itemsIncoming))
            {
                if(getItemInPedestal().getCount() < getMaxStackSize())
                {
                    canAccept = (getMaxStackSize() - getItemInPedestal().getCount());
                }
            }
        }

        return canAccept;
    }

    public boolean hasFilter(TilePedestal pedestalSendingTo)
    {
        boolean returner = false;
        if(pedestalSendingTo.hasCoin())
        {
            Item coinInPed = pedestalSendingTo.getCoinOnPedestal().getItem();
            if(coinInPed instanceof ipuBasic)
            {
                if(((ipuBasic) coinInPed).isFilter)
                {
                    returner = true;
                }
            }
        }

        return returner;
    }

    private boolean canSendToPedestal(BlockPos pedestalToSendTo)
    {
        boolean returner = false;

        //Check if Block is Loaded in World
        if(world.isBlockLoaded(pedestalToSendTo,false))
        {
            //If block ISNT powered
            if(!world.isBlockPowered(pedestalToSendTo))
            {
                //Make sure its a pedestal before getting the tile
                if(world.getBlockState(pedestalToSendTo).getBlock() instanceof BlockPedestal)
                {
                    //Get the tile before checking other things
                    if(world.getTileEntity(pedestalToSendTo) instanceof TilePedestal)
                    {
                        TilePedestal tilePedestalToSendTo = (TilePedestal)world.getTileEntity(pedestalToSendTo);

                        //Checks if pedestal is empty or if not then checks if items match and how many can be insert
                        if(tilePedestalToSendTo.canAcceptItems(getItemInPedestal()) > 0)
                        {
                            //Check if it has filter, if not return true
                            if(hasFilter(tilePedestalToSendTo))
                            {
                                Item coinInPed = tilePedestalToSendTo.getCoinOnPedestal().getItem();
                                if(coinInPed instanceof ipuBasic)
                                {
                                    //Already checked if its a filter, so now check if it can accept items.
                                    if(((ipuBasic) coinInPed).canAcceptItem(getItemInPedestal()))
                                    {
                                        returner = true;
                                    }
                                }
                            }
                            else
                            {
                                returner = true;
                            }
                        }
                    }
                }
            }
        }

        return returner;
    }

    public void sendItemsToPedestal(BlockPos pedestalToSendTo)
    {
        if(world.getTileEntity(pedestalToSendTo) instanceof TilePedestal)
        {
            TilePedestal tileToSendTo = ((TilePedestal)world.getTileEntity(pedestalToSendTo));

            //Max that can be recieved
            int countToSend = tileToSendTo.spaceInPedestal();
            ItemStack copyStackToSend = getItemInPedestal().copy();
            //Max that is available to send
            if(copyStackToSend.getCount()<countToSend)
            {
                countToSend = copyStackToSend.getCount();
            }
            //Get max that can be sent
            if(countToSend > getItemTransferRate())
            {
                countToSend = getItemTransferRate();
            }


            if(countToSend >=1)
            {
                //Send items
                copyStackToSend.setCount(countToSend);
                removeItem(copyStackToSend.getCount());
                tileToSendTo.addItem(copyStackToSend);
                tileToSendTo.updateBlock();
            }
        }
    }












    //Needed for Rendering Tile Stuff
    public boolean isBlockUnder(int x,int y,int z)
    {
        TileEntity tileEntity = world.getTileEntity(pos.add(x,y,z));
        if(tileEntity instanceof ICapabilityProvider)
        {
            return true;
        }
        return false;
    }



    int impTicker = 0;
    int pedTicker = 0;
    @Override
    public void update() {

        if(!world.isRemote)
        {
            if(world.isBlockLoaded(pos))
            {
                int speed = getOperationSpeed();
                if(speed<1){speed = 20;}

                //dont bother unless pedestal has items in it.
                if(!getItemInPedestal().isEmpty())
                {
                    if(!world.isBlockPowered(pos))
                    {
                        if(getNumberOfStoredLocations()>0)
                        {
                            pedTicker++;
                            if (pedTicker%speed == 0) {

                                for(int i=0; i<getNumberOfStoredLocations();i++)
                                {
                                    if(getStoredPositionAt(i) != getPos())
                                    {
                                        //check for any slots that can accept items if not then keep trying
                                        if(canSendToPedestal(getStoredPositionAt(i)))
                                        {
                                            //Once a slot is found and items transfered, stop loop(so it restarts next check)
                                            sendItemsToPedestal(getStoredPositionAt(i));
                                            break;
                                        }
                                    }
                                }
                                if(pedTicker >=20){pedTicker=0;}
                            }
                        }
                    }
                }

                if(hasCoin())
                {
                    Item coinInPed = getCoinOnPedestal().getItem();
                    if(coinInPed instanceof ipuBasic)
                    {
                        impTicker++;
                        ((ipuBasic) coinInPed).updateAction(impTicker,this.world,getItemInPedestal(),getCoinOnPedestal(),this.getPos());
                        if(impTicker >=200){impTicker=0;}
                    }
                }

                
                display = getItemInPedestal();
                updateBlock();
            }
        }

        if(world.isRemote)
        {
            if(hasCoin())
            {
                Item coinInPed = getCoinOnPedestal().getItem();
                if(coinInPed instanceof ipuBasic)
                {
                    Random rand = new Random();
                    ((ipuBasic) coinInPed).onRandomDisplayTick(this, world.getBlockState(getPos()), world, getPos(), rand);
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("ItemStackItemInventoryHandler", this.item.serializeNBT());
        compound.setTag("ItemStackCoinInventoryHandler", this.coin.serializeNBT());
        compound.setTag("display",display.writeToNBT(new NBTTagCompound()));
        compound.setInteger("storedUpgradeValue",storedValueForUpgrades);
        compound.setInteger("intTransferAmount",intTransferAmount);
        compound.setInteger("intTransferSpeed",intTransferSpeed);
        compound.setInteger("intTransferRange",intTransferRange);
        int counter = 0;
        for(int i=0;i<getNumberOfStoredLocations();i++)
        {
            String keyNameX = "storedLocationX" + i;
            String keyNameY = "storedLocationY" + i;
            String keyNameZ = "storedLocationZ" + i;
            compound.setInteger(keyNameX,storedLocations.get(i).getX());
            compound.setInteger(keyNameY,storedLocations.get(i).getY());
            compound.setInteger(keyNameZ,storedLocations.get(i).getZ());
            counter++;
        }
        compound.setInteger("storedBlockPosCounter",counter);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.item.deserializeNBT(compound.getCompoundTag("ItemStackItemInventoryHandler"));
        this.coin.deserializeNBT(compound.getCompoundTag("ItemStackCoinInventoryHandler"));
        NBTTagCompound itemTagD = compound.getCompoundTag("display");
        this.storedValueForUpgrades = compound.getInteger("storedUpgradeValue");
        this.intTransferAmount = compound.getInteger("intTransferAmount");
        this.intTransferSpeed = compound.getInteger("intTransferSpeed");
        this.intTransferRange = compound.getInteger("intTransferRange");

        int counter = compound.getInteger("storedBlockPosCounter");
        for(int i=0;i<counter;i++)
        {
            String getKeyNameX = "storedLocationX" + i;
            String getKeyNameY = "storedLocationY" + i;
            String getKeyNameZ = "storedLocationZ" + i;
            int getX = compound.getInteger(getKeyNameX);
            int getY = compound.getInteger(getKeyNameY);
            int getZ = compound.getInteger(getKeyNameZ);
            BlockPos gotPos = new BlockPos(getX,getY,getZ);
            storeNewLocation(gotPos);
        }

        this.display = new ItemStack(itemTagD);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        //if(!hasCoin())
        //{
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this.item;

        //}
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        //if(!hasCoin())
        //{
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        //}
        return super.hasCapability(capability, facing);
    }
}