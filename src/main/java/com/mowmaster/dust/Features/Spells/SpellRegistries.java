package com.mowmaster.dust.Features.Spells;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.api.EffectType;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellAttribute;
import com.mowmaster.dust.Features.Spells.api.SpellModifier;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.modifiers.FusionModifier;
import com.mowmaster.dust.Features.Spells.pylons.SpellLinks;
import com.mowmaster.dust.Features.Spells.modifiers.BasicAspect;
import com.mowmaster.dust.Features.Spells.modifiers.EchoFragment;
import com.mowmaster.dust.Features.Spells.modifiers.FragmentSlotModifier;
import com.mowmaster.dust.Features.Spells.modifiers.LeveledModifier;
import com.mowmaster.dust.Features.Spells.api.ModifierFit;
import com.mowmaster.dust.Features.Spells.api.SpellFragment;
import com.mowmaster.dust.Features.Spells.api.SpellConfig;
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

    // ---- modifiers (drawn from the tree's pool into its deck, and from the deck onto pylons: SpellPylonBlockEntity) ----
    // Each is a LeveledModifier: what its levels do, which pylon categories it's in, which spells it suits, and its
    // draw rules. modifier(name, ...) registers it and tells it its own id.

    // General (any spell; a Neutral pylon only)
    // Cast Speed: 1 tick sooner to cast again per level
    public static final DeferredHolder<SpellModifier, SpellModifier> CAST_SPEED = modifier("cast_speed",
            () -> new LeveledModifier((stats, levels) -> stats.withCooldown(stats.cooldown() - levels)));

    // Thrifty
    // Mana Reduction / Dust Reduction: 1% less a level (rounded down)
    public static final DeferredHolder<SpellModifier, SpellModifier> MANA_REDUCTION = modifier("mana_reduction",
            () -> new LeveledModifier((stats, levels) -> stats.withManaPercent(stats.manaPercent() - levels)).categories(ModifierCategory.THRIFTY));
    public static final DeferredHolder<SpellModifier, SpellModifier> DUST_REDUCTION = modifier("dust_reduction",
            () -> new LeveledModifier((stats, levels) -> stats).categories(ModifierCategory.THRIFTY).costPerLevel(-1));
    // Transmutation: short of the element's dust, one of each of its parent elements pays for each missing dust instead
    // (SpellCasting). From level 50; one level only; once on a spell, it's knocked out of the deck and pool (onlyOne).
    // It has a card (DustItemRegistry.CARD_TRANSMUTATION), dropped when the spell is broken.
    public static final DeferredHolder<SpellModifier, SpellModifier> TRANSMUTATION = modifier("transmutation",
            () -> LeveledModifier.marker().categories(ModifierCategory.THRIFTY).unlock(50).onlyOne().oneLevel());

    // Experienced
    // Spell XP Gain: a cast gives the tree 1 experience; this adds 0.1 a level (xpPercent 10 a level), rounded up
    // (SpellScrollItem.giveExperience)
    public static final DeferredHolder<SpellModifier, SpellModifier> SPELL_XP = modifier("spell_xp",
            () -> new LeveledModifier((stats, levels) -> stats.withXpPercent(stats.xpPercent() + 10 * levels)).categories(ModifierCategory.EXPERIENCED));
    // Fragment Progress: the next fragment slot comes a level sooner per level. Only once the line has a fragment and
    // there are more fragments for the element to come.
    public static final DeferredHolder<SpellModifier, SpellModifier> FRAGMENT_PROGRESS = modifier("fragment_progress",
            () -> LeveledModifier.marker().categories(ModifierCategory.EXPERIENCED)
                    .requires(context -> context.countOf(SpellFragment.class) > 0 && FragmentSlotModifier.moreToCome(context)));
    // Reduced Requirements (one use, from level 20, rare): every modifier unlocks PylonConfig.REDUCED_REQUIREMENT levels sooner
    public static final DeferredHolder<SpellModifier, SpellModifier> REDUCED_REQUIREMENTS = modifier("reduced_requirements",
            () -> LeveledModifier.marker().categories(ModifierCategory.EXPERIENCED).uses(1).unlock(20).weight(5));

    // Instant effects
    // Damage: half a heart more a level
    public static final DeferredHolder<SpellModifier, SpellModifier> DAMAGE = modifier("damage",
            () -> new LeveledModifier((stats, levels) -> stats.withBonusDamage(stats.bonusDamage() + 0.5F * levels))
                    .categories(ModifierCategory.DAMAGE).fit(ModifierFit.INSTANT));
    // Crit Chance: 1% a level to deal double damage. Only for spells that damage what they hit (damagesTargets).
    public static final DeferredHolder<SpellModifier, SpellModifier> CRIT_CHANCE = modifier("crit_chance",
            () -> new LeveledModifier((stats, levels) -> stats.withCritChance(stats.critChance() + 0.01F * levels)).fit(ModifierFit.INSTANT)
                    .requires(SpellRegistries::damagesTargets));
    // Luck: 1% a level on the chance of an effect's chance-based results
    public static final DeferredHolder<SpellModifier, SpellModifier> LUCK = modifier("luck",
            () -> new LeveledModifier((stats, levels) -> stats.withLuck(stats.luck() + 0.01F * levels)).fit(ModifierFit.INSTANT)
                    .onlyWhen(SpellRegistries::luckMatters));

    // Lingering (DOT) effects
    // Duration: a second longer a level
    public static final DeferredHolder<SpellModifier, SpellModifier> DURATION_MODIFIER = modifier("duration",
            () -> new LeveledModifier((stats, levels) -> stats.withDuration(stats.duration() + 20 * levels)).fit(ModifierFit.DOT));
    // Potency: a quarter level stronger a level
    public static final DeferredHolder<SpellModifier, SpellModifier> POTENCY_MODIFIER = modifier("potency",
            () -> new LeveledModifier((stats, levels) -> stats.withPotency(stats.potency() + 0.25F * levels))
                    .categories(ModifierCategory.DAMAGE).fit(ModifierFit.DOT));

    // Projectile spells
    // Falloff: a block further before gravity starts pulling the bolt down, a level
    public static final DeferredHolder<SpellModifier, SpellModifier> FALLOFF = modifier("falloff",
            () -> new LeveledModifier((stats, levels) -> stats.withFalloff(stats.falloff() + levels)).shapes(id("projectile")));
    // Velocity: the bolt flies 0.1 blocks a tick faster a level
    public static final DeferredHolder<SpellModifier, SpellModifier> VELOCITY = modifier("velocity",
            () -> new LeveledModifier((stats, levels) -> stats.withSpeed(stats.speed() + 0.1F * levels)).shapes(id("projectile")).categories(ModifierCategory.GIANT));
    // Range: a block further before the bolt fizzles out, a level
    public static final DeferredHolder<SpellModifier, SpellModifier> RANGE_MODIFIER = modifier("range",
            () -> new LeveledModifier((stats, levels) -> stats.withRange(stats.range() + levels)).shapes(id("projectile")).categories(ModifierCategory.GIANT));
    // Impact Damage (from level 20, one more every 10 levels): 1 damage a level when the bolt hits a creature
    public static final DeferredHolder<SpellModifier, SpellModifier> IMPACT_DAMAGE = modifier("impact_damage",
            () -> new LeveledModifier((stats, levels) -> stats.withImpactDamage(stats.impactDamage() + levels)).shapes(id("projectile"))
                    .categories(ModifierCategory.DAMAGE).unlock(20).everyLevels(10));
    // Multishot (from level 30): one more bolt a level (1-3 a node), splitting off where the falloff ends
    // (SpellProjectileEntity). Not with Hitscan in the same line.
    public static final DeferredHolder<SpellModifier, SpellModifier> MULTISHOT = modifier("multishot",
            () -> LeveledModifier.marker().shapes(id("projectile")).unlock(30).perNode(1, 3).extraCost(2)
                    .requires(context -> !context.has(id("hitscan"))));
    // Hitscan (from level 50, rare, once per branch): no bolt; a ray reaches the target at once (ProjectileShape). Not
    // with Multishot in the same line.
    public static final DeferredHolder<SpellModifier, SpellModifier> HITSCAN = modifier("hitscan",
            () -> LeveledModifier.marker().shapes(id("projectile")).unlock(50).weight(2).oncePerBranch()
                    .requires(context -> !context.has(id("multishot"))));

    // Touch spells
    // Overshield (from level 20): the caster gets Resistance for OVERSHIELD_TICKS as they cast, one level higher for
    // each level of Shield Potency (which it brings into the pool)
    public static final DeferredHolder<SpellModifier, SpellModifier> OVERSHIELD = modifier("overshield",
            () -> LeveledModifier.marker().shapes(id("touch")).unlock(20).addsToPool(id("shield_potency"))
                    .onCast(cast -> {
                        if (cast.caster() == null) return;
                        int potency = cast.spell().levelsOf(id("shield_potency"));
                        cast.caster().addEffect(new net.minecraft.world.effect.MobEffectInstance(MobEffects.RESISTANCE, SpellRegistries.OVERSHIELD_TICKS, potency, false, true, true));
                    }));
    // Shield Potency (from level 20, one more every 10 levels; only once Overshield is in the spell): Overshield a level
    // stronger, a level
    public static final DeferredHolder<SpellModifier, SpellModifier> SHIELD_POTENCY = modifier("shield_potency",
            () -> LeveledModifier.marker().shapes(id("touch")).unlock(20).everyLevels(10).requires(context -> context.countInTree(id("overshield")) > 0));
    public static final int OVERSHIELD_TICKS = 40;

    // ---- shape modifiers (from level 30, one each per spell): they change how the spell is delivered (SpellCast,
    // SpellProjectileEntity, SpellCasting) and bring their own modifiers into the pool (addsToPool) ----

    // AOE (Touch, Projectile): where it lands, the effect reaches every block within its radius (SpellConfig.AOE_RADIUS,
    // plus Size) and the nearest creatures there (1, plus Target Count). Not with Wall.
    public static final DeferredHolder<SpellModifier, SpellModifier> AOE = modifier("aoe",
            () -> shapeModifier("aoe", "wall").shapes(id("touch"), id("projectile")).addsToPool(id("size"), id("target_count"), id("lingering")));
    // Size: a quarter block more radius a level
    public static final DeferredHolder<SpellModifier, SpellModifier> SIZE_MODIFIER = modifier("size",
            () -> LeveledModifier.marker().categories(ModifierCategory.GIANT));
    // Target Count: one more creature a level (1-3 a node)
    public static final DeferredHolder<SpellModifier, SpellModifier> TARGET_COUNT = modifier("target_count",
            () -> LeveledModifier.marker().categories(ModifierCategory.GIANT).perNode(1, 3));
    // Lingering (lingering effects; brought in by AOE): a pool where it lands keeps putting the effect on the creatures in
    // it (the AOE's radius) for the spell's duration
    public static final DeferredHolder<SpellModifier, SpellModifier> LINGERING = modifier("lingering",
            () -> LeveledModifier.marker().fit(ModifierFit.DOT).onlyOne().requires(context -> context.countInTree(id("lingering")) == 0));

    // Piercing (Projectile): the bolt goes through creatures and blocks, affecting each, before it fizzles (1, plus half
    // a level of Depth). Not with Wall.
    public static final DeferredHolder<SpellModifier, SpellModifier> PIERCING = modifier("piercing",
            () -> shapeModifier("piercing", "wall").shapes(id("projectile")).addsToPool(id("depth")));
    // Depth: half a creature / block more a level (1-3 a node)
    public static final DeferredHolder<SpellModifier, SpellModifier> DEPTH = modifier("depth",
            () -> LeveledModifier.marker().categories(ModifierCategory.GIANT).perNode(1, 3));

    // Chaining (Touch, Projectile): after the first creature, the effect jumps to the nearest one not yet hit, within the
    // search distance (SpellConfig.CHAIN_SEARCH, plus Search Distance), once (plus Chain Count); a line shows each jump.
    // Not with Wall.
    public static final DeferredHolder<SpellModifier, SpellModifier> CHAINING = modifier("chaining",
            () -> shapeModifier("chaining", "wall").shapes(id("touch"), id("projectile")).addsToPool(id("chain_count"), id("search_distance")));
    // Chain Count: one more jump a level (1-3 a node)
    public static final DeferredHolder<SpellModifier, SpellModifier> CHAIN_COUNT = modifier("chain_count",
            () -> LeveledModifier.marker().categories(ModifierCategory.GIANT).perNode(1, 3));
    // Search Distance: a block further to find the next creature, a level
    public static final DeferredHolder<SpellModifier, SpellModifier> SEARCH_DISTANCE = modifier("search_distance",
            () -> LeveledModifier.marker());

    // Rune (any shape): instead of landing, it leaves a glyph on the ground there (at the caster's feet for Self) that
    // puts the effect on whatever steps on it (RuneBlock), for SpellConfig.RUNE_SECONDS (plus Rune Duration)
    public static final DeferredHolder<SpellModifier, SpellModifier> RUNE = modifier("rune",
            () -> shapeModifier("rune").addsToPool(id("rune_duration"), id("permanent")));
    // Rune Duration: 20 seconds longer a level (3-7 a node)
    public static final DeferredHolder<SpellModifier, SpellModifier> RUNE_DURATION = modifier("rune_duration",
            () -> LeveledModifier.marker().perNode(3, 7));
    // Permanent (once the rune lasts over 10 minutes): the rune stays until it's broken, paying each trigger's dust from
    // Element Crafter jars nearby, or else the chunk's aura; it needs no mana after, but casting it costs 4x the mana
    public static final DeferredHolder<SpellModifier, SpellModifier> PERMANENT = modifier("permanent",
            () -> LeveledModifier.marker().uses(1).requires(context -> context.countInTree(id("permanent")) == 0
                    && SpellConfig.RUNE_SECONDS + 20 * context.levelsInTree(id("rune_duration")) > SpellConfig.PERMANENT_SECONDS));

    // Wall (Touch, Projectile): the effect spreads in a line across the spell's path where it lands (SpellConfig.WALL_HALF
    // either side, plus Wall Width; Wall Depth makes it deeper). Not with AOE, Chaining or Piercing.
    public static final DeferredHolder<SpellModifier, SpellModifier> WALL = modifier("wall",
            () -> shapeModifier("wall", "aoe", "chaining", "piercing").shapes(id("touch"), id("projectile")).addsToPool(id("wall_width"), id("wall_depth")));
    // Wall Width: half a block more either side, a level (1-3 a node)
    public static final DeferredHolder<SpellModifier, SpellModifier> WALL_WIDTH = modifier("wall_width",
            () -> LeveledModifier.marker().categories(ModifierCategory.GIANT).perNode(1, 3));
    // Wall Depth (from level 50): half a block deeper along the path, a level (1-3 a node)
    public static final DeferredHolder<SpellModifier, SpellModifier> WALL_DEPTH = modifier("wall_depth",
            () -> LeveledModifier.marker().categories(ModifierCategory.GIANT).perNode(1, 3).unlock(50));

    // Beam (Touch, Projectile): hold the cast to channel a piercing ray at what you're looking at, a pulse every
    // SpellConfig.BEAM_INTERVAL ticks (each costing a cast) for as long as you hold it (Channeling)
    public static final DeferredHolder<SpellModifier, SpellModifier> BEAM_MODIFIER = modifier("beam",
            () -> shapeModifier("beam").shapes(id("touch"), id("projectile")).addsToPool(id("beam_distance"), id("efficiency"), id("casting_speed")));
    // Beam Distance: a block further, a level (3-7 a node)
    public static final DeferredHolder<SpellModifier, SpellModifier> BEAM_DISTANCE = modifier("beam_distance",
            () -> LeveledModifier.marker().categories(ModifierCategory.GIANT).perNode(3, 7));
    // Efficiency (one more every 10 levels): each second channelled takes 1% a level off each pulse's cost
    public static final DeferredHolder<SpellModifier, SpellModifier> EFFICIENCY = modifier("efficiency",
            () -> LeveledModifier.marker().categories(ModifierCategory.THRIFTY).perNode(3, 7).unlock(30).everyLevels(10));
    // Casting Speed (one more every 10 levels): a tick less between pulses, a level (2-5 a node)
    public static final DeferredHolder<SpellModifier, SpellModifier> CASTING_SPEED = modifier("casting_speed",
            () -> LeveledModifier.marker().perNode(2, 5).unlock(30).everyLevels(10));

    // ---- blessed: the twelve signs (modifiers/Blessings). Into the deck when the tree's blessing reaches 10; a Neutral
    // pylon only; one use each ----
    public static final DeferredHolder<SpellModifier, SpellModifier> ARIES = MODIFIERS.register("aries", com.mowmaster.dust.Features.Spells.modifiers.Blessings::aries);
    public static final DeferredHolder<SpellModifier, SpellModifier> TAURUS = MODIFIERS.register("taurus", com.mowmaster.dust.Features.Spells.modifiers.Blessings::taurus);
    public static final DeferredHolder<SpellModifier, SpellModifier> GEMINI = MODIFIERS.register("gemini", com.mowmaster.dust.Features.Spells.modifiers.Blessings::gemini);
    public static final DeferredHolder<SpellModifier, SpellModifier> CANCER = MODIFIERS.register("cancer", com.mowmaster.dust.Features.Spells.modifiers.Blessings::cancer);
    public static final DeferredHolder<SpellModifier, SpellModifier> LEO = MODIFIERS.register("leo", com.mowmaster.dust.Features.Spells.modifiers.Blessings::leo);
    public static final DeferredHolder<SpellModifier, SpellModifier> VIRGO = MODIFIERS.register("virgo", com.mowmaster.dust.Features.Spells.modifiers.Blessings::virgo);
    public static final DeferredHolder<SpellModifier, SpellModifier> LIBRA = MODIFIERS.register("libra", com.mowmaster.dust.Features.Spells.modifiers.Blessings::libra);
    public static final DeferredHolder<SpellModifier, SpellModifier> SCORPIO = MODIFIERS.register("scorpio", com.mowmaster.dust.Features.Spells.modifiers.Blessings::scorpio);
    public static final DeferredHolder<SpellModifier, SpellModifier> SAGITTARIUS = MODIFIERS.register("sagittarius", com.mowmaster.dust.Features.Spells.modifiers.Blessings::sagittarius);
    public static final DeferredHolder<SpellModifier, SpellModifier> CAPRICORN = MODIFIERS.register("capricorn", com.mowmaster.dust.Features.Spells.modifiers.Blessings::capricorn);
    public static final DeferredHolder<SpellModifier, SpellModifier> AQUARIUS = MODIFIERS.register("aquarius", com.mowmaster.dust.Features.Spells.modifiers.Blessings::aquarius);
    public static final DeferredHolder<SpellModifier, SpellModifier> PISCES = MODIFIERS.register("pisces", com.mowmaster.dust.Features.Spells.modifiers.Blessings::pisces);

    // ---- corrupted: the seven sins (modifiers/Sins). Into the deck when the tree's corruption reaches 5; the next pylon
    // takes one; one use each ----
    public static final DeferredHolder<SpellModifier, SpellModifier> PRIDE = MODIFIERS.register("pride", com.mowmaster.dust.Features.Spells.modifiers.Sins::pride);
    public static final DeferredHolder<SpellModifier, SpellModifier> GREED = MODIFIERS.register("greed", com.mowmaster.dust.Features.Spells.modifiers.Sins::greed);
    public static final DeferredHolder<SpellModifier, SpellModifier> WRATH = MODIFIERS.register("wrath", com.mowmaster.dust.Features.Spells.modifiers.Sins::wrath);
    public static final DeferredHolder<SpellModifier, SpellModifier> LUST = MODIFIERS.register("lust", com.mowmaster.dust.Features.Spells.modifiers.Sins::lust);
    public static final DeferredHolder<SpellModifier, SpellModifier> ENVY = MODIFIERS.register("envy", com.mowmaster.dust.Features.Spells.modifiers.Sins::envy);
    public static final DeferredHolder<SpellModifier, SpellModifier> GLUTTONY = MODIFIERS.register("gluttony", com.mowmaster.dust.Features.Spells.modifiers.Sins::gluttony);
    public static final DeferredHolder<SpellModifier, SpellModifier> SLOTH = MODIFIERS.register("sloth", com.mowmaster.dust.Features.Spells.modifiers.Sins::sloth);

    // The deck and pool (read by the tree, not the cast: SpellPylonBlockEntity)
    // Category Weight: a category pylon's category is drawn into the deck more often, +1 weight a level (category pylons only)
    public static final DeferredHolder<SpellModifier, SpellModifier> CATEGORY_WEIGHT = modifier("category_weight",
            () -> LeveledModifier.marker().extraCost(0).categoryOnly()
                    .categories(ModifierCategory.DAMAGE, ModifierCategory.GIANT, ModifierCategory.THRIFTY, ModifierCategory.EXPERIENCED));
    // Double Blessed Progress (one use): each locked pylon counts twice towards a blessing
    public static final DeferredHolder<SpellModifier, SpellModifier> DOUBLE_BLESSED = modifier("double_blessed",
            () -> LeveledModifier.marker().extraCost(0).oneLevel().onlyOne());
    // Double Draw: 5% a level that a modifier drawn into the deck comes twice
    public static final DeferredHolder<SpellModifier, SpellModifier> DOUBLE_DRAW = modifier("double_draw",
            () -> LeveledModifier.marker().extraCost(0));

    // ---- fragments, aspects and fusions (the bigger changes, after spell breaks) ----
    // The Fragment slot: rolled at level milestones, becomes a random fragment for the element (FragmentSlotModifier)
    public static final DeferredHolder<SpellModifier, SpellModifier> FRAGMENT = MODIFIERS.register("fragment", FragmentSlotModifier::new);
    // Echo (any element): the spell casts again by itself; its aspects add echoes and quicken them
    public static final DeferredHolder<SpellModifier, SpellModifier> ECHO = MODIFIERS.register("echo", EchoFragment::new);
    public static final DeferredHolder<SpellModifier, SpellModifier> ECHO_RESONANCE = MODIFIERS.register("echo_resonance",
            () -> new BasicAspect(Identifier.fromNamespaceAndPath(DustReferences.MODID, "echo"), 10));
    public static final DeferredHolder<SpellModifier, SpellModifier> ECHO_QUICKENING = MODIFIERS.register("echo_quickening",
            () -> new BasicAspect(Identifier.fromNamespaceAndPath(DustReferences.MODID, "echo"), 8));
    // ---- linking two spells (pylons/SpellLinks): drawn from level 50, a Neutral pylon only. The next pylon beside it
    // must belong to another spell (or be a new Spell Pylon given its crystal there): the two spells are then linked ----
    // Fusion: if the two elements have a fusion (below), both spells become it; otherwise the link is a Spell Stack
    public static final DeferredHolder<SpellModifier, SpellModifier> FUSION = modifier("fusion",
            () -> LeveledModifier.marker().unlock(SpellLinks.LINK_LEVEL).oneLevel().weight(4).oncePerBranch());
    // Spell Stack: both spells cast, one after the other (the spell the link came from first)
    public static final DeferredHolder<SpellModifier, SpellModifier> SPELL_STACK = modifier("spell_stack",
            () -> LeveledModifier.marker().unlock(SpellLinks.LINK_LEVEL).oneLevel().weight(4).oncePerBranch());
    // Silky (one level): what the spell breaks drops as itself (Silk Touch); an explosion breaks no blocks; blocks the
    // spell makes don't fade
    public static final DeferredHolder<SpellModifier, SpellModifier> SILKY = modifier("silky",
            () -> LeveledModifier.marker().oneLevel().onlyOne().onlyWhen(SpellRegistries::silkyMatters)
                    .requires(context -> context.countInTree(id("silky")) == 0));

    // Silky only does something to a spell that breaks or makes blocks: a block effect that blows blocks apart or
    // makes blocks that fade (Explosion, Darkness's veil, Spirit's phase, Flight's floating block, Poison's pool), or
    // the Fracture + Earth fusion (it mines). Not on a Self spell (it never lands on a block).
    private static final java.util.Set<com.mowmaster.dust.Features.DustComposition.DustElement> SILKY_ELEMENTS = java.util.Set.of(
            DustElements.EXPLOSION, DustElements.DARKNESS, DustElements.SPIRIT, DustElements.FLIGHT, DustElements.POISON);

    public static boolean silkyMatters(com.mowmaster.dust.Features.Spells.api.ModifierContext context)
    {
        if (context.shape().equals(SELF.getId())) return false;
        return SILKY_ELEMENTS.contains(context.element()) || context.countInTree(id("fusion_fracture_earth")) > 0;
    }

    // Luck only matters to an effect with chance-based results: the Fracture + Earth fusion's mining (Fortune)
    public static boolean luckMatters(com.mowmaster.dust.Features.Spells.api.ModifierContext context)
    {
        return context.countInTree(id("fusion_fracture_earth")) > 0;
    }

    // Prevent Block Action / Prevent Attack Action (Touch, Projectile; from level 10; one use, and only one of the two
    // per spell): the spell does nothing to blocks / to creatures. Drawing one knocks the other out of the deck and pool;
    // a pylon holding one that's broken loses it for good and the other comes back, taken by the next Neutral pylon
    // (SpellDeck.breakPrevent). Neither blesses when locked nor corrupts when broken.
    // Spell Branch: does nothing to the spell; the pylon holding it lets the spell have one more branch (no levels: its
    // pylon has no upgrade nodes). Never drawn from the pool by chance: one goes in the deck every
    // PylonConfig.BRANCH_EVERY levels (SpellDeck.addDueBranches).
    public static final DeferredHolder<SpellModifier, SpellModifier> SPELL_BRANCH = modifier("spell_branch",
            () -> LeveledModifier.marker().oneLevel());
    public static final DeferredHolder<SpellModifier, SpellModifier> PREVENT_BLOCK = modifier("prevent_block",
            () -> LeveledModifier.marker().shapes(id("touch"), id("projectile")).unlock(10).oneLevel().onlyOne()
                    .requires(context -> context.countInTree(id("prevent_block")) == 0 && context.countInTree(id("prevent_attack")) == 0));
    public static final DeferredHolder<SpellModifier, SpellModifier> PREVENT_ATTACK = modifier("prevent_attack",
            () -> LeveledModifier.marker().shapes(id("touch"), id("projectile")).unlock(10).oneLevel().onlyOne()
                    .requires(context -> context.countInTree(id("prevent_block")) == 0 && context.countInTree(id("prevent_attack")) == 0));

    // Whether a modifier is one of the two Prevent cards
    public static boolean isPrevent(@org.jspecify.annotations.Nullable Identifier modifier)
    {
        return PREVENT_BLOCK.getId().equals(modifier) || PREVENT_ATTACK.getId().equals(modifier);
    }

    // The other Prevent card
    public static Identifier otherPrevent(Identifier modifier)
    {
        return PREVENT_BLOCK.getId().equals(modifier) ? PREVENT_ATTACK.getId() : PREVENT_BLOCK.getId();
    }

    // Whether a cast has Silky
    public static boolean silky(com.mowmaster.dust.Features.Spells.api.SpellCast cast)
    {
        return cast.spell().levelsOf(SILKY.getId()) > 0 || cast.spell().modifiers().contains(SILKY.getId());
    }

    // Fusions: one per pair of elements, each with its own effects (parts it leaves out fall back to the spell's element).
    // They aren't drawn: a Fusion link between spells of the two elements gives the spells the fusion (SpellLinks).
    public static final DeferredHolder<SpellModifier, SpellModifier> FUSION_FLIGHT_MOTION = MODIFIERS.register("fusion_flight_motion",
            () -> new FusionModifier(DustElements.FLIGHT, DustElements.MOTION).self(MovementParts::glide)
                    .types(EffectType.DOT, null, null));
    // Warp + Vacuum: Telekinesis (effects/Telekinesis): tap to push, hold to pull, swipe to throw; self: Vacuum's own
    public static final DeferredHolder<SpellModifier, SpellModifier> FUSION_WARP_VACUUM = MODIFIERS.register("fusion_warp_vacuum",
            () -> new FusionModifier(DustElements.WARP, DustElements.VACUUM)
                    .self(BasicEffect.give(DustEffectsRegistry.VACUUM))
                    .entity(com.mowmaster.dust.Features.Spells.effects.Telekinesis::grabEntity)
                    .block(com.mowmaster.dust.Features.Spells.effects.Telekinesis::grabBlock)
                    .types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT));
    // Fracture + Earth: it breaks the block it lands on as a pickaxe would (FusionParts.mine)
    public static final DeferredHolder<SpellModifier, SpellModifier> FUSION_FRACTURE_EARTH = MODIFIERS.register("fusion_fracture_earth",
            () -> new FusionModifier(DustElements.FRACTURE, DustElements.EARTH).block(com.mowmaster.dust.Features.Spells.effects.FusionParts::mine)
                    .types(null, EffectType.INSTANT, null));

    // ---- the elements' effects (the rest come in later steps; until then an element does nothing) ----
    public static final DeferredHolder<ElementEffect, ElementEffect> FIRE = ELEMENT_EFFECTS.register(DustElements.FIRE.id().getPath(), FireEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> WATER = ELEMENT_EFFECTS.register(DustElements.WATER.id().getPath(), WaterEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> EARTH = ELEMENT_EFFECTS.register(DustElements.EARTH.id().getPath(), EarthEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> ORDER = ELEMENT_EFFECTS.register(DustElements.ORDER.id().getPath(), OrderEffect::new);
    public static final DeferredHolder<ElementEffect, ElementEffect> CHAOS = ELEMENT_EFFECTS.register(DustElements.CHAOS.id().getPath(), ChaosEffect::new);

    // Elements whose effects are short (BasicEffect: parts from SimpleParts or a mob effect). A part not listed does
    // nothing yet: it comes in a later step. types(self, block, entity): each part's EffectType (DOT: it lingers;
    // INSTANT: once, on the hit), for sorting modifiers in the modifier rework.
    public static final DeferredHolder<ElementEffect, ElementEffect> MOTION_EFFECT = element(DustElements.MOTION,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).block(com.mowmaster.dust.Features.Spells.effects.Quickening::quicken).self(SimpleParts::speedSelfWithMount).entity(SimpleParts::speedWithMount));
    public static final DeferredHolder<ElementEffect, ElementEffect> LIFE_EFFECT = element(DustElements.LIFE,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT).entity(PetGhosts::reviveOrCure).block(SpellConversions::convert).self(BasicEffect.give(MobEffects.REGENERATION)));
    public static final DeferredHolder<ElementEffect, ElementEffect> HEALING_EFFECT = element(DustElements.HEALING,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.INSTANT, EffectType.INSTANT).block(SpellBlocks::purifyOrMend).self(SimpleParts::healSelf).entity(SimpleParts::heal));
    public static final DeferredHolder<ElementEffect, ElementEffect> LIGHT_EFFECT = element(DustElements.LIGHT,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).block(SpecialBlockParts::light).self(BasicEffect.give(MobEffects.NIGHT_VISION)).entity(BasicEffect.inflict(MobEffects.GLOWING)));
    public static final DeferredHolder<ElementEffect, ElementEffect> DARKNESS_EFFECT = element(DustElements.DARKNESS,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).block(SpecialBlockParts::veil).self(BasicEffect.give(MobEffects.INVISIBILITY)).entity(BasicEffect.inflict(MobEffects.BLINDNESS)));
    public static final DeferredHolder<ElementEffect, ElementEffect> FLIGHT_EFFECT = element(DustElements.FLIGHT,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).block(SpecialBlockParts::floatBlock).self(MovementParts::flight).entity(BasicEffect.inflict(MobEffects.SLOW_FALLING)));
    public static final DeferredHolder<ElementEffect, ElementEffect> AIR_EFFECT = element(DustElements.AIR,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT).block(MobParts::topple).self(BasicEffect.give(MobEffects.JUMP_BOOST)).entity(SimpleParts::pushAway));
    public static final DeferredHolder<ElementEffect, ElementEffect> POISON_EFFECT = element(DustElements.POISON,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.DOT, EffectType.DOT).block(SpellBlocks::poisonPool).self(SimpleParts::cleanse).entity(BasicEffect.inflict(MobEffects.POISON)));
    public static final DeferredHolder<ElementEffect, ElementEffect> ARMOR_EFFECT = element(DustElements.ARMOR,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).block(SpecialBlockParts::ward).self(BasicEffect.give(MobEffects.ABSORPTION)).entity(BasicEffect.inflict(MobEffects.ABSORPTION)));
    public static final DeferredHolder<ElementEffect, ElementEffect> PROTECTION_EFFECT = element(DustElements.PROTECTION,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).block(SpecialBlockParts::wardedGlass).self(BasicEffect.give(MobEffects.RESISTANCE)).entity(BasicEffect.inflict(DustEffectsRegistry.PIERCED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> FLESH_EFFECT = element(DustElements.FLESH,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).block(SpellBlocks::blight).entity(BasicEffect.inflict(DustEffectsRegistry.BUTCHERED)).self(BasicEffect.give(MobEffects.STRENGTH)));
    public static final DeferredHolder<ElementEffect, ElementEffect> DAMAGE_EFFECT = element(DustElements.DAMAGE,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.INSTANT).block(MobParts::markQuickBreak).self(BasicEffect.give(DustEffectsRegistry.MAGIC_WARD)).entity(SimpleParts::magicDamage).damaging());
    public static final DeferredHolder<ElementEffect, ElementEffect> MAGIC_EFFECT = element(DustElements.MAGIC,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.INSTANT).self(MagicParts::surge).block(MagicParts::amplify).entity(SimpleParts::magicDamage).damaging());
    public static final DeferredHolder<ElementEffect, ElementEffect> VOID_EFFECT = element(DustElements.VOID,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.INSTANT).self(VoidParts::chest).block(VoidParts::rift).entity(SimpleParts::voidDamage).damaging());
    public static final DeferredHolder<ElementEffect, ElementEffect> SOUL_EFFECT = element(DustElements.SOUL,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).self(SoulParts::anchor).block(SoulParts::bind).entity(SoulParts::pull));
    public static final DeferredHolder<ElementEffect, ElementEffect> STONE_EFFECT = element(DustElements.STONE,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT).block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.STONE_SKIN)).entity(SimpleParts::kineticDamage).damaging());
    public static final DeferredHolder<ElementEffect, ElementEffect> EXPLOSION_EFFECT = element(DustElements.EXPLOSION,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.INSTANT, EffectType.INSTANT).self(MobParts::flare).block(SimpleParts::explodeBlock).entity(SimpleParts::explodeAt));
    public static final DeferredHolder<ElementEffect, ElementEffect> SOUND_EFFECT = element(DustElements.SOUND,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.INSTANT, EffectType.INSTANT).block(SimpleParts::noteSound).entity(SimpleParts::mobSound));
    public static final DeferredHolder<ElementEffect, ElementEffect> CHANGE_EFFECT = element(DustElements.CHANGE,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT).block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.ADAPTIVE)).entity(SimpleParts::toggleAge));
    public static final DeferredHolder<ElementEffect, ElementEffect> DEATH_EFFECT = element(DustElements.DEATH,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT).block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.DEATHLESS)).entity(SimpleParts::execute));
    public static final DeferredHolder<ElementEffect, ElementEffect> MUD_EFFECT = element(DustElements.MUD,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.SURE_FOOTING)).entity(BasicEffect.inflict(MobEffects.SLOWNESS)));
    public static final DeferredHolder<ElementEffect, ElementEffect> ICE_EFFECT = element(DustElements.ICE,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).block(SpellConversions::convert).self(SimpleParts::cool).entity(BasicEffect.inflict(DustEffectsRegistry.CHILLED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> METAL_EFFECT = element(DustElements.METAL,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).block(MetalParts::markBonus).self(BasicEffect.give(DustEffectsRegistry.IRON_SKIN)).entity(SimpleParts::bleed));
    public static final DeferredHolder<ElementEffect, ElementEffect> ABYSS_EFFECT = element(DustElements.ABYSS,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).block(SpellBlocks::corruptBlock).self(BasicEffect.give(DustEffectsRegistry.LIFE_LEECH)).entity(BasicEffect.inflict(DustEffectsRegistry.SILENCED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> ENERGY_EFFECT = element(DustElements.ENERGY,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.INSTANT, EffectType.DOT).block(UtilityParts::fuelFurnaces).self(BasicEffect.give(DustEffectsRegistry.ENERGY_DRAIN)).entity(EnergyCharge::charge).damaging());
    public static final DeferredHolder<ElementEffect, ElementEffect> CREATURE_EFFECT = element(DustElements.CREATURE,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).block(SpecialBlockParts::animate).self(BasicEffect.give(DustEffectsRegistry.PACK_TACTICS)).entity(SimpleParts::mark));
    public static final DeferredHolder<ElementEffect, ElementEffect> PLANTS_EFFECT = element(DustElements.PLANTS,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.INSTANT, EffectType.DOT).self(UtilityParts::plantHeld).block(UtilityParts::plantFlower).entity(BasicEffect.inflict(DustEffectsRegistry.TANGLED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> TREES_EFFECT = element(DustElements.TREES,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).block(UtilityParts::fellTree).self(BasicEffect.give(DustEffectsRegistry.THORNS)).entity(RootParts::roots));
    public static final DeferredHolder<ElementEffect, ElementEffect> KNOWLEDGE_EFFECT = element(DustElements.KNOWLEDGE,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).block(MobParts::markWise).self(BasicEffect.give(DustEffectsRegistry.INSIGHT)).entity(BasicEffect.inflict(DustEffectsRegistry.ENLIGHTENED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> FUNGUS_EFFECT = element(DustElements.FUNGUS,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).entity(MobParts::enthrall).block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.FUNGAL)));
    public static final DeferredHolder<ElementEffect, ElementEffect> GEM_EFFECT = element(DustElements.GEM,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).entity(MobParts::encase).block(SpellConversions::convert).self(BasicEffect.give(DustEffectsRegistry.REFLECTING)));
    public static final DeferredHolder<ElementEffect, ElementEffect> FRACTURE_EFFECT = element(DustElements.FRACTURE,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.INSTANT, EffectType.DOT).self(FractureParts::crushHeld).block(SpellConversions::convert).entity(BasicEffect.inflict(DustEffectsRegistry.FRACTURED)));
    public static final DeferredHolder<ElementEffect, ElementEffect> LIGHTNING_EFFECT = element(DustElements.LIGHTNING,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).self(BasicEffect.give(DustEffectsRegistry.DASHING)).block(MovementParts::conductiveBlock).entity(BasicEffect.inflict(DustEffectsRegistry.CONDUCTIVE)));

    // Elements whose only part so far is their block conversions (Features/Spells/conversions)
    public static final DeferredHolder<ElementEffect, ElementEffect> RUST_EFFECT = element(DustElements.RUST,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).self(RustParts::corrosiveAura).block(RustParts::oxidise).entity(RustParts::corrode));
    public static final DeferredHolder<ElementEffect, ElementEffect> CRYSTAL_EFFECT = element(DustElements.CRYSTAL,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT).self(StoreParts::crystallize).entity(MobParts::shatter).damaging().block(SpellConversions::convert));
    public static final DeferredHolder<ElementEffect, ElementEffect> RADIANCE_EFFECT = element(DustElements.RADIANCE,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.INSTANT, EffectType.INSTANT).self(Radiance::shareSelf).block(SpellConversions::convert).entity(Radiance::shareMob));
    public static final DeferredHolder<ElementEffect, ElementEffect> SEEDS_EFFECT = element(DustElements.SEEDS,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT).self(StoreParts::plantSeed).entity(MobParts::toBreedingItem).block(SpellConversions::convert));

    public static final DeferredHolder<ElementEffect, ElementEffect> VACUUM_EFFECT = element(DustElements.VACUUM,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.INSTANT).self(BasicEffect.give(DustEffectsRegistry.VACUUM)).block(MobParts::vacuumBlock).entity(MobParts::vacuumMob));
    public static final DeferredHolder<ElementEffect, ElementEffect> TOOL_EFFECT = element(DustElements.TOOL,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.INSTANT).self(BasicEffect.give(DustEffectsRegistry.TOOL_SENSE)).block(UtilityParts::markLowerTier).entity(MobParts::pickUpLoot));
    public static final DeferredHolder<ElementEffect, ElementEffect> GROWTH_EFFECT = element(DustElements.GROWTH,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.INSTANT).self(BasicEffect.give(DustEffectsRegistry.GROWN)).block(MobParts::bonemeal).entity(MobParts::grow));

    public static final DeferredHolder<ElementEffect, ElementEffect> WARP_EFFECT = element(DustElements.WARP,
            () -> new BasicEffect().types(EffectType.INSTANT, EffectType.INSTANT, EffectType.INSTANT).self(UtilityParts::warpSelf).block(UtilityParts::warpBlock).entity(UtilityParts::warpMob));
    public static final DeferredHolder<ElementEffect, ElementEffect> SLIME_EFFECT = element(DustElements.SLIME,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.INSTANT, EffectType.DOT).self(BasicEffect.give(DustEffectsRegistry.SLIPPERY)).entity(BasicEffect.inflict(DustEffectsRegistry.SLIPPERY)).block(com.mowmaster.dust.Features.Spells.blocks.Sticking::stick));

    public static final DeferredHolder<ElementEffect, ElementEffect> SPIRIT_EFFECT = element(DustElements.SPIRIT,
            () -> new BasicEffect().types(EffectType.DOT, EffectType.DOT, EffectType.DOT).block(SpecialBlockParts::phase).self(BasicEffect.give(DustEffectsRegistry.SPIRIT_FORM)).entity(MovementParts::ghost));

    // Registers a LeveledModifier and gives it its own id (for its "once per branch" / "every N levels" counts)
    // A shape modifier: from level 30, one per spell, and not in a spell with any of the others named (incompatible)
    private static LeveledModifier shapeModifier(String self, String... incompatible)
    {
        return LeveledModifier.marker().unlock(30).extraCost(2).onlyOne().requires(context -> {
            if (context.countInTree(id(self)) > 0) return false;
            for (String other : incompatible) if (context.countInTree(id(other)) > 0) return false;
            return true;
        });
    }

    // A Dust id, for modifiers naming each other and shapes (by id: the holders may not be filled in yet when a modifier
    // is built). A method, so the modifiers above can use it: a lambda can't name a static field declared further down
    // (an "illegal forward reference"), but it can call a method.
    private static Identifier id(String path)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
    }

    private static DeferredHolder<SpellModifier, SpellModifier> modifier(String name, java.util.function.Supplier<LeveledModifier> modifier)
    {
        return MODIFIERS.register(name, () -> modifier.get().named(Identifier.fromNamespaceAndPath(DustReferences.MODID, name)));
    }

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

    // Whether a spell in the making deals damage to what it hits: a shape that lands on creatures (not Self), with an
    // element whose effect on a creature is the spell's damage (ElementEffect.damages). Damage-only modifiers need it.
    public static boolean damagesTargets(com.mowmaster.dust.Features.Spells.api.ModifierContext context)
    {
        if (context.element() == null || SELF.getId().equals(context.shape())) return false;
        ElementEffect effect = ELEMENT_EFFECT_REGISTRY.getValue(context.element().id());
        return effect != null && effect.damages();
    }
}
