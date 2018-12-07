package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockDust;
import com.mowmaster.dust.blocks.BlockDustCloud;
import com.mowmaster.dust.items.ItemDust;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;


public class TileDustBlock extends TileEntity implements IItemHandler
{

    private ItemStackHandler dust;
    private boolean isFull=false;

    public TileDustBlock()
    {
        this.dust = new ItemStackHandler(8);
    }


    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return dust.getStackInSlot(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }

    @Override
    public int getSlots() {
        return dust.getSlots();
    }

    public boolean isBlockFull()
    {
        if(isFull)
        {
            return true;
        }

        return false;
    }


    public boolean canAddItem(ItemStack stack)
    {
        if(Block.getBlockFromItem(stack.getItem()) instanceof BlockDustCloud)
        {
            if(isFull)
            {
                return false;
            }
            else return true;
        }

        return false;
    }

    public int getNextAvailSlot()
    {
        int i;

        for(i=0;i<dust.getSlots();i++)
        {
            if(dust.getStackInSlot(i).isEmpty())
            {
                break;
            }
        }

        return i;
    }

    public void addItem(ItemStack stack)
    {
        int slot = getNextAvailSlot();
        if(canAddItem(stack))
        {
            if(slot>=7)
            {
                if(slot>7)
                {
                    dust.insertItem(7,stack,false);
                    isFull=true;
                }
                else
                {
                    dust.insertItem(slot,stack,false);
                    isFull=true;
                }
                updateBlock();
            }
            else
            {
                dust.insertItem(slot,stack,false);
                updateBlock();
            }
        }
    }

    public ItemStack getBlockAbove()
    {
        ItemStack blockAbove = ItemStack.EMPTY;
        //Use y+2 because the block above will update the tile and the 2nd block above will become the new block above
        if(world.getBlockState(this.getPos().add(0,2,0)).getBlock() instanceof BlockDustCloud)
        {
            Block blocky = world.getBlockState(this.getPos().add(0,2,0)).getBlock();
            blockAbove = new ItemStack(Item.getItemFromBlock(blocky),1);
        }

        return blockAbove;
    }



    public ItemStack removeItem()
    {
        int slotted = getNextAvailSlot()-1;
        ItemStack returned = dust.extractItem(slotted,1,false).copy();
        ItemStack returner = ItemStack.EMPTY;
        if(Block.getBlockFromItem(returned.getItem()) instanceof BlockDustCloud)
        {
            returner = ((BlockDustCloud) Block.getBlockFromItem(returned.getItem())).getDustDropped();
        }
        dust.setStackInSlot(slotted,ItemStack.EMPTY);
        updateBlock();

        return returner;

    }


    private void updateBlock()
    {
        markDirty();
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.setBlockState(pos,state,3);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("dustInBlock", this.dust.serializeNBT());
        compound.setBoolean("isFull",isFull);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.dust.deserializeNBT(compound.getCompoundTag("dustInBlock"));
        this.isFull=compound.getBoolean("isFull");
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
