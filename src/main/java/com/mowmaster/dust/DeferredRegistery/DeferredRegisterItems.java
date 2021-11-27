package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.Upgrades.Pedestal.ItemUpgradeImport;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mowmaster.dust.References.Constants.MODID;

public class DeferredRegisterItems
{
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);



    public static final RegistryObject<Item> COLOR_APPLICATOR = ITEMS.register("applicator",
            () -> new ColorApplicator(new Item.Properties()));

    public static final RegistryObject<Item> PEDESTAL_UPGRADE_IMPORT = ITEMS.register("item_pedestal_upgrade_import",
            () -> new ItemUpgradeImport(new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
