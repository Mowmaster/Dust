package com.mowmaster.dust.Features.Research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

// Everything a player has researched: block entries (blocks and plants) and entity entries (animals, mobs, bosses),
// by registry id. Kept on the player (DustAttachmentTypeRegistry.RESEARCH), and copied onto a Shared Research Guide.
public record ResearchData(Map<Identifier, EntryResearch> blocks, Map<Identifier, EntryResearch> entities)
{
    public static final ResearchData EMPTY = new ResearchData(Map.of(), Map.of());

    public static final Codec<ResearchData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Identifier.CODEC, EntryResearch.CODEC).optionalFieldOf("blocks", Map.of()).forGetter(ResearchData::blocks),
            Codec.unboundedMap(Identifier.CODEC, EntryResearch.CODEC).optionalFieldOf("entities", Map.of()).forGetter(ResearchData::entities)
    ).apply(instance, ResearchData::new));

    // Sent to the client through the codec (simple; the data is small)
    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchData> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public @Nullable EntryResearch get(ResearchEntry entry)
    {
        return (entry.isEntity() ? entities : blocks).get(entry.id());
    }

    public boolean has(ResearchEntry entry)
    {
        return get(entry) != null;
    }

    public int count(ResearchCategory category)
    {
        int found = 0;
        for (ResearchEntry entry : category.entries()) {
            if (has(entry)) found++;
        }
        return found;
    }

    // A copy with one entry changed (or added). The function gets null when the entry is new.
    public ResearchData update(ResearchEntry entry, UnaryOperator<@Nullable EntryResearch> change)
    {
        Map<Identifier, EntryResearch> map = new HashMap<>(entry.isEntity() ? entities : blocks);
        EntryResearch before = map.get(entry.id());
        EntryResearch after = change.apply(before);
        if (after == before) return this;
        if (after == null) map.remove(entry.id());
        else map.put(entry.id(), after);
        return entry.isEntity() ? new ResearchData(blocks, Map.copyOf(map)) : new ResearchData(Map.copyOf(map), entities);
    }
}
