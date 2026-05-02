package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustUtils.DustReferences;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DustItemRegistry
{
    public static final DeferredRegister.Items DUSTITEMS = DeferredRegister.createItems(DustReferences.MODID);

    public static final DeferredItem<Item> DUST_RED = DUSTITEMS.registerSimpleItem("dust_red", properties -> properties);
    public static final DeferredItem<Item> DUST_GREEN = DUSTITEMS.registerSimpleItem("dust_green", properties -> properties);
    public static final DeferredItem<Item> DUST_BLUE = DUSTITEMS.registerSimpleItem("dust_blue", properties -> properties);
    public static final DeferredItem<Item> DUST_WHITE = DUSTITEMS.registerSimpleItem("dust_white", properties -> properties);
    public static final DeferredItem<Item> DUST_BLACK = DUSTITEMS.registerSimpleItem("dust_black", properties -> properties);


    public static final DeferredItem<Item> CRYSTAL_RED = DUSTITEMS.registerSimpleItem("crystal_red", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_GREEN = DUSTITEMS.registerSimpleItem("crystal_green", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_BLUE = DUSTITEMS.registerSimpleItem("crystal_blue", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_WHITE = DUSTITEMS.registerSimpleItem("crystal_white", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_BLACK = DUSTITEMS.registerSimpleItem("crystal_black", properties -> properties);


    public static void register(IEventBus eventBus)
    {
        DUSTITEMS.register(eventBus);
    }

}
