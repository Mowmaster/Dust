package com.mowmaster.dust.Features.Spells.api;

// How a spell is delivered: on the caster, to whatever they touch, as a projectile... A shape works out what the spell
// lands on and calls cast.applySelf / applyBlock / applyEntity, which run the element's matching effect. Registered in
// SpellRegistries.SHAPES; other mods add their own the same way.
public interface SpellShape
{
    // Delivers the spell (server side). False if there was nothing to cast at (Touch with nothing in reach): the cast
    // is then refunded.
    boolean cast(SpellCast cast);
}
