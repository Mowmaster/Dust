package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellAttribute;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.modifiers.MultishotModifier;
import com.mowmaster.dust.Features.Spells.modifiers.FusionModifier;
import com.mowmaster.dust.Features.Spells.modifiers.BasicAspect;
import com.mowmaster.dust.Features.Spells.modifiers.EchoFragment;
import com.mowmaster.dust.Features.Spells.modifiers.FragmentSlotModifier;
import com.mowmaster.dust.Features.Spells.modifiers.PercentModifier;
import com.mowmaster.dust.Features.Spells.modifiers.StatModifier;
import com.mowmaster.dust.Features.Spells.attributes.StatAttribute;
import com.mowmaster.dust.Features.Spells.effects.BasicEffect;
import com.mowmaster.dust.Features.Spells.effects.MagicParts;
import com.mowmaster.dust.Features.Spells.effects.PetGhosts;
import com.mowmaster.dust.Features.Spells.effects.MetalParts;
import com.mowmaster.dust.Features.Spells.effects.FractureParts;
import com.mowmaster.dust.Features.Spells.effects.StoreParts;
import com.mowmaster.dust.Features.Spells.effects.RustParts;
import com.mowmaster.dust.Features.Spells.effects.RootParts;
import com.mowmaster.dust.Features.Spells.radiance.Radiance;
import com.mowmaster.dust.Features.Spells.voidrift.VoidParts;
import com.mowmaster.dust.Features.Spells.soul.SoulParts;
import com.mowmaster.dust.Features.Spells.effects.ChaosEffect;
import com.mowmaster.dust.Features.Spells.effects.MobParts;
import com.mowmaster.dust.Features.Spells.effects.MovementParts;
import com.mowmaster.dust.Features.Spells.blocks.SpellBlocks;
import com.mowmaster.dust.Features.Spells.blocks.SpecialBlockParts;
import com.mowmaster.dust.Features.Spells.effects.UtilityParts;
import com.mowmaster.dust.Features.Spells.conversions.SpellConversions;
import com.mowmaster.dust.Features.Spells.effects.EnergyCharge;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.effects.SimpleParts;
import net.minecraft.world.effect.MobEffects;
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
    // Beam: a piercing ray, unlocked when one of the player's spell trees first reaches PylonConfig.BEAM_LEVEL
    public static final DeferredHolder<SpellShape, SpellShape> BEAM = SHAPES.register("beam", com.mowmaster.dust.Features.Spells.shapes.BeamShape::new);

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

    // ---- modifiers (rolled onto pylons; a Change / Cost / Count / Damage pylon favours its category) ----
    // Count
    public static final DeferredHolder<SpellModifier, SpellModifier> WIDER = MODIFIERS.register("wider",
            () -> StatModifier.of(ModifierCategory.COUNT, 10, stats -> stats.withSize(stats.size() + 1)));
    public static final DeferredHolder<SpellModifier, SpellModifier> LONGER = MODIFIERS.register("longer",
            () -> StatModifier.of(ModifierCategory.COUNT, 10, stats -> stats.withDuration(Math.round(stats.duration() * 1.5F))));
    public static final DeferredHolder<SpellModifier, SpellModifier> FARTHER = MODIFIERS.register("farther",
            () -> StatModifier.of(ModifierCategory.COUNT, 10, stats -> stats.withRange(stats.range() + 8)));
    public static final DeferredHolder<SpellModifier, SpellModifier> FASTER = MODIFIERS.register("faster",
            () -> StatModifier.of(ModifierCategory.COUNT, 8, stats -> stats.withSpeed(stats.speed() + 0.4F)));
    // corrupted: wider (1-3 more), but it lasts strength% less
    public static final DeferredHolder<SpellModifier, SpellModifier> SPRAWLING = MODIFIERS.register("sprawling",
            () -> new PercentModifier(ModifierCategory.COUNT, ModifierTier.CORRUPTED, 6,
                    (stats, p) -> stats.withSize(stats.size() + Math.max(1, Math.round(p * 6))).withDuration(Math.round(stats.duration() * (1 - p))), 1, 0));
    // blessed: lasts strength% longer
    public static final DeferredHolder<SpellModifier, SpellModifier> ENDURING = MODIFIERS.register("enduring",
            () -> new PercentModifier(ModifierCategory.COUNT, ModifierTier.BLESSED, 6, (stats, p) -> stats.withDuration(Math.round(stats.duration() * (1 + p))), 1, 0));
    // Damage
    public static final DeferredHolder<SpellModifier, SpellModifier> STRONGER = MODIFIERS.register("stronger",
            () -> StatModifier.of(ModifierCategory.DAMAGE, 10, stats -> stats.withPotency(stats.potency() + 1)));
    // blessed: strength% stronger
    public static final DeferredHolder<SpellModifier, SpellModifier> SURGING = MODIFIERS.register("surging",
            () -> new PercentModifier(ModifierCategory.DAMAGE, ModifierTier.BLESSED, 6, (stats, p) -> stats.withPotency(stats.potency() * (1 + p)), 1, 0));
    // corrupted: twice strength% stronger, but reaches strength% less
    public static final DeferredHolder<SpellModifier, SpellModifier> BRUTAL = MODIFIERS.register("brutal",
            () -> new PercentModifier(ModifierCategory.DAMAGE, ModifierTier.CORRUPTED, 6,
                    (stats, p) -> stats.withPotency(stats.potency() * (1 + 2 * p)).withRange(Math.max(4, stats.range() * (1 - p))), 1, 0));
    // Cost
    public static final DeferredHolder<SpellModifier, SpellModifier> THRIFTY = MODIFIERS.register("thrifty",
            () -> new StatModifier(ModifierCategory.COST, ModifierTier.NORMAL, 8, stats -> stats, -1, 0));
    // blessed: strength% cheaper
    public static final DeferredHolder<SpellModifier, SpellModifier> FRUGAL = MODIFIERS.register("frugal",
            () -> new PercentModifier(ModifierCategory.COST, ModifierTier.BLESSED, 6, (stats, p) -> stats, 0, -1));
    // corrupted: twice strength% stronger, but strength% dearer
    public static final DeferredHolder<SpellModifier, SpellModifier> OVERCHARGED = MODIFIERS.register("overcharged",
            () -> new PercentModifier(ModifierCategory.COST, ModifierTier.CORRUPTED, 6, (stats, p) -> stats.withPotency(stats.potency() * (1 + 2 * p)), 1, 1));
    // Change
    public static final DeferredHolder<SpellModifier, SpellModifier> MULTISHOT = MODIFIERS.register("multishot", MultishotModifier::new);

    // ---- fragments, aspects and fusions (the bigger changes, after spell breaks) ----
    // The Fragment slot: rolled at level milestones, becomes a random fragment for the element (FragmentSlotModifier)
    public static final DeferredHolder<SpellModifier, SpellModifier> FRAGMENT = MODIFIERS.register("fragment", FragmentSlotModifier::new);
    // Echo (any element): the spell casts again by itself; its aspects add echoes and quicken them
    public static final DeferredHolder<SpellModifier, SpellModifier> ECHO = MODIFIERS.register("echo", EchoFragment::new);
    public static final DeferredHolder<SpellModifier, SpellModifier> ECHO_RESONANCE = MODIFIERS.register("echo_resonance",
            () -> new BasicAspect(Identifier.fromNamespaceAndPath(DustReferences.MODID, "echo"), ModifierCategory.COUNT, 10));
    public static final DeferredHolder<SpellModifier, SpellModifier> ECHO_QUICKENING = MODIFIERS.register("echo_quickening",
            () -> new BasicAspect(Identifier.fromNamespaceAndPath(DustReferences.MODID, "echo"), ModifierCategory.CHANGE, 8));
    // Fusions: one per pair of elements, each with its own effects (parts it leaves out fall back to the spell's element)
    public static final DeferredHolder<SpellModifier, SpellModifier> FUSION_FLIGHT_MOTION = MODIFIERS.register("fusion_flight_motion",
            () -> new FusionModifier(DustElements.FLIGHT, DustElements.MOTION).self(MovementParts::glide));

    // ---- the elements' effects (the rest come in later steps; until then an element does nothing) ----
    public static final DeferredHolder<ElementEffect, ElementEffect> FIRE = ELEMENT_EFFECTS.register(DustElements.FIRE.id().getPath(), FireEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> WATER = ELEMENT_EFFECTS.register(DustElements.WATER.id().getPath(), WaterEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> EARTH = ELEMENT_EFFECTS.register(DustElements.EARTH.id().getPath(), EarthEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> ORDER = ELEMENT_EFFECTS.register(DustElements.ORDER.id().getPath(), OrderEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> CHAOS = ELEMENT_EFFECTS.register(DustElements.CHAOS.id().getPath(), ChaosEffect::new);

    // Elements whose effects are short (BasicEffect: parts from SimpleParts or a mob effect). A part not listed does
    // nothing yet: it comes in a later step.
    public static final DeferredHolder<ElementEffect, ElementEffect> MOTION_EFFECT = element(DustElements.MOTION,
            () -> new BasicEffect().block(SpellConversions::convert).self(SimpleParts::speedSelfWithMount).entity(SimpleParts::speedWithMount));
    public static final DeferredHolder<ElementEffect, ElementEffect> LIFE_EFFECT = element(DustElements.LIFE,
            () -> new BasicEffect().entity(PetGhosts::reviveOrCure).block(SpellConversions::convert).self(BasicEffect.give(MobEffects.REGENERATION)));
    public static final DeferredHolder<ElementEffect, ElementEffect> HEALING_EFFECT = element(DustElements.HEALING,
            () -> new BasicEffect().block(SpellBlocks::purifyOrMend).self(SimpleParts::healSelf).entity(SimpleParts::heal));
    public static final DeferredHolder<ElementEffect, ElementEffect> LIGHT_EFFECT = element(DustElements.LIGHT,
            () -> new BasicEffect().block(SpecialBlockParts::light).self(BasicEffect.give(MobEffects.NIGHT_VISION)).entity(BasicEffect.inflict(MobEffects.GLOWING)));
    public static final DeferredHolder<ElementEffect, ElementEffect> DARKNESS_EFFECT = element(DustElements.DARKNESS,
            () -> new BasicEffect().block(SpecialBlockParts::veil).self(BasicEffect.give(MobEffects.INVISIBILITY)).entity(BasicEffect.inflict(MobEffects.BLINDNESS)));
    public static final DeferredHolder<ElementEffect, ElementEffect> FLIGHT_EFFECT = element(DustElements.FLIGHT,
            () -> new BasicEffect().block(SpecialBlockParts::floatBlock).self(MovementParts::flight).entity(BasicEffect.inflict(MobEffects.SLOW_FALLING)));
    public static final DeferredHolder<ElementEffect, ElementEffect> AIR_EFFECT = element(DustElements.AIR,
            () -> new BasicEffect().block(MobParts::topple).self(BasicEffect.give(MobEffects.JUMP_BOOST)).entity(SimpleParts::pushAway));
    public static final DeferredHolder<ElementEffect, ElementEffect> POISON_EFFECT = element(DustElements.POISON,
            () -> new BasicEffect().block(SpellBlocks::poisonPool).self(SimpleParts::cleanse).entity(BasicEffect.inflict(MobEffects.POISON)));
    public static final DeferredHolder<ElementEffect, ElementEffect> ARMOR_EFFECT = element(DustElements.ARMOR,
            () -> new BasicEffect().block(SpecialBlockParts::ward).self(BasicEffect.give(MobEffects.ABSORPTION)).entity(BasicEffect.inflict(MobEffects.ABSORPTION)));
    public static final DeferredHolder<ElementEffect, ElementEffect> PROTECTION_EFFECT = element(DustElements.PROTECTION,
            () -> new BasicEffect().block(SpecialBlockParts::wardedGlass).self(BasicEffect.give(MobEffects.RESISTANCE)).entity(BasicEffect.inflict(DustEffectsRegistry.PIERCED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> FLESH_EFFECT = element(DustElements.FLESH,
            () -> new BasicEffect().block(SpellBlocks::blight).entity(BasicEffect.inflict(DustEffectsRegistry.BUTCHERED)).self(BasicEffect.give(MobEffects.STRENGTH)));
    public static final DeferredHolder<ElementEffect, ElementEffect> DAMAGE_EFFECT = element(DustElements.DAMAGE,
            () -> new BasicEffect().block(MobParts::markQuickBreak).self(BasicEffect.give(DustEffectsRegistry.MAGIC_WARD)).entity(SimpleParts::magicDamage));
    public static final DeferredHolder<ElementEffect, ElementEffect> MAGIC_EFFECT = element(DustElements.MAGIC,
            () -> new BasicEffect().self(MagicParts::surge).block(MagicParts::amplify).entity(SimpleParts::magicDamage));
    public static final DeferredHolder<ElementEffect, ElementEffect> VOID_EFFECT = element(DustElements.VOID,
            () -> new BasicEffect().self(VoidParts::chest).block(VoidParts::rift).entity(SimpleParts::voidDamage));
    public static final DeferredHolder<ElementEffect, ElementEffect> SOUL_EFFECT = element(DustElements.SOUL,
            () -> new BasicEffect().self(SoulParts::anchor).block(SoulParts::bind).entity(SoulParts::pull));
    public static final DeferredHolder<ElementEffect, ElementEffect> STONE_EFFECT = element(DustElements.STONE,
            () -> new BasicEffect().block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.STONE_SKIN)).entity(SimpleParts::kineticDamage));
    public static final DeferredHolder<ElementEffect, ElementEffect> EXPLOSION_EFFECT = element(DustElements.EXPLOSION,
            () -> new BasicEffect().self(MobParts::flare).block(SimpleParts::explodeBlock).entity(SimpleParts::explodeAt));
    public static final DeferredHolder<ElementEffect, ElementEffect> SOUND_EFFECT = element(DustElements.SOUND,
            () -> new BasicEffect().block(SimpleParts::noteSound).entity(SimpleParts::mobSound));
    public static final DeferredHolder<ElementEffect, ElementEffect> CHANGE_EFFECT = element(DustElements.CHANGE,
            () -> new BasicEffect().block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.ADAPTIVE)).entity(SimpleParts::toggleAge));
    public static final DeferredHolder<ElementEffect, ElementEffect> DEATH_EFFECT = element(DustElements.DEATH,
            () -> new BasicEffect().block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.DEATHLESS)).entity(SimpleParts::execute));
    public static final DeferredHolder<ElementEffect, ElementEffect> MUD_EFFECT = element(DustElements.MUD,
            () -> new BasicEffect().block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.SURE_FOOTING)).entity(BasicEffect.inflict(MobEffects.SLOWNESS)));
    public static final DeferredHolder<ElementEffect, ElementEffect> ICE_EFFECT = element(DustElements.ICE,
            () -> new BasicEffect().block(SpellConversions::convert).self(SimpleParts::cool).entity(BasicEffect.inflict(DustEffectsRegistry.CHILLED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> METAL_EFFECT = element(DustElements.METAL,
            () -> new BasicEffect().block(MetalParts::markBonus).self(BasicEffect.give(DustEffectsRegistry.IRON_SKIN)).entity(SimpleParts::bleed));
    public static final DeferredHolder<ElementEffect, ElementEffect> ABYSS_EFFECT = element(DustElements.ABYSS,
            () -> new BasicEffect().block(SpellBlocks::corruptBlock).self(BasicEffect.give(DustEffectsRegistry.LIFE_LEECH)).entity(BasicEffect.inflict(DustEffectsRegistry.SILENCED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> ENERGY_EFFECT = element(DustElements.ENERGY,
            () -> new BasicEffect().block(UtilityParts::fuelFurnaces).self(BasicEffect.give(DustEffectsRegistry.ENERGY_DRAIN)).entity(EnergyCharge::charge));
    public static final DeferredHolder<ElementEffect, ElementEffect> CREATURE_EFFECT = element(DustElements.CREATURE,
            () -> new BasicEffect().block(SpecialBlockParts::animate).self(BasicEffect.give(DustEffectsRegistry.PACK_TACTICS)).entity(SimpleParts::mark));
    public static final DeferredHolder<ElementEffect, ElementEffect> PLANTS_EFFECT = element(DustElements.PLANTS,
            () -> new BasicEffect().self(UtilityParts::plantHeld).block(UtilityParts::plantFlower).entity(BasicEffect.inflict(DustEffectsRegistry.TANGLED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> TREES_EFFECT = element(DustElements.TREES,
            () -> new BasicEffect().block(UtilityParts::fellTree).self(BasicEffect.give(DustEffectsRegistry.THORNS)).entity(RootParts::roots));
    public static final DeferredHolder<ElementEffect, ElementEffect> KNOWLEDGE_EFFECT = element(DustElements.KNOWLEDGE,
            () -> new BasicEffect().block(MobParts::markWise).self(BasicEffect.give(DustEffectsRegistry.INSIGHT)).entity(BasicEffect.inflict(DustEffectsRegistry.ENLIGHTENED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> FUNGUS_EFFECT = element(DustElements.FUNGUS,
            () -> new BasicEffect().entity(MobParts::enthrall).block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.FUNGAL)));
    public static final DeferredHolder<ElementEffect, ElementEffect> GEM_EFFECT = element(DustElements.GEM,
            () -> new BasicEffect().entity(MobParts::encase).block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.REFLECTING)));
    public static final DeferredHolder<ElementEffect, ElementEffect> FRACTURE_EFFECT = element(DustElements.FRACTURE,
            () -> new BasicEffect().self(FractureParts::crushHeld).block(SpellConversions::convert).entity(BasicEffect.inflict(DustEffectsRegistry.FRACTURED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> LIGHTNING_EFFECT = element(DustElements.LIGHTNING,
            () -> new BasicEffect().self(BasicEffect.give(DustEffectsRegistry.DASHING)).block(MovementParts::conductiveBlock).entity(BasicEffect.inflict(DustEffectsRegistry.CONDUCTIVE)));

    // Elements whose only part so far is their block conversions (Features/Spells/conversions)
    public static final DeferredHolder<ElementEffect, ElementEffect> RUST_EFFECT = element(DustElements.RUST,
            () -> new BasicEffect().self(RustParts::corrosiveAura).block(RustParts::oxidise).entity(RustParts::corrode));
    public static final DeferredHolder<ElementEffect, ElementEffect> CRYSTAL_EFFECT = element(DustElements.CRYSTAL,
            () -> new BasicEffect().self(StoreParts::crystallize).entity(MobParts::shatter).block(SpellConversions::convert));
    public static final DeferredHolder<ElementEffect, ElementEffect> RADIANCE_EFFECT = element(DustElements.RADIANCE,
            () -> new BasicEffect().self(Radiance::shareSelf).block(SpellConversions::convert).entity(Radiance::shareMob));
    public static final DeferredHolder<ElementEffect, ElementEffect> SEEDS_EFFECT = element(DustElements.SEEDS,
            () -> new BasicEffect().self(StoreParts::plantSeed).entity(MobParts::toBreedingItem).block(SpellConversions::convert));

    public static final DeferredHolder<ElementEffect, ElementEffect> VACUUM_EFFECT = element(DustElements.VACUUM,
            () -> new BasicEffect().self(BasicEffect.give(DustEffectsRegistry.VACUUM)).block(MobParts::vacuumBlock).entity(MobParts::vacuumMob));
    public static final DeferredHolder<ElementEffect, ElementEffect> TOOL_EFFECT = element(DustElements.TOOL,
            () -> new BasicEffect().self(BasicEffect.give(DustEffectsRegistry.TOOL_SENSE)).block(UtilityParts::markLowerTier).entity(MobParts::pickUpLoot));
    public static final DeferredHolder<ElementEffect, ElementEffect> GROWTH_EFFECT = element(DustElements.GROWTH,
            () -> new BasicEffect().self(BasicEffect.give(DustEffectsRegistry.GROWN)).block(MobParts::bonemeal).entity(MobParts::grow));

    public static final DeferredHolder<ElementEffect, ElementEffect> WARP_EFFECT = element(DustElements.WARP,
            () -> new BasicEffect().self(UtilityParts::warpSelf).block(UtilityParts::warpBlock).entity(UtilityParts::warpMob));
    public static final DeferredHolder<ElementEffect, ElementEffect> SLIME_EFFECT = element(DustElements.SLIME,
            () -> new BasicEffect().self(BasicEffect.give(DustEffectsRegistry.SLIPPERY)).entity(BasicEffect.inflict(DustEffectsRegistry.SLIPPERY)).block(com.mowmaster.dust.Features.Spells.blocks.Sticking::stick));

    public static final DeferredHolder<ElementEffect, ElementEffect> SPIRIT_EFFECT = element(DustElements.SPIRIT,
            () -> new BasicEffect().block(SpecialBlockParts::phase).self(BasicEffect.give(DustEffectsRegistry.SPIRIT_FORM)).entity(MovementParts::ghost));

    private static DeferredHolder<ElementEffect, ElementEffect> element(com.mowmaster.dust.Features.DustComposition.DustElement element,
                                                                        java.util.function.Supplier<ElementEffect> effect)
    {
        return ELEMENT_EFFECTS.register(element.id().getPath(), effect);
    }

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
