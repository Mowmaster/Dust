package com.mowmaster.dust.handlers;

import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class LootHandler
{
    @SubscribeEvent
    public void onLootTablesLoaded(LootTableLoadEvent event) {

        ResourceLocation name = event.getName();
        if (name.equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {

            final LootPool simpleDungeonPool1 = event.getTable().getPool("pool1");
            if (simpleDungeonPool1 != null) {
                simpleDungeonPool1.addEntry(new LootEntryItem(ItemRegistry.scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll"));

            }
        }
        else if (name.equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)) {
            final LootPool mineshaftPool1 = event.getTable().getPool("pool1");
            if (mineshaftPool1 != null) {
                mineshaftPool1.addEntry(new LootEntryItem(ItemRegistry.scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll"));
            }
        }
        else if (name.equals(LootTableList.CHESTS_DESERT_PYRAMID)) {
            final LootPool desertPyramidMain = event.getTable().getPool("main");
            if (desertPyramidMain != null) {
                desertPyramidMain.addEntry(new LootEntryItem(ItemRegistry.scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll"));
            }
        }
        else if (name.equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST)) {
            final LootPool spawnChestPool2 = event.getTable().getPool("pool2");
            if (spawnChestPool2 != null) {
                spawnChestPool2.addEntry(new LootEntryItem(ItemRegistry.scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll"));
            }
        }
        else if (name.equals(LootTableList.CHESTS_END_CITY_TREASURE)) {
            final LootPool spawnChestPool2 = event.getTable().getPool("pool1");
            if (spawnChestPool2 != null) {
                spawnChestPool2.addEntry(new LootEntryItem(ItemRegistry.scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll"));
            }
        }
    }
}
