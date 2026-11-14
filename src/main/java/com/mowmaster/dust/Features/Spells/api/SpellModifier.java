package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import com.mowmaster.dust.Features.DustComposition.DustElement;

import java.util.Map;

// A change to a spell, rolled onto a pylon: more of a number (size, potency...), cheaper casting, or something bigger
// (multishot, splitting, an effect after the hit...). Registered in SpellRegistries.MODIFIERS; other mods add their
// own the same way. Every method has a default, so a modifier only overrides what it needs.
public interface SpellModifier
{
    // ---- rolling (on a newly placed pylon) ----

    // Which pylon category makes it more likely
    default ModifierCategory category() { return ModifierCategory.GENERAL; }

    // Normal, or only while the tree is blessed / corrupted
    default ModifierTier tier() { return ModifierTier.NORMAL; }

    // How likely it is, against the others (before boosts)
    default int weight() { return 10; }

    // Its preconditions: whether it can be rolled for this spell at this point in the line
    default boolean canRoll(ModifierContext context) { return true; }

    // False for modifiers that have to be unlocked another way (research, exploring...) before they can be rolled.
    // Once a modifier has been on one of a player's spells, it's unlocked for them for that element.
    default boolean startsUnlocked() { return true; }

    // Chosen by the roll: the modifier the pylon actually gets. Normally itself (null); a stand-in can hand back another,
    // e.g. the Fragment slot picks a random fragment for the spell's element.
    default @Nullable Identifier resolveRoll(ModifierContext context, RandomSource random)
    {
        return null;
    }

    // ---- casting ----

    // The extra dust it costs to cast, by element (1 of the spell's element unless it says otherwise; a negative
    // amount makes it cheaper, never below 1 in all)
    default Map<DustElement, Integer> cost(DustElement spellElement)
    {
        return Map.of(spellElement, 1);
    }

    // A change to the whole cost, in percent (-25: a quarter cheaper), applied after every modifier's cost
    default int costPercent() { return 0; }

    // Changes the cast's numbers (after attributes)
    default SpellStats modifyStats(SpellStats stats)
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
