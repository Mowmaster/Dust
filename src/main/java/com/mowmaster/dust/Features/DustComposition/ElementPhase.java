package com.mowmaster.dust.Features.DustComposition;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

// The state an element takes when it's broken out of something, and so what it needs to be caught:
//   GASEOUS       a glass bottle, or it hisses away in a puff of its colour
//   AQUEOUS       a bowl, or it bubbles away in its colour
//   AMORPHOUS     paper, or it burns away in fire of its colour, leaving alchemical soot
//   CRYSTALLINE   nothing: it stays behind as it is
// When something breaks down into two or more elements at once, an elemental phial is needed to keep them together;
// without one the crystalline part stays and the rest boils or burns off (with an amorphous part in the mix, both burn
// away to soot). That's for the breakdown machines; this only says what each phase is.
//
// StringRepresentable: has a lowercase name for saving and data ("gaseous"), like vanilla's block state enums.
public enum ElementPhase implements StringRepresentable
{
    GASEOUS("gaseous", () -> Items.GLASS_BOTTLE),
    AQUEOUS("aqueous", () -> Items.BOWL),
    AMORPHOUS("amorphous", () -> Items.PAPER),
    CRYSTALLINE("crystalline", () -> null);

    private final String name;
    // A Supplier (a function giving a value when called), so the item is looked up when needed, not when this enum
    // loads (which could be before items exist)
    private final Supplier<@Nullable Item> container;

    ElementPhase(String name, Supplier<@Nullable Item> container)
    {
        this.name = name;
        this.container = container;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }

    // What catches it (null for crystalline, which needs nothing)
    public @Nullable Item container()
    {
        return container.get();
    }

    // Whether it's lost when nothing is there to catch it
    public boolean escapesUncaught()
    {
        return this != CRYSTALLINE;
    }

    // "Gaseous" (dust.phase.<name>)
    public Component displayName()
    {
        return Component.translatable("dust.phase." + name);
    }

    // What happens to it uncaught, for the book: "Caught in a glass bottle, or it hisses away." (dust.phase.<name>.note)
    public Component note()
    {
        return Component.translatable("dust.phase." + name + ".note");
    }
}
