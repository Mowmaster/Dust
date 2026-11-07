package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.concurrent.CompletableFuture;

// 26.x tag appenders take registry keys (ResourceKey<Item>), not the Item objects. A DeferredItem's key() gives that
// key without needing the item to exist yet. A block's item has the same id as the block, so blockItem() builds it.
public class DustTagProviderItem extends ItemTagsProvider {
    public DustTagProviderItem(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DustReferences.MODID);
    }

    private static ResourceKey<Item> blockItem(DeferredBlock<? extends Block> block) {
        return ResourceKey.create(Registries.ITEM, block.getId());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DustTags.Items.MAGICAL_DUST_ITEMS)
                .add(DustItemRegistry.DUST_RED.key())
                .add(DustItemRegistry.DUST_GREEN.key())
                .add(DustItemRegistry.DUST_BLUE.key())
                .add(DustItemRegistry.DUST_WHITE.key())
                .add(DustItemRegistry.DUST_BLACK.key());

        tag(DustTags.Items.MAGICAL_CRYSTAL_ITEMS)
                .add(DustItemRegistry.CRYSTAL_RED.key())
                .add(DustItemRegistry.CRYSTAL_GREEN.key())
                .add(DustItemRegistry.CRYSTAL_BLUE.key())
                .add(DustItemRegistry.CRYSTAL_WHITE.key())
                .add(DustItemRegistry.CRYSTAL_BLACK.key());

        tag(DustTags.Items.CRYSTAL_REPAIRABLES)
                .add(DustItemRegistry.CRYSTAL_RED.key())
                .add(DustItemRegistry.CRYSTAL_GREEN.key())
                .add(DustItemRegistry.CRYSTAL_BLUE.key())
                .add(DustItemRegistry.CRYSTAL_WHITE.key())
                .add(DustItemRegistry.CRYSTAL_BLACK.key());

        tag(DustTags.Items.CRYSTAL_BLOCK_REPAIRABLES)
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED))
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN))
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE))
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE))
                .add(blockItem(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK));

        //This is required for specific enchants to work
        tag(ItemTags.SWORDS)
                .add(DustItemRegistry.CRYSTAL_SWORD.key());
        tag(ItemTags.PICKAXES)
                .add(DustItemRegistry.CRYSTAL_PICKAXE.key())
                .add(DustItemRegistry.CRYSTAL_HAMMER.key());
        tag(ItemTags.AXES)
                .add(DustItemRegistry.CRYSTAL_AXE.key())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.key());
        tag(ItemTags.SHOVELS)
                .add(DustItemRegistry.CRYSTAL_SHOVEL.key())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.key());
        tag(ItemTags.HOES)
                .add(DustItemRegistry.CRYSTAL_HOE.key())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.key());
        tag(ItemTags.SPEARS)
                .add(DustItemRegistry.CRYSTAL_SPEAR.key());

        tag(ItemTags.HEAD_ARMOR)
                .add(DustItemRegistry.CRYSTAL_HELMET.key());
        tag(ItemTags.CHEST_ARMOR)
                .add(DustItemRegistry.CRYSTAL_CHESTPLATE.key());
        tag(ItemTags.LEG_ARMOR)
                .add(DustItemRegistry.CRYSTAL_LEGGINGS.key());
        tag(ItemTags.FOOT_ARMOR)
                .add(DustItemRegistry.CRYSTAL_BOOTS.key());
    }
}
