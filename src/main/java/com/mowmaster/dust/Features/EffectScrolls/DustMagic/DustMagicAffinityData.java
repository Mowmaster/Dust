package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import net.minecraft.util.StringRepresentable;

import java.util.Map;

public final class DustMagicAffinityData {
    private final float[] weights; // length = Element.values().length, each in [0..1]

    private DustMagicAffinityData(float[] weights) {
        this.weights = weights;
    }

    public boolean touches(ElementEnum e) {
        // Use a tiny epsilon to avoid float noise
        return weight(e) > 1e-6f;
    }

    public static DustMagicAffinityData single(ElementEnum e) {
        float[] w = new float[ElementEnum.values().length];
        w[e.ordinal()] = 1.0f;
        return new DustMagicAffinityData(w);
    }

    public static DustMagicAffinityData composite(java.util.Map<ElementEnum, Float> map) {
        float[] w = new float[ElementEnum.values().length];
        for (var entry : map.entrySet()) {
            int i = entry.getKey().ordinal();
            w[i] = Math.max(0f, Math.min(1f, entry.getValue()));
        }
        return new DustMagicAffinityData(w);
    }

    public float weight(ElementEnum e) {
        return weights[e.ordinal()];
    }

    // Persistence
    public static final com.mojang.serialization.Codec<DustMagicAffinityData> CODEC =
            com.mojang.serialization.codecs.RecordCodecBuilder.create(inst -> inst.group(
                    com.mojang.serialization.Codec.list(com.mojang.serialization.Codec.FLOAT)
                            .fieldOf("weights")
                            .forGetter(a -> {
                                java.util.ArrayList<Float> list = new java.util.ArrayList<>(a.weights.length);
                                for (float v : a.weights) list.add(v);
                                return list;
                            })
            ).apply(inst, list -> {
                float[] w = new float[ElementEnum.values().length];
                for (int i = 0; i < w.length && i < list.size(); i++) {
                    w[i] = Math.max(0f, Math.min(1f, list.get(i)));
                }
                return new DustMagicAffinityData(w);
            }));

    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, DustMagicAffinityData> STREAM_CODEC =
            new net.minecraft.network.codec.StreamCodec<>() {
                @Override public DustMagicAffinityData decode(net.minecraft.network.RegistryFriendlyByteBuf buf) {
                    float[] w = new float[ElementEnum.values().length];
                    for (int i = 0; i < w.length; i++) w[i] = buf.readFloat();
                    return new DustMagicAffinityData(w);
                }
                @Override public void encode(net.minecraft.network.RegistryFriendlyByteBuf buf, DustMagicAffinityData a) {
                    for (float v : a.weights) buf.writeFloat(v);
                }
            };

    // Primary affinities
    public static DustMagicAffinityData fire() {
        return DustMagicAffinityData.single(ElementEnum.FIRE);
    }
    public static DustMagicAffinityData water() {
        return DustMagicAffinityData.single(ElementEnum.WATER);
    }
    public static DustMagicAffinityData earth() {
        return DustMagicAffinityData.single(ElementEnum.EARTH);
    }
    public static DustMagicAffinityData chaos() {
        return DustMagicAffinityData.single(ElementEnum.CHAOS);
    }
    public static DustMagicAffinityData order() {
        return DustMagicAffinityData.single(ElementEnum.ORDER);
    }

    // Secondary affinities (Make sure to manually set bonus mana and cost reduction to be half of the normal single element)
    public static DustMagicAffinityData air() { // 50/50 Fire/Water
        return DustMagicAffinityData.composite(Map.of(ElementEnum.FIRE, 0.5f, ElementEnum.WATER, 0.5f));
    }
    public static DustMagicAffinityData lava() { // 50/50 Fire/Earth
        return DustMagicAffinityData.composite(Map.of(ElementEnum.FIRE, 0.5f, ElementEnum.EARTH, 0.5f));
    }
    public static DustMagicAffinityData Mud() { // 50/50 Earth/Water
        return DustMagicAffinityData.composite(Map.of(ElementEnum.EARTH, 0.5f, ElementEnum.WATER, 0.5f));
    }
    //Secondary affinities of Order
    public static DustMagicAffinityData light() { // 50/50 Fire/Order
        return DustMagicAffinityData.composite(Map.of(ElementEnum.FIRE, 0.5f, ElementEnum.ORDER, 0.5f));
    }
    public static DustMagicAffinityData ice() { // 50/50 Water/Order
        return DustMagicAffinityData.composite(Map.of(ElementEnum.WATER, 0.5f, ElementEnum.ORDER, 0.5f));
    }
    public static DustMagicAffinityData stone() { // 50/50 Earth/Order
        return DustMagicAffinityData.composite(Map.of(ElementEnum.EARTH, 0.5f, ElementEnum.ORDER, 0.5f));
    }
    //Secondary affinities of Chaos
    public static DustMagicAffinityData hell() { // 50/50 Fire/Chaos
        return DustMagicAffinityData.composite(Map.of(ElementEnum.FIRE, 0.5f, ElementEnum.CHAOS, 0.5f));
    }
    public static DustMagicAffinityData movement() { // 50/50 Water/Chaos
        return DustMagicAffinityData.composite(Map.of(ElementEnum.WATER, 0.5f, ElementEnum.CHAOS, 0.5f));
    }
    public static DustMagicAffinityData explosion() { // 50/50 Earth/Chaos
        return DustMagicAffinityData.composite(Map.of(ElementEnum.EARTH, 0.5f, ElementEnum.CHAOS, 0.5f));
    }
}

