package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustLoot.AddItemStackModifier;
import com.mowmaster.dust.DustLoot.AddRandomItemStackModifier;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class DustProviderGlobalLootModifier extends GlobalLootModifierProvider {
    public DustProviderGlobalLootModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, DustReferences.MODID);
    }

    @Override
    protected void start() {
        add("cropdustdrop_beetroot_fire",
                new AddRandomItemStackModifier(
                        new LootItemCondition[]{LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BEETROOTS).build(),
                                LootItemRandomChanceCondition.randomChance(1.0F).build()},
                        DustItemRegistry.DUST_RED,
                        1,
                        3));
        add("cropdustdrop_wheat_water",
                new AddRandomItemStackModifier(
                        new LootItemCondition[]{LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT).build(),
                                LootItemRandomChanceCondition.randomChance(1.0F).build()},
                        DustItemRegistry.DUST_BLUE,
                        1,
                        3));
        add("cropdustdrop_leattuce_earth",
                new AddRandomItemStackModifier(
                        new LootItemCondition[]{LootItemBlockStatePropertyCondition.hasBlockStateProperties(DustBlockRegistry.CROP_LETTUCE.get()).build(),
                                LootItemRandomChanceCondition.randomChance(1.0F).build()},
                        DustItemRegistry.DUST_GREEN,
                        1,
                        3));
        add("cropdustdrop_carrot_chaos",
                new AddRandomItemStackModifier(
                        new LootItemCondition[]{LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CARROTS).build(),
                                LootItemRandomChanceCondition.randomChance(1.0F).build()},
                        DustItemRegistry.DUST_BLACK,
                        1,
                        3));
        add("cropdustdrop_potato_order",
                new AddRandomItemStackModifier(
                        new LootItemCondition[]{LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.POTATOES).build(),
                                LootItemRandomChanceCondition.randomChance(1.0F).build()},
                        DustItemRegistry.DUST_WHITE,
                        1,
                        3));

        add("earth_crystal_in_jungle_loot",
                new AddItemStackModifier(new LootItemCondition[]{new LootTableIdCondition.Builder(Identifier.withDefaultNamespace("chests/jungle_temple")).build()}
                , new ItemStackTemplate(DustItemRegistry.CRYSTAL_GREEN)));

        add("fire_dust_from_blaze",
                new AddItemStackModifier(new LootItemCondition[]{new LootTableIdCondition.Builder(Identifier.withDefaultNamespace("entities/blaze")).build()},
                        new ItemStackTemplate(DustItemRegistry.DUST_RED, 1)));

        add("slime_dustdrop_water",
                new AddRandomItemStackModifier(
                        new LootItemCondition[]{new LootTableIdCondition.Builder(Identifier.withDefaultNamespace("entities/slime")).build(),
                                LootItemRandomChanceCondition.randomChance(1.0F).build()},
                        DustItemRegistry.DUST_BLUE,
                        1,
                        3));


    }
}
