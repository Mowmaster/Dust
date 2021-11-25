package com.mowmaster.dust.Blocks.TileEntityBlocks;

import com.mowmaster.dust.Blocks.BuildingBlocks.BaseColoredStairBlock;
import com.mowmaster.dust.Blocks.TileEntityBlocks.Pedestal.BasePedestalBlock;
import com.mowmaster.dust.CreativeTabs.DustBlockTabs;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.mowmaster.dust.References.Constants.MODID;

public class DeferredRegisterTileBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);


    public static final RegistryObject<Block> BLOCK_PEDESTAL = registerBlock("block_pedestal",
            () -> new BasePedestalBlock(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE)));




    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        DeferredRegisterItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(DustBlockTabs.TAB_BLOCKS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
