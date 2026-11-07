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
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

// Worldgen datapack entries, three steps for one feature:
//   feature        dust:ancient_ruin  - what to build (AncientRuinFeature)
//   placed feature dust:ancient_ruin  - where: about once every RARITY chunks, at a random spot, on the surface
//   biome modifier dust:add_ancient_ruins - which biomes get it (all overworld biomes), in the surface structures step
// They're registered from DustDataGenerator#gatherDataRegistryEntries and written to data/dust/worldgen/... and
// data/dust/neoforge/biome_modifier/.
public class DustWorldgen
{
    // On average one ruin per this many chunks
    public static final int RARITY = 64;

    public static final ResourceKey<Feature> ANCIENT_RUIN = ResourceKey.create(Registries.FEATURE, id("ancient_ruin"));
    public static final ResourceKey<PlacedFeature> ANCIENT_RUIN_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, id("ancient_ruin"));
    public static final ResourceKey<BiomeModifier> ADD_ANCIENT_RUINS = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, id("add_ancient_ruins"));

    private static Identifier id(String path)
    {
        return Identifier.fromNamespaceAndPath(DustReferences.MODID, path);
    }

    public static void features(BootstrapContext<Feature> context)
    {
        context.register(ANCIENT_RUIN, AncientRuinFeature.INSTANCE);
    }

    public static void placedFeatures(BootstrapContext<PlacedFeature> context)
    {
        context.register(ANCIENT_RUIN_PLACED, new PlacedFeature(
                context.lookup(Registries.FEATURE).getOrThrow(ANCIENT_RUIN),
                List.of(RarityFilter.onAverageOnceEvery(RARITY),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome())));
    }

    public static void biomeModifiers(BootstrapContext<BiomeModifier> context)
    {
        context.register(ADD_ANCIENT_RUINS, new BiomeModifiers.AddFeaturesBiomeModifier(
                context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(context.lookup(Registries.PLACED_FEATURE).getOrThrow(ANCIENT_RUIN_PLACED)),
                GenerationStep.Decoration.SURFACE_STRUCTURES));
    }
}
