package com.mowmaster.dust.Items;

import com.mowmaster.dust.CreativeTabs.DustItemTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mowmaster.dust.References.Constants.MODID;

public class ItemRegistry
{
    public static void onItemRegistryReady(RegistryEvent.Register<Item> e)
    {
        ColorApplicator.onItemRegistryReady(e);
    }



    public static void handleItemColors(ColorHandlerEvent.Item event)
    {
        ColorApplicator.handleItemColors(event);
    }

    /*public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> COLOR_APPLICATOR = ITEMS.register("applicator", () -> new ColorApplicator(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }*/
}
