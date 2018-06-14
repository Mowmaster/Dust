package com.mowmaster.dust.tiles.slots;

import com.mowmaster.dust.tiles.TileCrystalFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCrystalFurnaceFuel extends Slot
    {
        public SlotCrystalFurnaceFuel(IInventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return TileCrystalFurnace.isItemFuel(stack);
        }

        @Override
        public int getItemStackLimit(ItemStack stack)
        {
            return super.getItemStackLimit(stack);
        }
    }
