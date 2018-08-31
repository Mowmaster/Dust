package com.mowmaster.dust.tiles.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by KingMowmaster on 6/13/2018.
 */
public class SlotCrystalFurnaceOutput extends Slot
{
    private final EntityPlayer player;   // you might wanna make sure u want to use final here as the object cannot change once instantiated
    // name for above line should probably change so its easier to remember that it is only called here and cannot change.

    private int removeCount;

    public SlotCrystalFurnaceOutput(EntityPlayer player, IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        this.onCrafting(stack);
        super.onTake(thePlayer, stack);
        return stack;
    }

    @Override
    public ItemStack decrStackSize(int amount)
    {
        if(this.getHasStack()) this.removeCount += Math.min(amount, this.getStack().getCount());
        return super.decrStackSize(amount);
    }
}
