package com.mowmaster.dust.Features.Machines;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

// Saving and loading a station's item stacks (an empty stack isn't written)
public final class StationSaving
{
    private StationSaving() {}

    public static void store(ValueOutput output, String key, ItemStack stack)
    {
        if (!stack.isEmpty()) output.store(key, ItemStack.OPTIONAL_CODEC, stack);
    }

    public static ItemStack read(ValueInput input, String key)
    {
        return input.read(key, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }
}
