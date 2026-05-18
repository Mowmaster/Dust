package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class DustTagProviderItem extends ItemTagsProvider {
    public DustTagProviderItem(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DustReferences.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DustTags.Items.MAGICAL_DUST_ITEMS)
                .add(DustItemRegistry.DUST_RED.get())
                .add(DustItemRegistry.DUST_GREEN.get())
                .add(DustItemRegistry.DUST_BLUE.get())
                .add(DustItemRegistry.DUST_WHITE.get())
                .add(DustItemRegistry.DUST_BLACK.get());

        tag(DustTags.Items.MAGICAL_CRYSTAL_ITEMS)
                .add(DustItemRegistry.CRYSTAL_RED.get())
                .add(DustItemRegistry.CRYSTAL_GREEN.get())
                .add(DustItemRegistry.CRYSTAL_BLUE.get())
                .add(DustItemRegistry.CRYSTAL_WHITE.get())
                .add(DustItemRegistry.CRYSTAL_BLACK.get());

        tag(DustTags.Items.CRYSTAL_REPAIRABLES)
                .add(DustItemRegistry.CRYSTAL_RED.get())
                .add(DustItemRegistry.CRYSTAL_GREEN.get())
                .add(DustItemRegistry.CRYSTAL_BLUE.get())
                .add(DustItemRegistry.CRYSTAL_WHITE.get())
                .add(DustItemRegistry.CRYSTAL_BLACK.get());

        tag(DustTags.Items.CRYSTAL_BLOCK_REPAIRABLES)
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.asItem())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.asItem())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.asItem())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.asItem())
                .add(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.asItem());

        //This is required for specific enchants to work
        tag(ItemTags.SWORDS)
                .add(DustItemRegistry.CRYSTAL_SWORD.get());
        tag(ItemTags.PICKAXES)
                .add(DustItemRegistry.CRYSTAL_PICKAXE.get())
                .add(DustItemRegistry.CRYSTAL_HAMMER.get());
        tag(ItemTags.AXES)
                .add(DustItemRegistry.CRYSTAL_AXE.get())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.get());
        tag(ItemTags.SHOVELS)
                .add(DustItemRegistry.CRYSTAL_SHOVEL.get())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.get());
        tag(ItemTags.HOES)
                .add(DustItemRegistry.CRYSTAL_HOE.get())
                .add(DustItemRegistry.CRYSTAL_MATTOCK.get());
        tag(ItemTags.SPEARS)
                .add(DustItemRegistry.CRYSTAL_SPEAR.get());


        tag(ItemTags.HEAD_ARMOR)
                .add(DustItemRegistry.CRYSTAL_HELMET.get());
        tag(ItemTags.CHEST_ARMOR)
                .add(DustItemRegistry.CRYSTAL_CHESTPLATE.get());
        tag(ItemTags.LEG_ARMOR)
                .add(DustItemRegistry.CRYSTAL_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR)
                .add(DustItemRegistry.CRYSTAL_BOOTS.get());


        tag(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .add(DustItemRegistry.SEEDS_LETTUCE.get());

    }
}
