package com.mowmaster.dust.Features.Worldgen;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.Features.Trees.ElementalTrees;
import com.mowmaster.dust.Features.Trees.ElementalWood;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
//? if <26.3 {
/*import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
*///?}

import java.util.ArrayList;
import java.util.List;

// An elemental tree in the wild (Features/Trees): one of the woods that grow in the biome here (ElementalWood.biomes),
// grown from its own tree entry (ElementalTrees: the same tree its sapling grows, fruit included), where its sapling could
// stand. Tried on the surface of every overworld chunk (DustWorldgen); a tree grows in about one chunk in
// DustConfig.TREE_RARITY of those where some elemental wood grows. Never in water.
//? if >=26.3 {
public record ElementalTreeFeature() implements Feature
{
    public static final ElementalTreeFeature INSTANCE = new ElementalTreeFeature();
    public static final MapCodec<ElementalTreeFeature> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<ElementalTreeFeature> codec()
    {
        return CODEC;
    }
//?} else {
/*public class ElementalTreeFeature extends Feature<NoneFeatureConfiguration>
{
    public ElementalTreeFeature()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        return place(context.level(), context.chunkGenerator(), context.random(), context.origin());
    }
*///?}

    //? if >=26.3
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin)
    {
        if (random.nextInt(DustConfig.rarity(DustConfig.TREE_RARITY)) != 0) return false;
        if (!level.getFluidState(origin).isEmpty() || !level.getBlockState(origin).canBeReplaced()) return false;
        Holder<Biome> biome = level.getBiome(origin);
        List<ElementalWood> choices = new ArrayList<>();
        for (ElementalWood wood : ElementalWood.values()) if (wood.growsIn(biome)) choices.add(wood);
        if (choices.isEmpty()) return false;
        ElementalWood wood = choices.get(random.nextInt(choices.size()));
        BlockState sapling = ElementalWoods.set(wood).sapling().get().defaultBlockState();
        if (!sapling.canSurvive(level, origin)) return false;
        //? if >=26.3 {
        return level.registryAccess().lookupOrThrow(Registries.FEATURE).get(ElementalTrees.treeKey(wood))
                .map(tree -> tree.value().place(level, chunkGenerator, random, origin)).orElse(false);
        //?} else {
        /*return level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(ElementalTrees.treeKey(wood))
                .map(tree -> tree.value().place(level, chunkGenerator, random, origin)).orElse(false);
        *///?}
    }
}
