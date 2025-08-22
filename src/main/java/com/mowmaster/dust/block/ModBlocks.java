package com.mowmaster.dust.block;

import com.mowmaster.dust.Dust;
import com.mowmaster.dust.block.dustFire.DustFireRedBlock;
import com.mowmaster.dust.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    // Create a Deferred Register to hold Blocks which will all be registered under the "dust" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Dust.MODID);

    // Creates a new Block with the id "dust:example_block", combining the namespace and path
    public static final DeferredBlock<Block> DUSTBLOCK_RED = registerBlock("dustblock_red", () -> new Block(BlockBehaviour.Properties.of()
            .strength(1f).requiresCorrectToolForDrops().sound(SoundType.SAND).mapColor(DyeColor.RED)));

    public static final DeferredBlock<Block> DUSTFIREBLOCK_RED = registerBlock("dustfireblock_red", () -> new DustFireRedBlock(BlockBehaviour.Properties.of()
            .mapColor(DyeColor.RED).replaceable().noCollission().instabreak().lightLevel((p_50884_) -> 10).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));





    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
