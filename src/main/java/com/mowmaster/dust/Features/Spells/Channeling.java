package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellConfig;

// Channelling a Beam spell: held down (a scroll or Grimoire in use), it pulses, each pulse a cast of the beam's ray
// (SpellCasting.deliver -> shapes/BeamRay), for as long as it's held and the dust lasts.
//   interval    ticks between pulses: SpellConfig.BEAM_INTERVAL, less a tick per level of Casting Speed (never below
//               BEAM_MIN_INTERVAL)
//   reduction   Efficiency: each second held takes 1% a level off a pulse's cost (at most MAX_EFFICIENCY percent)
public final class Channeling
{
    // How long it can be held (as good as forever)
    public static final int MAX_USE = 72000;

    private Channeling() {}

    public static boolean channels(Spell spell)
    {
        return spell.modifiers().contains(SpellRegistries.BEAM_MODIFIER.getId());
    }

    public static int interval(Spell spell)
    {
        return Math.max(SpellConfig.BEAM_MIN_INTERVAL, SpellConfig.BEAM_INTERVAL - spell.levelsOf(SpellRegistries.CASTING_SPEED.getId()));
    }

    // The cost off, in percent, after holding it this many ticks
    public static int reduction(Spell spell, int heldTicks)
    {
        int levels = spell.levelsOf(SpellRegistries.EFFICIENCY.getId());
        return Math.min(SpellConfig.MAX_EFFICIENCY, heldTicks / 20 * levels);
    }
}
