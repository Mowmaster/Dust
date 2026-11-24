package com.mowmaster.dust.Features.Flora;

import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

// The elemental flowers' blocks (ElementalFlowerBlock) and their flower pots, registered with Dust's other blocks. The
// Windwheel Aster was Dust's first flower and keeps its own ids (DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL,
// BLOCK_POTTEDFLOWER_WINDWHEEL); the rest are <name> and potted_<name>.
public final class ElementalFlowers
{
    public static final Map<ElementalFlower, DeferredBlock<? extends Block>> FLOWERS;
    public static final Map<ElementalFlower, DeferredBlock<? extends Block>> POTS;

    static {
        Map<ElementalFlower, DeferredBlock<? extends Block>> flowers = new EnumMap<>(ElementalFlower.class);
        Map<ElementalFlower, DeferredBlock<? extends Block>> pots = new EnumMap<>(ElementalFlower.class);
        for (ElementalFlower flower : ElementalFlower.values()) {
            if (flower == ElementalFlower.WINDWHEEL) {
                flowers.put(flower, DustBlockRegistry.BLOCK_FLOWER_WHINDWHEEL);
                pots.put(flower, DustBlockRegistry.BLOCK_POTTEDFLOWER_WINDWHEEL);
                continue;
            }
            DeferredBlock<ElementalFlowerBlock> block = DustBlockRegistry.DUSTBLOCKS.registerBlock(flower.id, properties -> new ElementalFlowerBlock(flower, plant(properties)));
            DustItemRegistry.DUSTITEMS.registerItem(flower.id, properties -> new BlockItem(block.get(), compostable(properties.useBlockDescriptionPrefix())));
            flowers.put(flower, block);
            pots.put(flower, DustBlockRegistry.DUSTBLOCKS.registerBlock("potted_" + flower.id, properties -> new FlowerPotBlock(
                    () -> (FlowerPotBlock) Blocks.FLOWER_POT, block, properties.noOcclusion().instabreak().pushReaction(PushReaction.POPPED))));
        }
        FLOWERS = Collections.unmodifiableMap(flowers);
        POTS = Collections.unmodifiableMap(pots);
    }

    private ElementalFlowers() {}

    // Loads the class (and so registers everything above) from the mod constructor (Dust)
    public static void init() {}

    // A flower's block settings, as vanilla's flowers
    public static BlockBehaviour.Properties plant(BlockBehaviour.Properties properties)
    {
        return properties.mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.GRASS)
                .offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.POPPED);
    }

    // 26.3: composting is an item component (vanilla's 65% chance to add a layer, as for flowers); 26.1.2 uses the
    // compostables data map (src/main/resources-26.1.2/data/neoforge/data_maps/item/compostables.json)
    private static net.minecraft.world.item.Item.Properties compostable(net.minecraft.world.item.Item.Properties properties)
    {
        //? if >=26.3 {
        return properties.compostable(net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.COMPOSTABLE_MEDIUM);
        //?} else {
        /*return properties;
        *///?}
    }
}
