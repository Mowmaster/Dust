package com.mowmaster.dust.Features.Worldgen;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.Features.Crystals.CrystalSize;
import com.mowmaster.dust.Features.Crystals.ElementCrystals;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
//? if >=26.3 {
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
//?} else {
/*import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
*///?}

import java.util.List;

// An element geode: vanilla's amethyst geode (calcite and smooth basalt round a hollow), lined with an element's Crystal
// Blocks, some of them Budding Crystals with that element's crystals growing off them (Features/Crystals). The element
// comes from the biome where it forms (ElementGroup: the family of the primals the biome is rich in; a biome rich in
// none gets no geode). Placed underground in every overworld chunk (DustWorldgen), it only forms in about one chunk in
// DustConfig.GEODE_RARITY (48 by default: half as often as amethyst geodes).
// The geode's numbers are vanilla's amethyst geode's.
// Versions: 26.3's geode is a record taking its settings directly, with holders for its block providers and holder
// sets for its block tags; 26.1.2's is Feature.GEODE given a GeodeConfiguration of plain providers and tag keys, in a
// FeaturePlaceContext. Like AncientRuinFeature, the 26.1.2 class's place(context) hands over to place(level, ...).
//? if >=26.3 {
public record ElementGeodeFeature() implements Feature
{
    public static final ElementGeodeFeature INSTANCE = new ElementGeodeFeature();
    public static final MapCodec<ElementGeodeFeature> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<ElementGeodeFeature> codec()
    {
        return CODEC;
    }
//?} else {
/*public class ElementGeodeFeature extends Feature<NoneFeatureConfiguration>
{
    public ElementGeodeFeature()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        return place(context.level(), context.chunkGenerator(), context.random(), context.origin());
    }
*///?}

    private static final GeodeLayerSettings LAYERS = new GeodeLayerSettings(1.7, 2.2, 3.2, 4.2);
    private static final GeodeCrackSettings CRACK = new GeodeCrackSettings(0.95, 2.0, 2);

    //? if >=26.3
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin)
    {
        if (random.nextInt(DustConfig.rarity(DustConfig.GEODE_RARITY)) != 0) return false;
        ElementGroup group = ElementGroup.pick(level.getBiome(origin), random);
        if (group == null) return false;
        DustElement element = group.pickElement(random);
        BlockState lining = ElementCrystals.BLOCKS.get(element).get().defaultBlockState();
        BlockState budding = ElementCrystals.BUDDING.get(element).get().defaultBlockState();
        List<BlockState> crystals = List.of(
                ElementCrystals.node(element, CrystalSize.SMALL).defaultBlockState().setValue(AmethystClusterBlock.FACING, net.minecraft.core.Direction.UP),
                ElementCrystals.node(element, CrystalSize.MEDIUM).defaultBlockState().setValue(AmethystClusterBlock.FACING, net.minecraft.core.Direction.UP),
                ElementCrystals.node(element, CrystalSize.LARGE).defaultBlockState().setValue(AmethystClusterBlock.FACING, net.minecraft.core.Direction.UP));
        //? if >=26.3 {
        var blocks = level.registryAccess().lookupOrThrow(Registries.BLOCK);
        GeodeBlockSettings settings = new GeodeBlockSettings(
                BlockStateProvider.holderOf(Blocks.AIR.defaultBlockState()),
                BlockStateProvider.holderOf(lining),
                BlockStateProvider.holderOf(budding),
                BlockStateProvider.holderOf(Blocks.CALCITE.defaultBlockState()),
                BlockStateProvider.holderOf(Blocks.SMOOTH_BASALT.defaultBlockState()),
                crystals,
                blocks.getOrThrow(BlockTags.FEATURES_CANNOT_REPLACE),
                blocks.getOrThrow(BlockTags.GEODE_INVALID_BLOCKS));
        return new GeodeFeature(settings, LAYERS, CRACK, 0.35, 0.083, true, UniformInt.of(4, 6), UniformInt.of(3, 4), UniformInt.of(1, 2),
                -16, 16, 0.05, 1).place(level, chunkGenerator, random, origin);
        //?} else {
        /*GeodeBlockSettings settings = new GeodeBlockSettings(
                BlockStateProvider.simple(Blocks.AIR),
                BlockStateProvider.simple(lining),
                BlockStateProvider.simple(budding),
                BlockStateProvider.simple(Blocks.CALCITE),
                BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
                crystals,
                BlockTags.FEATURES_CANNOT_REPLACE,
                BlockTags.GEODE_INVALID_BLOCKS);
        GeodeConfiguration config = new GeodeConfiguration(settings, LAYERS, CRACK, 0.35, 0.083, true, UniformInt.of(4, 6), UniformInt.of(3, 4),
                UniformInt.of(1, 2), -16, 16, 0.05, 1);
        return Feature.GEODE.place(new FeaturePlaceContext<>(java.util.Optional.empty(), level, chunkGenerator, random, origin, config));
        *///?}
    }
}
