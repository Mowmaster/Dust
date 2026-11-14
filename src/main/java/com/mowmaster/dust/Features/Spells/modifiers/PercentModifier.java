package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.api.SpellStats;

import java.util.Map;
import java.util.function.BiFunction;

// A blessed / corrupted modifier: it doesn't level up, its strength is a percent rolled onto its pylon instead
// (PylonConfig.MIN_STRENGTH-MAX_STRENGTH) and carried on the spell (Spell.strengths).
//   change           what it does to the stats, given the strength as a fraction (30% -> 0.3F). BiFunction: a function
//                    of two arguments.
//   extraCost        dust of the spell's element it adds
//   costPerPercent   how the whole cost changes per percent of strength (-1: as many percent cheaper; 1: dearer)
public record PercentModifier(ModifierCategory category, ModifierTier tier, int weight, BiFunction<SpellStats, Float, SpellStats> change,
                              int extraCost, int costPerPercent) implements SpellModifier
{
    // Used if a spell somehow has no strength for it (an old scroll): the middle of the range
    private static final int FALLBACK_STRENGTH = 30;

    @Override
    public boolean usesStrength()
    {
        return true;
    }

    @Override
    public Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return Map.of(spellElement, extraCost);
    }

    @Override
    public SpellStats modifyStats(SpellStats stats)
    {
        return modifyStats(stats, FALLBACK_STRENGTH);
    }

    @Override
    public SpellStats modifyStats(SpellStats stats, int strength)
    {
        return change.apply(stats, (strength <= 0 ? FALLBACK_STRENGTH : strength) / 100F);
    }

    @Override
    public int costPercent(int strength)
    {
        return costPerPercent * (strength <= 0 ? FALLBACK_STRENGTH : strength);
    }
}
