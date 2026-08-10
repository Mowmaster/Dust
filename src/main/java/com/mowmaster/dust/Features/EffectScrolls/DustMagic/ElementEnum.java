package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ElementEnum implements StringRepresentable {
    FIRE("fire"), WATER("water"), EARTH("earth"), CHAOS("chaos"), ORDER("order");

    private final String name;
    ElementEnum(String name) { this.name = name; }
    @Override public @NotNull String getSerializedName() { return name; }

    public static final com.mojang.serialization.Codec<ElementEnum> CODEC =
            StringRepresentable.fromEnum(ElementEnum::values);
}

