package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// Which element a crystal or a dust item is (DustItemRegistry.CRYSTALS / DUSTS: the five primal items, and an
// ElementItem for every other element).
public final class CrystalElements
{
    // Item -> element, built the first time it's asked for (items are registered by then)
    private static @Nullable Map<Item, DustElement> crystals;
    private static @Nullable Map<Item, DustElement> dusts;

    private CrystalElements() {}

    // The crystal's element, or null if it isn't an element crystal
    public static @Nullable DustElement of(ItemStack stack)
    {
        if (crystals == null) crystals = reverse(DustItemRegistry.CRYSTALS);
        return stack.isEmpty() ? null : crystals.get(stack.getItem());
    }

    // The dust's element, or null if it isn't an element dust
    public static @Nullable DustElement ofDust(ItemStack stack)
    {
        if (dusts == null) dusts = reverse(DustItemRegistry.DUSTS);
        return stack.isEmpty() ? null : dusts.get(stack.getItem());
    }

    // A crystal of this element
    public static ItemStack stackOf(DustElement element)
    {
        DeferredItem<? extends Item> item = DustItemRegistry.CRYSTALS.get(element);
        return item == null ? ItemStack.EMPTY : new ItemStack(item.get());
    }

    // A dust of this element
    public static ItemStack dustOf(DustElement element, int count)
    {
        DeferredItem<? extends Item> item = DustItemRegistry.DUSTS.get(element);
        return item == null ? ItemStack.EMPTY : new ItemStack(item.get(), count);
    }

    private static Map<Item, DustElement> reverse(Map<DustElement, DeferredItem<? extends Item>> items)
    {
        Map<Item, DustElement> out = new HashMap<>();
        items.forEach((element, item) -> out.put(item.get(), element));
        return out;
    }
}
