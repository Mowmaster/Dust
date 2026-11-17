package com.mowmaster.dust.Features.Spells.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// One spell as it's cast: its element, shape, attribute levels and modifiers (the active branch of a spell tree,
// worked out by the pylons in step 2; for now set by command). Saved on a Spell Scroll as the dust:spell data
// component. Shapes, attributes and modifiers are registry ids, so other mods' entries save the same way; an id whose
// mod was removed is skipped when the spell is cast.
// levels: for each modifier (same order), how many levels of it the spell has (its pylon's levels, doubled on a
// Legendary pylon). Missing entries count as 1.
// stack: spells cast straight after this one, in order (a Spell Stack link: pylons/SpellLinks; SpellCasting casts
// each a moment after the one before, and the dust and mana are all of them added together)
public record Spell(DustElement element, Identifier shape, Map<Identifier, Integer> attributes, List<Identifier> modifiers, List<Integer> levels,
                    List<Spell> stack)
{
    public Spell
    {
        attributes = java.util.Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
        modifiers = List.copyOf(modifiers);
        levels = List.copyOf(levels);
        stack = List.copyOf(stack);
    }

    // A spell with nothing stacked after it
    public Spell(DustElement element, Identifier shape, Map<Identifier, Integer> attributes, List<Identifier> modifiers, List<Integer> levels)
    {
        this(element, shape, attributes, modifiers, levels, List.of());
    }

    // The same spell with these spells stacked after it
    public Spell withStack(List<Spell> stacked)
    {
        return new Spell(element, shape, attributes, modifiers, levels, stacked);
    }

    // A spell with one level of each modifier (by command, an awakening...). A record can have more constructors as
    // long as they hand over to the main one with this(...).
    public Spell(DustElement element, Identifier shape, Map<Identifier, Integer> attributes, List<Identifier> modifiers)
    {
        this(element, shape, attributes, modifiers, List.of());
    }

    // The levels of the modifier at this place in the list
    public int level(int index)
    {
        return index < levels.size() ? levels.get(index) : 1;
    }

    // All the levels of one modifier the spell has (it can be on several pylons)
    public int levelsOf(Identifier modifier)
    {
        int total = 0;
        for (int i = 0; i < modifiers.size(); i++) if (modifiers.get(i).equals(modifier)) total += level(i);
        return total;
    }

    // Codec.recursive: a codec that can refer to itself (a spell holds a list of spells: its stack). Spells saved
    // before stacks existed have no "stack" field, so it's optional.
    public static final Codec<Spell> CODEC = Codec.recursive("spell", self -> RecordCodecBuilder.create(instance -> instance.group(
            DustElements.CODEC.fieldOf("element").forGetter(Spell::element),
            Identifier.CODEC.fieldOf("shape").forGetter(Spell::shape),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).optionalFieldOf("attributes", Map.of()).forGetter(Spell::attributes),
            Identifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(Spell::modifiers),
            Codec.INT.listOf().optionalFieldOf("levels", List.of()).forGetter(Spell::levels),
            self.listOf().optionalFieldOf("stack", List.of()).forGetter(Spell::stack)
    ).apply(instance, Spell::new)));

    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    // Its stats: the defaults, then each attribute's levels, then each modifier, then the caster's power (attunement)
    public SpellStats stats(float power)
    {
        SpellStats stats = SpellStats.defaults();
        for (Map.Entry<Identifier, Integer> attribute : attributes.entrySet()) {
            SpellAttribute type = SpellRegistries.ATTRIBUTE_REGISTRY.getValue(attribute.getKey());
            if (type != null) stats = type.apply(stats, attribute.getValue());
        }
        for (int i = 0; i < modifiers.size(); i++) {
            SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(modifiers.get(i));
            if (modifier != null) stats = modifier.modifyStats(this, stats, level(i));
        }
        return stats.withPotency(stats.potency() * power);
    }

    // What it costs to cast, by element, before attunement: the base cost of its element, 1 per attribute level, and
    // each modifier's own cost
    public Map<DustElement, Integer> dustCost()
    {
        Map<DustElement, Integer> cost = new LinkedHashMap<>();
        cost.merge(element, SpellConfig.BASE_DUST_COST, Integer::sum);
        int levels = attributes.values().stream().mapToInt(Integer::intValue).sum();
        if (levels > 0) cost.merge(element, levels, Integer::sum);
        int percent = 0;
        for (int i = 0; i < modifiers.size(); i++) {
            SpellModifier modifier = SpellRegistries.MODIFIER_REGISTRY.getValue(modifiers.get(i));
            if (modifier == null) continue;
            modifier.cost(element).forEach((type, amount) -> cost.merge(type, amount, Integer::sum));
            percent += modifier.costPercent(level(i));
        }
        // The percentages together, then nothing below 1 of the spell's own element (other elements can drop to 0)
        int finalPercent = percent;
        cost.replaceAll((type, amount) -> Math.round(amount * Math.max(0, 100 + finalPercent) / 100F));
        cost.replaceAll((type, amount) -> type == element ? Math.max(1, amount) : Math.max(0, amount));
        cost.values().removeIf(amount -> amount <= 0);
        return cost;
    }

    // What casting it costs with its whole stack: every stacked spell's dust added to its own
    public Map<DustElement, Integer> totalDustCost()
    {
        Map<DustElement, Integer> cost = new LinkedHashMap<>(dustCost());
        for (Spell stacked : stack) stacked.totalDustCost().forEach((type, amount) -> cost.merge(type, amount, Integer::sum));
        return cost;
    }

    public List<SpellModifier> modifierList()
    {
        return modifiers.stream().map(SpellRegistries.MODIFIER_REGISTRY::getValue).filter(java.util.Objects::nonNull).toList();
    }

    public SpellShape shapeType()
    {
        return SpellRegistries.SHAPE_REGISTRY.getValue(shape);
    }
}
