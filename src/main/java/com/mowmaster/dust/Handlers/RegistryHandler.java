/*
package com.mowmaster.dust.Handlers;

import com.mowmaster.dust.Blocks.BlockRegistry;
import com.mowmaster.dust.Blocks.GeneratedBlocks.BaseCrystalNodeBlock;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mowmaster.dust.References.Constants.MODID;

public class RegistryHandler
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static void init() {
        ItemRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Item> COLOR_APPLICATOR = ITEMS.register("applicator", ColorApplicator::new);
}
    public static final RegistryObject<Block> CRYSTAL_NODE = BLOCKS.register("block_inertnode", BaseCrystalNodeBlock::new);
    public static final RegistryObject<Item> CRYSTAL_NODE_ITEM = ITEMS.register("item_inertnode", () -> new BaseCrystalNodeBlock(CRYSTAL_NODE.get()));
*/
