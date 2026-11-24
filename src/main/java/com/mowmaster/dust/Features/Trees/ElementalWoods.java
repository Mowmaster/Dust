package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

// Registers every elemental wood's blocks and items (WoodSet) with Dust's other blocks and items, and the charcoals
// (Charcoal: <colour>_charcoal). Block settings are vanilla's for the same blocks (Blocks: oak's family, chest, leaves,
// sapling), coloured on the map as the wood is.
// Burning in a furnace: 26.3 gives each item vanilla's cooking-time entry for its kind (logs, planks, stairs, fences,
// gates, pressure plates and chests 300 ticks, slabs and vertical slabs 150, buttons and saplings 100) and each charcoal
// its own (DustWorldgen.charcoalFuels); 26.1.2 has NeoForge's furnace fuel data map instead, which covers the tagged
// kinds, with chests, vertical slabs and charcoals added in src/main/resources-26.1.2/data/neoforge/data_maps/item/furnace_fuels.json.
public final class ElementalWoods
{
    public static final Map<ElementalWood, WoodSet> SETS;
    public static final Map<Charcoal, DeferredItem<CharcoalItem>> CHARCOAL;

    static {
        Map<ElementalWood, WoodSet> sets = new EnumMap<>(ElementalWood.class);
        for (ElementalWood wood : ElementalWood.values()) sets.put(wood, register(wood));
        SETS = Collections.unmodifiableMap(sets);
        Map<Charcoal, DeferredItem<CharcoalItem>> charcoal = new EnumMap<>(Charcoal.class);
        for (Charcoal kind : Charcoal.values()) {
            charcoal.put(kind, DustItemRegistry.DUSTITEMS.registerItem(kind.itemName(), properties -> new CharcoalItem(kind, charcoalFuel(properties, kind))));
        }
        CHARCOAL = Collections.unmodifiableMap(charcoal);
    }

    private ElementalWoods() {}

    // Loads the class (and so registers everything above) from the mod constructor (Dust)
    public static void init() {}

    public static WoodSet set(ElementalWood wood)
    {
        return SETS.get(wood);
    }

    public static Collection<WoodSet> all()
    {
        return SETS.values();
    }

