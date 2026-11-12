package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.Holder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.Set;

// 26.3: loot tables are registry entries. This sub provider is handed to GatherDataRegistryEntriesEvent
// (see DustDataGenerator) and receives a Context instead of a HolderLookup.Provider.
public class DustProviderBlockLootTable extends BlockLootSubProvider {
    public DustProviderBlockLootTable(Context context) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), context);
    }

    @Override
    protected void generate() {
        dropSelf(DustBlockRegistry.BLOCK_OF_DUST_RED.get());
        dropSelf(DustBlockRegistry.BLOCK_OF_DUST_GREEN.get());
        dropSelf(DustBlockRegistry.BLOCK_OF_DUST_BLUE.get());
        dropSelf(DustBlockRegistry.BLOCK_OF_DUST_WHITE.get());
        dropSelf(DustBlockRegistry.BLOCK_OF_DUST_BLACK.get());

        dropSelf(DustBlockRegistry.BLOCK_OF_CRYSTAL_RED.get());
        dropSelf(DustBlockRegistry.BLOCK_OF_CRYSTAL_GREEN.get());
        dropSelf(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLUE.get());
        dropSelf(DustBlockRegistry.BLOCK_OF_CRYSTAL_WHITE.get());
        dropSelf(DustBlockRegistry.BLOCK_OF_CRYSTAL_BLACK.get());

        add(DustBlockRegistry.INERT_CRYSTAL_ORE.get(), block -> createOreDrop(block, DustItemRegistry.CRYSTAL_INERT.get()));
        add(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.get(), block -> createMultipleOreDrops(block, DustItemRegistry.CRYSTAL_INERT.get(), 2, 5));

        // Glyphs crumble when broken: glyph stones give cobblestone (so players can't collect glyphs from ruins)
        add(DustBlockRegistry.GLYPH_STONE.get(), block -> createSingleItemTableWithSilkTouch(block, Items.COBBLESTONE));

        dropSelf(DustBlockRegistry.ELEMENT_TESTER.get());
        dropSelf(DustBlockRegistry.RESEARCH_CRUSHER.get());

        dropSelf(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.get());
        add(DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL.get(), createPotFlowerItemTable(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.get()));
    }

    protected LootTable.Builder createMultipleOreDrops(Block block, Item item, int minDrops, int maxDrops)
    {
        return this.createSilkTouchDispatchTable(block,
                this.applyExplosionCondition(block, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(this.enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DustBlockRegistry.DUSTBLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
