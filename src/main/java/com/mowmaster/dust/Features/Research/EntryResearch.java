package com.mowmaster.dust.Features.Research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// What one player has learned about one research entry.
// Immutable (a record): every change makes a new copy via the with... methods, which is what lets the attachment
// notice the change and sync it.
//
//   day, time     when it was first observed (world day number, and the time of day in ticks 0-23999)
//   biomes        every biome it has been observed in (2+ unlocks the "Where found" page)
//   drops         items it has been seen to drop (breaking it / defeating it)
//   creature      a mob's health, breeding food and how hard it hits (see Creature)
//   madeFrom      items it was made from: crafted, cooked, or (for an effect) brewed with
//   makes         items made using it (for an enchantment: items it was found on)
//   modifiers     items that strengthen or lengthen it (an effect: redstone, glowstone...)
//   madeBy        recipes seen making it (station, ingredient grid, result): the "Crafted from" pages
//   usedIn        recipes seen using it as an ingredient: the "Crafted into" pages
//   food          what eating it does (see Food)
//   levels        an effect's levels the player has had (amplifier 0 = level I), and for instant effects how much
//                 health a level gave or took; an enchantment's levels seen
//   dust          what's known of its dust: the colour it burns with, and its composition once read (see Dust)
//   notes         the player's own overview notes, edited in the book (empty = the built-in notes)
//
// RecordCodecBuilder can only take 16 fields, so related fields are grouped into small records (Creature, Food, Dust).
// Each group has a MapCodec whose fields are saved straight into the entry's own map, next to the others (not nested
// inside a sub-object), so saves from before the grouping load unchanged. Add new fields to a group, or a new group.
public record EntryResearch(long day, int time, List<Identifier> biomes, List<Identifier> drops, Creature creature,
                            List<Identifier> madeFrom, List<Identifier> makes, List<Identifier> modifiers,
                            List<RecipeRecord> madeBy, List<RecipeRecord> usedIn, Food food, List<EffectLevel> levels,
                            Dust dust, String notes)
{
    // The overview notes a player can write for one entry (the book screen counts down the last few characters)
    public static final int MAX_NOTES_LENGTH = 256;

    // A mob's damage per hit, as seen: the range widens as armour, difficulty and the mob's weapon vary
    public record Hits(float min, float max)
    {
        public static final Hits NONE = new Hits(-1F, -1F);
        public static final Codec<Hits> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("min").forGetter(Hits::min),
                Codec.FLOAT.fieldOf("max").forGetter(Hits::max)
        ).apply(instance, Hits::new));

        public boolean known()
        {
            return max >= 0;
        }

        public Hits with(float damage)
        {
            return known() ? new Hits(Math.min(min, damage), Math.max(max, damage)) : new Hits(damage, damage);
        }
    }

    // One level of an effect the player has had. health: what one dose did straight away (instant effects): healed
    // (positive) or hurt (negative); 0 for effects that last. What a lasting level does to attributes (Speed II:
    // +40% speed) isn't saved: the book works it out from the effect itself.
    public record EffectLevel(int amplifier, float health)
    {
        public static final Codec<EffectLevel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("amplifier").forGetter(EffectLevel::amplifier),
                Codec.FLOAT.optionalFieldOf("health", 0F).forGetter(EffectLevel::health)
        ).apply(instance, EffectLevel::new));
    }

    //   maxHealth   a mob's maximum health, once fought (-1 = not known)
    //   breeding    items it has been fed to breed
    //   hits        the least and most damage it has dealt to the player in one hit (Hits.NONE = never hit)
    public record Creature(float maxHealth, List<Identifier> breeding, Hits hits)
    {
        public static final Creature NONE = new Creature(-1F, List.of(), Hits.NONE);
        // RecordCodecBuilder.mapCodec builds a MapCodec: a set of fields, without saying where the map lives
        public static final MapCodec<Creature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.optionalFieldOf("max_health", -1F).forGetter(Creature::maxHealth),
                Identifier.CODEC.listOf().optionalFieldOf("breeding", List.of()).forGetter(Creature::breeding),
                Hits.CODEC.optionalFieldOf("hits", Hits.NONE).forGetter(Creature::hits)
        ).apply(instance, Creature::new));
    }

    //   saturation    how much saturation it gives when eaten (-1 = not eaten yet)
    //   effects       effects that eating it has given (chance effects are added as they turn up)
    public record Food(float saturation, List<Identifier> effects)
    {
        public static final Food NONE = new Food(-1F, List.of());
        public static final MapCodec<Food> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.FLOAT.optionalFieldOf("saturation", -1F).forGetter(Food::saturation),
                Identifier.CODEC.listOf().optionalFieldOf("food_effects", List.of()).forGetter(Food::effects)
        ).apply(instance, Food::new));
    }

    //   fireColor     the colour it burned with in a Magical Flint and Steel's fire, 0xRRGGBB (-1 = not burned)
    //   composition   what a Dust Testing Disk showed it's made of (empty until read). Kept as read, so the book
    //                 shows what the player actually saw (a water bottle's water, though every potion is one entry).
    public record Dust(int fireColor, Optional<DustComposition> composition)
    {
        public static final Dust NONE = new Dust(-1, Optional.empty());
        public static final MapCodec<Dust> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.optionalFieldOf("fire_color", -1).forGetter(Dust::fireColor),
                // lenientOptionalFieldOf: an Optional, empty when the field isn't there, or can't be read (a composition
                // saved in an older form, or naming an element that no longer exists), so a bad entry doesn't fail the save
                DustComposition.CODEC.lenientOptionalFieldOf("composition").forGetter(Dust::composition)
        ).apply(instance, Dust::new));

        public boolean burned()
        {
            return fireColor >= 0;
        }
    }

    // optionalFieldOf(name, default): missing fields load as the default, so entries saved by older versions still load.
    // MAP_CODEC.forGetter(...) puts a group's fields into this same map.
    public static final Codec<EntryResearch> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("day").forGetter(EntryResearch::day),
            Codec.INT.fieldOf("time").forGetter(EntryResearch::time),
            Identifier.CODEC.listOf().optionalFieldOf("biomes", List.of()).forGetter(EntryResearch::biomes),
            Identifier.CODEC.listOf().optionalFieldOf("drops", List.of()).forGetter(EntryResearch::drops),
            Creature.MAP_CODEC.forGetter(EntryResearch::creature),
            Identifier.CODEC.listOf().optionalFieldOf("made_from", List.of()).forGetter(EntryResearch::madeFrom),
            Identifier.CODEC.listOf().optionalFieldOf("makes", List.of()).forGetter(EntryResearch::makes),
            Identifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(EntryResearch::modifiers),
            RecipeRecord.CODEC.listOf().optionalFieldOf("made_by", List.of()).forGetter(EntryResearch::madeBy),
            RecipeRecord.CODEC.listOf().optionalFieldOf("used_in", List.of()).forGetter(EntryResearch::usedIn),
            Food.MAP_CODEC.forGetter(EntryResearch::food),
            EffectLevel.CODEC.listOf().optionalFieldOf("levels", List.of()).forGetter(EntryResearch::levels),
            Dust.MAP_CODEC.forGetter(EntryResearch::dust),
            Codec.STRING.optionalFieldOf("notes", "").forGetter(EntryResearch::notes)
    ).apply(instance, EntryResearch::new));

    public static EntryResearch firstSeen(long day, int time, Identifier biome)
    {
        return new EntryResearch(day, time, List.of(biome), List.of(), Creature.NONE, List.of(), List.of(), List.of(),
                List.of(), List.of(), Food.NONE, List.of(), Dust.NONE, "");
    }

    // ---- shortcuts into the groups (the names the rest of the code has always used) ----

    public float maxHealth() { return creature.maxHealth(); }
    public List<Identifier> breeding() { return creature.breeding(); }
    public Hits hits() { return creature.hits(); }
    public float saturation() { return food.saturation(); }
    public List<Identifier> foodEffects() { return food.effects(); }

    public boolean hasExperiments()
    {
        return !drops.isEmpty() || maxHealth() > 0 || !breeding().isEmpty() || !madeFrom.isEmpty() || !makes.isEmpty() || !modifiers.isEmpty()
                || saturation() >= 0 || !foodEffects().isEmpty()
                || hits().known() || !levels.isEmpty();
    }

    // ---- changes ----
    // Each with... returns a copy with one thing changed. The private with(...) helpers below swap a single field or
    // group so the long constructor call is only written out a few times.

    public EntryResearch withBiome(Identifier biome)
    {
        return biomes.contains(biome) ? this : new EntryResearch(day, time, add(biomes, List.of(biome)), drops, creature, madeFrom, makes, modifiers, madeBy, usedIn, food, levels, dust, notes);
    }

    public EntryResearch withDrops(List<Identifier> items)
    {
        return new EntryResearch(day, time, biomes, add(drops, items), creature, madeFrom, makes, modifiers, madeBy, usedIn, food, levels, dust, notes);
    }

    public EntryResearch withMaxHealth(float health)
    {
        return with(new Creature(health, breeding(), hits()));
    }

    public EntryResearch withBreeding(Identifier item)
    {
        return with(new Creature(maxHealth(), add(breeding(), List.of(item)), hits()));
    }

    public EntryResearch withHit(float damage)
    {
        return with(new Creature(maxHealth(), breeding(), hits().with(damage)));
    }

    public EntryResearch withMadeFrom(List<Identifier> items)
    {
        return new EntryResearch(day, time, biomes, drops, creature, add(madeFrom, items), makes, modifiers, madeBy, usedIn, food, levels, dust, notes);
    }

    public EntryResearch withMakes(Identifier item)
    {
        return new EntryResearch(day, time, biomes, drops, creature, madeFrom, add(makes, List.of(item)), modifiers, madeBy, usedIn, food, levels, dust, notes);
    }

    public EntryResearch withModifier(Identifier item)
    {
        return new EntryResearch(day, time, biomes, drops, creature, madeFrom, makes, add(modifiers, List.of(item)), madeBy, usedIn, food, levels, dust, notes);
    }

    public EntryResearch withMadeBy(RecipeRecord recipe)
    {
        return madeBy.contains(recipe) ? this : new EntryResearch(day, time, biomes, drops, creature, madeFrom, makes, modifiers, append(madeBy, recipe), usedIn, food, levels, dust, notes);
    }

    public EntryResearch withUsedIn(RecipeRecord recipe)
    {
        return usedIn.contains(recipe) ? this : new EntryResearch(day, time, biomes, drops, creature, madeFrom, makes, modifiers, madeBy, append(usedIn, recipe), food, levels, dust, notes);
    }

    public EntryResearch withSaturation(float value)
    {
        return with(new Food(value, foodEffects()));
    }

    public EntryResearch withFoodEffect(Identifier effect)
    {
        return with(new Food(saturation(), add(foodEffects(), List.of(effect))));
    }

    // Notes a level of this effect; health (if not 0) is what one dose of it did right away
    public EntryResearch withLevel(int amplifier, float health)
    {
        List<EffectLevel> out = new ArrayList<>();
        boolean found = false;
        for (EffectLevel level : levels) {
            if (level.amplifier() == amplifier) {
                found = true;
                // Keep the first amount seen; a later dose can only fill in one that wasn't known
                out.add(level.health() == 0 && health != 0 ? new EffectLevel(amplifier, health) : level);
            } else {
                out.add(level);
            }
        }
        if (!found) out.add(new EffectLevel(amplifier, health));
        // Comparator.comparingInt makes a comparator from a "key" function: here, lowest level first
        out.sort(Comparator.comparingInt(EffectLevel::amplifier));
        List<EffectLevel> sorted = List.copyOf(out);
        return sorted.equals(levels) ? this : new EntryResearch(day, time, biomes, drops, creature, madeFrom, makes, modifiers, madeBy, usedIn, food, sorted, dust, notes);
    }

    // Burned in a Magical Flint and Steel's fire: the colour of the flames
    public EntryResearch withFireColor(int color)
    {
        return with(new Dust(color, dust.composition()));
    }

    // Read on a Dust Testing Disk
    public EntryResearch withComposition(DustComposition composition)
    {
        return with(new Dust(dust.fireColor(), Optional.of(composition)));
    }

    public EntryResearch withNotes(String text)
    {
        String clipped = text.length() > MAX_NOTES_LENGTH ? text.substring(0, MAX_NOTES_LENGTH) : text;
        return new EntryResearch(day, time, biomes, drops, creature, madeFrom, makes, modifiers, madeBy, usedIn, food, levels, dust, clipped);
    }

    // ---- helpers ----

    // A copy with one group replaced (the same entry if nothing changed: records compare by their fields)
    private EntryResearch with(Creature value)
    {
        return value.equals(creature) ? this : new EntryResearch(day, time, biomes, drops, value, madeFrom, makes, modifiers, madeBy, usedIn, food, levels, dust, notes);
    }

    private EntryResearch with(Food value)
    {
        return value.equals(food) ? this : new EntryResearch(day, time, biomes, drops, creature, madeFrom, makes, modifiers, madeBy, usedIn, value, levels, dust, notes);
    }

    private EntryResearch with(Dust value)
    {
        return value.equals(dust) ? this : new EntryResearch(day, time, biomes, drops, creature, madeFrom, makes, modifiers, madeBy, usedIn, food, levels, value, notes);
    }

    private static <T> List<T> append(List<T> list, T item)
    {
        List<T> out = new ArrayList<>(list);
        out.add(item);
        return List.copyOf(out);
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
