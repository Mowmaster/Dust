package com.mowmaster.dust.Features.Spells.api;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import org.jspecify.annotations.Nullable;

// A fragment: a modifier that changes significantly how a spell works. Fragments aren't rolled themselves: at spell
// levels 25, 50, 75 and 100 the Fragment slot (FragmentSlotModifier) joins the pool, after a spell break, and when it's
// rolled it becomes a random fragment for the spell's element that isn't in the line yet. Each fragment then lets its
// own aspects (SpellAspect) be rolled. Register them in SpellRegistries.MODIFIERS like any modifier.
public interface SpellFragment extends SpellModifier
{
    // The element it belongs to, or null for a fragment any element can get
    @Nullable DustElement element();

    default boolean fitsElement(DustElement spellElement)
    {
        DustElement own = element();
        return own == null || own.equals(spellElement);
    }

    // Only ever placed by the Fragment slot
    @Override
    default boolean canRoll(ModifierContext context) { return false; }

    @Override
    default ModifierCategory category() { return ModifierCategory.CHANGE; }
}
