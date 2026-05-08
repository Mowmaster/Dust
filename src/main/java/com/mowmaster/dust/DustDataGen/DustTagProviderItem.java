package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
    }
}
