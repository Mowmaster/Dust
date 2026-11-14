package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import com.mowmaster.dust.Features.DustComposition.DustElement;

import java.util.List;
import java.util.Map;
import java.util.Set;

// A change to a spell, rolled onto a pylon: more of a number (size, potency...), cheaper casting, or something bigger
// (multishot, splitting, an effect after the hit...). Registered in SpellRegistries.MODIFIERS; other mods add their
// own the same way. Every method has a default, so a modifier only overrides what it needs.
public interface SpellModifier
{
    // ---- drawing (the pool, the deck, a newly placed pylon) ----

    // The pylon categories it belongs to (none: only a Neutral pylon takes it)
    default Set<ModifierCategory> categories() { return Set.of(); }

    // Normal, blessed or corrupted (how it gets into the deck: ModifierTier)
    default ModifierTier tier() { return ModifierTier.NORMAL; }

    // Which spells it suits: instant ones, lingering ones, or any (ModifierFit)
    default ModifierFit fit() { return ModifierFit.ANY; }

    // How likely it is to be drawn from the pool into the deck, against the others
    default int weight() { return 10; }

    // The spell level it's drawn into the deck from
    default int unlockLevel() { return 0; }

    // How many levels of it each upgrade node gives (a random number from min to max). A pylon starts with one level.
    default int minLevelsPerNode() { return 1; }
    default int maxLevelsPerNode() { return 1; }

    // How many times it can come out of the pool: 0 for no limit, otherwise a "knockout" modifier, gone from the pool
    // once it's been drawn that many times
    default int uses() { return 0; }

    // Modifiers that only join the pool once this one is on a pylon (a shape modifier's own modifiers); one of them
    // also goes straight into the deck
    default List<Identifier> addsToPool() { return List.of(); }

    // Its preconditions: whether it can go on a pylon at this point in the line (shape, counts, incompatibilities...)
    default boolean canRoll(ModifierContext context) { return true; }

    // False for modifiers that have to be unlocked another way (research, exploring...) before they can be drawn.
    // Once a modifier has been on one of a player's spells, it's unlocked for them for that element.
    default boolean startsUnlocked() { return true; }

    // Chosen for a pylon: the modifier the pylon actually gets. Normally itself (null); a stand-in can hand back another,
    // e.g. the Fragment slot picks a random fragment for the spell's element.
    default @Nullable Identifier resolveRoll(ModifierContext context, RandomSource random)
    {
        return null;
    }

    // ---- casting (levels: how many levels of it the spell has; a Legendary pylon's count double) ----

    // The extra dust it costs to cast, by element (1 of the spell's element unless it says otherwise; a negative
    // amount makes it cheaper, never below 1 in all)
    default Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return Map.of(spellElement, 1);
    }

    // A change to the whole dust cost, in percent (-25: a quarter cheaper), applied after every modifier's cost
    default int costPercent(int levels) { return 0; }

    // Changes the cast's numbers (after attributes)
    default SpellStats modifyStats(SpellStats stats, int levels)
    {
        return stats;
    }

    // Runs as the spell is cast, before the shape delivers it. Returning false stops the shape: the modifier delivered
    // the spell itself (e.g. several projectiles instead of one) and calls cast.markDelivered().
    default boolean beforeDelivery(SpellCast cast)
    {
        return true;
    }
}
