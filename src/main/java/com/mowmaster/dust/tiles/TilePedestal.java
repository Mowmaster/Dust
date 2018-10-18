package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.items.ItemCoin;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TilePedestal extends TileEntity implements ITickable, ICapabilityProvider, IInventory
{
    private ItemStackHandler item;
    private static final int[] SLOTS_ALLSIDES = new int[] {0};
    public ItemStack display = ItemStack.EMPTY;
    private String customName = "Item_Pedestal";

    public TilePedestal()
    {
        this.item = new ItemStackHandler(2);
    }//Slots

    public ItemStack getItemInPedestal() {return item.getStackInSlot(0);}
    public ItemStack getCoinOnPedestal() {return item.getStackInSlot(1);}
    public ItemStack getDisplay() {return display;}

    @Override
    public String getName() {
        return customName;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return this.item.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.item.getStackInSlot(index);//gets one of our 5 slots like input=0 input crystal=1 input fuel=2 output=3 for example
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return this.item.extractItem(index,count,false);//no idea what true and false do here
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.item.getStackInSlot(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.item.setStackInSlot(index,stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        switch (index)
        {
            case 0:
                return true;
            case 1:
                return isEqualToCoin(stack);
            default:
                return false;
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public void clear() {
        for(int i=0;i<=this.item.getSlots();i++)
        {
            this.item.setStackInSlot(i,ItemStack.EMPTY);
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    public boolean isEmpty() {
        for(int i=0;i<=this.item.getSlots();i++)
        {
            if(!this.item.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
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
        if(item.getStackInSlot(1).isEmpty())
        {
            return false;
        }
        else  return true;
    }

    private void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
    }

    public boolean isEqualToCoin(ItemStack itemFromBlock)
    {
        if(itemFromBlock.getItem() instanceof ItemCoin)
        {
            if(hasCoin()){}
            else
            return true;
        }
        return false;
    }

    public boolean addItem(ItemStack itemFromBlock)
    {
        if(hasItem()){} else {item.insertItem(0, itemFromBlock.copy(), false);}
        updateBlock();
        return true;
    }

    public boolean addCoin(ItemStack coinFromBlock)
    {
        if(hasCoin()){} else item.insertItem(1,coinFromBlock.copy(),false);
        updateBlock();
        return true;
    }

    public ItemStack removeItem() {
        ItemStack stack = item.extractItem(0,item.getStackInSlot(0).getCount(),false);
        updateBlock();
        return stack;
    }
    public ItemStack removeCoin() {
        ItemStack stack = item.extractItem(1,item.getStackInSlot(1).getCount(),false);
        updateBlock();
        return stack;
    }

    private int ticker=0;
    ItemStack checker = ItemStack.EMPTY;
    @Override
    public void update() {

        if(item.getStackInSlot(0).equals(checker)){}else{checker = item.getStackInSlot(0); updateBlock();}



        IBlockState state = this.getWorld().getBlockState(this.getPos());
        EnumFacing enumfacing = state.getValue(BlockDirectional.FACING);
        if(!world.isRemote)
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
        TileEntity tileEntity = world.getTileEntity(pos.add(x,y,z));
        return IntStream.range(0,((TileEntityLockable) tileEntity).getSizeInventory())//Int Range
                .mapToObj(((TileEntityLockable) tileEntity)::getStackInSlot)//Function being applied to each interval
                .filter(itemStack -> !itemStack.isEmpty())
                .findFirst().orElse(ItemStack.EMPTY);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("ItemStackInventoryHandler", this.item.serializeNBT());
        compound.setTag("display",display.writeToNBT(new NBTTagCompound()));
        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.item.deserializeNBT(compound.getCompoundTag("ItemStackInventoryHandler"));
        NBTTagCompound itemTagD = compound.getCompoundTag("display");
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

    /*
    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.WEST);
     */

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {

        /*
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
         */


        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this.item;
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }
}
