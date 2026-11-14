package com.mowmaster.dust.Features.Spells.api;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

// How an element's effect plays out, for each thing it lands on (ElementEffect.type): modifiers will be sorted by this
// in the modifier rework (some only suit lingering effects, some only one-off ones).
//   DOT       damage over time, or anything that lingers: a status effect, a buff, a timed mark, an area left behind
//   INSTANT   does its thing once, on the hit: damage, a heal, a push, a block changed for good
public enum EffectType implements StringRepresentable
{
    DOT("dot"), INSTANT("instant");

    public static final com.mojang.serialization.Codec<EffectType> CODEC = StringRepresentable.fromEnum(EffectType::values);

    private final String name;

    EffectType(String name)
    {
        this.name = name;
    }

    // "Lingering" / "Instant" (dust.effect_type.<name>)
    public Component displayName()
    {
        return Component.translatable("dust.effect_type." + name);
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
