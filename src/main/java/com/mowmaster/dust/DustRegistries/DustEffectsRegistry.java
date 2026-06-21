package com.mowmaster.dust.DustRegistries;

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


    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
