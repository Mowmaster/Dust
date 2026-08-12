package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum EnumAffinity implements StringRepresentable {
    NONE("none", 0, List.of("none"), 0.0f, 1.0f),

    FIRE("fire", 1, List.of("fire"), 0.5f, 1.5f),
    WATER("water", 1, List.of("water"), 0.5f, 1.5f),
    EARTH("earth", 1, List.of("earth"), 0.5f, 1.5f),
    CHAOS("chaos", 1, List.of("chaos"), 0.5f, 1.5f),
    ORDER("order", 1, List.of("order"), 0.5f, 1.5f),

    AIR("air", 2, List.of("fire","water"), 0.8f, 1.2f),
    METAL("metal", 2, List.of("fire","earth"), 0.8f, 1.2f),
    MUD("mud", 2, List.of("earth","water"), 0.8f, 1.2f),

    LIGHT("light", 2, List.of("fire","order"), 0.8f, 1.2f),
    ICE("ice", 2, List.of("water","order"), 0.8f, 1.2f),
    STONE("stone", 2, List.of("earth","order"), 0.8f, 1.2f),

    LAVA("lava", 2, List.of("fire","chaos"), 0.8f, 1.2f),
    MOVEMENT("movement", 2, List.of("water","chaos"), 0.8f, 1.2f),
    EXPLOSION("explosion", 2, List.of("earth","chaos"), 0.8f, 1.2f);

    private final String name;
    private final int tier;
    private final List<String> parts;
    private final float discount;
    private final float multiplier;

    private EnumAffinity(String name, int tier, List<String> parts, float discount, float multiplier)
    {
        this.name = name;
        this.tier = tier;
        this.parts = parts;
        this.discount = discount;
        this.multiplier = multiplier;
    }


    public @NotNull String getSerializedName() { return name; }
    public int getAffinityTier() {return tier;}
    public List<String> getAffinityComposition() { return parts; }
    public float getElementDiscount() { return discount; }
    public float getElementMultiplier() { return multiplier; }

    public static final com.mojang.serialization.Codec<EnumAffinity> CODEC =
            StringRepresentable.fromEnum(EnumAffinity::values);
}

