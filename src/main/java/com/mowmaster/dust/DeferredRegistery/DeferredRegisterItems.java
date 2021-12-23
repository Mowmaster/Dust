package com.mowmaster.dust.DeferredRegistery;

import com.mowmaster.dust.CreativeTabs.DustItemTabs;
import com.mowmaster.dust.Items.Augments.AugmentBase;
import com.mowmaster.dust.Items.Augments.AugmentRenderDiffuser;
import com.mowmaster.dust.Items.ColorApplicator;
import com.mowmaster.dust.Items.ColoredCrystalBase;
import com.mowmaster.dust.Items.ColoredCrystalDustBase;
import com.mowmaster.dust.Items.Filters.*;
import com.mowmaster.dust.Items.Readable.RepairScrolls.T2RepairScroll;
import com.mowmaster.dust.Items.Tools.*;
import com.mowmaster.dust.Items.Upgrades.Pedestal.ItemUpgradeBase;
import com.mowmaster.dust.Items.Upgrades.Pedestal.ItemUpgradeExport;
import com.mowmaster.dust.Items.Upgrades.Pedestal.ItemUpgradeImport;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mowmaster.dust.References.Constants.MODID;

public class DeferredRegisterItems
{
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);



    public static final RegistryObject<Item> COLOR_APPLICATOR = ITEMS.register("applicator",
            () -> new ColorApplicator(new Item.Properties().stacksTo(1).tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> COLORED_CRYSTAL = ITEMS.register("item_crystal",
            () -> new ColoredCrystalBase(new Item.Properties().stacksTo(64).tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> COLORED_CRYSTAL_DUST = ITEMS.register("item_crystal_dust",
            () -> new ColoredCrystalDustBase(new Item.Properties().stacksTo(64).tab(DustItemTabs.TAB_ITEMS)));


    public static final RegistryObject<Item> TOOL_LINKINGTOOL = ITEMS.register("tool_linkingtool",
            () -> new LinkingTool(new Item.Properties().stacksTo(64).tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_LINKINGTOOLBACKWARDS = ITEMS.register("tool_linkingtoolbackwards",
            () -> new LinkingToolBackwards(new Item.Properties().stacksTo(64).tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_UPGRADETOOL = ITEMS.register("tool_upgradetool",
            () -> new UpgradeTool(new Item.Properties().stacksTo(64).tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_FILTERTOOL = ITEMS.register("tool_filtertool",
            () -> new FilterTool(new Item.Properties().stacksTo(64).tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_TAGTOOL = ITEMS.register("tool_tagtool",
            () -> new TagTool(new Item.Properties().stacksTo(64).tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> TOOL_DEVTOOL = ITEMS.register("tool_devtool",
            () -> new DevTool(new Item.Properties().stacksTo(64).tab(DustItemTabs.TAB_ITEMS)));

    public static final RegistryObject<Item> FILTER_BASE = ITEMS.register("filter_base",
            () -> new BaseFilter(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_ITEM = ITEMS.register("filter_item",
            () -> new FilterItem(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_ITEMSTACK = ITEMS.register("filter_itemstack",
            () -> new FilterItemStack(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_DURABILITY = ITEMS.register("filter_durability",
            () -> new FilterDurability(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_ENCHANTED = ITEMS.register("filter_enchanted",
            () -> new FilterEnchanted(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_ENCHANTED_COUNT = ITEMS.register("filter_enchantedcount",
            () -> new FilterEnchantCount(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_ENCHANTED_EXACT = ITEMS.register("filter_enchantedexact",
            () -> new FilterEnchantedExact(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_ENCHANTED_FUZZY = ITEMS.register("filter_enchantedfuzzy",
            () -> new FilterEnchantedFuzzy(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_FOOD = ITEMS.register("filter_food",
            () -> new FilterFood(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_MOD = ITEMS.register("filter_mod",
            () -> new FilterMod(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_RESTRICTED = ITEMS.register("filter_restricted",
            () -> new FilterRestricted(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> FILTER_TAG = ITEMS.register("filter_tag",
            () -> new FilterTag(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));

    public static final RegistryObject<Item> PEDESTAL_UPGRADE_BASE = ITEMS.register("upgrade_pedestal_base",
            () -> new ItemUpgradeBase(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> PEDESTAL_UPGRADE_IMPORT = ITEMS.register("upgrade_pedestal_import",
            () -> new ItemUpgradeImport(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> PEDESTAL_UPGRADE_EXPORT = ITEMS.register("upgrade_pedestal_export",
            () -> new ItemUpgradeExport(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));

    public static final RegistryObject<Item> AUGMENT_PEDESTAL_ROUNDROBIN = ITEMS.register("augment_pedestal_roundrobin",
            () -> new AugmentBase(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> AUGMENT_PEDESTAL_RENDERDIFFUSER = ITEMS.register("augment_pedestal_renderdiffuser",
            () -> new AugmentRenderDiffuser(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));
    public static final RegistryObject<Item> AUGMENT_PEDESTAL_NOCOLLIDE = ITEMS.register("augment_pedestal_nocollide",
            () -> new AugmentBase(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));

    public static final RegistryObject<Item> SCROLL_T2_REPAIR = ITEMS.register("scroll_t2_repair",
            () -> new T2RepairScroll(new Item.Properties().tab(DustItemTabs.TAB_ITEMS)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
