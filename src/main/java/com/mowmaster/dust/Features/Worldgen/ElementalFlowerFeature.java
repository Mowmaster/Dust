package com.mowmaster.dust.Features.Worldgen;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.Features.Flora.ElementalFlower;
import com.mowmaster.dust.Features.Flora.ElementalFlowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
//? if <26.3 {
/*import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
*///?}

import java.util.ArrayList;
import java.util.List;

// A patch of elemental flowers (Features/Flora): one kind, picked from those that grow in the biome here
// (ElementalFlower.biomes), PATCH_MIN to PATCH_MAX of them scattered over the ground round the spot. Placed on the
// surface of every overworld chunk (DustWorldgen); a patch forms in about one chunk in DustConfig.FLOWER_RARITY, and
// only where some flower grows.
//? if >=26.3 {
public record ElementalFlowerFeature() implements Feature
{
    public static final ElementalFlowerFeature INSTANCE = new ElementalFlowerFeature();
    public static final MapCodec<ElementalFlowerFeature> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<ElementalFlowerFeature> codec()
    {
        return CODEC;
    }
//?} else {
/*public class ElementalFlowerFeature extends Feature<NoneFeatureConfiguration>
{
    public ElementalFlowerFeature()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        return place(context.level(), context.chunkGenerator(), context.random(), context.origin());
    }
*///?}

    private static final int PATCH_MIN = 3;
    private static final int PATCH_MAX = 7;
    private static final int SPREAD = 3;

    //? if >=26.3
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin)
    {
        if (random.nextInt(DustConfig.rarity(DustConfig.FLOWER_RARITY)) != 0) return false;
        Holder<Biome> biome = level.getBiome(origin);
        List<ElementalFlower> choices = new ArrayList<>();
        for (ElementalFlower flower : ElementalFlower.values()) if (flower.growsIn(biome)) choices.add(flower);
        if (choices.isEmpty()) return false;
        BlockState state = ElementalFlowers.FLOWERS.get(choices.get(random.nextInt(choices.size()))).get().defaultBlockState();
        int wanted = PATCH_MIN + random.nextInt(PATCH_MAX - PATCH_MIN + 1);
        int placed = 0;
        for (int tries = 0; tries < wanted * 4 && placed < wanted; tries++) {
            int x = origin.getX() + random.nextInt(SPREAD * 2 + 1) - SPREAD, z = origin.getZ() + random.nextInt(SPREAD * 2 + 1) - SPREAD;
            BlockPos at = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
            if (!level.getBlockState(at).isAir() || !state.canSurvive(level, at)) continue;
            level.setBlock(at, state, Block.UPDATE_CLIENTS);
            placed++;
        }
        return placed > 0;
    }
}
