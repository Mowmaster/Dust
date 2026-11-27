package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientRuinFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
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
    *///?}

    // Tree decorators (the same on both versions): magical fruit on the elemental trees (Features/Trees)
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, DustReferences.MODID);

    public static final Supplier<TreeDecoratorType<com.mowmaster.dust.Features.Trees.ElementalFruitDecorator>> ELEMENTAL_FRUIT =
            TREE_DECORATORS.register("elemental_fruit", () -> new TreeDecoratorType<>(com.mowmaster.dust.Features.Trees.ElementalFruitDecorator.CODEC));

    // Structures (the same on both versions): the sanctums (Features/Sanctums), a structure type and the one piece each
    // is built from (a structure template: SanctumPiece). The structure and its structure set are datapack entries made
    // in datagen (DustWorldgen).
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, DustReferences.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, DustReferences.MODID);

    public static final Supplier<StructureType<com.mowmaster.dust.Features.Sanctums.SanctumStructure>> SANCTUM_STRUCTURE =
            STRUCTURE_TYPES.register("sanctum", () -> () -> com.mowmaster.dust.Features.Sanctums.SanctumStructure.CODEC);
    public static final Supplier<StructurePieceType> SANCTUM_PIECE =
            STRUCTURE_PIECES.register("sanctum", () -> com.mowmaster.dust.Features.Sanctums.SanctumPiece::new);

    // Structure processors: the sanctum templates' stand-in blocks swapped for an element's (SanctumProcessor)
    // Stonecutter: 26.3 registers a processor's codec itself; before, a StructureProcessorType wrapping it
    //? if >=26.3 {
    public static final DeferredRegister<MapCodec<? extends net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor>> STRUCTURE_PROCESSORS =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, DustReferences.MODID);
    public static final Supplier<MapCodec<com.mowmaster.dust.Features.Sanctums.SanctumProcessor>> SANCTUM_PROCESSOR =
            STRUCTURE_PROCESSORS.register("sanctum", () -> com.mowmaster.dust.Features.Sanctums.SanctumProcessor.CODEC);
    //?} else {
    /*public static final DeferredRegister<net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType<?>> STRUCTURE_PROCESSORS =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, DustReferences.MODID);
    public static final Supplier<net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType<com.mowmaster.dust.Features.Sanctums.SanctumProcessor>> SANCTUM_PROCESSOR =
            STRUCTURE_PROCESSORS.register("sanctum", () -> () -> com.mowmaster.dust.Features.Sanctums.SanctumProcessor.CODEC);
    *///?}

    public static void register(IEventBus eventBus)
    {
        FEATURE_TYPES.register(eventBus);
        TREE_DECORATORS.register(eventBus);
        STRUCTURE_TYPES.register(eventBus);
        STRUCTURE_PIECES.register(eventBus);
        STRUCTURE_PROCESSORS.register(eventBus);
    }
}
