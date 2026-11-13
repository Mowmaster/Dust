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
public record Spell(DustElement element, Identifier shape, Map<Identifier, Integer> attributes, List<Identifier> modifiers)
{
    public Spell
    {
        attributes = java.util.Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
        modifiers = List.copyOf(modifiers);
    }

    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DustElements.CODEC.fieldOf("element").forGetter(Spell::element),
            Identifier.CODEC.fieldOf("shape").forGetter(Spell::shape),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).optionalFieldOf("attributes", Map.of()).forGetter(Spell::attributes),
            Identifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(Spell::modifiers)
    ).apply(instance, Spell::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Spell> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    // Its stats: the defaults, then each attribute's levels, then each modifier, then the caster's power (attunement)
    public SpellStats stats(float power)
    {
        SpellStats stats = SpellStats.defaults();
        for (Map.Entry<Identifier, Integer> attribute : attributes.entrySet()) {
            SpellAttribute type = SpellRegistries.ATTRIBUTE_REGISTRY.getValue(attribute.getKey());
            if (type != null) stats = type.apply(stats, attribute.getValue());
        }
        for (SpellModifier modifier : modifierList()) stats = modifier.modifyStats(stats);
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
        for (SpellModifier modifier : modifierList()) {
            modifier.cost(element).forEach((type, amount) -> cost.merge(type, amount, Integer::sum));
            percent += modifier.costPercent();
        }
        // The percentages together, then nothing below 1 of the spell's own element (other elements can drop to 0)
        int finalPercent = percent;
        cost.replaceAll((type, amount) -> Math.round(amount * Math.max(0, 100 + finalPercent) / 100F));
        cost.replaceAll((type, amount) -> type == element ? Math.max(1, amount) : Math.max(0, amount));
        cost.values().removeIf(amount -> amount <= 0);
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
