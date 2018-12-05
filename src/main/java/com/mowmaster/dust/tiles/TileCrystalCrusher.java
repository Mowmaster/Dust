package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class TileCrystalCrusher extends TileEntityBase implements ITickable, IItemHandler
{

    private ItemStackHandler crystalCrusher;

    public TileCrystalCrusher()
    {
        this.crystalCrusher = new ItemStackHandler(3);//one for input, one for fuel, and the last for the output
    }

    @Override
    public int getSlotLimit(int slot) {
        int limiter = 0;

        switch (slot)
        {
            case 0:
                limiter = 1;
                break;
            case 1:
                limiter = 8;
                break;
            case 2:
                limiter = 64;
                break;
        }
        return limiter;
    }

    @Override
    public int getSlots() {
        return crystalCrusher.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return crystalCrusher.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    public boolean canAddItem(ItemStack itemIn)
    {
        if(itemIn.getItem() instanceof ItemCrystal)
        {
            return true;
        }

        return false;
    }

    public void addItem(ItemStack itemIn)
    {
        if(itemIn.getItem() instanceof ItemCrystal)
        {
            if(crystalCrusher.getStackInSlot(0).isEmpty())
            {
                crystalCrusher.insertItem(0,new ItemStack(itemIn.getItem(),1,itemIn.getMetadata()),false);
                updateBlock();
            }
        }
    }

    int ticker = 0;
    @Override
    public void update()
    {

        if (!world.isRemote)
        {
            ticker++;

            if(!crystalCrusher.getStackInSlot(0).isEmpty())
            {
                if(ticker>20)
                {
                    //if(crystalCrusher.getStackInSlot(0).getMetadata()==0)
                    //{
                    crystalCrusher.insertItem(2,new ItemStack(BlockRegistry.redDust,8),false);
                    crystalCrusher.extractItem(0,1,false);
                    updateBlock();
                    //}
                    ticker=0;
                }
            }

            int zmin=-2;
            int zmax=2;
            int xmin=-2;
            int xmax=2;
            int ymin=0;
            int ymax=4;

            if(!crystalCrusher.getStackInSlot(2).isEmpty())
            {

                for(int c=zmin;c<=zmax;c++) {
                    for (int a = xmin; a <= xmax; a++) {
                        //for (int b = ymin; b <= ymax; b++) {
                            if(world.getBlockState(getPos().add(a,4,c)).getBlock() instanceof BlockAir)
                            {
                                if(ticker>13)
                                {
                                    world.setBlockState(getPos().add(a,4,c), Block.getBlockFromItem(crystalCrusher.getStackInSlot(2).getItem()).getDefaultState());
                                    crystalCrusher.extractItem(2,1,false);
                                    updateBlock();
                                    ticker=0;
                                }
                            }
                            //Later on make a bad thing happen if the area is full of dust
                        //}
                    }
                }

            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        //compound.setTag("coin",coin.writeToNBT(new NBTTagCompound()));
        compound.setTag("crystalCrusherInventory",this.crystalCrusher.serializeNBT());

        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        //NBTTagCompound itemCoin = compound.getCompoundTag("coin");
        //this.coin = new ItemStack(itemCoin);
        this.crystalCrusher.deserializeNBT(compound.getCompoundTag("crystalCrusherInventory"));
    }
}
