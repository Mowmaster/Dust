package com.mowmaster.dust.Features.Research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

// What one player has learned about one research entry (a block, plant or mob).
// Immutable (a record): every change makes a new copy via the with... methods, which is what lets the attachment
// notice the change and sync it.
//
//   day, time     when it was first observed (world day number, and the time of day in ticks 0-23999)
//   biomes        every biome it has been observed in (2+ unlocks the "Where found" page)
//   drops         items it has been seen to drop (breaking it / defeating it)
//   maxHealth     a mob's maximum health, once fought (-1 = not known)
//   breeding      items it has been fed to breed
//   madeFrom      items used to craft it
//   makes         items crafted using it
//   notes         the player's own notes (edited in the book)
public record EntryResearch(long day, int time, List<Identifier> biomes, List<Identifier> drops, float maxHealth,
                            List<Identifier> breeding, List<Identifier> madeFrom, List<Identifier> makes, String notes)
{
    public static final int MAX_NOTES_LENGTH = 1024;

    // optionalFieldOf(name, default): missing fields load as the default, so entries saved by older versions still load
    public static final Codec<EntryResearch> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("day").forGetter(EntryResearch::day),
            Codec.INT.fieldOf("time").forGetter(EntryResearch::time),
            Identifier.CODEC.listOf().optionalFieldOf("biomes", List.of()).forGetter(EntryResearch::biomes),
            Identifier.CODEC.listOf().optionalFieldOf("drops", List.of()).forGetter(EntryResearch::drops),
            Codec.FLOAT.optionalFieldOf("max_health", -1F).forGetter(EntryResearch::maxHealth),
            Identifier.CODEC.listOf().optionalFieldOf("breeding", List.of()).forGetter(EntryResearch::breeding),
            Identifier.CODEC.listOf().optionalFieldOf("made_from", List.of()).forGetter(EntryResearch::madeFrom),
            Identifier.CODEC.listOf().optionalFieldOf("makes", List.of()).forGetter(EntryResearch::makes),
            Codec.STRING.optionalFieldOf("notes", "").forGetter(EntryResearch::notes)
    ).apply(instance, EntryResearch::new));

    public static EntryResearch firstSeen(long day, int time, Identifier biome)
    {
        return new EntryResearch(day, time, List.of(biome), List.of(), -1F, List.of(), List.of(), List.of(), "");
    }

    public boolean hasExperiments()
    {
        return !drops.isEmpty() || maxHealth > 0 || !breeding.isEmpty() || !madeFrom.isEmpty() || !makes.isEmpty();
    }

    public EntryResearch withBiome(Identifier biome)
    {
        return biomes.contains(biome) ? this : new EntryResearch(day, time, add(biomes, List.of(biome)), drops, maxHealth, breeding, madeFrom, makes, notes);
    }

    public EntryResearch withDrops(List<Identifier> items)
    {
        return new EntryResearch(day, time, biomes, add(drops, items), maxHealth, breeding, madeFrom, makes, notes);
    }

    public EntryResearch withMaxHealth(float health)
    {
        return new EntryResearch(day, time, biomes, drops, health, breeding, madeFrom, makes, notes);
    }

    public EntryResearch withBreeding(Identifier item)
    {
        return new EntryResearch(day, time, biomes, drops, maxHealth, add(breeding, List.of(item)), madeFrom, makes, notes);
    }

    public EntryResearch withMadeFrom(List<Identifier> items)
    {
        return new EntryResearch(day, time, biomes, drops, maxHealth, breeding, add(madeFrom, items), makes, notes);
    }

    public EntryResearch withMakes(Identifier item)
    {
        return new EntryResearch(day, time, biomes, drops, maxHealth, breeding, madeFrom, add(makes, List.of(item)), notes);
    }

    public EntryResearch withNotes(String text)
    {
        String clipped = text.length() > MAX_NOTES_LENGTH ? text.substring(0, MAX_NOTES_LENGTH) : text;
        return new EntryResearch(day, time, biomes, drops, maxHealth, breeding, madeFrom, makes, clipped);
    }

    // A new list with the items not already in it added on the end (keeps the order things were learned in)
    private static List<Identifier> add(List<Identifier> list, List<Identifier> items)
    {
        List<Identifier> out = new ArrayList<>(list);
        for (Identifier item : items) {
            if (!out.contains(item)) out.add(item);
        }
        return out.size() == list.size() ? list : List.copyOf(out);
    }
}
