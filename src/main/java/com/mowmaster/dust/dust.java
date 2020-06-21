package com.mowmaster.dust;

import com.mowmaster.dust.blocks.*;
import com.mowmaster.dust.blocks.TreeBits.BlockDustLeaves;
import com.mowmaster.dust.blocks.TreeBits.BlockDustLogs;
import com.mowmaster.dust.blocks.TreeBits.BlockDustPlanks;
import com.mowmaster.dust.configtabs.CreativeTab;
import com.mowmaster.dust.configtabs.CreativeTabBlock;
import com.mowmaster.dust.crafting.Recipes;
import com.mowmaster.dust.item.ItemRegistry;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import com.mowmaster.dust.tiles.TilePedestal;
import com.mowmaster.dust.tiles.TileTrap;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;


@EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD)
@Mod(Reference.MODID)
public class dust
{

    public static final ItemGroup ITEM_GROUP = new CreativeTab();
    public static final ItemGroup BLOCK_GROUP = new CreativeTabBlock();

    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        ItemRegistry.onItemRegistryReady(event);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        BlockDustStone.onBlockRegistryReady(event);
        BlockDustPlanks.onBlockRegistryReady(event);
        BlockDustLeaves.onBlockRegistryReady(event);
        BlockDustLogs.onBlockRegistryReady(event);
        BlockPedestalTE.onBlockRegistryReady(event);
        BlockVoidPot.onBlockRegistryReady(event);
        BlockTrap.onBlockRegistryReady(event);
        BlockCrystalClusterTE.onBlockRegistryReady(event);
    }

    @SubscribeEvent
    public static void onTileRegistryReady(RegistryEvent.Register<TileEntityType<?>> event)
    {
        TilePedestal.onTileEntityRegistry(event);
        TileTrap.onTileEntityRegistry(event);
        TileCrystalCluster.onTileEntityRegistry(event);
    }

    @SubscribeEvent
    public static void onBlockColorsReady(ColorHandlerEvent.Block event)
    {
        BlockDustStone.handleBlockColors(event);
        BlockDustPlanks.handleBlockColors(event);
        BlockDustLogs.handleBlockColors(event);
        BlockDustLeaves.handleBlockColors(event);
        BlockPedestalTE.handleBlockColors(event);
    }

    @SubscribeEvent
    public static void onItemColorsReady(ColorHandlerEvent.Item event)
    {
        ItemRegistry.onItemColorsReady(event);
    }

    @SubscribeEvent
    public static void onDataGatheredReady(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(new Recipes(gen));

    }


}