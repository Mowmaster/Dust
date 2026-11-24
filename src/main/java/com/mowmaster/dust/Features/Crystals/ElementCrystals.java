package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Every element's crystal blocks, in element order (registered with Dust's other blocks and items):
//   <element>_crystal_small / _medium / _large   crystals growing off a face (CrystalNodeBlock), each with its own item
//                                                drawn as the crystal itself (the 3D model, not a flat icon), which
//                                                tells them apart from the alchemical crystals (DustItemRegistry.CRYSTALS).
//                                                The large one carries its charge for Crystal Displays (LargeCrystalItem).
//   <element>_crystal_block                      a block of the crystal (CrystalBlock), its own texture per element
//   budding_<element>_crystal                    grows the crystals (BuddingCrystalBlock), no item; the crystal block's
//                                                texture with one hole in the middle
// Names are shared ("Medium Crystal"...); the element shows in the tooltip, as for the crystal items (it may not have
// been discovered yet).
public final class ElementCrystals
{
    private static final Map<DustElement, DeferredBlock<CrystalNodeBlock>> SMALL = new LinkedHashMap<>();
    private static final Map<DustElement, DeferredBlock<CrystalNodeBlock>> MEDIUM = new LinkedHashMap<>();
    private static final Map<DustElement, DeferredBlock<CrystalNodeBlock>> LARGE = new LinkedHashMap<>();
    public static final Map<DustElement, DeferredBlock<CrystalBlock>> BLOCKS;
    public static final Map<DustElement, DeferredBlock<BuddingCrystalBlock>> BUDDING;
    public static final Map<DustElement, DeferredItem<BlockItem>> SMALL_ITEMS;
    public static final Map<DustElement, DeferredItem<BlockItem>> MEDIUM_ITEMS;
    public static final Map<DustElement, DeferredItem<LargeCrystalItem>> LARGE_ITEMS;
    public static final Map<DustElement, DeferredItem<BlockItem>> BLOCK_ITEMS;

    static {
        Map<DustElement, DeferredBlock<CrystalBlock>> blocks = new LinkedHashMap<>();
        Map<DustElement, DeferredBlock<BuddingCrystalBlock>> budding = new LinkedHashMap<>();
        Map<DustElement, DeferredItem<BlockItem>> smallItems = new LinkedHashMap<>();
        Map<DustElement, DeferredItem<BlockItem>> mediumItems = new LinkedHashMap<>();
        Map<DustElement, DeferredItem<LargeCrystalItem>> largeItems = new LinkedHashMap<>();
        Map<DustElement, DeferredItem<BlockItem>> blockItems = new LinkedHashMap<>();
        for (DustElement element : DustElements.all()) {
            String name = element.id().getPath();
            for (CrystalSize size : CrystalSize.values()) {
                DeferredBlock<CrystalNodeBlock> node = DustBlockRegistry.DUSTBLOCKS.registerBlock(name + "_crystal_" + size.getSerializedName(),
                        properties -> new CrystalNodeBlock(element, size, properties.mapColor(MapColor.COLOR_PURPLE).forceSolidOn().noOcclusion()
                                .sound(sound(size)).strength(1.5F).lightLevel(state -> size.light).pushReaction(PushReaction.POPPED)
                                .overrideDescription("block.dust.crystal_" + size.getSerializedName())));
                (size == CrystalSize.SMALL ? SMALL : size == CrystalSize.MEDIUM ? MEDIUM : LARGE).put(element, node);
            }
            blocks.put(element, DustBlockRegistry.DUSTBLOCKS.registerBlock(name + "_crystal_block",
                    properties -> new CrystalBlock(element, properties.mapColor(MapColor.COLOR_PURPLE).strength(1.5F).sound(SoundType.AMETHYST)
                            .requiresCorrectToolForDrops().overrideDescription("block.dust.crystal_block"))));
            budding.put(element, DustBlockRegistry.DUSTBLOCKS.registerBlock("budding_" + name + "_crystal",
                    properties -> new BuddingCrystalBlock(element, properties.mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(1.5F)
                            .sound(SoundType.AMETHYST).requiresCorrectToolForDrops().pushReaction(PushReaction.POPPED)
                            .overrideDescription("block.dust.budding_crystal"))));
            DeferredBlock<CrystalNodeBlock> small = SMALL.get(element), medium = MEDIUM.get(element), large = LARGE.get(element);
            DeferredBlock<CrystalBlock> block = blocks.get(element);
            smallItems.put(element, DustItemRegistry.DUSTITEMS.registerItem(name + "_crystal_small",
                    properties -> new BlockItem(small.get(), properties.overrideDescription("block.dust.crystal_small"))));
            mediumItems.put(element, DustItemRegistry.DUSTITEMS.registerItem(name + "_crystal_medium",
                    properties -> new BlockItem(medium.get(), properties.overrideDescription("block.dust.crystal_medium"))));
            largeItems.put(element, DustItemRegistry.DUSTITEMS.registerItem(name + "_crystal_large",
                    properties -> new LargeCrystalItem(large.get(), properties.overrideDescription("block.dust.crystal_large"))));
            blockItems.put(element, DustItemRegistry.DUSTITEMS.registerItem(name + "_crystal_block",
                    properties -> new BlockItem(block.get(), properties.overrideDescription("block.dust.crystal_block"))));
        }
        BLOCKS = Collections.unmodifiableMap(blocks);
        BUDDING = Collections.unmodifiableMap(budding);
        SMALL_ITEMS = Collections.unmodifiableMap(smallItems);
        MEDIUM_ITEMS = Collections.unmodifiableMap(mediumItems);
        LARGE_ITEMS = Collections.unmodifiableMap(largeItems);
        BLOCK_ITEMS = Collections.unmodifiableMap(blockItems);
    }

    private ElementCrystals() {}

    // Loads the class (and so registers everything above) from the mod constructor (Dust)
    public static void init() {}

    private static SoundType sound(CrystalSize size)
    {
        return switch (size) {
            case SMALL -> SoundType.SMALL_AMETHYST_BUD;
            case MEDIUM -> SoundType.MEDIUM_AMETHYST_BUD;
            case LARGE -> SoundType.LARGE_AMETHYST_BUD;
        };
    }

    // The element's crystal of this size
    public static CrystalNodeBlock node(DustElement element, CrystalSize size)
    {
        return (size == CrystalSize.SMALL ? SMALL : size == CrystalSize.MEDIUM ? MEDIUM : LARGE).get(element).get();
    }

    // One element's three crystals (small, medium, large): their models are tinted the element's colour
    public static List<Block> nodesOf(DustElement element)
    {
        return List.of(SMALL.get(element).get(), MEDIUM.get(element).get(), LARGE.get(element).get());
    }

    // Every crystal block of one element (all sizes, the block, the budding block)
    public static List<Block> allOf(DustElement element)
    {
        return List.of(SMALL.get(element).get(), MEDIUM.get(element).get(), LARGE.get(element).get(),
                BLOCKS.get(element).get(), BUDDING.get(element).get());
    }

    // Every crystal block there is
    public static List<Block> all()
    {
        List<Block> out = new ArrayList<>();
        for (DustElement element : DustElements.all()) out.addAll(allOf(element));
        return out;
    }

    // The element of a crystal block's item (small, medium, large, block), or null
    public static @Nullable DustElement elementOf(ItemStack stack)
    {
        return stack.getItem() instanceof BlockItem item && item.getBlock() instanceof ElementCrystal crystal ? crystal.element() : null;
    }
}
