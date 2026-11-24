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

import java.util.List;
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

        dropSelf(DustBlockRegistry.CRYSTAL_DISPLAY.get());
        // Element crystals: with Silk Touch, the crystal (a small one is the element's crystal item); without, nothing
        // here: CrystalNodeBlock gives an orb of its dust instead. Crystal blocks drop themselves; budding ones nothing.
        for (com.mowmaster.dust.Features.DustComposition.DustElement element : com.mowmaster.dust.Features.DustComposition.DustElements.all()) {
            add(com.mowmaster.dust.Features.Crystals.ElementCrystals.node(element, com.mowmaster.dust.Features.Crystals.CrystalSize.SMALL),
                    block -> createSilkTouchOnlyTable(DustItemRegistry.CRYSTALS.get(element).get()));
            add(com.mowmaster.dust.Features.Crystals.ElementCrystals.node(element, com.mowmaster.dust.Features.Crystals.CrystalSize.MEDIUM),
                    block -> createSilkTouchOnlyTable(com.mowmaster.dust.Features.Crystals.ElementCrystals.MEDIUM_ITEMS.get(element).get()));
            add(com.mowmaster.dust.Features.Crystals.ElementCrystals.node(element, com.mowmaster.dust.Features.Crystals.CrystalSize.LARGE),
                    block -> createSilkTouchOnlyTable(com.mowmaster.dust.Features.Crystals.ElementCrystals.LARGE_ITEMS.get(element).get()));
            dropSelf(com.mowmaster.dust.Features.Crystals.ElementCrystals.BLOCKS.get(element).get());
            add(com.mowmaster.dust.Features.Crystals.ElementCrystals.BUDDING.get(element).get(), noDrop());
        }

        // Glyphs crumble when broken: glyph stones give cobblestone (so players can't collect glyphs from ruins)
        add(DustBlockRegistry.GLYPH_STONE.get(), block -> createSingleItemTableWithSilkTouch(block, Items.COBBLESTONE));

        dropSelf(DustBlockRegistry.ELEMENT_TESTER.get());
        // The Element Crafter and Element Replicator keep their dye colour (copy_components: the block entity's dyed_color
        // onto the dropped item)
        add(DustBlockRegistry.ELEMENT_CRAFTER.get(), block -> LootTable.lootTable().withPool(applyExplosionCondition(block,
                LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(block)
                        .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.DYED_COLOR))))));
        add(DustBlockRegistry.ELEMENT_REPLICATOR.get(), block -> LootTable.lootTable().withPool(applyExplosionCondition(block,
                LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(block)
                        .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(DataComponents.DYED_COLOR))))));
        // The Dust Jar keeps its dust (copy_components: the block entity's dust:dust_jar onto the dropped item)
        add(DustBlockRegistry.DUST_JAR.get(), block -> LootTable.lootTable().withPool(applyExplosionCondition(block,
                LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(LootItem.lootTableItem(block)
                        .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                .include(com.mowmaster.dust.DustRegistries.DustComponentDataRegistry.DUST_JAR.get()))))));
        dropSelf(DustBlockRegistry.DUST_GRINDER.get());
        dropSelf(DustBlockRegistry.GROWTH_LAMP.get());
        // The alchemical apparatus and the Glass Dust Tube (what's inside an apparatus drops when it's broken: its block entity)
        for (var block : List.of(DustBlockRegistry.CRUSHING_MILL, DustBlockRegistry.ALCHEMICAL_SEPARATOR, DustBlockRegistry.EVAPORATING_KETTLE,
                DustBlockRegistry.STILL_FIREBOX, DustBlockRegistry.STILL_POT, DustBlockRegistry.STILL_TOWER, DustBlockRegistry.STILL_TOP, DustBlockRegistry.TRANSMUTER, DustBlockRegistry.CONJOINING_CRUCIBLE, DustBlockRegistry.MIXING_VAT,
                DustBlockRegistry.DISSOLVING_RETORT, DustBlockRegistry.DUST_TUBE, DustBlockRegistry.DUST_CHUTE, DustBlockRegistry.DUST_COLLECTOR,
                DustBlockRegistry.DUST_SEPARATOR, DustBlockRegistry.DUST_SEPARATOR_EXTENSION, DustBlockRegistry.DUST_MIXER,
                DustBlockRegistry.DUST_MIXER_WET, DustBlockRegistry.DUST_CYCLONE, DustBlockRegistry.DUST_CYCLONE_HOPPER, DustBlockRegistry.DUST_CENTRIFUGE)) dropSelf(block.get());
        dropSelf(DustBlockRegistry.LOVELY_LAMP.get());
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
        dropSelf(DustBlockRegistry.NEUTRAL_PYLON.get());
        dropSelf(DustBlockRegistry.PYLON_BLESSED.get());
        dropSelf(DustBlockRegistry.PYLON_CORRUPTED.get());
        dropSelf(DustBlockRegistry.THRIFTY_PYLON.get());
        dropSelf(DustBlockRegistry.GIANT_PYLON.get());
        dropSelf(DustBlockRegistry.DAMAGE_PYLON.get());
        dropSelf(DustBlockRegistry.EXPERIENCED_PYLON.get());

        // The elemental flowers (the Windwheel Aster among them): themselves with shears or Silk Touch, else nothing here
        // (ElementalFlowerBlock gives an orb of its dust instead); their pots give the pot and the flower
        for (com.mowmaster.dust.Features.Flora.ElementalFlower flower : com.mowmaster.dust.Features.Flora.ElementalFlower.values()) {
            Block block = com.mowmaster.dust.Features.Flora.ElementalFlowers.FLOWERS.get(flower).get();
            add(block, createSilkTouchOrShearsDispatchTable(block, net.minecraft.world.level.storage.loot.entries.EmptyLootItem.emptyItem()));
            add(com.mowmaster.dust.Features.Flora.ElementalFlowers.POTS.get(flower).get(), createPotFlowerItemTable(block));
        }

        // The elemental woods (Features/Trees), as vanilla's oak: each block itself (two for a double slab, standing or
        // not), a chest keeps its name, leaves give their sapling and sticks now and then (themselves with shears or Silk
        // Touch), a potted sapling the pot and the sapling. A fruit pod gives its fruit only when ripe.
        for (com.mowmaster.dust.Features.Trees.WoodSet set : com.mowmaster.dust.Features.Trees.ElementalWoods.all()) {
            for (Block block : List.of(set.log().get(), set.wood().get(), set.strippedLog().get(), set.strippedWood().get(), set.planks().get(),
                    set.stairs().get(), set.fence().get(), set.fenceGate().get(), set.button().get(), set.pressurePlate().get(), set.sapling().get())) {
                dropSelf(block);
            }
            add(set.slab().get(), this::createSlabItemTable);
            add(set.verticalSlab().get(), block -> doubleWhen(block, com.mowmaster.dust.Features.Trees.VerticalSlabBlock.DOUBLE));
            add(set.chest().get(), this::createNameableBlockEntityTable);
            add(set.leaves().get(), block -> createLeavesDrops(block, set.sapling().get(), NORMAL_LEAVES_SAPLING_CHANCES));
            add(set.pottedSapling().get(), createPotFlowerItemTable(set.sapling().get()));
            add(set.fruitPod().get(), block -> LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                    .setRolls(ContextIntProviders.exactly(1))
                    .add(LootItem.lootTableItem(set.fruit().get()))
                    .when(net.minecraft.world.level.storage.loot.predicates.MatchBlock.blockMatches(this.blocks, block,
                            net.minecraft.advancements.predicates.StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(com.mowmaster.dust.Features.Trees.ElementalFruitBlock.AGE, com.mowmaster.dust.Features.Trees.ElementalFruitBlock.RIPE))))));
        }
    }

    // A block that drops itself, two of it when this property is true (as a slab when double)
    private LootTable.Builder doubleWhen(Block block, net.minecraft.world.level.block.state.properties.BooleanProperty property)
    {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
                .add(applyExplosionDecay(block, LootItem.lootTableItem(block)
                        .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(2))
                                .when(net.minecraft.world.level.storage.loot.predicates.MatchBlock.blockMatches(this.blocks, block,
                                        net.minecraft.advancements.predicates.StatePropertiesPredicate.Builder.properties().hasProperty(property, true)))))));
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
