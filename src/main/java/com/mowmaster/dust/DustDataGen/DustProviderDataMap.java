package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class DustProviderDataMap extends DataMapProvider {
    /**
     * Create a new provider.
     *
     * @param packOutput     the output location
     * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
     */
    protected DustProviderDataMap(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(DustItemRegistry.CHARCOAL_WHITE.getId(), new FurnaceFuel(3200),false)
        ;
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(DustItemRegistry.SEEDS_LETTUCE.getId(), new Compostable(0.30f),false)
                .add(DustItemRegistry.FOOD_LETTUCE.getId(), new Compostable(0.65f),false)
                .add(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.getId(), new Compostable(0.30f),false)
        ;
    }


}
