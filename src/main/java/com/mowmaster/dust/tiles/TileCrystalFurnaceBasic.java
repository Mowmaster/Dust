package com.mowmaster.dust.tiles;

import com.mowmaster.dust.recipes.crusher_recipes.CrusherRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Random;

import static com.mowmaster.dust.misc.DustConfigurationFile.funhaters;

public class TileCrystalFurnaceBasic extends TileEntityBase implements ITickable, IItemHandler
{

    private ItemStackHandler tierZeroFurnace;
    private int burnTime;
    public boolean isBurning=false;

    public TileCrystalFurnaceBasic()
    {
        this.tierZeroFurnace = new ItemStackHandler(3);//one for input, one for fuel, and the last for the output
        this.burnTime = getBurnTimeInStack();
    }

    private int getBurnTimeInStack()
    {
        int burnage = 0;
        if(!tierZeroFurnace.getStackInSlot(1).isEmpty())
        {
            burnage = getItemBurnTime(tierZeroFurnace.getStackInSlot(1));
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
        return tierZeroFurnace.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return tierZeroFurnace.getStackInSlot(slot);
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
                //System.out.println(FurnaceRecipes.instance().getSmeltingResult(itemIn));
                if(!(FurnaceRecipes.instance().getSmeltingResult(itemIn).isEmpty()))
                {
                    //should only hold 1 item in this slot, so if its not empty it cant hold any more crystals.
                    if(tierZeroFurnace.getStackInSlot(0).isEmpty())
                    {
                        return true;
                    }
                }
                break;
            case 1:
                if(getItemBurnTime(itemIn)>0)
                {
                    if(tierZeroFurnace.getStackInSlot(slot).isEmpty())
                    {
                        return true;
                    }
                    else if(doItemsMatch(itemIn,tierZeroFurnace.getStackInSlot(slot)))
                    {
                        if((tierZeroFurnace.getStackInSlot(slot).getCount())<getSlotLimit(slot))
                        {
                            return true;
                        }
                    }
                }
                break;
            case 2:
                if(tierZeroFurnace.getStackInSlot(slot).isEmpty())
                {
                    return true;
                }
                else if(doItemsMatch(itemIn,tierZeroFurnace.getStackInSlot(slot)))
                {
                    //we're checking to see if when a crystal is processed there is room to output the result
                    if((tierZeroFurnace.getStackInSlot(slot).getCount()+itemIn.getCount())<=getSlotLimit(slot))
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
                    tierZeroFurnace.insertItem(slot,new ItemStack(itemIn.getItem(),1,itemIn.getMetadata()),simulate);
                    countInserted = 1;
                }
                break;
            case 1:
                //if we can add in fuel items
                if(canAddItem(slot,itemIn))
                {
                    //either the inventory is empty or the fuel items match to pass the first check.
                    if(tierZeroFurnace.getStackInSlot(slot).isEmpty())
                    {
                        if(itemIn.getCount()>getSlotLimit(slot))
                        {
                            //if the stack holds more then we can accept then grab the max available from the stack and return what we got.
                            tierZeroFurnace.insertItem(slot,new ItemStack(itemIn.getItem(),getSlotLimit(slot),itemIn.getMetadata()),simulate);
                            countInserted = getSlotLimit(slot);
                        }
                        else
                        {
                            //if we cant pull in the max pull in all we can and return the amount there was in the stack.
                            tierZeroFurnace.insertItem(slot,new ItemStack(itemIn.getItem(),itemIn.getCount(),itemIn.getMetadata()),simulate);
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
                            tierZeroFurnace.insertItem(slot,new ItemStack(itemIn.getItem(),leftToFill,itemIn.getMetadata()),simulate);
                            countInserted = (leftToFill);
                        }
                        else
                        {
                            tierZeroFurnace.insertItem(slot,new ItemStack(itemIn.getItem(),itemIn.getCount(),itemIn.getMetadata()),simulate);
                            countInserted = itemIn.getCount();
                        }
                    }
                }
                break;
            case 2:
                if(canAddItem(slot,itemIn))
                {
                    //since it the check sees if it matches and wont overflow we can just add in whatever we are given.
                    tierZeroFurnace.insertItem(slot,itemIn,simulate);
                }
                break;
        }

        updateBlock();
        return countInserted;
    }

