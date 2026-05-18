package com.mowmaster.dust.DustDataGen;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustyDelights.Block.CropBlockLettuce;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
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
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
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
        dropSelf(DustBlockRegistry.BLOCK_FENCEGATE_GREEN.get());
        dropSelf(DustBlockRegistry.BLOCK_FENCE_GREEN.get());
        dropSelf(DustBlockRegistry.BLOCK_WALL_GREEN.get());
        add(DustBlockRegistry.BLOCK_DOOR_GREEN.get(), this::createDoorTable);
        dropSelf(DustBlockRegistry.BLOCK_TRAPDOOR_GREEN.get());

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


        add(DustBlockRegistry.CROP_LETTUCE.get(),createCropDrops(
                DustBlockRegistry.CROP_LETTUCE.get(),
                DustItemRegistry.FOOD_LETTUCE.get(),
                DustItemRegistry.SEEDS_LETTUCE.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(DustBlockRegistry.CROP_LETTUCE.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlockLettuce.AGE, CropBlockLettuce.MAX_AGE))));



        dropSelf(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.get());
        add(DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL.get(),createPotFlowerItemTable(DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL.get()));


        dropSelf(DustBlockRegistry.CRYSTAL_STONE_RED.get());
        dropSelf(DustBlockRegistry.CRYSTAL_STONE_GREEN.get());
        dropSelf(DustBlockRegistry.CRYSTAL_STONE_BLUE.get());
        dropSelf(DustBlockRegistry.CRYSTAL_STONE_WHITE.get());
        dropSelf(DustBlockRegistry.CRYSTAL_STONE_BLACK.get());


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
