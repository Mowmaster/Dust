package com.mowmaster.dust.Blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockRegistry
{
    @SubscribeEvent
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        //CrystalBlock.onItemRegistryReady(event);
    }

    @SubscribeEvent
    public static void onBlockRegistryReady(RegistryEvent.Register<Block> event)
    {
        //CrystalBlock.onBlockRegistryReady(event);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event)
    {
        //CrystalBlock.handleItemColors(event);
    }

    public static void handleBlockColors(ColorHandlerEvent.Block event)
    {
        //CrystalBlock.handleBlockColors(event);
    }
}
