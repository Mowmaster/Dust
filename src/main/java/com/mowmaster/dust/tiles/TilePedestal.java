package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.items.ItemCoin;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TilePedestal extends TileEntity implements ITickable, ICapabilityProvider, IItemHandler
{
    private ItemStackHandler item;
    private ItemStackHandler coin;
    private static final int[] SLOTS_ALLSIDES = new int[] {0};
    public ItemStack display = ItemStack.EMPTY;

    public TilePedestal()
    {
        this.item = new ItemStackHandler(1);
        this.coin = new ItemStackHandler(1);
    }
    public ItemStack getItemInPedestal() {return item.getStackInSlot(0);}
    public ItemStack getCoinOnPedestal() {return coin.getStackInSlot(0);}
    public ItemStack getDisplay() {return display;}

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return getItemInPedestal();
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return extractItem(0,item.getStackInSlot(0).getCount(),false);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(stack.getItem().equals(Items.WHEAT))
        {
            return insertItem(0, stack.copy(), false);
        }
        else return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    public boolean doItemsMatch(ItemStack itemStackIn)
    {
        if(itemStackIn.getItem().equals(getItemInPedestal().getItem()) && itemStackIn.getMetadata()==getItemInPedestal().getMetadata())
            return true;
        else
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

    public ItemStack removeItem() {
        ItemStack stack = item.extractItem(0,item.getStackInSlot(0).getCount(),false);
        updateBlock();
        return stack;
    }
    public ItemStack removeCoin() {
        ItemStack stack = coin.extractItem(0,coin.getStackInSlot(0).getCount(),false);
        updateBlock();
        return stack;
    }

    private int ticker=0;
    private int ticker2=0;
    @Override
    public void update() {

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

            if(item.getStackInSlot(0).isEmpty())
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
            else display = item.getStackInSlot(0);
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
        compound.setTag("ItemStackItemInventoryHandler", this.item.serializeNBT());
        compound.setTag("ItemStackCoinInventoryHandler", this.coin.serializeNBT());
        compound.setTag("display",display.writeToNBT(new NBTTagCompound()));
        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.item.deserializeNBT(compound.getCompoundTag("ItemStackItemInventoryHandler"));
        this.coin.deserializeNBT(compound.getCompoundTag("ItemStackCoinInventoryHandler"));
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



    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(!hasCoin())
        {
            if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return (T) this.item;

        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(!hasCoin())
        {
            if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        }
        return super.hasCapability(capability, facing);
    }
}
