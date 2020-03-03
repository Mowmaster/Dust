package com.mowmaster.dust;

import com.mowmaster.dust.blocks.BlockDustStone;
import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.crafting.Recipes;
import com.mowmaster.dust.crafting.SpellCraftingBasic;
import com.mowmaster.dust.item.ItemColorDust;
import com.mowmaster.dust.item.ItemDust;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LifecycleEventProvider;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IRegistryDelegate;


@EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD)
@Mod(Reference.MODID)
public class dust
{



    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        BlockDustStone.onItemRegistryReady(event);
        BlockPedestal.onItemRegistryReady(event);
        ItemDust.onItemRegistryReady(event);
        ItemColorDust.onItemRegistryReady(event);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        BlockDustStone.onBlockRegistryReady(event);
        BlockPedestal.onBlockRegistryReady(event);

    }

    @SubscribeEvent
    public static void onTileRegistryReady(RegistryEvent.Register<TileEntityType<?>> event)
    {
        TilePedestal.onTileEntityRegistry(event);
    }

    @SubscribeEvent
    public static void onBlockColorsReady(ColorHandlerEvent.Block event)
    {
        BlockDustStone.handleBlockColors(event);
    }

    @SubscribeEvent
    public static void onItemColorsReady(ColorHandlerEvent.Item event)
    {
        BlockDustStone.handleItemColors(event);
        ItemColorDust.handleItemColors(event);
    }

    @SubscribeEvent
    public static void onDataGatheredReady(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(new Recipes(gen));

    }


}