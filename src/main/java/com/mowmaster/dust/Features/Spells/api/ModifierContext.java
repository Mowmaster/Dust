package com.mowmaster.dust.Features.Spells.api;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

// What a modifier can look at when deciding whether it can be rolled on a pylon (its preconditions):
//   element, shape  the spell's
//   modifiers       those already in the tree's line up to this pylon, in order
//   level           the spell tree's level
//   breaks          how many spell breaks come before this pylon in its line
//   mastery         the placing player's highest spell level for each element (SpellMastery; empty without a player)
public record ModifierContext(DustElement element, Identifier shape, List<Identifier> modifiers, int level, int breaks,
                              Map<DustElement, Integer> mastery)
{
    // The old form, for code that only knows the first three: level 0, no breaks, no mastery
    public ModifierContext(DustElement element, Identifier shape, List<Identifier> modifiers)
    {
        this(element, shape, modifiers, 0, 0, Map.of());
    }

    public boolean has(Identifier modifier)
    {
        return modifiers.contains(modifier);
    }

    public long count(Identifier modifier)
    {
        return modifiers.stream().filter(modifier::equals).count();
    }

    // How many of the line's modifiers are of a kind (SpellFragment, FusionModifier...)
    public long countOf(Class<?> kind)
    {
        return modifiers.stream().map(SpellRegistries.MODIFIER_REGISTRY::getValue).filter(Objects::nonNull).filter(kind::isInstance).count();
    }

    public int masteryOf(DustElement element)
    {
        return mastery.getOrDefault(element, 0);
    }
}
