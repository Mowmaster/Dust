package com.mowmaster.dust.Client;

import com.mowmaster.dust.Blocks.BlockRegistry;
import com.mowmaster.dust.Items.ItemRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "dust", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry
{
    @SubscribeEvent
    public static void onItemColorsReady(ColorHandlerEvent.Item event)
    {
        BlockRegistry.handleItemColors(event);
        ItemRegistry.handleItemColors(event);
    }

    @SubscribeEvent
    public static void onBlockColorsReady(ColorHandlerEvent.Block event)
    {
        BlockRegistry.handleBlockColors(event);
    }
}
