package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientRuinFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
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
    public static final Supplier<MapCodec<com.mowmaster.dust.Features.Worldgen.ElementGeodeFeature>> ELEMENT_GEODE =
            FEATURE_TYPES.register("element_geode", () -> com.mowmaster.dust.Features.Worldgen.ElementGeodeFeature.CODEC);
    public static final Supplier<MapCodec<com.mowmaster.dust.Features.Worldgen.OreBoulderFeature>> ORE_BOULDER =
            FEATURE_TYPES.register("ore_boulder", () -> com.mowmaster.dust.Features.Worldgen.OreBoulderFeature.CODEC);
    public static final Supplier<MapCodec<com.mowmaster.dust.Features.Worldgen.ElementalFlowerFeature>> ELEMENTAL_FLOWERS =
            FEATURE_TYPES.register("elemental_flowers", () -> com.mowmaster.dust.Features.Worldgen.ElementalFlowerFeature.CODEC);
    public static final Supplier<MapCodec<com.mowmaster.dust.Features.Worldgen.ElementalTreeFeature>> ELEMENTAL_TREES =
            FEATURE_TYPES.register("elemental_trees", () -> com.mowmaster.dust.Features.Worldgen.ElementalTreeFeature.CODEC);
    public static final Supplier<MapCodec<com.mowmaster.dust.Features.Sanctums.SanctumFeature>> SANCTUM =
            FEATURE_TYPES.register("sanctum", () -> com.mowmaster.dust.Features.Sanctums.SanctumFeature.CODEC);
    //?} else {
    /*public static final DeferredRegister<Feature<?>> FEATURE_TYPES =
            DeferredRegister.create(Registries.FEATURE, DustReferences.MODID);

    public static final Supplier<AncientRuinFeature> ANCIENT_RUIN = FEATURE_TYPES.register("ancient_ruin", AncientRuinFeature::new);
    public static final Supplier<com.mowmaster.dust.Features.Worldgen.ElementGeodeFeature> ELEMENT_GEODE =
            FEATURE_TYPES.register("element_geode", com.mowmaster.dust.Features.Worldgen.ElementGeodeFeature::new);
    public static final Supplier<com.mowmaster.dust.Features.Worldgen.OreBoulderFeature> ORE_BOULDER =
            FEATURE_TYPES.register("ore_boulder", com.mowmaster.dust.Features.Worldgen.OreBoulderFeature::new);
    public static final Supplier<com.mowmaster.dust.Features.Worldgen.ElementalFlowerFeature> ELEMENTAL_FLOWERS =
            FEATURE_TYPES.register("elemental_flowers", com.mowmaster.dust.Features.Worldgen.ElementalFlowerFeature::new);
    public static final Supplier<com.mowmaster.dust.Features.Worldgen.ElementalTreeFeature> ELEMENTAL_TREES =
            FEATURE_TYPES.register("elemental_trees", com.mowmaster.dust.Features.Worldgen.ElementalTreeFeature::new);
    public static final Supplier<com.mowmaster.dust.Features.Sanctums.SanctumFeature> SANCTUM =
            FEATURE_TYPES.register("sanctum", com.mowmaster.dust.Features.Sanctums.SanctumFeature::new);
    *///?}

    // Tree decorators (the same on both versions): magical fruit on the elemental trees (Features/Trees)
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, DustReferences.MODID);

    public static final Supplier<TreeDecoratorType<com.mowmaster.dust.Features.Trees.ElementalFruitDecorator>> ELEMENTAL_FRUIT =
            TREE_DECORATORS.register("elemental_fruit", () -> new TreeDecoratorType<>(com.mowmaster.dust.Features.Trees.ElementalFruitDecorator.CODEC));

    public static void register(IEventBus eventBus)
    {
        FEATURE_TYPES.register(eventBus);
        TREE_DECORATORS.register(eventBus);
    }
}
