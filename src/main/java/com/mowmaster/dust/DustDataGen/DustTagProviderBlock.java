package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

// Tag appenders take ResourceKey<Block>; DeferredBlock.key() is that key.
public class DustTagProviderBlock extends BlockTagsProvider {
    public DustTagProviderBlock(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DustReferences.MODID);
    }

    // A vanilla block's registry key, for tag appenders (they take keys, not blocks)
    private static ResourceKey<Block> key(Block block)
    {
        return block.builtInRegistryHolder().key();
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(DustBlockRegistry.BLOCK_OF_DUST_RED.key())
                .add(DustBlockRegistry.BLOCK_OF_DUST_GREEN.key())
                .add(DustBlockRegistry.BLOCK_OF_DUST_BLUE.key())
                .add(DustBlockRegistry.BLOCK_OF_DUST_WHITE.key())
                .add(DustBlockRegistry.BLOCK_OF_DUST_BLACK.key())

                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.key())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.key())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.key())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.key())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.key())

                .add(DustBlockRegistry.GLYPH_STONE.key())
                .add(DustBlockRegistry.INERT_CRYSTAL_ORE.key())
                .add(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.key());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.key());

        tag(DustTags.Blocks.MAGICAL_CRYSTAL_BLOCKS)
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.key())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.key())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.key())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.key())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.key());

        tag(DustTags.Blocks.NEEDS_CRYSTAL_TOOL)
                .addTags(BlockTags.NEEDS_DIAMOND_TOOL);
        tag(DustTags.Blocks.INCORRECT_FOR_CRYSTAL_TOOL)
                .addTags(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .remove(DustTags.Blocks.NEEDS_CRYSTAL_TOOL);

        tag(DustTags.Blocks.MATTOCK_MINEABLE)
                .addTags(BlockTags.MINEABLE_WITH_AXE)
                .addTags(BlockTags.MINEABLE_WITH_SHOVEL)
                .addTags(BlockTags.MINEABLE_WITH_HOE);

        // What the Research Notes list under "Plants" (everything else with an item is under "Blocks").
        // Other mods' plants get in through these vanilla tags, or by adding to #dust:research/plants.
        tag(DustTags.Blocks.RESEARCH_PLANTS)
                .addTags(BlockTags.FLOWERS, BlockTags.SMALL_FLOWERS, BlockTags.SAPLINGS, BlockTags.LEAVES, BlockTags.CROPS,
                        BlockTags.CAVE_VINES, BlockTags.CORAL_PLANTS, BlockTags.CORALS)
                .add(key(Blocks.SHORT_GRASS), key(Blocks.TALL_GRASS), key(Blocks.FERN), key(Blocks.LARGE_FERN), key(Blocks.DEAD_BUSH),
                        key(Blocks.CACTUS), key(Blocks.SUGAR_CANE), key(Blocks.BAMBOO), key(Blocks.VINE), key(Blocks.LILY_PAD),
                        key(Blocks.KELP), key(Blocks.SEAGRASS), key(Blocks.SEA_PICKLE), key(Blocks.BROWN_MUSHROOM), key(Blocks.RED_MUSHROOM),
                        key(Blocks.CRIMSON_FUNGUS), key(Blocks.WARPED_FUNGUS), key(Blocks.CRIMSON_ROOTS), key(Blocks.WARPED_ROOTS),
                        key(Blocks.NETHER_SPROUTS), key(Blocks.WEEPING_VINES), key(Blocks.TWISTING_VINES), key(Blocks.SWEET_BERRY_BUSH),
                        key(Blocks.GLOW_LICHEN), key(Blocks.MOSS_BLOCK), key(Blocks.MOSS_CARPET), key(Blocks.AZALEA), key(Blocks.FLOWERING_AZALEA),
                        key(Blocks.SPORE_BLOSSOM), key(Blocks.HANGING_ROOTS), key(Blocks.BIG_DRIPLEAF), key(Blocks.SMALL_DRIPLEAF),
                        key(Blocks.CHORUS_PLANT), key(Blocks.CHORUS_FLOWER), key(Blocks.PUMPKIN), key(Blocks.MELON), key(Blocks.COCOA),
                        key(Blocks.NETHER_WART), key(Blocks.PITCHER_PLANT), key(Blocks.TORCHFLOWER),
                        DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.key());

        tag(BlockTags.SMALL_FLOWERS)
                .add(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.key());
        tag(BlockTags.FLOWER_POTS)
                .add(DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL.key());
    }
}
