package com.mowmaster.dust.Features.Trees;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

// Everything one elemental wood is made of (registered by ElementalWoods). Ids, for wood <w>:
//   <w>_log, <w>_wood, stripped_<w>_log, stripped_<w>_wood, <w>_planks, <w>_stairs, <w>_slab, <w>_vertical_slab,
//   <w>_fence, <w>_fence_gate, <w>_button, <w>_pressure_plate, <w>_chest, <w>_leaves, <w>_sapling,
//   potted_<w>_sapling (no item), <w>_fruit_pod (no item: the fruit grows only on trees), and the item <w>_fruit
public record WoodSet(
        ElementalWood type,
        DeferredBlock<WoodBlocks.Log> log,
        DeferredBlock<WoodBlocks.Log> wood,
        DeferredBlock<WoodBlocks.Log> strippedLog,
        DeferredBlock<WoodBlocks.Log> strippedWood,
        DeferredBlock<WoodBlocks.Planks> planks,
        DeferredBlock<WoodBlocks.Stairs> stairs,
        DeferredBlock<WoodBlocks.Slab> slab,
        DeferredBlock<VerticalSlabBlock> verticalSlab,
        DeferredBlock<WoodBlocks.Fence> fence,
        DeferredBlock<WoodBlocks.FenceGate> fenceGate,
        DeferredBlock<ButtonBlock> button,
        DeferredBlock<PressurePlateBlock> pressurePlate,
        DeferredBlock<ElementalChestBlock> chest,
        DeferredBlock<WoodBlocks.Leaves> leaves,
        DeferredBlock<ElementalSaplingBlock> sapling,
        DeferredBlock<FlowerPotBlock> pottedSapling,
        DeferredBlock<ElementalFruitBlock> fruitPod,
        DeferredItem<ElementalFruitItem> fruit)
{
    // The four logs (logs and wood, plain and stripped): what its log tag holds
    public List<DeferredBlock<? extends Block>> logs()
    {
        return List.of(log, wood, strippedLog, strippedWood);
    }

    // Every block that has an item, in creative tab order
    public List<DeferredBlock<? extends Block>> itemBlocks()
    {
        return List.of(log, wood, strippedLog, strippedWood, planks, stairs, slab, verticalSlab, fence, fenceGate, button, pressurePlate,
                chest, leaves, sapling);
    }
}
