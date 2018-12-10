package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemCrystal;
import com.mowmaster.dust.recipes.crusher_recipes.CrusherRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;


public class TileCrystalCrusher extends TileEntityBase implements ITickable, IItemHandler
{

    private ItemStackHandler crystalCrusher;
    private int burnTime;

    public TileCrystalCrusher()
    {
        this.crystalCrusher = new ItemStackHandler(3);//one for input, one for fuel, and the last for the output
        this.burnTime = getBurnTimeInStack();
    }

    private int getBurnTimeInStack()
    {
        int burnage = 0;
        if(!crystalCrusher.getStackInSlot(1).isEmpty())
        {
            burnage = getItemBurnTime(crystalCrusher.getStackInSlot(1));
        }
        return burnage;
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
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) { return null; }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) { return null; }

    public boolean canAddItem(int slot, ItemStack itemIn)
    {
        switch (slot)
        {
            case 0:
                if(CrusherRecipes.instance().hasOutput(itemIn))
                {
                    //should only hold 1 item in this slot, so if its not empty it cant hold any more crystals.
                    if(crystalCrusher.getStackInSlot(0).isEmpty())
                    {
                        return true;
                    }
                }
                break;
            case 1:
                if(getItemBurnTime(itemIn)>0)
                {
                    if(crystalCrusher.getStackInSlot(slot).isEmpty())
                    {
                        return true;
                    }
                    else if(doItemsMatch(itemIn,crystalCrusher.getStackInSlot(slot)))
                    {
                        if((crystalCrusher.getStackInSlot(slot).getCount())<getSlotLimit(slot))
                        {
                            return true;
                        }
                    }
                }
                break;
            case 2:
                if(crystalCrusher.getStackInSlot(slot).isEmpty())
                {
                    return true;
                }
                else if(doItemsMatch(itemIn,crystalCrusher.getStackInSlot(slot)))
                {
                    //we're checking to see if when a crystal is processed there is room to output the result
                    if((crystalCrusher.getStackInSlot(slot).getCount()+itemIn.getCount())<=getSlotLimit(slot))
                    {
                        return true;
                    }
                }
                break;
        }

        return false;
    }

    public int addItem(int slot,ItemStack itemIn,boolean simulate)
    {
        int countInserted = 0;
        switch (slot)
        {
            case 0:
                if(canAddItem(slot,itemIn))
                {
                    //Because this only accepts 1 item at a time it can only take in one if it passes canAddItem()
                    crystalCrusher.insertItem(slot,new ItemStack(itemIn.getItem(),1,itemIn.getMetadata()),simulate);
                    countInserted = 1;
                }
                break;
            case 1:
                //if we can add in fuel items
                if(canAddItem(slot,itemIn))
                {
                    //either the inventory is empty or the fuel items match to pass the first check.
                    if(crystalCrusher.getStackInSlot(slot).isEmpty())
                    {
                        if(itemIn.getCount()>getSlotLimit(slot))
                        {
                            //if the stack holds more then we can accept then grab the max available from the stack and return what we got.
                            crystalCrusher.insertItem(slot,new ItemStack(itemIn.getItem(),getSlotLimit(slot),itemIn.getMetadata()),simulate);
                            countInserted = getSlotLimit(slot);
                        }
                        else
                        {
                            //if we cant pull in the max pull in all we can and return the amount there was in the stack.
                            crystalCrusher.insertItem(slot,new ItemStack(itemIn.getItem(),itemIn.getCount(),itemIn.getMetadata()),simulate);
                            countInserted = itemIn.getCount();
                        }
                    }
                    else
                    {
                        //for if there is already fuel in the slot
                        //since it passed the first test we know its the same item, now to just add to its number
                        int getAmmoutPossibleToFill = itemIn.getCount()+getStackInSlot(slot).getCount();
                        if(getAmmoutPossibleToFill>getSlotLimit(slot))
                        {
                            int leftToFill = getSlotLimit(slot)-getStackInSlot(slot).getCount();
                            //if incomming items plus items already inside exceed the limit fill to the max and return the count of what was used.
                            crystalCrusher.insertItem(slot,new ItemStack(itemIn.getItem(),leftToFill,itemIn.getMetadata()),simulate);
                            countInserted = (leftToFill);
                        }
                        else
                        {
                            crystalCrusher.insertItem(slot,new ItemStack(itemIn.getItem(),itemIn.getCount(),itemIn.getMetadata()),simulate);
                            countInserted = itemIn.getCount();
                        }
                    }
                }
                break;
            case 2:
                if(canAddItem(slot,itemIn))
                {
                    //since it the check sees if it matches and wont overflow we can just add in whatever we are given.
                    crystalCrusher.insertItem(slot,itemIn,simulate);
                }
                break;
        }

        updateBlock();
        return countInserted;
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
//                    addItem(2, CrusherRecipes.instance().getOutput(getStackInSlot(0)),false);
                    crystalCrusher.extractItem(0,1,false);
                    updateBlock();
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