    private void checkBurning()
    {
        if(burnTime>0)
        {
            isBurning=true;
            updateBlock();
        }
        else {
            isBurning=false;
            updateBlock();
        }
    }

    public boolean checkIsBurning()
    {
        if(isBurning)
        {
            return true;
        }
        else return false;
    }

    private void startBurning(ItemStack fuelSlot)
    {
        if(!fuelSlot.isEmpty() && isBurning==false)
        {
            ItemStack singleFuelItem = new ItemStack(fuelSlot.getItem(),1,fuelSlot.getMetadata());
            burnTime = getItemBurnTime(singleFuelItem);
            fuelSlot.shrink(1);
            isBurning=true;
            updateBlock();
        }
    }



    int ticker = 0;
    int ticker2 = 0;
    int explody = 0;
    private Float[] randNums = { -0.1f,-0.09f,-0.08f,-0.07f,-0.06f,-0.05f,-0.04f,-0.03f,-0.02f,-0.01f,0.0f,0.01f,0.02f,0.03f,0.04f,0.05f,0.06f,0.07f,0.08f,0.09f,0.1f };
    private Float[] randPosNums = { 0.1f,0.2f,0.3f,0.4f,0.5f };
    @Override
    public void update()
    {
        if(world.isRemote)
        {
            if (isBurning) {
                world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.1, pos.getY() + 0.125, pos.getZ() + 0.5, 0.0, 0.0, 0.0, new int[0]);
                world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.9, pos.getY() + 0.125, pos.getZ() + 0.5, 0.0, 0.0, 0.0, new int[0]);
                world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.125, pos.getZ() + 0.1, 0.0, 0.0, 0.0, new int[0]);
                world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.125, pos.getZ() + 0.9, 0.0, 0.0, 0.0, new int[0]);
                //Gets Bigger with core temp
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5, pos.getY() + 0.875, pos.getZ() + 0.5, 0.0, 0.0, 0.0, new int[0]);
            }
        }

        if (!world.isRemote)
        {

            //Checks if is burning or not
            checkBurning();
            //if not burning but has items to process it will start it up
            startBurning(tierZeroFurnace.getStackInSlot(1));

            if(isBurning)
            {
                burnTime--;

                ticker++;

                if(!tierZeroFurnace.getStackInSlot(0).isEmpty())
                {
                    if(ticker>200)
                    {
                        if(canAddItem(2,FurnaceRecipes.instance().getSmeltingResult((ItemStack)this.tierZeroFurnace.getStackInSlot(0))))
                        {
                            addItem(2, FurnaceRecipes.instance().getSmeltingResult((ItemStack)this.tierZeroFurnace.getStackInSlot(0)),false);
                            tierZeroFurnace.extractItem(0,1,false);
                            updateBlock();
                            ticker=0;
                        }
                    }
                }
                ticker2++;
                if(!tierZeroFurnace.getStackInSlot(2).isEmpty() && ticker2>=20)
                {
                    Random rand = new Random();
                    ItemStack stacked = tierZeroFurnace.getStackInSlot(2).copy();
                    stacked.setCount(1);
                    EntityItem meme = new EntityItem(world,pos.getX()+0.5,pos.getY()+1.0,pos.getZ()+0.5,stacked);
                    meme.motionY = randPosNums[rand.nextInt(4)];
                    meme.motionX = rand.nextInt(10)*randNums[rand.nextInt(20)];
                    meme.motionZ = rand.nextInt(10)*randNums[rand.nextInt(20)];
                    meme.setNoPickupDelay();
                    world.spawnEntity(meme);
                    tierZeroFurnace.extractItem(2,1,false);
                    ticker2=0;
                    updateBlock();
                }
            }

            if(!funhaters)
            {
                //StartFires and Melt floor blocks over time
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        //compound.setTag("coin",coin.writeToNBT(new NBTTagCompound()));
        compound.setTag("crystalfurnacebasic",this.tierZeroFurnace.serializeNBT());
        compound.setBoolean("burning",this.isBurning);
        compound.setInteger("burntime",this.burnTime);

        return compound;
    }



    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        //NBTTagCompound itemCoin = compound.getCompoundTag("coin");
        //this.coin = new ItemStack(itemCoin);
        this.tierZeroFurnace.deserializeNBT(compound.getCompoundTag("crystalfurnacebasic"));
        this.isBurning=compound.getBoolean("burning");
        this.burnTime=compound.getInteger("burntime");
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
