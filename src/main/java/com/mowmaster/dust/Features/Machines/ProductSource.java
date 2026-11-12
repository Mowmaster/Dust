package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

// Process products remember what they were made from (the dust:source component): crushed stone knows it was stone.
// The research book uses this to follow a material through each process, and the Elemental Examiner notes what it
// finds in a product on the original material's entry.
public final class ProductSource
{
    private ProductSource() {}

    // A product made from source (one of it): the same product with dust:source set
    public static ItemStack madeFrom(ItemStack product, ItemStack source)
    {
        product.set(DustComponentDataRegistry.SOURCE.get(), ItemStackTemplate.fromNonEmptyStack(source, 1));
        return product;
    }

    // What this product was made from, one step back (null if it isn't a product)
    public static @Nullable ItemStack sourceOf(ItemStack stack)
    {
        ItemStackTemplate source = stack.get(DustComponentDataRegistry.SOURCE.get());
        return source == null ? null : source.create();
    }

    // The material it all started as: follows sources back until one isn't a product (crushed, then ground, stone is
    // stone). The stack itself if it isn't a product. Stops after 16 steps, in case of a loop.
    public static ItemStack original(ItemStack stack)
    {
        ItemStack current = stack;
        for (int i = 0; i < 16; i++) {
            ItemStack source = sourceOf(current);
            if (source == null || source.isEmpty()) return current;
            current = source;
        }
        return current;
    }
}
