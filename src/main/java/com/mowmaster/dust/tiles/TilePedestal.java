package com.mowmaster.dust.tiles;

import com.mowmaster.dust.items.ItemCoin;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class TilePedestal extends TileEntity implements ITickable
{

    private ItemStack item = ItemStack.EMPTY;
    private ItemStack coin = ItemStack.EMPTY;

    public ItemStack getItemInPedestal() {return item;}
    public ItemStack getCoinOnPedestal() {return coin;}
    public boolean hasItem()
    {
        if(item.isEmpty())
        {
            return false;
        }
        else  return true;
    }
    public boolean hasCoin()
    {
        if(coin.isEmpty())
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

    public boolean isEqualTo(ItemStack itemFromBlock)
    {
        if(itemFromBlock.getItem() instanceof ItemCoin)
        {
            return true;
        }
        return false;
    }

    public boolean addItem(ItemStack itemFromBlock)
    {
        item = itemFromBlock.copy();
        item.setCount(1);
        updateBlock();
        return true;
    }

    public boolean addCoin(ItemStack coinFromBlock)
    {
        coin = coinFromBlock.copy();
        coin.setCount(1);
        updateBlock();
        return true;
    }

    public ItemStack removeItem() {
        ItemStack old = item.copy();
        item = ItemStack.EMPTY;
        updateBlock();
        return old;
    }
    public ItemStack removeCoin() {
        ItemStack old = coin.copy();
        coin = ItemStack.EMPTY;
        updateBlock();
        return old;
    }

    private int ticker=0;
    @Override
    public void update() {
        IBlockState state = world.getBlockState(pos.add(0,-1,0));
        TileEntity tileEntity = world.getTileEntity(pos.add(0,-1,0));
        if(!world.isRemote)
        {
            if(tileEntity instanceof TileEntityChest)
            {
                ticker++;
                if(ticker>=20)
                {
                    System.out.println(((TileEntityChest) tileEntity).getSizeInventory());
                    System.out.println(getNextSlotInChest(tileEntity));
                    System.out.println(i);
                    i=0;
                    ticker=0;
                }
            }
        }
    }

    private int i=0;
    private ItemStack getNextSlotInChest(TileEntity entity)
    {
        int max=((TileEntityChest) entity).getSizeInventory();//27 for chests

        for(i=0;i>((TileEntityChest) entity).getSizeInventory();i++)
        {
            if(!(((TileEntityChest) entity).getStackInSlot(i).equals(ItemStack.EMPTY)))
            {
                break;
            }
        }
        return ((TileEntityChest) entity).getStackInSlot(i);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("coin",coin.writeToNBT(new NBTTagCompound()));
        compound.setTag("item",item.writeToNBT(new NBTTagCompound()));
        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound itemCoin = compound.getCompoundTag("coin");
        this.coin = new ItemStack(itemCoin);
        NBTTagCompound itemTag = compound.getCompoundTag("item");
        this.item = new ItemStack(itemTag);
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
}
