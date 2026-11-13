package com.mowmaster.dust.Features.Spells.api;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.resources.Identifier;

import java.util.List;

// What a modifier can look at when deciding whether it can be rolled on a pylon (its preconditions): the spell's
// element and shape, and the modifiers already in the tree's line up to this pylon
public record ModifierContext(DustElement element, Identifier shape, List<Identifier> modifiers)
{
    public boolean has(Identifier modifier)
    {
        return modifiers.contains(modifier);
    }

    public long count(Identifier modifier)
    {
        return modifiers.stream().filter(modifier::equals).count();
    }
}
