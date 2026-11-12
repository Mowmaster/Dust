package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.ElementPhase;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
import java.util.function.Supplier;

// Alchemical compounds: elements taken out of something together (the dust:compound component), remembering the
// material they came from (dust:source). A solid one is kept on paper, a liquid in a bowl, a gas in a glass bottle
// (Kind). Made by the Separation, Evaporation and Distillation Tables.
public final class Compounds
{
    private Compounds() {}

    // What a compound is caught in, by the most volatile element in it: anything gaseous makes it a gas (in a glass
    // bottle), else anything aqueous a liquid (in a bowl), else it's a solid (on paper)
    public enum Kind
    {
        SOLID(() -> Items.PAPER), AQUEOUS(() -> Items.BOWL), GASEOUS(() -> Items.GLASS_BOTTLE);

        private final Supplier<Item> container;

        Kind(Supplier<Item> container)
        {
            this.container = container;
        }

        // What it needs to be caught in
        public Item container()
        {
            return container.get();
        }

        // The compound item for this kind
        public Item item()
        {
            return switch (this) {
                case SOLID -> DustItemRegistry.ALCHEMICAL_COMPOUND.get();
                case AQUEOUS -> DustItemRegistry.ALCHEMICAL_LIQUID.get();
                case GASEOUS -> DustItemRegistry.ALCHEMICAL_GAS.get();
            };
        }

        public static Kind of(List<DustElement> elements)
        {
            if (elements.stream().anyMatch(element -> element.phase() == ElementPhase.GASEOUS)) return GASEOUS;
            if (elements.stream().anyMatch(element -> element.phase() == ElementPhase.AQUEOUS)) return AQUEOUS;
            return SOLID;
        }
    }

    // A solid compound of these elements, from this material
    public static ItemStack create(List<DustElement> elements, ItemStack original)
    {
        return create(elements, original, Kind.SOLID);
    }

    // A compound of these elements, from this material, of this kind (which item it is)
    public static ItemStack create(List<DustElement> elements, ItemStack original, Kind kind)
    {
        ItemStack compound = new ItemStack(kind.item());
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
