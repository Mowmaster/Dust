package com.mowmaster.dust.tiles.slots;

import com.mowmaster.dust.tiles.TileCrystalFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCrystalFurnaceCrystal extends Slot
    {
        public SlotCrystalFurnaceCrystal(IInventory inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return TileCrystalFurnace.isItemCrystal(stack);
        }

        @Override
        public int getItemStackLimit(ItemStack stack)
        {
            return super.getItemStackLimit(stack);
        }
    }
