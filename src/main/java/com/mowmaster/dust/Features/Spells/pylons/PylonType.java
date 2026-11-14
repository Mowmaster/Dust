package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import org.jspecify.annotations.Nullable;

// The modifier pylons. SHAPE goes right after the Spell Pylon and holds the spell's shape (no level needed); the others
// each roll a modifier, favouring their category for a while (PylonConfig.CATEGORY_BOOST).
public enum PylonType
{
    SHAPE("shape", null),
    CHANGE("change", ModifierCategory.CHANGE),
    COST("cost", ModifierCategory.COST),
    COUNT("count", ModifierCategory.COUNT),
    DAMAGE("damage", ModifierCategory.DAMAGE);

    private final String name;
    private final @Nullable ModifierCategory category;

    PylonType(String name, @Nullable ModifierCategory category)
    {
        this.name = name;
        this.category = category;
    }

    public String serializedName() { return name; }

    // Whether a spell break can be rolled on this kind: only the neutral pylon. Until there is one (see the TODO), the
    // plain white Change pylon stands in for it.
    public boolean canBreak() { return this == CHANGE; }
    public @Nullable ModifierCategory category() { return category; }
}
