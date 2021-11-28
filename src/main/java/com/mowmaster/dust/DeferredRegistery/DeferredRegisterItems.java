package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.CreativeTabs.DustItemTabs;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.Tools.DevTool;
import com.mowmaster.dust.Items.Tools.FilterTool;
import com.mowmaster.dust.Items.Tools.LinkingTool;
import com.mowmaster.dust.Items.Tools.UpgradeTool;
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
            () -> new ColorApplicator(new Item.Properties().stacksTo(1).tab(DustItemTabs.TAB_ITEMS)));

    public static final RegistryObject<Item> TOOL_LINKINGTOOL = ITEMS.register("tool_linkingtool",
            () -> new LinkingTool(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_LINKINGTOOLBACKWARDS = ITEMS.register("tool_linkingtoolbackwards",
            () -> new LinkingTool(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_UPGRADETOOL = ITEMS.register("tool_upgradetool",
            () -> new UpgradeTool(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_FILTERTOOL = ITEMS.register("tool_filtertool",
            () -> new FilterTool(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_DEVTOOL = ITEMS.register("tool_devtool",
            () -> new DevTool(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));

    public static final RegistryObject<Item> PEDESTAL_UPGRADE_IMPORT = ITEMS.register("item_pedestal_upgrade_import",
            () -> new ItemUpgradeImport(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
