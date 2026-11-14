package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.util.StringRepresentable;

// The pylon categories a modifier belongs to (SpellModifier.categories): a category pylon draws its modifier from the
// deck among that category's (any if there are none), and adds another of them to the deck when it locks. A
// modifier in no category only comes on a Neutral pylon (which can draw anything).
//   DAMAGE        how hard it hits: Damage, Potency, Impact Damage           (red)
//   GIANT         how big / how far: Size, Targets, Chain, Depth, Range...   (blue)
//   THRIFTY       cheaper: Mana / Dust Reduction, Transmutation, Efficiency  (yellow)
//   EXPERIENCED   the spell's growth: XP Gain, Fragment Progress...          (green)
public enum ModifierCategory implements StringRepresentable
{
    DAMAGE("damage"), GIANT("giant"), THRIFTY("thrifty"), EXPERIENCED("experienced");

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
