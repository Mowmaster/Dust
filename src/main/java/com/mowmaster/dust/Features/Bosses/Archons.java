package com.mowmaster.dust.Features.Bosses;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// Registers the Archons (ArchonEntity, one per elemental armor set: dust:<set>_archon) with their spawn eggs, loot bags
// (dust:<set>_loot_bag) and Sigils (dust:<set>_sigil), and the three legendary trinkets.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Archons
{
    public static final Map<ElementalArmorSet, DeferredHolder<EntityType<?>, EntityType<ArchonEntity>>> TYPES;
    public static final Map<ElementalArmorSet, DeferredItem<SpawnEggItem>> EGGS;
    public static final Map<ElementalArmorSet, DeferredItem<LootBagItem>> BAGS;
    public static final Map<ElementalArmorSet, DeferredItem<TrinketItem>> SIGILS;
    public static final DeferredItem<TrinketItem> PRISM = trinket("prism_of_archons", TrinketItem.Kind.PRISM);
    public static final DeferredItem<TrinketItem> MANA_HEART = trinket("heart_of_mana", TrinketItem.Kind.MANA_HEART);
    public static final DeferredItem<TrinketItem> VOID_LOCKET = trinket("void_locket", TrinketItem.Kind.VOID_LOCKET);
    public static final List<DeferredItem<TrinketItem>> LEGENDARY = List.of(PRISM, MANA_HEART, VOID_LOCKET);

    static {
        Map<ElementalArmorSet, DeferredHolder<EntityType<?>, EntityType<ArchonEntity>>> types = new EnumMap<>(ElementalArmorSet.class);
        Map<ElementalArmorSet, DeferredItem<SpawnEggItem>> eggs = new EnumMap<>(ElementalArmorSet.class);
        Map<ElementalArmorSet, DeferredItem<LootBagItem>> bags = new EnumMap<>(ElementalArmorSet.class);
        Map<ElementalArmorSet, DeferredItem<TrinketItem>> sigils = new EnumMap<>(ElementalArmorSet.class);
        for (ElementalArmorSet set : ElementalArmorSet.values()) {
            // Player-sized (its SCALE attribute makes it a quarter bigger); fire can't hurt the Pyreborn and Blastward Archons
            boolean fireproof = set == ElementalArmorSet.PYREBORN || set == ElementalArmorSet.BLASTWARD;
            DeferredHolder<EntityType<?>, EntityType<ArchonEntity>> type = DustEntityRegistry.ENTITY_TYPES.registerEntityType(set.id + "_archon",
                    (entityType, level) -> new ArchonEntity(entityType, level, set), MobCategory.MONSTER, builder -> {
                        EntityType.Builder<ArchonEntity> sized = builder.sized(0.6F, 1.95F).eyeHeight(1.74F).clientTrackingRange(10);
                        return fireproof ? sized.fireImmune() : sized;
                    });
            types.put(set, type);
            eggs.put(set, DustItemRegistry.DUSTITEMS.registerItem(set.id + "_archon_spawn_egg", properties -> new SpawnEggItem(properties.spawnEgg(type.get()))));
            bags.put(set, DustItemRegistry.DUSTITEMS.registerItem(set.id + "_loot_bag", properties -> new LootBagItem(set, properties.stacksTo(16).rarity(Rarity.RARE))));
            sigils.put(set, DustItemRegistry.DUSTITEMS.registerItem(set.id + "_sigil",
                    properties -> new TrinketItem(TrinketItem.Kind.SIGIL, set, properties.stacksTo(1).rarity(Rarity.RARE).fireResistant())));
        }
        TYPES = Collections.unmodifiableMap(types);
        EGGS = Collections.unmodifiableMap(eggs);
        BAGS = Collections.unmodifiableMap(bags);
        SIGILS = Collections.unmodifiableMap(sigils);
    }

    private Archons() {}

    // Loads the class (and so registers everything above) from the mod constructor (Dust)
    public static void init() {}

    private static DeferredItem<TrinketItem> trinket(String name, TrinketItem.Kind kind)
    {
        return DustItemRegistry.DUSTITEMS.registerItem(name, properties -> new TrinketItem(kind, null, properties.stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    }

    @SubscribeEvent
    public static void onAttributes(EntityAttributeCreationEvent event)
    {
        for (DeferredHolder<EntityType<?>, EntityType<ArchonEntity>> type : TYPES.values()) event.put(type.get(), ArchonEntity.createAttributes().build());
    }

    // The spawn eggs go in vanilla's Spawn Eggs tab too
    @SubscribeEvent
    public static void onCreativeTabs(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            for (DeferredItem<SpawnEggItem> egg : EGGS.values()) event.accept(egg.get());
        }
    }
}
