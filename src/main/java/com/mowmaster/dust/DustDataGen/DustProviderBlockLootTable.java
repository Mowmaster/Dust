package com.mowmaster.dust.DustDataGen;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.core.component.DataComponents;
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
        dropSelf(DustBlockRegistry.SPELL_CASTER.get());
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
        // The primal ores drop their primal's crystal, the same amounts as the inert ores
        DustBlockRegistry.PRIMAL_ORES.forEach((primal, ore) -> add(ore.get(),
                block -> createOreDrop(block, DustItemRegistry.CRYSTALS.get(com.mowmaster.dust.Features.DustComposition.DustElements.of(primal)).get())));
        DustBlockRegistry.PRIMAL_ORES_DEEPSLATE.forEach((primal, ore) -> add(ore.get(),
                block -> createMultipleOreDrops(block, DustItemRegistry.CRYSTALS.get(com.mowmaster.dust.Features.DustComposition.DustElements.of(primal)).get(), 2, 5)));

        // Glyphs crumble when broken: glyph stones give cobblestone (so players can't collect glyphs from ruins)
        add(DustBlockRegistry.GLYPH_STONE.get(), block -> createSingleItemTableWithSilkTouch(block, Items.COBBLESTONE));

        dropSelf(DustBlockRegistry.ELEMENT_TESTER.get());
        // The Element Crafter keeps its dye colour (copy_components: the block entity's dyed_color onto the dropped item)
        add(DustBlockRegistry.ELEMENT_CRAFTER.get(), block -> LootTable.lootTable().withPool(applyExplosionCondition(block,
                LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(block)
                        .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.DYED_COLOR))))));
        dropSelf(DustBlockRegistry.RESEARCH_CRUSHER.get());
        dropSelf(DustBlockRegistry.RESEARCH_SEPARATION.get());
        dropSelf(DustBlockRegistry.RESEARCH_EVAPORATION.get());
        dropSelf(DustBlockRegistry.RESEARCH_DISTILLATION.get());
        dropSelf(DustBlockRegistry.RESEARCH_TRANSFORMATION.get());
        dropSelf(DustBlockRegistry.RESEARCH_COMBINATION.get());
        dropSelf(DustBlockRegistry.RESEARCH_MIXING.get());
        // The Spell Pylon drops itself in code, remembering its element and levels (SpellPylonBlockEntity), so it has
        // an empty loot table here
        add(DustBlockRegistry.SPELL_PYLON.get(), noDrop());
        dropSelf(DustBlockRegistry.SHAPE_PYLON.get());
        dropSelf(DustBlockRegistry.CHANGE_PYLON.get());
        dropSelf(DustBlockRegistry.COST_PYLON.get());
        dropSelf(DustBlockRegistry.COUNT_PYLON.get());
        dropSelf(DustBlockRegistry.DAMAGE_PYLON.get());

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
