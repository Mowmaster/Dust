package com.mowmaster.dust.Features.Machines;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// What the Research Crusher makes of a block: pebbles of it. Which block is the dust:source component (ProductSource);
// the name says so ("Crushed Stone") and the pebbles are tinted in the block's colour (Client/SourceBlockTint).
public class CrushedItem extends Item
{
    public CrushedItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack)
    {
        ItemStack source = ProductSource.sourceOf(stack);
        return source == null || source.isEmpty() ? super.getName(stack) : Component.translatable("item.dust.crushed_block.of", source.getHoverName());
    }
}
