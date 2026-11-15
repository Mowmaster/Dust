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
// Stonecutter: before 26.3 the Feature object itself is registered (registry "feature"), and the datapack entry is a
// "configured_feature": hand-written copies in src/main/resources-26.1.2/data/dust/worldgen/configured_feature
// (only the 26.1.2 build includes that folder; 26.1.2 ignores 26.3's worldgen/feature). Keep them matching DustWorldgen.
public class DustFeatureRegistry
{
    //? if >=26.3 {
    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURE_TYPES =
            DeferredRegister.create(Registries.FEATURE_TYPE, DustReferences.MODID);

    public static final Supplier<MapCodec<AncientRuinFeature>> ANCIENT_RUIN = FEATURE_TYPES.register("ancient_ruin", () -> AncientRuinFeature.CODEC);
    //?} else {
    /*public static final DeferredRegister<Feature<?>> FEATURE_TYPES =
            DeferredRegister.create(Registries.FEATURE, DustReferences.MODID);

    public static final Supplier<AncientRuinFeature> ANCIENT_RUIN = FEATURE_TYPES.register("ancient_ruin", AncientRuinFeature::new);
    *///?}

    public static void register(IEventBus eventBus)
    {
        FEATURE_TYPES.register(eventBus);
    }
}
