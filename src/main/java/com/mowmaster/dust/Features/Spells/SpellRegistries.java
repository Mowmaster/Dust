package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellAttribute;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import com.mowmaster.dust.Features.Spells.attributes.StatAttribute;
import com.mowmaster.dust.Features.Spells.effects.ChaosEffect;
import com.mowmaster.dust.Features.Spells.effects.EarthEffect;
import com.mowmaster.dust.Features.Spells.effects.FireEffect;
import com.mowmaster.dust.Features.Spells.effects.OrderEffect;
import com.mowmaster.dust.Features.Spells.effects.WaterEffect;
import com.mowmaster.dust.Features.Spells.shapes.ProjectileShape;
import com.mowmaster.dust.Features.Spells.shapes.SelfShape;
import com.mowmaster.dust.Features.Spells.shapes.TouchShape;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// The spell system's own registries, so other mods can add to them like they add items: a DeferredRegister on one of
// these keys (DeferredRegister.create(SpellRegistries.SHAPES_KEY, "theirmod")), registered on their mod bus.
//   dust:spell_shapes            how a spell is delivered (SpellShape)
//   dust:spell_attributes        numbers a spell can have more of (SpellAttribute)
//   dust:spell_modifiers         bigger changes (SpellModifier)
//   dust:spell_element_effects   what each element does, under the element's id (ElementEffect)
// makeRegistry creates the registry itself (NeoForge's NewRegistryEvent, done for us); sync(true) sends its ids to
// clients, so a spell's ids mean the same on both sides.
public final class SpellRegistries
{
    public static final ResourceKey<Registry<SpellShape>> SHAPES_KEY = key("spell_shapes");
    public static final ResourceKey<Registry<SpellAttribute>> ATTRIBUTES_KEY = key("spell_attributes");
    public static final ResourceKey<Registry<SpellModifier>> MODIFIERS_KEY = key("spell_modifiers");
    public static final ResourceKey<Registry<ElementEffect>> ELEMENT_EFFECTS_KEY = key("spell_element_effects");

    public static final DeferredRegister<SpellShape> SHAPES = DeferredRegister.create(SHAPES_KEY, DustReferences.MODID);
    public static final DeferredRegister<SpellAttribute> ATTRIBUTES = DeferredRegister.create(ATTRIBUTES_KEY, DustReferences.MODID);
    public static final DeferredRegister<SpellModifier> MODIFIERS = DeferredRegister.create(MODIFIERS_KEY, DustReferences.MODID);
    public static final DeferredRegister<ElementEffect> ELEMENT_EFFECTS = DeferredRegister.create(ELEMENT_EFFECTS_KEY, DustReferences.MODID);

    public static final Registry<SpellShape> SHAPE_REGISTRY = SHAPES.makeRegistry(builder -> builder.sync(true));
    public static final Registry<SpellAttribute> ATTRIBUTE_REGISTRY = ATTRIBUTES.makeRegistry(builder -> builder.sync(true));
    public static final Registry<SpellModifier> MODIFIER_REGISTRY = MODIFIERS.makeRegistry(builder -> builder.sync(true));
    public static final Registry<ElementEffect> ELEMENT_EFFECT_REGISTRY = ELEMENT_EFFECTS.makeRegistry(builder -> builder.sync(true));

    // ---- shapes ----
    public static final DeferredHolder<SpellShape, SpellShape> SELF = SHAPES.register("self", SelfShape::new);
    public static final DeferredHolder<SpellShape, SpellShape> TOUCH = SHAPES.register("touch", TouchShape::new);
    public static final DeferredHolder<SpellShape, SpellShape> PROJECTILE = SHAPES.register("projectile", ProjectileShape::new);

    // ---- attributes (what each level does: StatAttribute) ----
    public static final DeferredHolder<SpellAttribute, SpellAttribute> DURATION = ATTRIBUTES.register("duration",
            () -> new StatAttribute((stats, level) -> stats.withDuration(Math.round(stats.duration() * (1 + 0.5F * level)))));
    public static final DeferredHolder<SpellAttribute, SpellAttribute> POTENCY = ATTRIBUTES.register("potency",
            () -> new StatAttribute((stats, level) -> stats.withPotency(stats.potency() + level)));
    public static final DeferredHolder<SpellAttribute, SpellAttribute> SIZE = ATTRIBUTES.register("size",
            () -> new StatAttribute((stats, level) -> stats.withSize(stats.size() + level)));
    public static final DeferredHolder<SpellAttribute, SpellAttribute> RANGE = ATTRIBUTES.register("range",
            () -> new StatAttribute((stats, level) -> stats.withRange(stats.range() + 8 * level)));
    public static final DeferredHolder<SpellAttribute, SpellAttribute> SPEED = ATTRIBUTES.register("speed",
            () -> new StatAttribute((stats, level) -> stats.withSpeed(stats.speed() + 0.4F * level)));

    // ---- the elements' effects (the rest come in later steps; until then an element does nothing) ----
    public static final DeferredHolder<ElementEffect, ElementEffect> FIRE = ELEMENT_EFFECTS.register(DustElements.FIRE.id().getPath(), FireEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> WATER = ELEMENT_EFFECTS.register(DustElements.WATER.id().getPath(), WaterEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> EARTH = ELEMENT_EFFECTS.register(DustElements.EARTH.id().getPath(), EarthEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> ORDER = ELEMENT_EFFECTS.register(DustElements.ORDER.id().getPath(), OrderEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> CHAOS = ELEMENT_EFFECTS.register(DustElements.CHAOS.id().getPath(), ChaosEffect::new);

    private SpellRegistries() {}

    private static <T> ResourceKey<Registry<T>> key(String name)
    {
        return ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(DustReferences.MODID, name));
    }

    public static void register(IEventBus bus)
    {
        SHAPES.register(bus);
        ATTRIBUTES.register(bus);
        MODIFIERS.register(bus);
        ELEMENT_EFFECTS.register(bus);
    }
}
