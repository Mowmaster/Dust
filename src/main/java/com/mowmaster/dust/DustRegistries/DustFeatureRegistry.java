package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientRuinFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

// 26.3: a worldgen feature TYPE is just its codec (registry "feature_type"). The feature itself (with any settings)
// is a datapack entry made in datagen (DustWorldgen).
public class DustFeatureRegistry
{
    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURE_TYPES =
            DeferredRegister.create(Registries.FEATURE_TYPE, DustReferences.MODID);

    public static final Supplier<MapCodec<AncientRuinFeature>> ANCIENT_RUIN = FEATURE_TYPES.register("ancient_ruin", () -> AncientRuinFeature.CODEC);

    public static void register(IEventBus eventBus)
    {
        FEATURE_TYPES.register(eventBus);
    }
}
