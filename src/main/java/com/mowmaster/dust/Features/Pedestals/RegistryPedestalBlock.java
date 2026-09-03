package com.mowmaster.dust.Features.Pedestals;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustItemRegistry;
import com.mowmaster.dust.Features.CrystalBlocks.Block.CrystalStone;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class RegistryPedestalBlock {
    public static final DeferredRegister.Blocks PEDESTALBLOCKS = DeferredRegister.createBlocks(DustReferences.MODID+"_pedestal");

    public static final DeferredBlock<Block> PEDESTAL_BLOCK = registerBlock("pedestal_block",
            properties -> new BlockBasePedestal(properties
                    .mapColor(MapColor.STONE).sound(SoundType.STONE).strength(2f).noOcclusion()));



    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function)
    {
        DeferredBlock<T> toReturn = PEDESTALBLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        RegistryPedestalItem.PEDESTALITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus)
    {
        PEDESTALBLOCKS.register(eventBus);
    }
}
