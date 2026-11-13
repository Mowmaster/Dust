package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.Features.Spells.mobeffects.SpellMobEffect;
import com.mowmaster.dust.Features.Spells.mobeffects.TickingMobEffect;
import com.mowmaster.dust.Features.Spells.mobeffects.SpellMobEffects;
import com.mowmaster.dust.Features.Spells.mobeffects.MovementEffects;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.EffectAuraReplentish;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.EffectFireScorch;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.EffectVoidMagnet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DustEffectsRegistry
{
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, DustReferences.MODID);


    //uses hex for color values
    public static final Holder<MobEffect> EFFECT_SCORCH =
            MOB_EFFECTS.register("effect_scorch", () -> new EffectFireScorch(MobEffectCategory.HARMFUL, 0x36ebab));
    public static final Holder<MobEffect> EFFECT_MAGNET =
            MOB_EFFECTS.register("effect_magnet", () -> new EffectVoidMagnet(MobEffectCategory.BENEFICIAL, 0x36ebab));


    // ---- spell effects (Features/Spells) ----
    // Earth on yourself: harder to knock back (+50% knockback resistance per level)
    public static final Holder<MobEffect> STEADFAST = MOB_EFFECTS.register("steadfast", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x8B5A2B)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, Identifier.fromNamespaceAndPath(DustReferences.MODID, "effect.steadfast"), 0.5,
                    AttributeModifier.Operation.ADD_VALUE));
    // Order on a mob: it won't pick a target (SpellMobEffects)
    public static final Holder<MobEffect> CALMED = MOB_EFFECTS.register("calmed", () -> new SpellMobEffect(MobEffectCategory.NEUTRAL, 0xF0F0FF));
    // Chaos on yourself: steps up a full block (the strength, speed, hunger, mining fatigue and nausea that come with it
    // are added alongside: ChaosEffect)
    public static final Holder<MobEffect> RAGE = MOB_EFFECTS.register("rage", () -> new SpellMobEffect(MobEffectCategory.NEUTRAL, 0x5A0A2A)
            .addAttributeModifier(Attributes.STEP_HEIGHT, Identifier.fromNamespaceAndPath(DustReferences.MODID, "effect.rage"), 0.5,
                    AttributeModifier.Operation.ADD_VALUE));

    // Ice on yourself: puts out fire and keeps it out while it lasts
    public static final Holder<MobEffect> COOLING = MOB_EFFECTS.register("cooling", () -> new TickingMobEffect(MobEffectCategory.BENEFICIAL, 0xA8E4FF, 1,
            (level, entity, amplifier) -> entity.clearFire()));
    // Ice on a mob: slowed (15% a level) and frosted over (the powder snow overlay, without its damage)
    public static final Holder<MobEffect> CHILLED = MOB_EFFECTS.register("chilled", () -> new TickingMobEffect(MobEffectCategory.HARMFUL, 0x7FCFFF, 1,
            (level, entity, amplifier) -> entity.setTicksFrozen(Math.max(entity.getTicksFrozen(), entity.getTicksRequiredToFreeze() - 1)))
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, id("effect.chilled"), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                    amplifier -> -Math.min(0.9, 0.15 * (amplifier + 1))));
    // Stone on yourself: physical damage (anything armour can stop, and kinetic) is cut 20% a level, up to 80%
    public static final Holder<MobEffect> STONE_SKIN = MOB_EFFECTS.register("stone_skin", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x8C8C8C));
    // Metal on yourself: projectiles do nothing (unless they pierce)
    public static final Holder<MobEffect> IRON_SKIN = MOB_EFFECTS.register("iron_skin", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0xD8D8D8));
    // Metal on a mob: half a heart every 2 seconds (faster with level), on any difficulty, until cured (milk, healing)
    public static final Holder<MobEffect> BLEEDING = MOB_EFFECTS.register("bleeding", () -> new TickingMobEffect(MobEffectCategory.HARMFUL, 0x8A0303, 40,
            (level, entity, amplifier) -> entity.hurtServer(level, level.damageSources().generic(), 1.0F)));
    // Abyss on yourself: heals you for 20% a level of the damage you deal
    public static final Holder<MobEffect> LIFE_LEECH = MOB_EFFECTS.register("life_leech", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x3B0A4F));
    // Abyss on a mob: no spells, shields, drinking potions, or teleporting
    public static final Holder<MobEffect> SILENCED = MOB_EFFECTS.register("silenced", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0x1E0A2E));
    // Damage on yourself: magic damage is cut 25% a level, up to 80%
    public static final Holder<MobEffect> MAGIC_WARD = MOB_EFFECTS.register("magic_ward", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0xB04BFF));
    // Energy on yourself: each hit you land gives you a point of mana back
    public static final Holder<MobEffect> ENERGY_DRAIN = MOB_EFFECTS.register("energy_drain", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0xFFE45C));
    // Energy on a mob: charge building up; the level is the stacks (EnergyCharge); at 100 it bursts
    public static final Holder<MobEffect> CHARGED = MOB_EFFECTS.register("charged", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0xFFF27A));
    // Creature on yourself: double damage to what something else has just been fighting
    public static final Holder<MobEffect> PACK_TACTICS = MOB_EFFECTS.register("pack_tactics", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0xC47A3C));
    // Creature on a mob: it takes double damage from everything
    public static final Holder<MobEffect> MARKED = MOB_EFFECTS.register("marked", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0xFF3030));
    // Plants on a mob: slowed 30% a level, and thorns prick it each second it keeps moving
    public static final Holder<MobEffect> TANGLED = MOB_EFFECTS.register("tangled", () -> new TickingMobEffect(MobEffectCategory.HARMFUL, 0x3C8A2E, 20,
            (level, entity, amplifier) -> {
                if (entity.position().distanceToSqr(entity.xo, entity.yo, entity.zo) > 1.0E-4) entity.hurtServer(level, level.damageSources().sweetBerryBush(), 1.0F);
            })
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, id("effect.tangled"), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                    amplifier -> -Math.min(0.9, 0.3 * (amplifier + 1))));
    // Trees on yourself: whatever hits you takes half a heart a level back
    public static final Holder<MobEffect> THORNS = MOB_EFFECTS.register("thorns", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x5A7A2A));
    // Knowledge on yourself: 50% more experience a level from orbs
    public static final Holder<MobEffect> INSIGHT = MOB_EFFECTS.register("insight", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x7FFF7F));
    // Knowledge on a mob: it drops 50% more experience a level when killed
    public static final Holder<MobEffect> ENLIGHTENED = MOB_EFFECTS.register("enlightened", () -> new SpellMobEffect(MobEffectCategory.NEUTRAL, 0xB8FFB8));
    // Fungus on yourself: poison, mining fatigue and slowness can't take hold
    public static final Holder<MobEffect> FUNGAL = MOB_EFFECTS.register("fungal", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x9C6B4E));
    // Death on yourself: withering, drowning and hunger can't touch you
    public static final Holder<MobEffect> DEATHLESS = MOB_EFFECTS.register("deathless", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x2B2B2B));
    // Change on yourself: harmful effects take hold at half strength, and Fractured can't take hold
    public static final Holder<MobEffect> ADAPTIVE = MOB_EFFECTS.register("adaptive", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0xD45CFF));
    // Mud on yourself: slowing ground (soul sand, honey), water and being off the ground don't slow you
    public static final Holder<MobEffect> SURE_FOOTING = MOB_EFFECTS.register("sure_footing", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x6B4A2B)
            .addAttributeModifier(Attributes.MOVEMENT_EFFICIENCY, id("effect.sure_footing.ground"), 1.0, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(Attributes.WATER_MOVEMENT_EFFICIENCY, id("effect.sure_footing.water"), 1.0, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(Attributes.SUBMERGED_MINING_SPEED, id("effect.sure_footing.mining"), 0.8, AttributeModifier.Operation.ADD_VALUE));
    // Gem on yourself: projectiles bounce back at whoever shot them
    public static final Holder<MobEffect> REFLECTING = MOB_EFFECTS.register("reflecting", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x4FE0D0));
    // Protection on a mob: projectiles do 50% more a level to it
    public static final Holder<MobEffect> PIERCED = MOB_EFFECTS.register("pierced", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0xA0A0C0));
    // Fracture on a mob: its max health is lowered 20% a level (up to 80%)
    public static final Holder<MobEffect> FRACTURED = MOB_EFFECTS.register("fractured", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0x9C7A5A)
            .addAttributeModifier(Attributes.MAX_HEALTH, id("effect.fractured"), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                    amplifier -> -Math.min(0.8, 0.2 * (amplifier + 1))));
    // Lightning on a mob: lightning does 50% more a level to it (lightning spells homing on it come later)
    public static final Holder<MobEffect> CONDUCTIVE = MOB_EFFECTS.register("conductive", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0x9FD8FF));

    // Vacuum on yourself: items, experience and creatures within 5 blocks (more with level) are drawn in to you
    public static final Holder<MobEffect> VACUUM = MOB_EFFECTS.register("vacuum", () -> new TickingMobEffect(MobEffectCategory.BENEFICIAL, 0x6A5ACD, 1,
            SpellMobEffects::vacuumTick));
    // Growth on yourself: bigger (a quarter more a level)
    public static final Holder<MobEffect> GROWN = MOB_EFFECTS.register("grown", () -> new SpellMobEffect(MobEffectCategory.NEUTRAL, 0x7FD650)
            .addAttributeModifier(Attributes.SCALE, id("effect.grown"), AttributeModifier.Operation.ADD_VALUE, amplifier -> 0.25 * (amplifier + 1)));
    // Fungus on a mob: it hunts the monsters around it, and won't turn on whoever cast it or theirs
    public static final Holder<MobEffect> ENTHRALLED = MOB_EFFECTS.register("enthralled", () -> new TickingMobEffect(MobEffectCategory.NEUTRAL, 0xB07A50, 20,
            SpellMobEffects::thrallTick));
    // Flesh on a mob: it drops twice the meat, leather and the like when killed
    public static final Holder<MobEffect> BUTCHERED = MOB_EFFECTS.register("butchered", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0xC05050));
    // Gem on a mob: held fast in crystal; when it breaks out it's hurt and left Brittle
    public static final Holder<MobEffect> ENCASED = MOB_EFFECTS.register("encased", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0x4FE0D0)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, id("effect.encased.move"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.JUMP_STRENGTH, id("effect.encased.jump"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    // After breaking out of Encased: kinetic damage does 50% more a level
    public static final Holder<MobEffect> BRITTLE = MOB_EFFECTS.register("brittle", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0x9FE0D8));

    // Tool on yourself: the right tool comes to hand, and a broken one is replaced (ToolSense)
    public static final Holder<MobEffect> TOOL_SENSE = MOB_EFFECTS.register("tool_sense", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0xC0C0C0));

    // ---- movement (Features/Spells/mobeffects/MovementEffects, the mixins) ----
    // Slime: the ground is as slick as ice
    public static final Holder<MobEffect> SLIPPERY = MOB_EFFECTS.register("slippery", () -> new SpellMobEffect(MobEffectCategory.NEUTRAL, 0x7FD67F));
    // Lightning on yourself: double-tap a movement key to dash a couple of blocks that way (client: DashHandler)
    public static final Holder<MobEffect> DASHING = MOB_EFFECTS.register("dashing", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0xBFE8FF));
    // Flight (stronger): glide like with an elytra, without one
    public static final Holder<MobEffect> GLIDING = MOB_EFFECTS.register("gliding", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0xE0F0FF));
    // Flight (strongest): fly as in creative while it lasts
    public static final Holder<MobEffect> SOARING = MOB_EFFECTS.register("soaring", () -> new TickingMobEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF, 10,
            MovementEffects::keepFlight));
    // Spirit on yourself: fly through blocks like a spectator
    public static final Holder<MobEffect> SPIRIT_FORM = MOB_EFFECTS.register("spirit_form", () -> new TickingMobEffect(MobEffectCategory.BENEFICIAL, 0xC8F0FF, 10,
            MovementEffects::keepFlight));
    // Spirit on a mob: a ghost for a while: it can't act or be hurt
    public static final Holder<MobEffect> GHOSTLY = MOB_EFFECTS.register("ghostly", () -> new SpellMobEffect(MobEffectCategory.NEUTRAL, 0xA0E0FF));
    // Soul on a player: their soul is out of their body (Features/Spells/soul/SoulEvents); it must get back before this ends
    public static final Holder<MobEffect> SOUL_SEVERED = MOB_EFFECTS.register("soul_severed", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0x5FD9D9));
    // Magic on yourself: mana regenerates twice as fast (Features/Magic/PlayerMagicHelper.onPlayerTick)
    public static final Holder<MobEffect> MANA_SURGE = MOB_EFFECTS.register("mana_surge", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x9F5FFF));
    // Radiance: fought another player in the last 5 minutes (can't be removed; Features/Spells/radiance/Radiance)
    public static final Holder<MobEffect> HOSTILE = MOB_EFFECTS.register("hostile", () -> new SpellMobEffect(MobEffectCategory.HARMFUL, 0xD03030));
    // Radiance: marked friendly by another player
    public static final Holder<MobEffect> FRIENDLY = MOB_EFFECTS.register("friendly", () -> new SpellMobEffect(MobEffectCategory.BENEFICIAL, 0x40C040));

    private static Identifier id(String path)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
    }

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
