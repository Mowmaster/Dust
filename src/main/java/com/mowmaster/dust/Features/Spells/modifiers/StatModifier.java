package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.api.SpellStats;

import java.util.Map;
import java.util.function.UnaryOperator;

// A modifier that changes the spell's numbers (and maybe its cost): most of the built-in ones (SpellRegistries).
//   change        what it does to the stats (UnaryOperator: a function from SpellStats to SpellStats)
//   extraCost     dust of the spell's element it adds (1 normally; negative to make the spell cheaper)
//   costPercent   a change to the whole cost, in percent
public record StatModifier(ModifierCategory category, ModifierTier tier, int weight, UnaryOperator<SpellStats> change,
                           int extraCost, int costPercent) implements SpellModifier
{
    // A normal modifier costing 1 dust
    public static StatModifier of(ModifierCategory category, int weight, UnaryOperator<SpellStats> change)
    {
        return new StatModifier(category, ModifierTier.NORMAL, weight, change, 1, 0);
    }

    @Override
    public Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return Map.of(spellElement, extraCost);
    }

    @Override
    public SpellStats modifyStats(SpellStats stats)
    {
        return change.apply(stats);
    }
}
