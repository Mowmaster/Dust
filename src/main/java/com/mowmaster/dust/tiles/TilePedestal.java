package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TilePedestal extends TileEntity implements ITickable, ICapabilityProvider
{
    private ItemStackHandler item;
    private ItemStackHandler coin;
    private static final int[] SLOTS_ALLSIDES = new int[] {0};
    public ItemStack display = ItemStack.EMPTY;
    private int transferSizeCount = 4;
    private static int getPedestalRange = 8;
    private int ticker=0;
    private int ticker2=0;
    private int ticker3=0;
    private static final BlockPos defaultPos = new BlockPos(0,-2000,0);
    public BlockPos[] storedOutputLocations = {defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos,defaultPos};

    public TilePedestal()
    {
        this.item = new ItemStackHandler(1);
        this.coin = new ItemStackHandler(1);
    }
    public ItemStack getItemInPedestal() {return item.getStackInSlot(0);}
    public ItemStack getCoinOnPedestal() {return coin.getStackInSlot(0);}
    public ItemStack getDisplay() {return display;}

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

    public boolean doItemsMatch(ItemStack itemStackIn, ItemStack toCompareTo)
    {
        if(hasItem())
        {
            if(itemStackIn.getHasSubtypes())
            {
                if(itemStackIn.getItem().equals(toCompareTo.getItem()) && itemStackIn.getMetadata()==toCompareTo.getMetadata())
                {
                    return true;
                }
                else return false;
            }
            else if(itemStackIn.hasTagCompound())
            {
                NBTTagCompound itemIn = itemStackIn.getTagCompound();
                NBTTagCompound itemStored = toCompareTo.getTagCompound();
                if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(toCompareTo.getItem()))
                {
                    return true;
                }
                else return false;
            }
            else
            {
                if(itemStackIn.getItem().equals(toCompareTo.getItem()))
                {
                    return true;
                }
            }
        }
        else{return true;}


        return false;
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

    public boolean hasFilterUpgrade(TilePedestal getReciever)
    {
        if(getReciever.hasCoin())
        {
            if(getReciever.getCoinOnPedestal().getItem().equals(ItemRegistry.filterUpgrade))
            {
                return true;
            }
        }

        return true;
    }

    private void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.getRedstonePower(pos,EnumFacing.UP);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
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
        if(hasCoin()){} else coin.insertItem(0,coinFromBlock.copy(),false);
        updateBlock();
        return true;
    }

    public boolean addOutputLocation(BlockPos getWrench)
    {

        if(canLinkToPedestalNetwork(getWrench))
        {
            for(int i=0;i<storedOutputLocations.length;i++)
            {
                if(getWrench == storedOutputLocations[i])
                {
                    return false;
                }
                else if(defaultPos == storedOutputLocations[i])
                {

                    storedOutputLocations[i] = getWrench;
                    return true;
                }
                System.out.println(storedOutputLocations[i]);
            }
            return false;
        }
        else return false;

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
            else if(world.getBlockState(pedestalToBeLinked).getBlock().equals(BlockRegistry.pedestalstone) ||
                    world.getBlockState(this.getPos()).getBlock().equals(BlockRegistry.pedestalstone))
            {
                return true;
            }
            else return false;
        }

        return false;
    }

    public boolean hasConnectionAlready(BlockPos pedestalToBeLinked)
    {
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            if(storedOutputLocations[i].equals(pedestalToBeLinked))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isSamePedestal(BlockPos pedestalToBeLinked)
    {
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            if(storedOutputLocations[i].equals(this.getPos()))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isPedestalInRange(BlockPos pedestalToBeLinked)
    {
        int x = pedestalToBeLinked.getX();
        int y = pedestalToBeLinked.getY();
        int z = pedestalToBeLinked.getZ();
        int x1 = this.getPos().getX();
        int y1 = this.getPos().getY();
        int z1 = this.getPos().getZ();
        int xF = Math.abs(Math.subtractExact(x,x1));
        int yF = Math.abs(Math.subtractExact(y,y1));
        int zF = Math.abs(Math.subtractExact(z,z1));

        if(xF>getPedestalRange || yF>getPedestalRange || zF>getPedestalRange)
        {
            return false;
        }
        else return true;
    }

    public int numConnections()
    {
        int connections = 0;
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            if(!storedOutputLocations[i].equals(defaultPos))
            {
                connections++;
            }
        }
        return connections;
    }

    private boolean doesRecieverExistandIsLoaded(BlockPos getRecieverPos)
    {
        if(world.getBlockState(getRecieverPos)!=null)
        {
            if(world.isBlockLoaded(getRecieverPos))
            {
                return true;
            }
            else return false;
        }
        else return false;
    }

    public Boolean areFilteredItemsEqual(TilePedestal recievingPedestal, ItemStack itemStackIn)
    {
        BlockPos filterInv = recievingPedestal.getPosOfBlockBelow();
        if(world.getTileEntity(filterInv).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
        {
            TileEntity tile = world.getTileEntity(filterInv);
            ItemStack stackInFilter = ItemStack.EMPTY;

            for(int i=0;i<tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots();i++)
            {
                if(!tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i).equals(ItemStack.EMPTY))
                {
                    stackInFilter = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getStackInSlot(i);

                    if(itemStackIn.getHasSubtypes())
                    {
                        if(itemStackIn.getItem().equals(stackInFilter.getItem()) && itemStackIn.getMetadata()==stackInFilter.getMetadata())
                        {
                            return true;
                        }
                    }
                    else if(itemStackIn.hasTagCompound())
                    {
                        NBTTagCompound itemIn = itemStackIn.getTagCompound();
                        NBTTagCompound itemStored = stackInFilter.getTagCompound();
                        if(itemIn.equals(itemStored) && itemStackIn.getItem().equals(stackInFilter.getItem()))
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if(itemStackIn.getItem().equals(stackInFilter.getItem()))
                        {
                            return true;
                        }
                    }
                }
                else return false;
            }

        }

        return false;
    }

    private boolean sendItemsToReciever(int listIndex)
    {
        if(!storedOutputLocations[listIndex].equals(defaultPos))
        {
            if(doesRecieverExistandIsLoaded(storedOutputLocations[listIndex]))
            {
                TileEntity tileEntity = world.getTileEntity(storedOutputLocations[listIndex]);
                if (tileEntity instanceof TilePedestal) {
                    TilePedestal tileRecieverPedestal = (TilePedestal) tileEntity;

                    if(tileRecieverPedestal.hasCoin())
                    {
                        if(tileRecieverPedestal.hasFilterUpgrade(tileRecieverPedestal))
                        {
                            if(tileRecieverPedestal.hasFilterableInventoryBelow())
                            {

                                if(tileRecieverPedestal.areFilteredItemsEqual(tileRecieverPedestal,this.item.getStackInSlot(0)))
                                {
                                    if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                                    {
                                        getAndSend(tileRecieverPedestal);
                                    }

                                }
                            }
                        }
                    }
                    else
                    {
                        if(tileRecieverPedestal.doItemsMatch(item.getStackInSlot(0)))
                        {
                            getAndSend(tileRecieverPedestal);
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean getAndSend(TilePedestal recieverPedestal)
    {
        //getting the stack from this, the sender
        ItemStack stackToAdd = item.getStackInSlot(0).copy();
        ItemStack stackInReciever = recieverPedestal.getItemInPedestal();
        int itemCountInReciever = stackInReciever.getCount();


        //is the reciever has room in inventory
        if(roomLeftInStack(stackInReciever)!=0)
        {
            //checking if sender has at least transferSizeCount in its inventory
            if(this.getItemInPedestal().getCount()>=transferSizeCount)
            {
                //checking if receiver has enough room for transferSizeCount or more
                if(roomLeftInStack(stackInReciever)>=transferSizeCount)
                {
                    //set stack size to transfer to the transfer rate
                    stackToAdd.setCount(transferSizeCount);
                    //add to receiver
                    recieverPedestal.addItem(stackToAdd);
                    //remove from this(sender)
                    this.removeItem(transferSizeCount);
                    return true;
                }
                else
                {
                    int smallStackCount = roomLeftInStack(stackInReciever);
                    stackToAdd.setCount(smallStackCount);
                    recieverPedestal.addItem(stackToAdd);
                    removeItem(smallStackCount);
                    return true;
                }
            }
            //has less the transfer size in sender
            else
            {
                //checks is reciever has enough storage for whats left in this (the sender)
                if(roomLeftInStack(stackInReciever)>=this.getItemInPedestal().getCount())
                {
                    //set the input itemstack count to be added
                    stackToAdd.setCount(this.getItemInPedestal().getCount());
                    //actually add it
                    recieverPedestal.addItem(stackToAdd);
                    //remove that count from the inventory ??just use regular remove since its all there is??
                    removeItem(this.getItemInPedestal().getCount());
                    return true;
                }
                //if the storage cant take in the remaining input item stack we figure out how much it can take.
                else
                {
                    //get how little room there is for storage
                    int smallStackCount = roomLeftInStack(stackInReciever);
                    //set the stack stack size for items being sent
                    stackToAdd.setCount(smallStackCount);
                    //recieve items from sender
                    recieverPedestal.addItem(stackToAdd);
                    //remove those items from sender
                    removeItem(smallStackCount);
                    return true;
                }
            }
        }

        return false;
    }

    public int roomLeftInStack(ItemStack addToThisStack)
    {
        int maxSize = getMaxStackSize();
        int currentlyStored = addToThisStack.getCount();
        int countLeftTillFilled = maxSize-currentlyStored;
        return countLeftTillFilled;
    }

    public BlockPos getPosOfBlockBelow()
    {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(BlockDirectional.FACING);
        BlockPos blockBelow = this.getPos();
        switch (enumfacing)
        {
            case UP:
                return blockBelow.add(0,-1,0);
            case DOWN:
                 return blockBelow.add(0,1,0);
            case NORTH:
                return blockBelow.add(0,0,1);
            case SOUTH:
                return blockBelow.add(0,0,-1);
            case EAST:
                return blockBelow.add(-1,0,0);
            case WEST:
                return blockBelow.add(1,0,0);
            default:
                return blockBelow;
        }
    }

    public boolean hasFilterableInventoryBelow()
    {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(BlockDirectional.FACING);

        switch (enumfacing)
        {
            case UP:
                if(isBlockUnder(0,-1,0)) {return true;}
                return false;
            case DOWN:
                if(isBlockUnder(0,1,0)) {return true;}
                return false;
            case NORTH:
                if(isBlockUnder(0,0,1)) {return true;}
                return false;
            case SOUTH:
                if(isBlockUnder(0,0,-1)) {return true;}
                return false;
            case EAST:
                if(isBlockUnder(-1,0,0)) {return true;}
                return false;
            case WEST:
                if(isBlockUnder(1,0,0)) {return true;}
                return false;
        }
        return false;
    }

    private void tryToSendItemToPedestal()
    {
        if(this.hasItem())
        {
            if(numConnections()>0)
            {
                if(sendItemsToReciever(0)){}
                else if(sendItemsToReciever(1)){}
                else if(sendItemsToReciever(2)){}
                else if(sendItemsToReciever(3)){}
                else if(sendItemsToReciever(4)){}
                else if(sendItemsToReciever(5)){}
                else if(sendItemsToReciever(6)){}
                else if(sendItemsToReciever(7)){}
            }
        }
    }

    public void getStoredBlockPoss()
    {
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            System.out.println(storedOutputLocations[i]);
        }
    }

    public ItemStack removeItem() {
        ItemStack stack = item.extractItem(0,item.getStackInSlot(0).getCount(),false);
        updateBlock();
        return stack;
    }

    public ItemStack removeItem(int numToRemove) {
        ItemStack stack = item.extractItem(0,numToRemove,false);
        updateBlock();
        return stack;
    }

    public ItemStack removeCoin() {
        ItemStack stack = coin.extractItem(0,coin.getStackInSlot(0).getCount(),false);
        updateBlock();
        return stack;
    }

    @Override
    public void update() {


        if(!world.isRemote)
        {
            ticker3++;
            if(ticker3>20)
            {
                tryToSendItemToPedestal();
                ticker3=0;
            }
        }



        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(BlockDirectional.FACING);
        if(!world.isRemote)
        {
            if(!getItemInPedestal().isEmpty())
            {
                if(getItemInPedestal().getCount()!=getMaxStackSize())
                {
                    ticker2++;
                    if(ticker2>=10)
                    {
                        updateBlock();
                        ticker2=0;
                    }
                }
            }


            if(hasCoin() && !hasItem())
            {
                display = ItemStack.EMPTY;
                updateBlock();
            }
            else if(item.getStackInSlot(0).isEmpty())
            {
                if(isBlockUnder(0,-1,0))
                {
                    ticker++;
                    if(ticker>=20)
                    {
                        if (enumfacing.equals(EnumFacing.UP))
                        {
                            display = getNextSlotInChest(0,-1,0);
                            updateBlock();
                            ticker=0;
                        }
                    }
                }
                if(isBlockUnder(0,1,0))
                {
                    ticker++;
                    if(ticker>=20)
                    {
                        if (enumfacing.equals(EnumFacing.DOWN))
                        {
                            display = getNextSlotInChest(0,1,0);
                            updateBlock();
                            ticker=0;
                        }
                    }
                }
                if(isBlockUnder(0,0,1))
                {
                    ticker++;
                    if(ticker>=20)
                    {
                        if (enumfacing.equals(EnumFacing.NORTH))
                        {
                            display = getNextSlotInChest(0,0,1);
                            updateBlock();
                            ticker=0;
                        }
                    }
                }
                if(isBlockUnder(0,0,-1))
                {
                    ticker++;
                    if(ticker>=20)
                    {
                        if (enumfacing.equals(EnumFacing.SOUTH))
                        {
                            display = getNextSlotInChest(0,0,-1);
                            updateBlock();
                            ticker=0;
                        }
                    }
                }
                if(isBlockUnder(-1,0,0))
                {
                    ticker++;
                    if(ticker>=20)
                    {
                        if (enumfacing.equals(EnumFacing.EAST))
                        {
                            display = getNextSlotInChest(-1,0,0);
                            updateBlock();
                            ticker=0;
                        }
                    }
                }
                if(isBlockUnder(1,0,0))
                {
                    ticker++;
                    if(ticker>=20)
                    {
                        if (enumfacing.equals(EnumFacing.WEST))
                        {
                            display = getNextSlotInChest(1,0,0);
                            updateBlock();
                            ticker=0;
                        }
                    }
                }
            }
            else
            {
                display = item.getStackInSlot(0);
                if(item.getStackInSlot(0).getCount()<=0)
                {
                    display = ItemStack.EMPTY;
                }
                updateBlock();
            }
        }
    }

    public boolean isBlockUnder(int x,int y,int z)
    {
        TileEntity tileEntity = world.getTileEntity(pos.add(x,y,z));
        if(tileEntity instanceof ICapabilityProvider)
        {
            return true;
        }
        return false;
    }

    public ItemStack getNextSlotInChest(int x,int y,int z)
    {
        ItemStack stack = ItemStack.EMPTY;
        if(world.getTileEntity(pos.add(x,y,z)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))
        {
            TileEntity tileEntity = world.getTileEntity(pos.add(x,y,z));
            if(tileEntity instanceof TilePedestal) {
                stack = ItemStack.EMPTY;

            }
            else {
                stack = IntStream.range(0,tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN).getSlots())//Int Range
                    .mapToObj((tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.DOWN))::getStackInSlot)//Function being applied to each interval
                    .filter(itemStack -> !itemStack.isEmpty())
                    .findFirst().orElse(ItemStack.EMPTY);}
        }
        return stack;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("ItemStackItemInventoryHandler", this.item.serializeNBT());
        compound.setTag("ItemStackCoinInventoryHandler", this.coin.serializeNBT());
        compound.setTag("display",display.writeToNBT(new NBTTagCompound()));
        int counter = 0;
        for(int i=0;i<storedOutputLocations.length;i++)
        {
            if(storedOutputLocations[i].equals(defaultPos))
            {
                continue;
            }
            else
            {
                String keyNameX = "storedLocationX" + i;
                String keyNameY = "storedLocationY" + i;
                String keyNameZ = "storedLocationZ" + i;
                compound.setInteger(keyNameX,storedOutputLocations[i].getX());
                compound.setInteger(keyNameY,storedOutputLocations[i].getY());
                compound.setInteger(keyNameZ,storedOutputLocations[i].getZ());
                counter++;
            }
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
            storedOutputLocations[i] = gotPos;
        }


        this.display = new ItemStack(itemTagD);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
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