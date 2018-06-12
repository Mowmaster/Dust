package com.mowmaster.dust.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static net.minecraft.tileentity.TileEntityFurnace.getItemBurnTime;


public class TileCrystalFurnace extends TileEntity implements IInventory, ITickable
{
    private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(4,ItemStack.EMPTY);//Input, Output, Fuel in, Crystal in
    private String customName;

    private int burnTime;
    private int currentBurnTime;
    private int cookTime;
    private int totalCookTime;

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.crystalfurnace";//If it has a custom name name it this.customName otherwise set it to container.crystalfurnace
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentString(this.getName());
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();//from the non null inventory above == 4
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory)
        {
            if(!stack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return (ItemStack)this.inventory.get(index);//gets one of our 4 slots liek input=0 output=1 fuel=2 and crystal=3 for example
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory,index,count);//in our inventory, what slot, and how many in the slot to remove
    }


    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory,index);//in our inventory, what slot to completely remove from
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemStack = (ItemStack)this.inventory.get(index);//makes a stack for each slot in the index
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemStack) && ItemStack.areItemStacksEqual(stack,itemStack);//CHecks to make sure index slot isnt null and checks incomming item/meta to item/meta already inside.
        this.inventory.set(index,stack);

        if(stack.getCount() > this.getInventoryStackLimit()) stack.setCount(this.getInventoryStackLimit());//if the stack is above 64(or whatever the limit is) make it the limit.
        if(index == 0 && index +1 == 1 && !flag)
        {
            ItemStack stack1 = (ItemStack)this.inventory.get(index +1);
            this.totalCookTime = this.getCookTime(stack,stack1);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.<ItemStack>withSize(this.getInventoryStackLimit(),ItemStack.EMPTY);//makes the inventory again
        ItemStackHelper.loadAllItems(compound,this.inventory);//loads all the items back into the inventory
        this.burnTime = compound.getInteger("burntime");
        this.cookTime = compound.getInteger("cooktime");
        this.totalCookTime = compound.getInteger("totalcooktime");
        this.currentBurnTime = getItemBurnTime((ItemStack)this.inventory.get(2));//sets burntime for slot 2

        if(compound.hasKey("customname",8)) this.setCustomName(compound.getString("customname"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("burntime",(short)this.burnTime);
        compound.setInteger("cooktime",(short)this.cookTime);
        compound.setInteger("totalcooktime",(short)this.totalCookTime);
        ItemStackHelper.saveAllItems(compound,this.inventory);//saves all item stack in the inventory to the nbt

        if(this.hasCustomName()) compound.setString("customname",this.customName);
        return compound;

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isBurning()
    {
        return this.burnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory iInventory)
    {
        return iInventory.getField(0) > 0;
    }

    public int getCookTime(ItemStack input, ItemStack input2)
    {
        return 200;//Could modify later to speed or slow down cook times
    }

    @Override
    public void update() {

    }
}
