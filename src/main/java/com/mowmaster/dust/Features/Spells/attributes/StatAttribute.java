package com.mowmaster.dust.Features.Spells.attributes;

import com.mowmaster.dust.Features.Spells.api.SpellAttribute;
import com.mowmaster.dust.Features.Spells.api.SpellStats;

import java.util.function.BiFunction;

// An attribute that's just a change to the stats per level (the built-in ones: SpellRegistries). A BiFunction is a
// function of two arguments: here (stats, level) -> changed stats.
public record StatAttribute(BiFunction<SpellStats, Integer, SpellStats> change) implements SpellAttribute
{
    @Override
    public SpellStats apply(SpellStats stats, int level)
    {
        return level <= 0 ? stats : change.apply(stats, level);
    }
}
