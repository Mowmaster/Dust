package com.mowmaster.dust.Features.Spells.api;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

// What a modifier can look at when deciding whether it can go on a pylon (its preconditions):
//   element, shape     the spell's
//   modifiers          those already in the line up to this pylon, in order (for "once per branch")
//   tree               every modifier in the whole tree (for "once every 10 levels")
//   level              the spell tree's level
//   breaks             how many spell breaks come before this pylon in its line
//   mastery            the placing player's highest spell level for each element (SpellMastery; empty without a player)
//   instant, dot       whether the spell's effect (on what its shape lands on) hits once / lingers (EffectType)
//   unlockReduction    levels taken off every modifier's unlock level (Reduced Requirements)
//   fragmentProgress   levels of Fragment Progress in the tree (the next fragment comes that much sooner)
public record ModifierContext(DustElement element, Identifier shape, List<Identifier> modifiers, List<Identifier> tree, int level, int breaks,
                              Map<DustElement, Integer> mastery, boolean instant, boolean dot, int unlockReduction, int fragmentProgress)
{
    public boolean has(Identifier modifier)
    {
        return modifiers.contains(modifier);
    }

    public long count(Identifier modifier)
    {
        return modifiers.stream().filter(modifier::equals).count();
    }

    // How many of this modifier the whole tree has
    public long countInTree(Identifier modifier)
    {
        return tree.stream().filter(modifier::equals).count();
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

    // Whether the tree has reached a modifier's unlock level (less any Reduced Requirements)
    public boolean reached(int unlockLevel)
    {
        return level >= unlockLevel - unlockReduction;
    }

    public boolean isShape(Identifier id)
    {
        return shape.equals(id);
    }
}
