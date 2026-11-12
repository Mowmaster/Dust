package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Research.Research;
import com.mowmaster.dust.Features.Research.ResearchEntry;
import com.mowmaster.dust.Features.Research.ResearchTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

// Alchemical compounds: elements separated out of something together, kept on a scrap of paper (the dust:compound
// component), and remembering the material they came from (dust:source). Made by the Alchemical Separation Table.
public final class Compounds
{
    private Compounds() {}

    // A compound of these elements, from this material
    public static ItemStack create(List<DustElement> elements, ItemStack original)
    {
        ItemStack compound = new ItemStack(DustItemRegistry.ALCHEMICAL_COMPOUND.get());
        compound.set(DustComponentDataRegistry.COMPOUND.get(), new DustComposition(elements));
        return ProductSource.madeFrom(compound, original);
    }

    // What a compound is made of (null for anything else)
    public static @Nullable DustComposition of(ItemStack stack)
    {
        return stack.get(DustComponentDataRegistry.COMPOUND.get());
    }

    // Whether this player has fully discovered the element (it's in their Elements tab)
    public static boolean knows(Player player, DustElement element)
    {
        return Research.get(player).has(new ResearchEntry(ResearchTabs.ELEMENTS, element.id()));
    }

    // "Fire, Earth, ???": the elements in it, each named only if known (known says which are)
    public static Component describe(DustComposition composition, Predicate<DustElement> known)
    {
        MutableComponent text = Component.empty();
        boolean first = true;
        for (DustElement element : composition.counted().keySet()) {
            if (!first) text.append(", ");
            text.append(known.test(element) ? element.name() : Component.translatable("research.dust.undiscovered"));
            first = false;
        }
        return text;
    }
}
