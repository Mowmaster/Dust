package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.util.StringRepresentable;

// How a modifier came about. Blessed ones only turn up while a spell tree has blessing built up (from keeping its
// rolls), corrupted ones while it has corruption (from rerolling); rolling one uses some of it up.
public enum ModifierTier implements StringRepresentable
{
    NORMAL("normal"), BLESSED("blessed"), CORRUPTED("corrupted");

    public static final com.mojang.serialization.Codec<ModifierTier> CODEC = StringRepresentable.fromEnum(ModifierTier::values);

    private final String name;

    ModifierTier(String name)
    {
        this.name = name;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
