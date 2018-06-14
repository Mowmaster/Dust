package com.mowmaster.dust.tiles.containers;

import com.mowmaster.dust.tiles.TileCrystalFurnace;
import com.mowmaster.dust.tiles.slots.SlotCrystalFurnaceFuel;
import com.mowmaster.dust.tiles.slots.SlotCrystalFurnaceOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCrystalFurnace extends Container
{
    private final TileCrystalFurnace tileCrystalFurnace;
    private int cookTime, totalCookTime, burnTime, currentBurnTime;

    public ContainerCrystalFurnace(InventoryPlayer player, TileCrystalFurnace tileCrystalFurnace)
    {
        this.tileCrystalFurnace = tileCrystalFurnace;

        this.addSlotToContainer(new Slot(tileCrystalFurnace,0,57,18));//inventory,index slot# so Input for this one, pixels in from the left side of the gui image top corner of the slot,top down to the top corner in pixels
        this.addSlotToContainer(new Slot(tileCrystalFurnace,1,21,30));//crystal input
        this.addSlotToContainer(new SlotCrystalFurnaceFuel(tileCrystalFurnace,2,57,54));//fuel input
        this.addSlotToContainer(new SlotCrystalFurnaceOutput(player.player,tileCrystalFurnace,3,113,32));//output

        for(int y = 0; y < 3; y++)//check for the player inventory portion of the GUI
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlotToContainer(new Slot(player, x + y*9 + 9, 8 + x*18, 84 + y*18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileCrystalFurnace);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for(int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener listener = (IContainerListener)this.listeners.get(i);

            if(this.cookTime != this.tileCrystalFurnace.getField(2)) listener.sendWindowProperty(this, 2, this.tileCrystalFurnace.getField(2));
            if(this.burnTime != this.tileCrystalFurnace.getField(0)) listener.sendWindowProperty(this, 0, this.tileCrystalFurnace.getField(0));
            if(this.currentBurnTime != this.tileCrystalFurnace.getField(1)) listener.sendWindowProperty(this, 1, this.tileCrystalFurnace.getField(1));
            if(this.totalCookTime != this.tileCrystalFurnace.getField(3)) listener.sendWindowProperty(this, 3, this.tileCrystalFurnace.getField(3));
        }

        this.cookTime = this.tileCrystalFurnace.getField(2);
        this.burnTime = this.tileCrystalFurnace.getField(0);
        this.currentBurnTime = this.tileCrystalFurnace.getField(1);
        this.totalCookTime = this.tileCrystalFurnace.getField(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileCrystalFurnace.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileCrystalFurnace.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if(index == 3)
            {
                if(!this.mergeItemStack(stack1, 4, 40, true)) return ItemStack.EMPTY;
                slot.onSlotChange(stack1, stack);
            }
            else if(index != 2 && index != 1 && index != 0)
            {
                Slot slot1 = (Slot)this.inventorySlots.get(index + 1);

                if(!FurnaceRecipes.instance().getSmeltingResult(slot1.getStack()).isEmpty())
                {
                    if(!this.mergeItemStack(stack1, 0, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    else if(TileCrystalFurnace.isItemFuel(stack1))
                    {
                        if(!this.mergeItemStack(stack1, 2, 3, false)) return ItemStack.EMPTY;
                    }
                    else if(TileCrystalFurnace.isItemFuel(stack1))
                    {
                        if(!this.mergeItemStack(stack1, 2, 3, false)) return ItemStack.EMPTY;
                    }
                    else if(TileCrystalFurnace.isItemFuel(stack1))
                    {
                        if(!this.mergeItemStack(stack1, 2, 3, false)) return ItemStack.EMPTY;
                    }
                    else if(index >= 4 && index < 31)
                    {
                        if(!this.mergeItemStack(stack1, 31, 40, false)) return ItemStack.EMPTY;
                    }
                    else if(index >= 31 && index < 40 && !this.mergeItemStack(stack1, 4, 31, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if(!this.mergeItemStack(stack1, 4, 40, false))
            {
                return ItemStack.EMPTY;
            }
            if(stack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();

            }
            if(stack1.getCount() == stack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, stack1);
        }
        return stack;
    }
}
