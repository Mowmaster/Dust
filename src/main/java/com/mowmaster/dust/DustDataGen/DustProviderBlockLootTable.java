package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class DustProviderBlockLootTable extends BlockLootSubProvider {
    public DustProviderBlockLootTable(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

        dropSelf(DustBlockRegistry.CRYSTAL_PATH_TIER1.get());
        dropSelf(DustBlockRegistry.CRYSTAL_PATH_TIER2.get());
        dropSelf(DustBlockRegistry.CRYSTAL_PATH_TIER3.get());
        dropSelf(DustBlockRegistry.CRYSTAL_PATH_TIER4.get());
        dropSelf(DustBlockRegistry.BLOCK_PLANKS_GREEN.get());
        dropSelf(DustBlockRegistry.BLOCK_STAIRS_GREEN.get());
        add(DustBlockRegistry.BLOCK_SLAB_GREEN.get(), this::createSlabItemTable);
        dropSelf(DustBlockRegistry.BLOCK_BUTTON_GREEN.get());
        dropSelf(DustBlockRegistry.BLOCK_PRESSUREPLATE_GREEN.get());

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

        add(DustBlockRegistry.INERT_CRYSTAL_ORE.get(),block -> createOreDrop(block, DustItemRegistry.CRYSTAL_INERT.get()));
        add(DustBlockRegistry.INERT_CRYSTAL_ORE_DEEPSLATE.get(),block -> createMultipleOreDrops(block, DustItemRegistry.CRYSTAL_INERT.get(),2,5));
    }

    protected LootTable.Builder createMultipleOreDrops(Block block, Item item, float minDrops, float maxDrops)
    {
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable( block,
                this.applyExplosionCondition(block, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops,maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DustBlockRegistry.DUSTBLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
