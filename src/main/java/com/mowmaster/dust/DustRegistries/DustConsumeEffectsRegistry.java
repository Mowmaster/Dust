package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.EffectAuraReplentish;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DustConsumeEffectsRegistry
{
    public static final DeferredRegister<ConsumeEffect.Type<?>> CONSUME_EFFECT_TYPES =
            DeferredRegister.create(BuiltInRegistries.CONSUME_EFFECT_TYPE, DustReferences.MODID);

    public static final Supplier<ConsumeEffect.Type<EffectAuraReplentish>> AURA_REPLENTISH_CONSUME_EFFECT =
            CONSUME_EFFECT_TYPES.register("replentish_aura", () -> new ConsumeEffect.Type<>(EffectAuraReplentish.CODEC, EffectAuraReplentish.STREAM_CODEC));


    public static void register(IEventBus eventBus)
    {
        CONSUME_EFFECT_TYPES.register(eventBus);
    }
}
