package com.mowmaster.dust.Features.Spells.api;

import com.mowmaster.dust.Features.DustComposition.DustElement;

import java.util.Map;

// A bigger change to a spell than more of a number: multishot, splitting, an area burst, an effect after the hit...
// Registered in SpellRegistries.MODIFIERS; other mods add their own the same way. Every hook is optional. What makes
// a modifier turn up on a pylon (its weight, category, tier, preconditions) comes with the pylons (step 2).
public interface SpellModifier
{
    // The extra dust it costs to cast, by element (1 of the spell's element unless it says otherwise)
    default Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return Map.of(spellElement, 1);
    }

    // Changes the cast's numbers (after attributes)
    default SpellStats modifyStats(SpellStats stats)
    {
        return stats;
    }

    // Runs as the spell is cast, before the shape delivers it. Returning false stops the shape (the modifier delivered
    // the spell itself, e.g. several projectiles instead of one).
    default boolean beforeDelivery(SpellCast cast)
    {
        return true;
    }
}
