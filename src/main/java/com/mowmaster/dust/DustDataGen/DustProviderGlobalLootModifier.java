package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustLoot.AddItemStackModifier;
import com.mowmaster.dust.DustReferences;
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
        add("earth_dust_to_dirt",
                new AddItemStackModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT).build(),
                        LootItemRandomChanceCondition.randomChance(0.25f).build()
                }, new ItemStackTemplate(DustItemRegistry.DUST_GREEN, 1)));

        add("earth_cryatl_in_jungle_loot",
                new AddItemStackModifier(new LootItemCondition[]{
                        new LootTableIdCondition.Builder(Identifier.withDefaultNamespace("chests/jungle_temple")).build(),
                }, new ItemStackTemplate(DustItemRegistry.CRYSTAL_GREEN)));

        add("fire_dust_from_blaze",
                new AddItemStackModifier(new LootItemCondition[]{
                        new LootTableIdCondition.Builder(Identifier.withDefaultNamespace("entities/blaze")).build() },
                        new ItemStackTemplate(DustItemRegistry.DUST_RED, 1)));
    }
}
