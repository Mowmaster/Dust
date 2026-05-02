package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustUtils.DustReferences;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class DustBlockRegistry {
    public static final DeferredRegister.Blocks DUSTBLOCKS = DeferredRegister.createBlocks(DustReferences.MODID);

    public static final DeferredBlock<Block> BLOCK_OF_DUST_RED = registerBlock("blockofdust_red", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_GREEN = registerBlock("blockofdust_green", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_BLUE = registerBlock("blockofdust_blue", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_WHITE = registerBlock("blockofdust_white", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_DUST_BLACK = registerBlock("blockofdust_black", properties -> new Block(
            properties.sound(SoundType.SAND).strength(2f).requiresCorrectToolForDrops()));


    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_RED = registerBlock("blockofcrystal_red", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_GREEN = registerBlock("blockofcrystal_green", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_BLUE = registerBlock("blockofcrystal_blue", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_WHITE = registerBlock("blockofcrystal_white", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BLOCK_OF_CRYSTAL_BLACK = registerBlock("blockofcrystal_black", properties -> new Block(
            properties.sound(SoundType.AMETHYST).strength(4f).requiresCorrectToolForDrops()));


    public static final DeferredBlock<Block> INERT_CRYSTAL_ORE = registerBlock("block_inert_crystal_ore", properties -> new DropExperienceBlock(
            UniformInt.of(2,4), properties.sound(SoundType.STONE).strength(4f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> INERT_CRYSTAL_ORE_DEEPSLATE = registerBlock("block_inert_crystal_ore_deepslate", properties -> new DropExperienceBlock(
            UniformInt.of(2,4), properties.sound(SoundType.DEEPSLATE).strength(5f).requiresCorrectToolForDrops()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function)
    {
        DeferredBlock<T> toReturn = DUSTBLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        DustItemRegistry.DUSTITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus)
    {
        DUSTBLOCKS.register(eventBus);
    }
}
