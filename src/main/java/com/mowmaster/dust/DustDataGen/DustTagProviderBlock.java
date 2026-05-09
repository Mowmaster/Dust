package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class DustTagProviderBlock extends BlockTagsProvider {
    public DustTagProviderBlock(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DustReferences.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(DustBlockRegistry.CRYSTAL_PATH_TIER1.get())
                .add(DustBlockRegistry.CRYSTAL_PATH_TIER2.get())
                .add(DustBlockRegistry.CRYSTAL_PATH_TIER3.get())
                .add(DustBlockRegistry.CRYSTAL_PATH_TIER4.get())


                .add(DustBlockRegistry.BLOCK_OF_DUST_RED.get())
                .add(DustBlockRegistry.BLOCK_OF_DUST_GREEN.get())
                .add(DustBlockRegistry.BLOCK_OF_DUST_BLUE.get())
                .add(DustBlockRegistry.BLOCK_OF_DUST_WHITE.get())
                .add(DustBlockRegistry.BLOCK_OF_DUST_BLACK.get())

                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.get())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.get())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.get())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.get())

                .add(DustBlockRegistry.INERT_CRYSTAL_ORE.get())
                .add(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.get());


        tag(BlockTags.PLANKS)
                .add(DustBlockRegistry.BLOCK_PLANKS_GREEN.get());
        tag(BlockTags.WOODEN_STAIRS)
                .add(DustBlockRegistry.BLOCK_STAIRS_GREEN.get());
        tag(BlockTags.WOODEN_SLABS)
                .add(DustBlockRegistry.BLOCK_SLAB_GREEN.get());
        tag(BlockTags.WOODEN_BUTTONS)
                .add(DustBlockRegistry.BLOCK_BUTTON_GREEN.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(DustBlockRegistry.BLOCK_PRESSUREPLATE_GREEN.get());
        tag(BlockTags.WOODEN_FENCES)
                .add(DustBlockRegistry.BLOCK_FENCE_GREEN.get());
        tag(BlockTags.FENCE_GATES)
                .add(DustBlockRegistry.BLOCK_FENCEGATE_GREEN.get());
        tag(BlockTags.WALLS)
                .add(DustBlockRegistry.BLOCK_WALL_GREEN.get());
        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(DustBlockRegistry.BLOCK_TRAPDOOR_GREEN.get());
        tag(BlockTags.WOODEN_DOORS)
                .add(DustBlockRegistry.BLOCK_DOOR_GREEN.get());

        tag(DustTags.Blocks.MAGICAL_CRYSTAL_BLOCKS)
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.get())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.get())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.get())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.get());

        tag(DustTags.Blocks.NEEDS_CRYSTAL_TOOL)
                .addTags(BlockTags.NEEDS_DIAMOND_TOOL);
        tag(DustTags.Blocks.INCORRECT_FOR_CRYSTAL_TOOL)
                .addTags(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .remove(DustTags.Blocks.NEEDS_CRYSTAL_TOOL);


        tag(DustTags.Blocks.MATTOCK_MINEABLE)
                .addTags(BlockTags.MINEABLE_WITH_AXE)
                .addTags(BlockTags.MINEABLE_WITH_SHOVEL)
                .addTags(BlockTags.MINEABLE_WITH_HOE);
    }
}
