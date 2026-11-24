package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientRuinFeature;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.tags.BlockTags;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;

import java.util.List;

// Worldgen datapack entries. Each feature takes three steps:
//   feature        dust:ancient_ruin  - what to build (AncientRuinFeature)
//   placed feature dust:ancient_ruin  - where: tried once in every chunk, at a random spot on the surface; the feature
//                                      itself decides (DustConfig.RUIN_RARITY: half as common as ore boulders)
//   biome modifier dust:add_ancient_ruins - which biomes get it (all overworld biomes), in the surface structures step
// and the same three for Inert Crystal Ore (about as common as diamond, in every overworld biome; what it becomes is up
// to each chunk's aura: Features/Magic/InertCrystalOreBlock):
//   feature        dust:ore_inert_crystal / ore_inert_crystal_large - a vein replacing stone (or deepslate, with the
//                  deepslate ore)
//   placed feature the same names - diamond's numbers: the small vein 7 times a chunk, the large one in 1 chunk in 9,
//                  on a triangle from 80 below the world's bottom to 80 above it (so mostly deep down)
//   biome modifier dust:add_inert_crystal_ore - all overworld biomes, in the underground ores step
// In 26.3 a configured feature is just a Feature (the old ConfiguredFeature layer is gone), so OreFeature takes its
// targets, vein size and how often blocks touching air are skipped directly.
// They're registered from DustDataGenerator#gatherDataRegistryEntries and written to data/dust/worldgen/... and
// data/dust/neoforge/biome_modifier/.
public class DustWorldgen
{
    public static final ResourceKey<Feature> ANCIENT_RUIN = ResourceKey.create(Registries.FEATURE, id("ancient_ruin"));
    public static final ResourceKey<PlacedFeature> ANCIENT_RUIN_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("ancient_ruin"));
    public static final ResourceKey<BiomeModifier> ADD_ANCIENT_RUINS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_ancient_ruins"));

    public static final ResourceKey<Feature> INERT_ORE = ResourceKey.create(Registries.FEATURE, id("ore_inert_crystal"));
    public static final ResourceKey<Feature> INERT_ORE_LARGE = ResourceKey.create(Registries.FEATURE, id("ore_inert_crystal_large"));
    public static final ResourceKey<PlacedFeature> INERT_ORE_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("ore_inert_crystal"));
    public static final ResourceKey<PlacedFeature> INERT_ORE_LARGE_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("ore_inert_crystal_large"));
    public static final ResourceKey<BiomeModifier> ADD_INERT_ORE = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_inert_crystal_ore"));

    // Element geodes (Features/Worldgen/ElementGeodeFeature): tried once in every chunk at a random height from 6 above
    // the bottom up to 30 (amethyst geodes' range); the feature itself decides whether one forms there (its biome, and
    // DustConfig.GEODE_RARITY)
    public static final ResourceKey<Feature> ELEMENT_GEODE = ResourceKey.create(Registries.FEATURE, id("element_geode"));
    public static final ResourceKey<PlacedFeature> ELEMENT_GEODE_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("element_geode"));
    public static final ResourceKey<BiomeModifier> ADD_ELEMENT_GEODES = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_element_geodes"));

    // Ore boulders (Features/Worldgen/OreBoulderFeature): tried once in every chunk on the surface; the feature decides
    // (DustConfig.BOULDER_RARITY, not in water)
    public static final ResourceKey<Feature> ORE_BOULDER = ResourceKey.create(Registries.FEATURE, id("ore_boulder"));
    public static final ResourceKey<PlacedFeature> ORE_BOULDER_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("ore_boulder"));
    public static final ResourceKey<BiomeModifier> ADD_ORE_BOULDERS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_ore_boulders"));

    // Elemental flowers (Features/Worldgen/ElementalFlowerFeature): tried once in every chunk on the surface
    public static final ResourceKey<Feature> ELEMENTAL_FLOWERS = ResourceKey.create(Registries.FEATURE, id("elemental_flowers"));
    public static final ResourceKey<PlacedFeature> ELEMENTAL_FLOWERS_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("elemental_flowers"));
    public static final ResourceKey<BiomeModifier> ADD_ELEMENTAL_FLOWERS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_elemental_flowers"));

    // Elemental trees (Features/Worldgen/ElementalTreeFeature): tried once in every chunk on the ground (under any other
    // trees' leaves); the feature decides (DustConfig.TREE_RARITY, the woods of the biome) and grows that wood's tree
    // (dust:<wood>_tree, below), the one its sapling grows
    public static final ResourceKey<Feature> ELEMENTAL_TREES = ResourceKey.create(Registries.FEATURE, id("elemental_trees"));
    public static final ResourceKey<PlacedFeature> ELEMENTAL_TREES_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("elemental_trees"));
    public static final ResourceKey<BiomeModifier> ADD_ELEMENTAL_TREES = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_elemental_trees"));

    private static Identifier id(String path)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
    }

    public static void features(BootstrapContext<Feature> context)
    {
        context.register(ANCIENT_RUIN, AncientRuinFeature.INSTANCE);
        // Stone becomes the ore, deepslate the deepslate ore
        List<BlockReplacement> targets = List.of(
                BlockReplacement.replace(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), DustBlockRegistry.INERT_CRYSTAL_ORE.get().defaultBlockState()),
                BlockReplacement.replace(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.get().defaultBlockState()));
        context.register(INERT_ORE, new OreFeature(targets, 4, 0.5F));
        context.register(INERT_ORE_LARGE, new OreFeature(targets, 12, 0.7F));
        context.register(ELEMENT_GEODE, com.mowmaster.dust.Features.Worldgen.ElementGeodeFeature.INSTANCE);
        context.register(ORE_BOULDER, com.mowmaster.dust.Features.Worldgen.OreBoulderFeature.INSTANCE);
        context.register(ELEMENTAL_FLOWERS, com.mowmaster.dust.Features.Worldgen.ElementalFlowerFeature.INSTANCE);
        context.register(ELEMENTAL_TREES, com.mowmaster.dust.Features.Worldgen.ElementalTreeFeature.INSTANCE);
        trees(context);
    }

    // Each elemental wood's tree (ElementalTrees.treeKey: dust:<wood>_tree): its logs and leaves in the shape of a
    // vanilla tree (TreeShape), with vanilla's numbers for that shape (TreeFeatures), dirt put under the trunk as
    // vanilla's trees do, and the wood's magical fruit (ElementalFruitDecorator). 26.1.2's copies (the same trees as
    // configured features) are in src/main/resources-26.1.2/data/dust/worldgen/configured_feature/<wood>_tree.json.
    private static void trees(BootstrapContext<Feature> context)
    {
        net.minecraft.core.Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> soil =
                context.lookup(Registries.BLOCK_STATE_PROVIDER).getOrThrow(net.minecraft.data.worldgen.BlockStateProviders.SOIL_BENEATH_TREE);
        for (com.mowmaster.dust.Features.Trees.WoodSet set : com.mowmaster.dust.Features.Trees.ElementalWoods.all()) {
            context.register(com.mowmaster.dust.Features.Trees.ElementalTrees.treeKey(set.type()), tree(set, soil));
        }
    }

    private static net.minecraft.world.level.levelgen.feature.TreeFeature tree(com.mowmaster.dust.Features.Trees.WoodSet set,
            net.minecraft.core.Holder<net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider> soil)
    {
        var log = net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.of(set.log().get());
        var leaves = net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.of(set.leaves().get());
        net.minecraft.world.level.levelgen.feature.TreeFeature.Builder builder = switch (set.type().shape) {
            case OAK -> new net.minecraft.world.level.levelgen.feature.TreeFeature.Builder(log,
                    new net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer(5, 2, 0), leaves,
                    new net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(1, 0, 1), soil);
            case TALL -> new net.minecraft.world.level.levelgen.feature.TreeFeature.Builder(log,
                    new net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer(6, 3, 0), leaves,
                    new net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(1, 0, 1), soil);
            case FANCY -> new net.minecraft.world.level.levelgen.feature.TreeFeature.Builder(log,
                    new net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer(3, 11, 0), leaves,
                    new net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                    new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(0, 0, 0, java.util.OptionalInt.of(4)), soil);
            case ACACIA -> new net.minecraft.world.level.levelgen.feature.TreeFeature.Builder(log,
                    new net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer(5, 2, 2), leaves,
                    new net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                    new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(1, 0, 2), soil);
            case SPRUCE -> new net.minecraft.world.level.levelgen.feature.TreeFeature.Builder(log,
                    new net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer(5, 2, 1), leaves,
                    new net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)),
                    new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(2, 0, 2), soil);
            case PINE -> new net.minecraft.world.level.levelgen.feature.TreeFeature.Builder(log,
                    new net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer(6, 4, 0), leaves,
                    new net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer(ConstantInt.of(1), ConstantInt.of(1), UniformInt.of(3, 4)),
                    new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(2, 0, 2), soil);
            case AZALEA -> new net.minecraft.world.level.levelgen.feature.TreeFeature.Builder(log,
                    new net.minecraft.world.level.levelgen.feature.trunkplacers.BendingTrunkPlacer(4, 2, 0, 3, UniformInt.of(1, 2)), leaves,
                    new net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 50),
                    new net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize(1, 0, 1), soil);
        };
        return builder.ignoreVines().decorators(List.of(new com.mowmaster.dust.Features.Trees.ElementalFruitDecorator(set.type()))).build();
    }

    public static void placedFeatures(BootstrapContext<PlacedFeature> context)
    {
        context.register(ANCIENT_RUIN_PLACED, new PlacedFeature(
                context.lookup(Registries.FEATURE).getOrThrow(ANCIENT_RUIN),
                List.of(InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome())));
        var features = context.lookup(Registries.FEATURE);
        HeightRangePlacement depth = HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80));
        context.register(INERT_ORE_PLACED, new PlacedFeature(features.getOrThrow(INERT_ORE),
                List.of(CountPlacement.of(7), InSquarePlacement.spread(), depth, BiomeFilter.biome())));
        context.register(INERT_ORE_LARGE_PLACED, new PlacedFeature(features.getOrThrow(INERT_ORE_LARGE),
                List.of(RarityFilter.onAverageOnceEvery(9), InSquarePlacement.spread(), depth, BiomeFilter.biome())));
        context.register(ELEMENT_GEODE_PLACED, new PlacedFeature(features.getOrThrow(ELEMENT_GEODE),
                List.of(InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)),
                        BiomeFilter.biome())));
        context.register(ORE_BOULDER_PLACED, new PlacedFeature(features.getOrThrow(ORE_BOULDER),
                List.of(InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome())));
        context.register(ELEMENTAL_FLOWERS_PLACED, new PlacedFeature(features.getOrThrow(ELEMENTAL_FLOWERS),
                List.of(InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
        context.register(ELEMENTAL_TREES_PLACED, new PlacedFeature(features.getOrThrow(ELEMENTAL_TREES),
                List.of(InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())));
    }

    public static void biomeModifiers(BootstrapContext<BiomeModifier> context)
    {
        context.register(ADD_ANCIENT_RUINS, new BiomeModifiers.AddFeaturesBiomeModifier(
                context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(context.lookup(Registries.PLACED_FEATURE).getOrThrow(ANCIENT_RUIN_PLACED)),
                GenerationStep.Decoration.SURFACE_STRUCTURES));
        var placed = context.lookup(Registries.PLACED_FEATURE);
        context.register(ADD_INERT_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placed.getOrThrow(INERT_ORE_PLACED), placed.getOrThrow(INERT_ORE_LARGE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_ELEMENT_GEODES, new BiomeModifiers.AddFeaturesBiomeModifier(
                context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placed.getOrThrow(ELEMENT_GEODE_PLACED)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_ORE_BOULDERS, new BiomeModifiers.AddFeaturesBiomeModifier(
                context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placed.getOrThrow(ORE_BOULDER_PLACED)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        context.register(ADD_ELEMENTAL_FLOWERS, new BiomeModifiers.AddFeaturesBiomeModifier(
                context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placed.getOrThrow(ELEMENTAL_FLOWERS_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_ELEMENTAL_TREES, new BiomeModifiers.AddFeaturesBiomeModifier(
                context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placed.getOrThrow(ELEMENTAL_TREES_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
    }
}
