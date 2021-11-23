package com.mowmaster.dust.Blocks;

import com.mowmaster.dust.Blocks.GeneratedBlocks.BaseCrystalClusterBlock;
import com.mowmaster.dust.Blocks.GeneratedBlocks.BaseCrystalNodeBlock;
import com.mowmaster.dust.CreativeTabs.DustBlockTabs;
import com.mowmaster.dust.Items.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.mowmaster.dust.References.Constants.MODID;

public class BlockRegistry
{
    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        BaseCrystalClusterBlock.onBlockRegistryReady(event);
        BaseCrystalNodeBlock.onBlockRegistryReady(event);
    }

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        BaseCrystalClusterBlock.onItemRegistryReady(event);
        BaseCrystalNodeBlock.onItemRegistryReady(event);
    }



    public static void handleItemColors(ColorHandlerEvent.Item event)
    {
        BaseCrystalClusterBlock.handleItemColors(event);
        BaseCrystalNodeBlock.handleItemColors(event);
    }

    public static void handleBlockColors(ColorHandlerEvent.Block event)
    {
        BaseCrystalClusterBlock.handleBlockColors(event);
        BaseCrystalNodeBlock.handleBlockColors(event);
    }



    /*public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);


    public static final RegistryObject<Block> RUBY_BLOCK = registerBlock("ruby_block", () -> new Block(Block.Properties.of(Material.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(DustBlockTabs.TAB_BLOCKS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }*/
}
