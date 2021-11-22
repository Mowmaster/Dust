package com.mowmaster.dust.Items;

import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;

public class ItemRegistry
{
    public static void onItemRegistryReady(RegistryEvent.Register<Item> e)
    {
        //ItemEnchantableBook.onItemRegistryReady(e);
    }

    public static void handleItemColors(ColorHandlerEvent.Item event)
    {
        //ItemEnchantableBook.handleItemColors(event);
    }
}
