package com.mowmaster.dust.Features.DustComposition;

import com.mowmaster.dust.DustReferences;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

// Data maps: NeoForge's way to attach data to registry entries (here, items) from datapack JSON. A datapack (or
// another mod) can add entries, override them, or remove them, without code.
//
// dust:elements, in data/<namespace>/data_maps/item/elements.json:
//     { "values": { "minecraft:rooted_dirt": { "elements": ["dust:earth", "dust:trees"] } } }
@EventBusSubscriber(modid = DustReferences.MODID)
public final class DustDataMaps
{
    public static final DataMapType<Item, DustComposition> ELEMENTS = DataMapType.builder(
                    Identifier.fromNamespaceAndPath(DustReferences.MODID, "elements"), Registries.ITEM, DustComposition.CODEC)
            // Sent to clients (the book and the Element Tester read it there). false: not required, so clients without
            // the data can still join.
            .synced(DustComposition.CODEC, false)
            .build();

    // dust:elements for mobs, in data/<namespace>/data_maps/entity_type/elements.json: what a mob is made of, and so
    // which orbs it drops when a player kills it (DustOrbDrops). Same form as the items'.
    public static final DataMapType<EntityType<?>, DustComposition> ENTITY_ELEMENTS = DataMapType.builder(
                    Identifier.fromNamespaceAndPath(DustReferences.MODID, "elements"), Registries.ENTITY_TYPE, DustComposition.CODEC)
            .synced(DustComposition.CODEC, false)
            .build();

    private DustDataMaps() {}

    @SubscribeEvent
    public static void register(RegisterDataMapTypesEvent event)
    {
        event.register(ELEMENTS);
        event.register(ENTITY_ELEMENTS);
    }
}
