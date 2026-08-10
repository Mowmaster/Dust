package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.Optional;

public final class DustMagicData {
    // Fixed order aligned with Element.values()
    private final int[] current = new int[ElementEnum.values().length];

    private int baseCapacity = 10;             // tune as you like
    private Optional<DustMagicAffinityData> affinity = Optional.empty();
    //Set affinityCostMultiplier bonus at 0.5f (50%) for single element affinities and 0.8f (20%) for double affinities,
    //as a way to balance out why a single affinity is better than a double,
    //and set capacity increase at 30% for single and 10% for double
    //or incrementally increase both benefits as player levels up?????
    private int affinityCapacityBonus = 0;
    private float affinityCostMultiplier = 1.0f;

    // Accessors
    public int get(ElementEnum e) { return current[e.ordinal()]; }

    public int getBaseElementCapacity() {
        return baseCapacity;
    }

    public int effectiveCapacity(ElementEnum e) {
        float w = affinity.map(a -> a.weight(e)).orElse(0f);
        int bonus = Math.round(affinityCapacityBonus * w);
        return baseCapacity + bonus;
    }

    public int effectiveCost(ElementEnum e, int baseCost) {
        float w = affinity.map(a -> a.weight(e)).orElse(0f);
        float mult = 1.0f + (affinityCostMultiplier - 1.0f) * w; // lerp by weight
        return Math.max(0, Math.round(baseCost * mult));
    }

    public int add(ElementEnum e, int amount) {
        if (amount <= 0) return 0;
        int idx = e.ordinal();
        int cap = effectiveCapacity(e);
        int space = cap - current[idx];
        int added = Math.min(space, amount);
        if (added > 0) current[idx] += added;
        return added;
    }

    public boolean consume(ElementEnum e, int baseCost) {
        int cost = effectiveCost(e, baseCost);
        int idx = e.ordinal();
        if (current[idx] >= cost) {
            current[idx] -= cost;
            return true;
        }
        return false;
    }

    public void setElement(ElementEnum element, int val)
    {
        int idx = element.ordinal();
        current[idx] = val;
    }
    // Config setters (adjust via game progression or items)
    public void setBaseCapacity(int v) { baseCapacity = Math.max(0, v); }
    public boolean hasAffinity() { return this.affinity.isPresent(); }
    public Optional<DustMagicAffinityData> getAffinity() { return this.affinity; }
    public boolean hasAnyAffinityTo(ElementEnum e) {return affinity.map(a -> a.touches(e)).orElse(false);}
    public void setAffinity(DustMagicAffinityData a) { this.affinity = Optional.ofNullable(a); }
    public void clearAffinity() { this.affinity = Optional.empty(); }
    public void setAffinityCapacityBonus(int v) { affinityCapacityBonus = Math.max(0, v); }
    public void setAffinityCostMultiplier(float v) { affinityCostMultiplier = Math.max(0f, v); }
    public float getAffinityCostMultiplier() { return affinityCostMultiplier; }

    // Persistence (Codec) — fixed-size int list for currents
    public static final Codec<DustMagicData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.list(Codec.INT).fieldOf("current").forGetter(mp -> {
                IntArrayList l = new IntArrayList(mp.current.length);
                for (int v : mp.current) l.add(v);
                return l;
            }),
            Codec.INT.fieldOf("base_capacity").forGetter(mp -> mp.baseCapacity),
            DustMagicAffinityData.CODEC.optionalFieldOf("affinity").forGetter(mp -> mp.affinity),
            Codec.INT.fieldOf("affinity_capacity_bonus").forGetter(mp -> mp.affinityCapacityBonus),
            Codec.FLOAT.fieldOf("affinity_cost_multiplier").forGetter(mp -> mp.affinityCostMultiplier)
    ).apply(inst, (currList, baseCap, aff, capBonus, costMul) -> {
        DustMagicData mp = new DustMagicData();
        Arrays.fill(mp.current, 0);
        for (int i = 0; i < mp.current.length && i < currList.size(); i++) {
            mp.current[i] = Math.max(0, currList.get(i));
        }
        mp.baseCapacity = Math.max(0, baseCap);
        mp.affinity = aff;
        mp.affinityCapacityBonus = Math.max(0, capBonus);
        mp.affinityCostMultiplier = Math.max(0f, costMul);
        return mp;
    }));

    // Networking (StreamCodec) — compact and stable
    public static final StreamCodec<RegistryFriendlyByteBuf, DustMagicData> STREAM_CODEC =
            new StreamCodec<>() {
                @Override public DustMagicData decode(RegistryFriendlyByteBuf buf) {
                    DustMagicData mp = new DustMagicData();
                    for (int i = 0; i < mp.current.length; i++) mp.current[i] = buf.readVarInt();
                    mp.baseCapacity = buf.readVarInt();
                    boolean hasAff = buf.readBoolean();
                    if (hasAff) mp.affinity = Optional.of(DustMagicAffinityData.STREAM_CODEC.decode(buf));
                    else mp.affinity = Optional.empty();
                    mp.affinityCapacityBonus = buf.readVarInt();
                    mp.affinityCostMultiplier = buf.readFloat();
                    return mp;
                }
                @Override public void encode(RegistryFriendlyByteBuf buf, DustMagicData mp) {
                    for (int v : mp.current) buf.writeVarInt(v);
                    buf.writeVarInt(mp.baseCapacity);
                    buf.writeBoolean(mp.affinity.isPresent());
                    mp.affinity.ifPresent(a -> DustMagicAffinityData.STREAM_CODEC.encode(buf, a));
                    buf.writeVarInt(mp.affinityCapacityBonus);
                    buf.writeFloat(mp.affinityCostMultiplier);
                }
            };
}

