package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.util.StringRepresentable;

// How a modifier comes about:
//   NORMAL      drawn into the deck from the pool as usual
//   BLESSED     put in the deck when the tree's blessing reaches 10; only a Neutral pylon can take one (it becomes a
//               Blessed pylon, or now and then a Legendary one); one use each
//   CORRUPTED   put in the deck when the tree's corruption reaches 5; the next pylon placed takes it, whatever its kind;
//               one use each
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
