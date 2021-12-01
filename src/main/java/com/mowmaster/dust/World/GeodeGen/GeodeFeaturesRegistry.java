package com.mowmaster.dust.World.GeodeGen;

import com.google.common.collect.ImmutableList;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.References.ColorReference;
//import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class GeodeFeaturesRegistry
{
    protected final String NAME;
    protected final BlockState AIR;
    protected final BlockState CALCITE;
    protected final BlockState SMOOTH_BASALT;
    protected final BlockState BLOCK;
    protected final BlockState BUDDING;
    protected final ConfiguredFeature<?, ?> GEODE;
    protected final Supplier<Integer> RARITY;

    public String getName() {
        return NAME;
    }

    public PlacedFeature getPlacedFeature() {
        return GEODE.placed(RarityFilter.onAverageOnceEvery(RARITY.get()), GeodeFeatures.RNG_DECORATOR, InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)), BiomeFilter.biome());
    }

    /*public ConfiguredFeature<?, ?> getGeodeFeature() {
        return GEODE;
    }*/

    public GeodeFeaturesRegistry(String name, int color, Supplier<Integer> rarityConfig) {
        NAME = name;
        AIR = Blocks.AIR.defaultBlockState();
        CALCITE = Blocks.CALCITE.defaultBlockState();
        SMOOTH_BASALT = Blocks.SMOOTH_BASALT.defaultBlockState();
        RARITY = rarityConfig;

        int colored = (color==-1)?(ColorReference.getColor(ColorReference.getIntColor(-1))):(color);

        BLOCK = ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_BLOCK.get().defaultBlockState(),colored);
        BUDDING = ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_NODE.get().defaultBlockState(),colored);

        GEODE = FeatureUtils.register(name + "_geode", Feature.GEODE.configured(new GeodeConfiguration(
                new GeodeBlockSettings(BlockStateProvider.simple(Blocks.AIR), SimpleStateProvider.simple(BLOCK),
                        SimpleStateProvider.simple(BUDDING), BlockStateProvider.simple(Blocks.CALCITE),
                        SimpleStateProvider.simple(Blocks.SMOOTH_BASALT),
                        List.of(ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get().defaultBlockState(),colored),
                                ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get().defaultBlockState(),colored),
                                ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get().defaultBlockState(),colored),
                                ColorReference.addColorToBlockState(DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get().defaultBlockState(),colored)),
                        BlockTags.FEATURES_CANNOT_REPLACE.getName(), BlockTags.GEODE_INVALID_BLOCKS.getName()),
                new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackSettings(0.95D, 2.0D, 2), 0.35D, 0.083D, true,
                UniformInt.of(4, 6), UniformInt.of(3, 4),
                UniformInt.of(1, 2), -16, 16, 0.05D, 1)));

    }
}
