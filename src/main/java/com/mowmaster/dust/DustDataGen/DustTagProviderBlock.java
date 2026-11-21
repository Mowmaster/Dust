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

    // A vanilla block's registry key (the tag builder takes keys)
    private static net.minecraft.resources.ResourceKey<net.minecraft.world.level.block.Block> key(net.minecraft.world.level.block.Block block)
    {
        return block.builtInRegistryHolder().key();
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(DustBlockRegistry.DUST_PILE.key());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(DustBlockRegistry.SPELL_CASTER.key())
                .add(DustBlockRegistry.ELEMENT_CRAFTER.key())
                .add(DustBlockRegistry.ELEMENT_REPLICATOR.key())
                .add(DustBlockRegistry.DUST_GRINDER.key())
                .add(DustBlockRegistry.GROWTH_LAMP.key())
                .add(DustBlockRegistry.CRUSHING_MILL.key())
                .add(DustBlockRegistry.ALCHEMICAL_SEPARATOR.key())
                .add(DustBlockRegistry.EVAPORATING_KETTLE.key())
                .add(DustBlockRegistry.STILL_FIREBOX.key())
                .add(DustBlockRegistry.STILL_POT.key())
                .add(DustBlockRegistry.STILL_TOWER.key())
                .add(DustBlockRegistry.STILL_TOP.key())
                .add(DustBlockRegistry.DUST_CHUTE.key())
                .add(DustBlockRegistry.DUST_COLLECTOR.key())
                .add(DustBlockRegistry.DUST_SEPARATOR.key())
                .add(DustBlockRegistry.DUST_SEPARATOR_EXTENSION.key())
                .add(DustBlockRegistry.DUST_MIXER.key())
                .add(DustBlockRegistry.TRANSMUTER.key())
                .add(DustBlockRegistry.CONJOINING_CRUCIBLE.key())
                .add(DustBlockRegistry.MIXING_VAT.key())
                .add(DustBlockRegistry.DISSOLVING_RETORT.key())
                .add(DustBlockRegistry.DUST_TUBE.key())
                .add(DustBlockRegistry.LOVELY_LAMP.key())
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
        // The primal ores: mined like the inert ores (deepslate ones need iron)
        DustBlockRegistry.PRIMAL_ORES.values().forEach(ore -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ore.key()));
        DustBlockRegistry.PRIMAL_ORES_DEEPSLATE.values().forEach(ore -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ore.key()));
        DustBlockRegistry.PRIMAL_ORES_DEEPSLATE.values().forEach(ore -> tag(BlockTags.NEEDS_IRON_TOOL).add(ore.key()));

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.key());

        // Research's "Found in" biomes: world generation's everyday blocks aren't noted; these crafted ones are
        tag(DustTags.Blocks.NO_BIOME_NOTES)
                .add(key(net.minecraft.world.level.block.Blocks.STONE), key(net.minecraft.world.level.block.Blocks.DEEPSLATE), key(net.minecraft.world.level.block.Blocks.GRANITE),
                        key(net.minecraft.world.level.block.Blocks.DIORITE), key(net.minecraft.world.level.block.Blocks.ANDESITE), key(net.minecraft.world.level.block.Blocks.TUFF),
                        key(net.minecraft.world.level.block.Blocks.DIRT), key(net.minecraft.world.level.block.Blocks.GRASS_BLOCK), key(net.minecraft.world.level.block.Blocks.COARSE_DIRT),
                        key(net.minecraft.world.level.block.Blocks.GRAVEL), key(net.minecraft.world.level.block.Blocks.SAND), key(net.minecraft.world.level.block.Blocks.COBBLESTONE),
                        key(net.minecraft.world.level.block.Blocks.NETHERRACK), key(net.minecraft.world.level.block.Blocks.BEDROCK), key(net.minecraft.world.level.block.Blocks.SHORT_GRASS),
                        key(net.minecraft.world.level.block.Blocks.TALL_GRASS), key(net.minecraft.world.level.block.Blocks.WATER), key(net.minecraft.world.level.block.Blocks.LAVA));
        tag(DustTags.Blocks.BIOME_NOTES)
                .add(key(net.minecraft.world.level.block.Blocks.SNOW_BLOCK), key(net.minecraft.world.level.block.Blocks.PACKED_ICE), key(net.minecraft.world.level.block.Blocks.BLUE_ICE));
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
