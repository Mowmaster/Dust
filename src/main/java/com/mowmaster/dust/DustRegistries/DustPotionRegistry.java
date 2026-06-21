package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.EffectFireScorch;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.EffectVoidMagnet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DustPotionRegistry
{
    public static final DeferredRegister<Potion> DUST_POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, DustReferences.MODID);

    public static final Holder<Potion> POTION_SCORCH =
            DUST_POTIONS.register("potion_scorch",
                    () -> new Potion("potion_scorch", new MobEffectInstance(DustEffectsRegistry.EFFECT_SCORCH, 1, 20)));

    public static void register(IEventBus eventBus)
    {
        DUST_POTIONS.register(eventBus);
    }
}
