package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.util.StringRepresentable;

// The kind of change a modifier makes. A pylon of a category (Change, Cost, Count, Damage) makes its category more
// likely to be rolled for a while (SpellPylonBlockEntity's boosts).
//   GENERAL   anything that fits no category
//   CHANGE    major changes to how the spell works (multishot, ...)
//   COST      cheaper (or, corrupted, dearer but stronger)
//   COUNT     how much / how far / how long: size, range, duration, splits...
//   DAMAGE    how hard it hits
public enum ModifierCategory implements StringRepresentable
{
    GENERAL("general"), CHANGE("change"), COST("cost"), COUNT("count"), DAMAGE("damage");

    public static final com.mojang.serialization.Codec<ModifierCategory> CODEC = StringRepresentable.fromEnum(ModifierCategory::values);

    private final String name;

    ModifierCategory(String name)
    {
        this.name = name;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
