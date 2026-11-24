package com.mowmaster.dust.Features.Worldgen;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustConfig;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Magic.BiomeElements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.common.Tags;
//? if <26.3 {
/*import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
*///?}

import java.util.ArrayList;
import java.util.List;

// An ore boulder: a lump of stone up to 5 x 5 x 5 lying on the surface, half sunk into the ground, studded with the ores
// of the land it lies in and at least one of Dust's crystal ores (the primal ore of the primal the biome is richest in,
// BiomeElements; inert crystal ore where it's rich in none). Placed in every overworld chunk (DustWorldgen); one forms
// in about one chunk in DustConfig.BOULDER_RARITY (64 by default: about one in a small biome, 4 in a medium one, 16 in
// a large one). Never in water.
//   desert, beach   sandstone; gold, copper, redstone
//   badlands        terracotta and red sandstone; gold, redstone, iron
//   savanna         granite; copper, iron, gold
//   snowy           stone and packed ice; iron, lapis, a rare diamond
//   mountains       stone and andesite; iron, emerald, coal
//   swamp, jungle,  mossy cobblestone; copper, lapis, coal
//   lush, mushroom
//   spooky          cobbled deepslate; coal, redstone, lapis
//   everywhere else stone, andesite, diorite; coal, iron, copper
//? if >=26.3 {
public record OreBoulderFeature() implements Feature
{
    public static final OreBoulderFeature INSTANCE = new OreBoulderFeature();
    public static final MapCodec<OreBoulderFeature> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<OreBoulderFeature> codec()
    {
        return CODEC;
    }
//?} else {
/*public class OreBoulderFeature extends Feature<NoneFeatureConfiguration>
{
    public OreBoulderFeature()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        return place(context.level(), context.chunkGenerator(), context.random(), context.origin());
    }
*///?}

    // The share of a boulder's blocks that are ores
    private static final float ORE_SHARE = 0.16F;

    private record Palette(List<BlockState> stones, List<BlockState> ores) {}

    //? if >=26.3
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin)
    {
        if (random.nextInt(DustConfig.rarity(DustConfig.BOULDER_RARITY)) != 0) return false;
        if (!level.getFluidState(origin).isEmpty() || !level.getFluidState(origin.below()).isEmpty()) return false;
        if (!level.getBlockState(origin.below()).isSolid()) return false;
        Holder<Biome> biome = level.getBiome(origin);
        Palette palette = palette(biome);
        BlockState dustOre = dustOre(biome).defaultBlockState();
        // A rough ball: radii 1.5 to 2.5 (at most 5 across), its middle 1 or 2 below the surface
        float rx = 1.5F + random.nextFloat(), ry = 1.2F + random.nextFloat() * 1.3F, rz = 1.5F + random.nextFloat();
        BlockPos centre = origin.below(1 + random.nextInt(2));
        List<BlockPos> placed = new ArrayList<>();
        for (BlockPos at : BlockPos.betweenClosed(centre.offset(-2, -2, -2), centre.offset(2, 2, 2))) {
            float dx = (at.getX() - centre.getX()) / rx, dy = (at.getY() - centre.getY()) / ry, dz = (at.getZ() - centre.getZ()) / rz;
            float distance = dx * dx + dy * dy + dz * dz + (random.nextFloat() - 0.5F) * 0.35F;
            if (distance > 1.0F) continue;
            BlockState there = level.getBlockState(at);
            if (there.is(Blocks.BEDROCK) || there.hasBlockEntity() || there.is(BlockTags.FEATURES_CANNOT_REPLACE)) continue;
            BlockState block = random.nextFloat() < ORE_SHARE ? pick(palette.ores, random) : pick(palette.stones, random);
            level.setBlock(at, block, Block.UPDATE_CLIENTS);
            placed.add(at.immutable());
        }
        if (placed.isEmpty()) return false;
        // At least one of Dust's ores, more in a big boulder
        int dust = 1 + (placed.size() > 40 ? random.nextInt(2) : 0);
        for (int i = 0; i < dust; i++) level.setBlock(placed.get(random.nextInt(placed.size())), dustOre, Block.UPDATE_CLIENTS);
        return true;
    }

    private static BlockState pick(List<BlockState> states, RandomSource random)
    {
        return states.get(random.nextInt(states.size()));
    }

    private static Palette palette(Holder<Biome> biome)
    {
        if (biome.is(Tags.Biomes.IS_BADLANDS)) return palette(List.of(Blocks.TERRACOTTA, Blocks.TERRACOTTA, Blocks.RED_SANDSTONE),
                List.of(Blocks.GOLD_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.IRON_ORE));
        if (biome.is(Tags.Biomes.IS_DESERT) || biome.is(Tags.Biomes.IS_BEACH)) return palette(List.of(Blocks.SANDSTONE, Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE),
                List.of(Blocks.GOLD_ORE, Blocks.COPPER_ORE, Blocks.REDSTONE_ORE));
        if (biome.is(Tags.Biomes.IS_SAVANNA)) return palette(List.of(Blocks.GRANITE, Blocks.GRANITE, Blocks.STONE),
                List.of(Blocks.COPPER_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE));
        if (biome.is(Tags.Biomes.IS_SNOWY)) return palette(List.of(Blocks.STONE, Blocks.STONE, Blocks.PACKED_ICE),
                List.of(Blocks.IRON_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.LAPIS_ORE, Blocks.DIAMOND_ORE));
        if (biome.is(Tags.Biomes.IS_MOUNTAIN) || biome.is(BiomeTags.IS_MOUNTAIN)) return palette(List.of(Blocks.STONE, Blocks.ANDESITE),
                List.of(Blocks.IRON_ORE, Blocks.EMERALD_ORE, Blocks.COAL_ORE));
        if (biome.is(Tags.Biomes.IS_SPOOKY)) return palette(List.of(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE),
                List.of(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.DEEPSLATE_LAPIS_ORE));
        if (biome.is(Tags.Biomes.IS_SWAMP) || biome.is(Tags.Biomes.IS_JUNGLE) || biome.is(Tags.Biomes.IS_LUSH) || biome.is(Tags.Biomes.IS_MUSHROOM)) {
            return palette(List.of(Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE),
                    List.of(Blocks.COPPER_ORE, Blocks.LAPIS_ORE, Blocks.COAL_ORE));
        }
        return palette(List.of(Blocks.STONE, Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE), List.of(Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.COPPER_ORE));
    }

    private static Palette palette(List<Block> stones, List<Block> ores)
    {
        return new Palette(stones.stream().map(Block::defaultBlockState).toList(), ores.stream().map(Block::defaultBlockState).toList());
    }

    // Dust's ore for the biome: the primal crystal ore of the primal it's richest in, else inert crystal ore
    private static Block dustOre(Holder<Biome> biome)
    {
        EnumElement best = null;
        float most = 0;
        for (EnumElement primal : EnumElement.values()) {
            float richness = BiomeElements.richness(biome, primal);
            if (richness > most) {
                most = richness;
                best = primal;
            }
        }
        return best == null ? DustBlockRegistry.INERT_CRYSTAL_ORE.get() : DustBlockRegistry.PRIMAL_ORES.get(best).get();
    }
}
