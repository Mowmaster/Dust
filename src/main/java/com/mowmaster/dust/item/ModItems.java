package com.mowmaster.dust.item;

import com.mowmaster.dust.Dust;
import com.mowmaster.dust.item.wand.BasicWandRed;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Dust.MODID);


    // Creates a new food item with the id "dust:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> DUST_RED = ITEMS.register("dust_red", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WAND_BASIC_RED = ITEMS.register("wand_basic_red", () -> new BasicWandRed(new Item.Properties().durability(20)));

    public static void register(IEventBus eventbus){
        ITEMS.register(eventbus);
    }
}
