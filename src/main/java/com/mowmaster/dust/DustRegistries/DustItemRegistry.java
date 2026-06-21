package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.Features.CrystalBlocks.Item.CrystalChiselBaseItem;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.CrystalBlocks.Item.CrystalHalfSawItem;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalArmorItem;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalToolHammer;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalToolMattock;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalWeaponSword;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialArmor;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialTools;
import com.mowmaster.dust.Features.DustyDelights.DDFoodProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class DustItemRegistry
{
    public static final DeferredRegister.Items DUSTITEMS = DeferredRegister.createItems(DustReferences.MODID);

    /*
    Crystal Blocks Features
     */


    public static final DeferredItem<Item> DUST_RED = DUSTITEMS.registerSimpleItem("dust_red", properties -> properties);
    public static final DeferredItem<Item> DUST_GREEN = DUSTITEMS.registerSimpleItem("dust_green", properties -> properties);
    public static final DeferredItem<Item> DUST_BLUE = DUSTITEMS.registerSimpleItem("dust_blue", properties -> properties);
    public static final DeferredItem<Item> DUST_WHITE = DUSTITEMS.registerSimpleItem("dust_white", properties -> properties);
    public static final DeferredItem<Item> DUST_BLACK = DUSTITEMS.registerSimpleItem("dust_black", properties -> properties);


    public static final DeferredItem<Item> CRYSTAL_INERT = DUSTITEMS.registerSimpleItem("crystal_inert", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_RED = DUSTITEMS.registerSimpleItem("crystal_red", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_GREEN = DUSTITEMS.registerSimpleItem("crystal_green", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_BLUE = DUSTITEMS.registerSimpleItem("crystal_blue", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_WHITE = DUSTITEMS.registerSimpleItem("crystal_white", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_BLACK = DUSTITEMS.registerSimpleItem("crystal_black", properties -> properties);

    public static final DeferredItem<Item> CRYSTAL_CHISEL_IRON = DUSTITEMS.registerItem("chisel_iron", properties -> new CrystalChiselBaseItem(properties.durability(100)));
    public static final DeferredItem<Item> CRYSTAL_HALFSAW_IRON = DUSTITEMS.registerItem("halfsaw_iron", properties -> new CrystalHalfSawItem(properties.durability(100)));

    public static final DeferredItem<Item> CORNBREAD = DUSTITEMS.registerItem("food_cornbread",
            properties -> new Item(properties.food(DDFoodProperties.CORNBREAD, DDFoodProperties.CORNBREAD_EDIBLE)) {
                @Override
                public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                    builder.accept(Component.translatable(DustReferences.TOOLTIP + "cornbread"));
                    super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
                }
            });


    public static final DeferredItem<Item> CHARCOAL_WHITE = DUSTITEMS.registerItem("fuel_charcoal_white", Item::new);


    public static final DeferredItem<Item> CRYSTAL_SWORD = DUSTITEMS.registerItem("crystal_sword",
            properties -> new CrystalWeaponSword(properties.sword(DustMaterialTools.CRYSTAL, 3.0f,-2.4f)));
    public static final DeferredItem<Item> CRYSTAL_PICKAXE = DUSTITEMS.registerItem("crystal_pickaxe",
            properties -> new Item(properties.pickaxe(DustMaterialTools.CRYSTAL, 1.0f,-2.8f)));
    public static final DeferredItem<Item> CRYSTAL_AXE = DUSTITEMS.registerItem("crystal_axe",
            properties -> new AxeItem(DustMaterialTools.CRYSTAL, 6.0f,-3.2f,properties));
    public static final DeferredItem<Item> CRYSTAL_SHOVEL = DUSTITEMS.registerItem("crystal_shovel",
            properties -> new ShovelItem(DustMaterialTools.CRYSTAL, 1.5f,-3.0f,properties));
    public static final DeferredItem<Item> CRYSTAL_HOE = DUSTITEMS.registerItem("crystal_hoe",
            properties -> new HoeItem(DustMaterialTools.CRYSTAL, 0.0f,-3.0f,properties));
    public static final DeferredItem<Item> CRYSTAL_SPEAR = DUSTITEMS.registerItem("crystal_spear",
            properties -> new Item(properties.spear(DustMaterialTools.CRYSTAL, 1.05F, 1.075F, 0.5F, 3.0F, 10.0F, 6.5F, 5.1F, 10.0F, 4.6F)));

    public static final DeferredItem<Item> CRYSTAL_MATTOCK = DUSTITEMS.registerItem("crystal_mattock",
            properties -> new CrystalToolMattock(DustMaterialTools.CRYSTAL, 5.0f, -3.0f, properties));
    public static final DeferredItem<Item> CRYSTAL_HAMMER = DUSTITEMS.registerItem("crystal_hammer",
            properties -> new CrystalToolHammer(DustMaterialTools.CRYSTAL_BLOCK, 1.0f, -2.8f, properties));


    public static final DeferredItem<Item> CRYSTAL_HELMET = DUSTITEMS.registerItem("crystal_helmet",
            properties -> new CrystalArmorItem(properties.humanoidArmor(DustMaterialArmor.CRYSTAL_ARMOR_MATERIAL, ArmorType.HELMET)));
    public static final DeferredItem<Item> CRYSTAL_CHESTPLATE = DUSTITEMS.registerItem("crystal_chestplate",
            properties -> new CrystalArmorItem(properties.humanoidArmor(DustMaterialArmor.CRYSTAL_ARMOR_MATERIAL, ArmorType.CHESTPLATE)));
    public static final DeferredItem<Item> CRYSTAL_LEGGINGS = DUSTITEMS.registerItem("crystal_leggings",
            properties -> new CrystalArmorItem(properties.humanoidArmor(DustMaterialArmor.CRYSTAL_ARMOR_MATERIAL, ArmorType.LEGGINGS)));
    public static final DeferredItem<Item> CRYSTAL_BOOTS = DUSTITEMS.registerItem("crystal_boots",
            properties -> new CrystalArmorItem(properties.humanoidArmor(DustMaterialArmor.CRYSTAL_ARMOR_MATERIAL, ArmorType.BOOTS)));

    public static final DeferredItem<Item> CRYSTAL_HORSE_ARMOR = DUSTITEMS.registerItem("crystal_horse_armor",
            properties -> new Item(properties.horseArmor(DustMaterialArmor.CRYSTAL_ARMOR_MATERIAL)));

    public static final DeferredItem<Item> SEEDS_LETTUCE = DUSTITEMS.registerItem("seeds_lettuce",
        properties -> new BlockItem(DustBlockRegistry.CROP_LETTUCE.get(), properties.useItemDescriptionPrefix()));

    public static final DeferredItem<Item> FOOD_LETTUCE = DUSTITEMS.registerItem("food_lettuce",
            properties -> new Item(properties.food(DDFoodProperties.LETTUCE)));
    public static final DeferredItem<Item> FOOD_FALLOLDBERRY = DUSTITEMS.registerItem("food_falloldberry",
            properties -> new BlockItem(DustBlockRegistry.BLOCK_FALLOLDBERRY_BUSH.get(), properties
                    .useItemDescriptionPrefix()
                    .food(DDFoodProperties.LETTUCE)
            ));



    public static void register(IEventBus eventBus)
    {
        DUSTITEMS.register(eventBus);
    }

}
