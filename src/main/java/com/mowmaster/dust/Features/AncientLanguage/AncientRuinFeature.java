package com.mowmaster.dust.Features.AncientLanguage;

import com.mojang.serialization.MapCodec;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
//? if <26.3 {
/*import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
*///?}

// A small crumbling stone-brick wall with an ancient word carved across its middle row in glyph stones, sometimes with
// a chest in front holding an Ancient Tablet. Placed on the surface of overworld biomes now and then (see DustWorldgen).
//
//        ##  #  ###            row 2: broken top
//     # [g][l][y][p][h] #      row 1: the word, read left to right standing in front
//     #  #  #  #  #  #  #      row 0: base
//
// 26.3: a Feature is a record with a codec. This one has no settings, so its codec is a "unit" that always gives the
// one instance. place() gets the surface position chosen by the placement modifiers.
//
// Stonecutter: before 26.3 a Feature is a class that takes a configuration (none here: NoneFeatureConfiguration),
// gets its arguments in a FeaturePlaceContext, and is registered itself (not its codec). The 26.1.2 place(context)
// just calls the same place(level, chunkGenerator, random, origin) that 26.3 uses.
//? if >=26.3 {
public record AncientRuinFeature() implements Feature
{
    public static final AncientRuinFeature INSTANCE = new AncientRuinFeature();
    public static final MapCodec<AncientRuinFeature> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<AncientRuinFeature> codec()
    {
        return CODEC;
    }
//?} else {
/*public class AncientRuinFeature extends Feature<NoneFeatureConfiguration>
{
    public AncientRuinFeature()
    {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        return place(context.level(), context.chunkGenerator(), context.random(), context.origin());
    }
*///?}

    private static final float CHEST_CHANCE = 0.5F;

    //? if >=26.3
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin)
    {
        if (!level.getFluidState(origin).isEmpty() || !level.getFluidState(origin.below()).isEmpty()) {
            return false;
        }

        String word = AncientWords.random(random);
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction right = facing.getCounterClockWise();   // the reader's right, standing in front of the wall
        int length = word.length() + 2;                   // a plain column at each end
        BlockPos start = origin.relative(right, -(length / 2));

        // Needs solid ground under most of the wall; small gaps get a brick filled in
        int gaps = 0;
        for (int i = 0; i < length; i++) {
            BlockPos below = start.relative(right, i).below();
            if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) gaps++;
        }
        if (gaps > 2) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            BlockPos column = start.relative(right, i);
            BlockPos below = column.below();
            if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                level.setBlock(below, Blocks.STONE_BRICKS.defaultBlockState(), 2);
            }
            boolean end = i == 0 || i == length - 1;
            level.setBlock(column, wallBlock(random), 2);
            if (end) {
                level.setBlock(column.above(), wallBlock(random), 2);
            } else {
                int glyph = word.charAt(i - 1) - 'a';
                level.setBlock(column.above(), DustBlockRegistry.GLYPH_STONE.get().defaultBlockState()
                        .setValue(GlyphStoneBlock.FACING, facing)
                        .setValue(GlyphStoneBlock.GLYPH, glyph), 2);
            }
            // The top row is mostly fallen away
            if (random.nextFloat() < 0.45F) {
                level.setBlock(column.above(2), wallBlock(random), 2);
            }
        }

        if (random.nextFloat() < CHEST_CHANCE) {
            placeChest(level, random, origin.relative(facing, 2), facing);
        }
        return true;
    }

    private static BlockState wallBlock(RandomSource random)
    {
        float roll = random.nextFloat();
        if (roll < 0.35F) return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        if (roll < 0.6F) return Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
        return Blocks.STONE_BRICKS.defaultBlockState();
    }

    private static void placeChest(WorldGenLevel level, RandomSource random, BlockPos pos, Direction facing)
    {
        BlockPos below = pos.below();
        if (!level.getBlockState(pos).canBeReplaced() || !level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
            return;
        }
        level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing), 2);
        if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
            chest.setItem(random.nextInt(27), AncientWritingItem.withWord(new ItemStack(DustItemRegistry.ANCIENT_TABLET.get()), AncientWords.random(random)));
            chest.setItem(random.nextInt(27), new ItemStack(DustItemRegistry.PARCHMENT.get(), 1 + random.nextInt(3)));
            if (random.nextBoolean()) chest.setItem(random.nextInt(27), new ItemStack(Items.BONE, 1 + random.nextInt(4)));
            if (random.nextBoolean()) chest.setItem(random.nextInt(27), new ItemStack(Items.PAPER, 1 + random.nextInt(5)));
        }
    }
}
