package com.mowmaster.dust.handlers;

import com.mowmaster.dust.items.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
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

        else if (name.equals(LootTableList.ENTITIES_BLAZE)) {
            final LootPool blaze = event.getTable().getPool("main");
            if (blaze != null) {
                // pool2.addEntry(new LootEntryItem(ITEM, WEIGHT, QUALITY, FUNCTIONS, CONDITIONS, NAME));
                //main.addEntry(new LootEntryItem(Items.CARROT, 1, 0, new LootFunction[] {new SetCount(new LootCondition[0], new RandomValueRange(1,3))}, new LootCondition[0], "tutorial:carrot"));
                //main.addEntry(new LootEntryItem(Items.CARROT, 1, 0, new LootFunction[0], new LootCondition[] { new KilledByPlayer(false)}, "tutorial:carrot"));
                blaze.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(0))}, new LootCondition[0], "loottable:blazer"));
                blaze.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(2))}, new LootCondition[0], "loottable:blazeb"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_CAVE_SPIDER)) {
            final LootPool cspider = event.getTable().getPool("main");
            if (cspider != null) {
                cspider.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(3))}, new LootCondition[0], "loottable:cspiderp"));
                cspider.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:cspiderbl"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_CREEPER)) {
            final LootPool creeper = event.getTable().getPool("main");
            if (creeper != null) {
                creeper.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(4))}, new LootCondition[0], "loottable:creeperg"));
                creeper.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(6))}, new LootCondition[0], "loottable:creeperw"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_ENDERMAN)) {
            final LootPool enderm = event.getTable().getPool("main");
            if (enderm != null) {
                enderm.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "loottable:endermb"));
                enderm.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:endermbl"));
            }
        }
        /*
        else if (name.equals(LootTableList.ENTITIES_ENDERMITE)) {
            final LootPool endermi = event.getTable().getPool("main");
            if (endermi != null) {
                endermi.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "loottable:endermib"));
                endermi.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:endermibl"));
            }
        }
         */
        else if (name.equals(LootTableList.ENTITIES_GHAST)) {
            final LootPool ghast = event.getTable().getPool("main");
            if (ghast != null) {
                ghast.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(0))}, new LootCondition[0], "loottable:ghastr"));
                ghast.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(5))}, new LootCondition[0], "loottable:ghasto"));
                ghast.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(6))}, new LootCondition[0], "loottable:ghastw"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_GUARDIAN)) {
            final LootPool guard = event.getTable().getPool("main");
            if (guard != null) {
                guard.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "loottable:guardb"));
                guard.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:guardbl"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_HUSK)) {
            final LootPool husk = event.getTable().getPool("main");
            if (husk != null) {
                husk.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(2))}, new LootCondition[0], "loottable:husky"));
                husk.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:huskbl"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_MAGMA_CUBE)) {
            final LootPool mslime = event.getTable().getPool("main");
            if (mslime != null) {
                mslime.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(0))}, new LootCondition[0], "loottable:mslimer"));
                mslime.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(5))}, new LootCondition[0], "loottable:mslimeo"));
                mslime.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(2))}, new LootCondition[0], "loottable:mslimey"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_SHULKER)) {
            final LootPool shulk = event.getTable().getPool("main");
            if (shulk != null) {
                shulk.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "loottable:shulkb"));
                shulk.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(5))}, new LootCondition[0], "loottable:shulko"));
                shulk.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:shulkbl"));
            }
        }
        /*
        else if (name.equals(LootTableList.ENTITIES_SILVERFISH)) {
            final LootPool sfish = event.getTable().getPool("main");
            if (sfish != null) {
                sfish.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "loottable:sfishb"));
                sfish.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(2))}, new LootCondition[0], "loottable:sfishy"));
                sfish.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(6))}, new LootCondition[0], "loottable:sfishw"));
            }
        }
         */
        else if (name.equals(LootTableList.ENTITIES_SKELETON)) {
            final LootPool skele = event.getTable().getPool("main");
            if (skele != null) {
                skele.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(0))}, new LootCondition[0], "loottable:skeler"));
                skele.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(2))}, new LootCondition[0], "loottable:skeley"));
                skele.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(6))}, new LootCondition[0], "loottable:skelew"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_SLIME)) {
            final LootPool slime = event.getTable().getPool("main");
            if (slime != null) {
                slime.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(4))}, new LootCondition[0], "loottable:slimeg"));
                slime.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(5))}, new LootCondition[0], "loottable:slimeo"));
            }
        }
        else if (name.equals(LootTableList.ENTITIES_SPIDER)) {
            final LootPool spider = event.getTable().getPool("main");
            if (spider != null) {
                spider.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(4))}, new LootCondition[0], "loottable:spiderg"));
                spider.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(5))}, new LootCondition[0], "loottable:spidero"));
            }
        }
        /*
        else if (name.equals(LootTableList.ENTITIES_VEX)) {
            final LootPool vex = event.getTable().getPool("main");
            if (vex != null) {
                vex.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(0))}, new LootCondition[0], "loottable:vexr"));
                vex.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "loottable:vexb"));
                vex.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(2))}, new LootCondition[0], "loottable:vexy"));
            }
        }
         */
        else if (name.equals(LootTableList.ENTITIES_WITCH)) {
            final LootPool witch = event.getTable().getPool("main");
            if (witch != null) {
                witch.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(3))}, new LootCondition[0], "loottable:witchp"));
                witch.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(4))}, new LootCondition[0], "loottable:witchg"));
                witch.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:witchbl"));
            }
        }else if (name.equals(LootTableList.ENTITIES_WITHER_SKELETON)) {
            final LootPool wskele = event.getTable().getPool("main");
            if (wskele != null) {
                wskele.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(4))}, new LootCondition[0], "loottable:wskeleg"));
                wskele.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:wskelebl"));
            }
        }else if (name.equals(LootTableList.ENTITIES_ZOMBIE)) {
            final LootPool zombie = event.getTable().getPool("main");
            if (zombie != null) {
                zombie.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(2))}, new LootCondition[0], "loottable:zombiey"));
                zombie.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:zombiebl"));
            }
        }else if (name.equals(LootTableList.ENTITIES_ZOMBIE_PIGMAN)) {
            final LootPool pzombie = event.getTable().getPool("main");
            if (pzombie != null) {
                pzombie.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,5)),new SetMetadata(new LootCondition[0], new RandomValueRange(3))}, new LootCondition[0], "loottable:pzombiep"));
                pzombie.addEntry(new LootEntryItem(ItemRegistry.bit, 1, 0, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1,3)),new SetMetadata(new LootCondition[0], new RandomValueRange(7))}, new LootCondition[0], "loottable:pzombiebl"));
            }
        }




    }
}
