package com.mowmaster.dust.Features.Spells.api;

// A number a spell can have more of: duration, potency, size, range... Each level changes the cast's stats. Registered
// in SpellRegistries.ATTRIBUTES; other mods add their own the same way.
public interface SpellAttribute
{
    // The stats with this many levels of the attribute applied
    SpellStats apply(SpellStats stats, int level);
}
