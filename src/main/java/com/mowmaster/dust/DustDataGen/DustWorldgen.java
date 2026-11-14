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

import java.util.List;

// Worldgen datapack entries. Each feature takes three steps:
//   feature        dust:ancient_ruin  - what to build (AncientRuinFeature)
//   placed feature dust:ancient_ruin  - where: about once every RARITY chunks, at a random spot, on the surface
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
    // On average one ruin per this many chunks
    public static final int RARITY = 64;

    public static final ResourceKey<Feature> ANCIENT_RUIN = ResourceKey.create(Registries.FEATURE, id("ancient_ruin"));
    public static final ResourceKey<PlacedFeature> ANCIENT_RUIN_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("ancient_ruin"));
    public static final ResourceKey<BiomeModifier> ADD_ANCIENT_RUINS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_ancient_ruins"));

    public static final ResourceKey<Feature> INERT_ORE = ResourceKey.create(Registries.FEATURE, id("ore_inert_crystal"));
    public static final ResourceKey<Feature> INERT_ORE_LARGE = ResourceKey.create(Registries.FEATURE, id("ore_inert_crystal_large"));
    public static final ResourceKey<PlacedFeature> INERT_ORE_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("ore_inert_crystal"));
    public static final ResourceKey<PlacedFeature> INERT_ORE_LARGE_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("ore_inert_crystal_large"));
    public static final ResourceKey<BiomeModifier> ADD_INERT_ORE = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_inert_crystal_ore"));

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
    }

    public static void placedFeatures(BootstrapContext<PlacedFeature> context)
    {
        context.register(ANCIENT_RUIN_PLACED, new PlacedFeature(
                context.lookup(Registries.FEATURE).getOrThrow(ANCIENT_RUIN),
                List.of(RarityFilter.onAverageOnceEvery(RARITY),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome())));
        var features = context.lookup(Registries.FEATURE);
        HeightRangePlacement depth = HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80));
        context.register(INERT_ORE_PLACED, new PlacedFeature(features.getOrThrow(INERT_ORE),
                List.of(CountPlacement.of(7), InSquarePlacement.spread(), depth, BiomeFilter.biome())));
        context.register(INERT_ORE_LARGE_PLACED, new PlacedFeature(features.getOrThrow(INERT_ORE_LARGE),
                List.of(RarityFilter.onAverageOnceEvery(9), InSquarePlacement.spread(), depth, BiomeFilter.biome())));
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
    }
}
