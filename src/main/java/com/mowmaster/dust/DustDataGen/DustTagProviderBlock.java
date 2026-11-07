package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

// Tag appenders take ResourceKey<Block>; DeferredBlock.key() is that key.
public class DustTagProviderBlock extends BlockTagsProvider {
    public DustTagProviderBlock(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DustReferences.MODID);
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

        tag(BlockTags.SMALL_FLOWERS)
                .add(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.key());
        tag(BlockTags.FLOWER_POTS)
                .add(DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL.key());
    }
}