    private static WoodSet register(ElementalWood wood)
    {
        String id = wood.id;
        MapColor color = wood.mapColor;
        DeferredBlock<WoodBlocks.Log> strippedLog = block("stripped_" + id + "_log", p -> new WoodBlocks.Log(log(p, color)));
        DeferredBlock<WoodBlocks.Log> strippedWood = block("stripped_" + id + "_wood", p -> new WoodBlocks.Log(log(p, color)));
        DeferredBlock<WoodBlocks.Log> log = block(id + "_log", p -> new WoodBlocks.Log(log(p, color)));
        DeferredBlock<WoodBlocks.Log> woodBlock = block(id + "_wood", p -> new WoodBlocks.Log(log(p, color)));
        DeferredBlock<WoodBlocks.Planks> planks = block(id + "_planks", p -> new WoodBlocks.Planks(planks(p, color)));
        DeferredBlock<WoodBlocks.Stairs> stairs = DustBlockRegistry.DUSTBLOCKS.registerBlock(id + "_stairs",
                p -> new WoodBlocks.Stairs(planks.get().defaultBlockState(), p), () -> BlockBehaviour.Properties.ofFullCopy(planks.get()));
        DeferredBlock<WoodBlocks.Slab> slab = DustBlockRegistry.DUSTBLOCKS.registerBlock(id + "_slab",
                WoodBlocks.Slab::new, () -> BlockBehaviour.Properties.ofFullCopy(planks.get()));
        DeferredBlock<VerticalSlabBlock> verticalSlab = DustBlockRegistry.DUSTBLOCKS.registerBlock(id + "_vertical_slab",
                VerticalSlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(planks.get()));
        DeferredBlock<WoodBlocks.Fence> fence = block(id + "_fence", p -> new WoodBlocks.Fence(
                p.mapColor(color).forceSolidOn().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()));
        DeferredBlock<WoodBlocks.FenceGate> fenceGate = block(id + "_fence_gate", p -> new WoodBlocks.FenceGate(wood.woodType,
                p.mapColor(color).forceSolidOn().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).ignitedByLava()));
        DeferredBlock<ButtonBlock> button = block(id + "_button", p -> new ButtonBlock(wood.setType, 30,
                p.noCollision().strength(0.5F).pushReaction(PushReaction.POPPED)));
        DeferredBlock<PressurePlateBlock> pressurePlate = block(id + "_pressure_plate", p -> new PressurePlateBlock(wood.setType,
                p.mapColor(color).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5F).ignitedByLava().pushReaction(PushReaction.POPPED)));
        DeferredBlock<ElementalChestBlock> chest = block(id + "_chest", p -> new ElementalChestBlock(wood,
                p.mapColor(color).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava()));
        DeferredBlock<WoodBlocks.Leaves> leaves = block(id + "_leaves", p -> new WoodBlocks.Leaves(wood.leafColor, leaves(p)));
        DeferredBlock<ElementalSaplingBlock> sapling = block(id + "_sapling", p -> new ElementalSaplingBlock(wood,
                p.mapColor(MapColor.PLANT).noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.POPPED)));
        DeferredBlock<FlowerPotBlock> pottedSapling = block("potted_" + id + "_sapling", p -> new FlowerPotBlock(
                () -> (FlowerPotBlock) Blocks.FLOWER_POT, sapling, p.instabreak().noOcclusion().pushReaction(PushReaction.POPPED)));
        DeferredItem<ElementalFruitItem> fruit = DustItemRegistry.DUSTITEMS.registerItem(id + "_fruit", p -> new ElementalFruitItem(wood, compostable(p)));
        DeferredBlock<ElementalFruitBlock> fruitPod = block(id + "_fruit_pod", p -> new ElementalFruitBlock(wood, fruit,
                p.mapColor(MapColor.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.POPPED)));

        for (DeferredBlock<WoodBlocks.Log> each : java.util.List.of(log, woodBlock, strippedLog, strippedWood)) item(each, ElementalWoods::woodFuel);
        item(planks, ElementalWoods::woodFuel);
        item(stairs, ElementalWoods::woodFuel);
        item(slab, ElementalWoods::slabFuel);
        item(verticalSlab, ElementalWoods::slabFuel);
        item(fence, ElementalWoods::woodFuel);
        item(fenceGate, ElementalWoods::woodFuel);
        item(button, ElementalWoods::smallFuel);
        item(pressurePlate, ElementalWoods::woodFuel);
        item(chest, p -> woodFuel(p.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
        item(leaves, ElementalWoods::leafCompost);
        item(sapling, ElementalWoods::saplingFuel);
        return new WoodSet(wood, log, woodBlock, strippedLog, strippedWood, planks, stairs, slab, verticalSlab, fence, fenceGate, button,
                pressurePlate, chest, leaves, sapling, pottedSapling, fruitPod, fruit);
    }

    private static <B extends net.minecraft.world.level.block.Block> DeferredBlock<B> block(String name, Function<BlockBehaviour.Properties, B> factory)
    {
        return DustBlockRegistry.DUSTBLOCKS.registerBlock(name, factory);
    }

    private static void item(DeferredBlock<? extends net.minecraft.world.level.block.Block> block, Function<Item.Properties, Item.Properties> settings)
    {
        DustItemRegistry.DUSTITEMS.registerItem(block.getId().getPath(), p -> new BlockItem(block.get(), settings.apply(p.useBlockDescriptionPrefix())));
    }

    private static BlockBehaviour.Properties log(BlockBehaviour.Properties p, MapColor color)
    {
        return p.mapColor(color).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava();
    }

    private static BlockBehaviour.Properties planks(BlockBehaviour.Properties p, MapColor color)
    {
        return p.mapColor(color).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava();
    }

    private static BlockBehaviour.Properties leaves(BlockBehaviour.Properties p)
    {
        p.mapColor(MapColor.PLANT).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()
                .isValidSpawn(Blocks::ocelotOrParrot)
                .isSuffocating((state, level, pos) -> false).ignitedByLava().pushReaction(PushReaction.POPPED)
                .isRedstoneConductor((state, level, pos) -> false);
        //? if <26.3 {
        /*p.isViewBlocking((state, level, pos) -> false);
        *///?}
        return p;
    }

    // Furnace burn times and composting: item components on 26.3 (vanilla's entries for each kind), data maps on 26.1.2
    private static Item.Properties woodFuel(Item.Properties p)
    {
        //? if >=26.3 {
        return p.cookingFuel(net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.COOKING_TIME_WOOD_BLOCKS);
        //?} else {
        /*return p;
        *///?}
    }

    private static Item.Properties slabFuel(Item.Properties p)
    {
        //? if >=26.3 {
        return p.cookingFuel(net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.COOKING_TIME_WOOD_SLABS);
        //?} else {
        /*return p;
        *///?}
    }

    private static Item.Properties smallFuel(Item.Properties p)
    {
        //? if >=26.3 {
        return p.cookingFuel(net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.COOKING_TIME_WOOD_ITEMS_EXTRA_SMALL);
        //?} else {
        /*return p;
        *///?}
    }

    private static Item.Properties saplingFuel(Item.Properties p)
    {
        //? if >=26.3 {
        return p.compostable(net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.COMPOSTABLE_LOW)
                .cookingFuel(net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.COOKING_TIME_DRY_PLANTS);
        //?} else {
        /*return p;
        *///?}
    }

    private static Item.Properties leafCompost(Item.Properties p)
    {
        //? if >=26.3 {
        return p.compostable(net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.COMPOSTABLE_LOW);
        //?} else {
        /*return p;
        *///?}
    }

    private static Item.Properties compostable(Item.Properties p)
    {
        //? if >=26.3 {
        return p.compostable(net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.COMPOSTABLE_MEDIUM);
        //?} else {
        /*return p;
        *///?}
    }

    private static Item.Properties charcoalFuel(Item.Properties p, Charcoal kind)
    {
        //? if >=26.3 {
        return p.cookingFuel(kind.fuelKey());
        //?} else {
        /*return p;
        *///?}
    }
}
