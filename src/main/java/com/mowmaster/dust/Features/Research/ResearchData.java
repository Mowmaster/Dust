package com.mowmaster.dust.Features.Research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

// Everything a player has researched: for each tab (by tab id), its entries (by entry id). Kept on the player
// (DustAttachmentTypeRegistry.RESEARCH) and copied onto a Shared Research Guide.
public record ResearchData(Map<Identifier, Map<Identifier, EntryResearch>> tabs)
{
    public static final ResearchData EMPTY = new ResearchData(Map.of());

    private static final Codec<Map<Identifier, EntryResearch>> ENTRIES_CODEC = Codec.unboundedMap(Identifier.CODEC, EntryResearch.CODEC);

    // Saved as {"tabs": {"dust:blocks": {...}, ...}}. Research saved before tabs existed ({"blocks": ..., "entities": ...})
    // still loads: blocks go to dust:blocks, entities to dust:creatures.
    public static final Codec<ResearchData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Identifier.CODEC, ENTRIES_CODEC).optionalFieldOf("tabs", Map.of()).forGetter(ResearchData::tabs),
            ENTRIES_CODEC.optionalFieldOf("blocks", Map.of()).forGetter(data -> Map.of()),
            ENTRIES_CODEC.optionalFieldOf("entities", Map.of()).forGetter(data -> Map.of())
    ).apply(instance, ResearchData::fromSaved));

    private static ResearchData fromSaved(Map<Identifier, Map<Identifier, EntryResearch>> tabs,
                                          Map<Identifier, EntryResearch> oldBlocks, Map<Identifier, EntryResearch> oldEntities)
    {
        if (oldBlocks.isEmpty() && oldEntities.isEmpty()) return new ResearchData(tabs);
        Map<Identifier, Map<Identifier, EntryResearch>> merged = new HashMap<>(tabs);
        mergeOld(merged, Identifier.fromNamespaceAndPath(DustReferences.MODID, "blocks"), oldBlocks);
        mergeOld(merged, Identifier.fromNamespaceAndPath(DustReferences.MODID, "creatures"), oldEntities);
        return new ResearchData(Map.copyOf(merged));
    }

    private static void mergeOld(Map<Identifier, Map<Identifier, EntryResearch>> into, Identifier tab, Map<Identifier, EntryResearch> old)
    {
        if (old.isEmpty()) return;
        Map<Identifier, EntryResearch> entries = new HashMap<>(old);
        entries.putAll(into.getOrDefault(tab, Map.of()));
        into.put(tab, Map.copyOf(entries));
    }

    // Sent to the client through the codec (simple; the data is small)
    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchData> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public @Nullable EntryResearch get(ResearchEntry entry)
    {
        return tabs.getOrDefault(entry.tab().id(), Map.of()).get(entry.id());
    }

    public boolean has(ResearchEntry entry)
    {
        return get(entry) != null;
    }

    // Any member of the group researched?
    public boolean hasAny(ResearchTab tab, List<Identifier> group)
    {
        Map<Identifier, EntryResearch> entries = tabs.getOrDefault(tab.id(), Map.of());
        for (Identifier id : group) {
            if (entries.containsKey(id)) return true;
        }
        return false;
    }

    // The group member researched first (earliest day and time), or null if none
    public @Nullable Identifier firstResearched(ResearchTab tab, List<Identifier> group)
    {
        Map<Identifier, EntryResearch> entries = tabs.getOrDefault(tab.id(), Map.of());
        Identifier first = null;
        long firstWhen = Long.MAX_VALUE;
        for (Identifier id : group) {
            EntryResearch research = entries.get(id);
            if (research == null) continue;
            long when = research.day() * 24000L + research.time();
            if (when < firstWhen) {
                firstWhen = when;
                first = id;
            }
        }
        return first;
    }

    // How many of the tab's list icons (groups) have something researched
    public int countGroups(ResearchTab tab)
    {
        int found = 0;
        for (List<Identifier> group : tab.groups()) {
            if (hasAny(tab, group)) found++;
        }
        return found;
    }

    // The researched entries (any tab) seen dropping this item, in tab order: an item entry's Sources
    public List<ResearchEntry> sourcesOf(Identifier item)
    {
        List<ResearchEntry> sources = new java.util.ArrayList<>();
        for (ResearchTab tab : ResearchTabs.all()) {
            for (Map.Entry<Identifier, EntryResearch> entry : tabs.getOrDefault(tab.id(), Map.of()).entrySet()) {
                if (entry.getValue().drops().contains(item)) sources.add(new ResearchEntry(tab, entry.getKey()));
            }
        }
        return sources;
    }

    // A copy with one entry changed (or added). The function gets null when the entry is new.
    public ResearchData update(ResearchEntry entry, UnaryOperator<@Nullable EntryResearch> change)
    {
        Map<Identifier, EntryResearch> entries = new HashMap<>(tabs.getOrDefault(entry.tab().id(), Map.of()));
        EntryResearch before = entries.get(entry.id());
        EntryResearch after = change.apply(before);
        if (after == before) return this;
        if (after == null) entries.remove(entry.id());
        else entries.put(entry.id(), after);
        Map<Identifier, Map<Identifier, EntryResearch>> copy = new HashMap<>(tabs);
        copy.put(entry.tab().id(), Map.copyOf(entries));
        return new ResearchData(Map.copyOf(copy));
    }
}
