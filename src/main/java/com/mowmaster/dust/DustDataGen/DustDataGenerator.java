package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = DustReferences.MODID)
public class DustDataGenerator {

    @SubscribeEvent
    public static void gatherClientDate(GatherDataEvent.Client event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();



        generator.addProvider(true, new DustProviderModel(packOutput));
        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(DustProviderBlockLootTable::new, LootContextParamSets.BLOCK)), lookupProvider));
        generator.addProvider(true, new DustProviderBasicRecipe.Runner(packOutput,lookupProvider));
        generator.addProvider(true, new DustProviderDataMap(packOutput,lookupProvider));

        generator.addProvider(true, new DustTagProviderItem(packOutput,lookupProvider));
        generator.addProvider(true, new DustTagProviderBlock(packOutput,lookupProvider));


        generator.addProvider(true, new DustAssetsEquipment(packOutput));


    }
}
