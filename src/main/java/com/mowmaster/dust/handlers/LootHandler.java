package com.mowmaster.dust.handlers;

import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    Item[] scrolls = {ItemRegistry.scrollA,ItemRegistry.scrollB,ItemRegistry.scrollC,ItemRegistry.scrollD,ItemRegistry.scrollE,ItemRegistry.scrollF,ItemRegistry.scrollG,
            ItemRegistry.scrollH,ItemRegistry.scrollI,ItemRegistry.scrollJ,ItemRegistry.scrollK,ItemRegistry.scrollL,ItemRegistry.scrollM,ItemRegistry.scrollN,ItemRegistry.scrollO,
            ItemRegistry.scrollP,ItemRegistry.scrollQ,ItemRegistry.scrollR,ItemRegistry.scrollS,ItemRegistry.scrollT,ItemRegistry.scrollU,ItemRegistry.scrollV,ItemRegistry.scrollW,
            ItemRegistry.scrollX,ItemRegistry.scrollY,ItemRegistry.scrollZ,};

    Item[] coins = {ItemRegistry.ancientCoin,ItemRegistry.ancientCoinB,ItemRegistry.ancientCoinC,ItemRegistry.ancientCoinD,ItemRegistry.ancientCoinE,ItemRegistry.ancientCoinF,ItemRegistry.ancientCoinG,
            ItemRegistry.ancientCoinH,ItemRegistry.ancientCoinI,ItemRegistry.ancientCoinJ,ItemRegistry.ancientCoinK,ItemRegistry.ancientCoinL,ItemRegistry.ancientCoinM,
            ItemRegistry.ancientCoinN,ItemRegistry.ancientCoinO, ItemRegistry.ancientCoinP,ItemRegistry.ancientCoinQ,ItemRegistry.ancientCoinR,ItemRegistry.ancientCoinS,
            ItemRegistry.ancientCoinT,ItemRegistry.ancientCoinU,ItemRegistry.ancientCoinV,ItemRegistry.ancientCoinW,ItemRegistry.ancientCoinX,ItemRegistry.ancientCoinY,ItemRegistry.ancientCoinZ,};

    @SubscribeEvent
    public void onLootTablesLoaded(LootTableLoadEvent event) {

        ResourceLocation name = event.getName();
        if (name.equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {

            final LootPool simpleDungeonPool1 = event.getTable().getPool("pool1");
            if (simpleDungeonPool1 != null) {
                int i=0;
                for(Item scroll : scrolls) {i++;simpleDungeonPool1.addEntry(new LootEntryItem(scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll" + i));}
                simpleDungeonPool1.addEntry(new LootEntryItem(ItemRegistry.ancientCoin, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:coin" + i));
            }
        }
        else if (name.equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)) {
            final LootPool mineshaftPool1 = event.getTable().getPool("pool1");
            if (mineshaftPool1 != null) {
                int i=0;
                for(Item scroll : scrolls) {i++;mineshaftPool1.addEntry(new LootEntryItem(scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll" + i));}
                mineshaftPool1.addEntry(new LootEntryItem(ItemRegistry.ancientCoin, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:coin" + i));
            }
        }
        else if (name.equals(LootTableList.CHESTS_DESERT_PYRAMID)) {
            final LootPool desertPyramidMain = event.getTable().getPool("main");
            if (desertPyramidMain != null) {
                int i=0;
                for(Item scroll : scrolls) {i++;desertPyramidMain.addEntry(new LootEntryItem(scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll" + i));}
                i++;desertPyramidMain.addEntry(new LootEntryItem(ItemRegistry.ancientCoin, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:coin" + i));
            }
        }
        else if (name.equals(LootTableList.CHESTS_SPAWN_BONUS_CHEST)) {
            final LootPool spawnChestPool2 = event.getTable().getPool("pool2");
            if (spawnChestPool2 != null) {
                int i=0;
                for(Item scroll : scrolls) {i++;spawnChestPool2.addEntry(new LootEntryItem(scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll" + i));}
                spawnChestPool2.addEntry(new LootEntryItem(ItemRegistry.ancientCoin, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:coin" + i));
            }
        }
        else if (name.equals(LootTableList.CHESTS_END_CITY_TREASURE)) {
            final LootPool spawnChestEndCity = event.getTable().getPool("pool1");
            if (spawnChestEndCity != null) {
                int i=0;
                for(Item scroll : scrolls) {i++;spawnChestEndCity.addEntry(new LootEntryItem(scroll, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:scroll" + i));}
                spawnChestEndCity.addEntry(new LootEntryItem(ItemRegistry.ancientCoin, 1, 0, new LootFunction[0], new LootCondition[0], "loottable:coin" + i));
            }
        }
    }
}
