package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

// Alchemical Dust (AlchemicalDustItem): one element, held stable, so it needs no container. Made on the Transformation
// Table from a compound of one element the player has discovered; put together into new elements on the Combination
// Table (solids) and the Mixing Table (with a gaseous or aqueous one); made into a crystal of its element on the
// Transformation Table with an inert crystal or nether quartz.
public final class AlchemicalDust
{
    private AlchemicalDust() {}

    // Its element (null for anything that isn't Alchemical Dust)
    public static @Nullable DustElement of(ItemStack stack)
    {
        return stack.get(DustComponentDataRegistry.ALCHEMICAL_ELEMENT.get());
    }

    public static boolean is(ItemStack stack)
    {
        return of(stack) != null;
    }

    public static ItemStack create(DustElement element)
    {
        ItemStack dust = new ItemStack(DustItemRegistry.ALCHEMICAL_DUST.get());
        dust.set(DustComponentDataRegistry.ALCHEMICAL_ELEMENT.get(), element);
        return dust;
    }
}
