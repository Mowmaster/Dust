package com.mowmaster.dust.tiles.containers;

import com.mowmaster.dust.tiles.TileCrystalFurnace;
import com.mowmaster.dust.tiles.slots.SlotCrystalFurnaceCrystal;
import com.mowmaster.dust.tiles.slots.SlotCrystalFurnaceCrystalOutput;
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
    private int cookTime, totalCookTime, burnTime, currentBurnTime, crystalEnergyLeft, crystalEffectActive;

    public ContainerCrystalFurnace(InventoryPlayer player, TileCrystalFurnace tileCrystalFurnace)
    {
        this.tileCrystalFurnace = tileCrystalFurnace;

        this.addSlotToContainer(new Slot(tileCrystalFurnace,0,71,11));//inventory,index slot# so Input for this one, pixels in from the left side of the gui image top corner of the slot,top down to the top corner in pixels
        this.addSlotToContainer(new SlotCrystalFurnaceCrystal(tileCrystalFurnace,1,34,12));//crystal input
        this.addSlotToContainer(new SlotCrystalFurnaceFuel(tileCrystalFurnace,2,49,59));//fuel input
        this.addSlotToContainer(new SlotCrystalFurnaceOutput(player.player,tileCrystalFurnace,3,138,35));//output
        this.addSlotToContainer(new SlotCrystalFurnaceCrystalOutput(player.player,tileCrystalFurnace,4,112,59));//crystal output

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
            if(this.crystalEnergyLeft != this.tileCrystalFurnace.getField(4)) listener.sendWindowProperty(this, 4, this.tileCrystalFurnace.getField(4));
            if(this.crystalEffectActive != this.tileCrystalFurnace.getField(5)) listener.sendWindowProperty(this, 5, this.tileCrystalFurnace.getField(5));
        }

        this.cookTime = this.tileCrystalFurnace.getField(2);
        this.burnTime = this.tileCrystalFurnace.getField(0);
        this.currentBurnTime = this.tileCrystalFurnace.getField(1);
        this.totalCookTime = this.tileCrystalFurnace.getField(3);
        this.crystalEnergyLeft = this.tileCrystalFurnace.getField(4);
        this.crystalEffectActive = this.tileCrystalFurnace.getField(5);
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
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if(index == 3)//3=output slot
            {
                if(!this.mergeItemStack(stack1, 4, 40, true)) return ItemStack.EMPTY;
                slot.onSlotChange(stack1, stack);
            }
            else if(index != 2 && index != 1 && index != 0)
            {


                if(!FurnaceRecipes.instance().getSmeltingResult(slot.getStack()).isEmpty())
                {
                    if(!this.mergeItemStack(stack1, 0, 2, false))//0-2 is input, crystal input, and fuel input
                    {
                        return ItemStack.EMPTY;
                    }
                    else if(TileCrystalFurnace.isItemFuel(stack1))
                    {
                        if(!this.mergeItemStack(stack1, 1, 3, false)) return ItemStack.EMPTY;
                    }
                    else if(TileCrystalFurnace.isItemFuel(stack1))
                    {
                        if(!this.mergeItemStack(stack1, 1, 3, false)) return ItemStack.EMPTY;
                    }
                    else if(TileCrystalFurnace.isItemFuel(stack1))
                    {
                        if(!this.mergeItemStack(stack1, 1, 3, false)) return ItemStack.EMPTY;
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
