package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustLoot.AddAncientTabletModifier;
import com.mowmaster.dust.DustLoot.AddItemStackModifier;
import com.mowmaster.dust.DustLoot.AddRandomItemStackModifier;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class DustProviderGlobalLootModifier extends GlobalLootModifierProvider {
    public DustProviderGlobalLootModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, DustReferences.MODID);
    }

    @Override
    protected void start() {
        // 26.3: LootItemBlockStatePropertyCondition was replaced by MatchBlock, which needs a block lookup.
        // `registries` is filled in by GlobalLootModifierProvider before start() runs.
        HolderGetter<Block> blocks = this.registries.lookupOrThrow(Registries.BLOCK);

        add("cropdustdrop_beetroot_fire",
                new AddRandomItemStackModifier(brokenBlock(blocks, Blocks.BEETROOTS), DustItemRegistry.DUST_RED, 1, 3));
        add("cropdustdrop_wheat_water",
                new AddRandomItemStackModifier(brokenBlock(blocks, Blocks.WHEAT), DustItemRegistry.DUST_BLUE, 1, 3));
        add("cropdustdrop_carrot_chaos",
                new AddRandomItemStackModifier(brokenBlock(blocks, Blocks.CARROTS), DustItemRegistry.DUST_BLACK, 1, 3));
        add("cropdustdrop_potato_order",
                new AddRandomItemStackModifier(brokenBlock(blocks, Blocks.POTATOES), DustItemRegistry.DUST_WHITE, 1, 3));

        add("earth_crystal_in_jungle_loot",
                new AddItemStackModifier(lootTable("chests/jungle_temple"), new ItemStackTemplate(DustItemRegistry.CRYSTAL_GREEN)));

        add("fire_dust_from_blaze",
                new AddItemStackModifier(lootTable("entities/blaze"), new ItemStackTemplate(DustItemRegistry.DUST_RED, 1)));

        // Ancient Tablets in structure chests: (loot table, chance per chest)
        tablet("chests/simple_dungeon", 0.25F);
        tablet("chests/abandoned_mineshaft", 0.15F);
        tablet("chests/desert_pyramid", 0.35F);
        tablet("chests/jungle_temple", 0.35F);
        tablet("chests/stronghold_library", 0.6F);
        tablet("chests/woodland_mansion", 0.4F);
        tablet("chests/ancient_city", 0.5F);
        tablet("chests/underwater_ruin_big", 0.3F);
        tablet("chests/shipwreck_map", 0.2F);

        add("slime_dustdrop_water",
                new AddRandomItemStackModifier(lootTable("entities/slime"), DustItemRegistry.DUST_BLUE, 1, 3));
    }

    private void tablet(String vanillaChest, float chance) {
        add("ancient_tablet_" + vanillaChest.substring(vanillaChest.indexOf('/') + 1),
                new AddAncientTabletModifier(lootTable(vanillaChest), chance));
    }

    // Matches when this block (any age/state) is broken
    private static LootItemCondition brokenBlock(HolderGetter<Block> blocks, Block block) {
        return MatchBlock.blockMatches(blocks, block).build();
    }

    // Matches one vanilla loot table, e.g. "entities/blaze"
    private static LootItemCondition lootTable(String vanillaPath) {
        return LootTableIdCondition.builder(Identifier.withDefaultNamespace(vanillaPath)).build();
    }
}
