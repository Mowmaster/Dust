package com.mowmaster.dust.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TilePedestal extends TileEntity implements ITickable
{
    public ItemStack item = ItemStack.EMPTY;
    public ItemStack coin = ItemStack.EMPTY;

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

    public void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
    }

    public boolean addItem(ItemStack itemFromBlock)
    {
        item = itemFromBlock;
        updateBlock();
        return true;
    }

    public boolean addCoin(ItemStack coinFromBlock)
    {
        coin = coinFromBlock;
        updateBlock();
        return true;
    }

    public ItemStack removeItem() {
        ItemStack old = ItemStack.EMPTY;
        if(item.getHasSubtypes())
        {
            old = new ItemStack(item.getItem(),1,item.getMetadata());
        }
        else old = new ItemStack(item.getItem(),1);
        item = ItemStack.EMPTY;
        updateBlock();
        return old;
    }
    public ItemStack removeCoin() {
        ItemStack old = new ItemStack(coin.getItem(),1);
        coin = ItemStack.EMPTY;
        updateBlock();
        return old;
    }


    @Override
    public void update() {
        updateBlock();
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
