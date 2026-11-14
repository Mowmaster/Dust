package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.resources.Identifier;

// An aspect: a modifier that strengthens one fragment's traits. It joins the pool once its fragment is in the spell's
// line (and not before). Register them in SpellRegistries.MODIFIERS like any modifier.
public interface SpellAspect extends SpellModifier
{
    // The fragment it belongs to (its modifier id)
    Identifier fragment();

    @Override
    default boolean canRoll(ModifierContext context) { return context.has(fragment()); }
}
