package com.mowmaster.dust.Features.Magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;

import java.util.ArrayList;
import java.util.List;

// One chunk's aura (attachment CHUNK_AURA): how much of each primal dust it holds, indexed by EnumElement.ordinal()
// (fire, water, earth, chaos, order), as fractions so slow changes aren't lost to rounding; and the game time it was
// last brought up to date (a chunk that was unloaded catches up on its drift back to normal when it loads).
// A chunk that has never been touched has no data at all and counts as the baseline (ChunkAura.BASELINE of each).
public record ChunkAuraData(float[] primals, long updated)
{
    public static final int COUNT = EnumElement.values().length;

    // float[] saved as a list of floats
    private static final Codec<float[]> PRIMALS_CODEC = Codec.FLOAT.listOf().xmap(
            list -> {
                float[] values = new float[COUNT];
                for (int i = 0; i < Math.min(COUNT, list.size()); i++) values[i] = list.get(i);
                return values;
            },
            values -> {
                List<Float> list = new ArrayList<>(values.length);
                for (float value : values) list.add(value);
                return list;
            });

    public static final Codec<ChunkAuraData> CODEC = RecordCodecBuilder.create(i -> i.group(
            PRIMALS_CODEC.fieldOf("primals").forGetter(ChunkAuraData::primals),
            Codec.LONG.optionalFieldOf("updated", 0L).forGetter(ChunkAuraData::updated)
    ).apply(i, ChunkAuraData::new));

    // An untouched chunk: the baseline of each primal
    public static ChunkAuraData baseline(long now)
    {
        float[] values = new float[COUNT];
        java.util.Arrays.fill(values, ChunkAura.BASELINE);
        return new ChunkAuraData(values, now);
    }

    public float get(EnumElement primal)
    {
        return primals[primal.ordinal()];
    }

    public float total()
    {
        float total = 0;
        for (float value : primals) total += value;
        return total;
    }

    // A copy to change (records keep their array, so changes always go through a copy)
    public float[] copy()
    {
        return primals.clone();
    }
}
