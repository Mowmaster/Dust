package com.mowmaster.dust.item;


import com.mowmaster.dust.blocks.BlockDustStone;
import com.mowmaster.dust.blocks.BlockPedestal;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;

public class ItemRegistry
{
    public static void onItemRegistryReady(RegistryEvent.Register<Item> event)
    {
        BlockDustStone.onItemRegistryReady(event);
        BlockPedestal.onItemRegistryReady(event);
        ItemDust.onItemRegistryReady(event);
        ItemColorDust.onItemRegistryReady(event);
        ItemCrystalWrench.onItemRegistryReady(event);
    }

    public static void onItemColorsReady(ColorHandlerEvent.Item event)
    {
        BlockDustStone.handleItemColors(event);
        ItemColorDust.handleItemColors(event);
    }
}
