package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.DustDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

// Run with the "data" run config (clientData), which writes to src/generated/resources.
@EventBusSubscriber(modid = DustReferences.MODID)
public class DustDataGenerator {

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        // 26.3: the lookup for providers that read registries (tags, loot modifiers) is the "reloadable" one
        var lookupProvider = event.getReloadableLookupProvider();

        generator.addProvider(true, new DustProviderModel(packOutput));
        generator.addProvider(true, new DustEquipmentAssets(packOutput));

        generator.addProvider(true, new DustTagProviderItem(packOutput, lookupProvider));
        generator.addProvider(true, new DustTagProviderBlock(packOutput, lookupProvider));

        generator.addProvider(true, new DustProviderGlobalLootModifier(packOutput, lookupProvider));
        generator.addProvider(true, new DustProviderSpellConversions(packOutput, lookupProvider));
    }

    // 26.3: recipes, loot tables and other datapack registry entries (damage types) are no longer DataProviders.
    // They are "registry bootstraps" handed to this event, and NeoForge writes them out as json.
    @SubscribeEvent
    public static void gatherDataRegistryEntries(GatherDataRegistryEntriesEvent event)
    {
        event.add(DustProviderBasicRecipe.create())
                .add(Registries.DAMAGE_TYPE, DustDamageTypes::bootstrap)
                .add(Registries.FEATURE, DustWorldgen::features)
                .add(Registries.PLACED_FEATURE, DustWorldgen::placedFeatures)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, DustWorldgen::biomeModifiers)
                .lootTable(new LootTableProvider.SubProviderEntry(DustProviderBlockLootTable::new, LootContextParamSets.BLOCK));
    }
}
