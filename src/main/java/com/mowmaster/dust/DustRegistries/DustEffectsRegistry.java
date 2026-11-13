package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.Features.Spells.mobeffects.SpellMobEffect;
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

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
