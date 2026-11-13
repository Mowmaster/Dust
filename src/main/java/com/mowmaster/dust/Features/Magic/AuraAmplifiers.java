package com.mowmaster.dust.Features.Magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

// Blocks in a chunk whose aura effects are amplified (Magic's block effect), kept on the chunk (attachment
// AURA_AMPLIFIERS): by position, how strongly and until when (game time). ChunkAura.amplification reads it.
public record AuraAmplifiers(Map<BlockPos, Amplifier> blocks)
{
    public record Amplifier(float factor, long until)
    {
        public static final Codec<Amplifier> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.FLOAT.fieldOf("factor").forGetter(Amplifier::factor),
                Codec.LONG.fieldOf("until").forGetter(Amplifier::until)
        ).apply(i, Amplifier::new));
    }

    // A map keyed by BlockPos is stored as a list of entries: map keys in codecs have to be strings
    private record Entry(BlockPos pos, Amplifier amplifier)
    {
        static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Entry::pos),
                Amplifier.CODEC.fieldOf("amplifier").forGetter(Entry::amplifier)
        ).apply(i, Entry::new));
    }

    public static final Codec<AuraAmplifiers> CODEC = Entry.CODEC.listOf().xmap(
            entries -> {
                Map<BlockPos, Amplifier> blocks = new HashMap<>();
                for (Entry entry : entries) blocks.put(entry.pos(), entry.amplifier());
                return new AuraAmplifiers(blocks);
            },
            amplifiers -> amplifiers.blocks().entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue())).toList());

    public static final AuraAmplifiers NONE = new AuraAmplifiers(Map.of());

    // A copy with pos amplified, and the expired ones dropped
    public AuraAmplifiers with(BlockPos pos, Amplifier amplifier, long now)
    {
        Map<BlockPos, Amplifier> copy = new HashMap<>(blocks);
        copy.values().removeIf(a -> a.until() < now);
        copy.put(pos.immutable(), amplifier);
        return new AuraAmplifiers(copy);
    }
}
