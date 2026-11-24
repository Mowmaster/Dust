package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Trees.ElementalWoods;
import com.mowmaster.dust.Features.Trees.WoodSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Transformable;

import java.util.concurrent.CompletableFuture;

// NeoForge data maps (26.3). An axe strips the elemental woods' logs and wood (Features/Trees): in 26.3 tool actions on
// blocks are block transformers, which the neoforge:transformables data map adds to. 26.1.2 has NeoForge's
// strippables data map for this instead (src/main/resources-26.1.2/data/neoforge/data_maps/block/strippables.json); it
// skips this file (logging that it doesn't know the data map).
public class DustProviderDataMaps extends DataMapProvider
{
    public DustProviderDataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        var transformables = builder(NeoForgeDataMaps.TRANSFORMABLES);
        for (WoodSet set : ElementalWoods.all()) {
            transformables.add(set.log().key(), Transformable.stripping(set.log().get(), set.strippedLog().get()), false);
            transformables.add(set.wood().key(), Transformable.stripping(set.wood().get(), set.strippedWood().get()), false);
        }
    }

    @Override
    public String getName()
    {
        return DustReferences.MODID + " data maps";
    }
}
