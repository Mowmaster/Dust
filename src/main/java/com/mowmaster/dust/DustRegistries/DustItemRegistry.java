package com.mowmaster.dust.DustRegistries;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.AncientLanguage.AncientInkItem;
import com.mowmaster.dust.Features.AncientLanguage.AncientWritingItem;
import com.mowmaster.dust.Features.AncientLanguage.ParchmentItem;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalArmorItem;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalToolHammer;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalToolMattock;
import com.mowmaster.dust.Features.CrystalTools.Item.CrystalWeaponSword;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialArmor;
import com.mowmaster.dust.Features.CrystalTools.Materials.DustMaterialTools;
import com.mowmaster.dust.Features.EffectScrolls.Item.EarthStickItem;
import com.mowmaster.dust.Features.EffectScrolls.Item.FireStickItem;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
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


    public static final DeferredItem<Item> CRYSTAL_INERT = DUSTITEMS.registerSimpleItem("crystal_inert", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_RED = DUSTITEMS.registerSimpleItem("crystal_red", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_GREEN = DUSTITEMS.registerSimpleItem("crystal_green", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_BLUE = DUSTITEMS.registerSimpleItem("crystal_blue", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_WHITE = DUSTITEMS.registerSimpleItem("crystal_white", properties -> properties);
    public static final DeferredItem<Item> CRYSTAL_BLACK = DUSTITEMS.registerSimpleItem("crystal_black", properties -> properties);

    public static final DeferredItem<Item> CRYSTAL_SWORD = DUSTITEMS.registerItem("crystal_sword",
            properties -> new CrystalWeaponSword(properties.sword(DustMaterialTools.CRYSTAL, 3.0f,-2.4f)));
    public static final DeferredItem<Item> CRYSTAL_PICKAXE = DUSTITEMS.registerItem("crystal_pickaxe",
            properties -> new Item(properties.pickaxe(DustMaterialTools.CRYSTAL, 1.0f,-2.8f)));
    public static final DeferredItem<Item> CRYSTAL_AXE = DUSTITEMS.registerItem("crystal_axe",
            properties -> new Item(properties.axe(DustMaterialTools.CRYSTAL, 6.0f,-3.2f)));
    public static final DeferredItem<Item> CRYSTAL_SHOVEL = DUSTITEMS.registerItem("crystal_shovel",
            properties -> new Item(properties.shovel(DustMaterialTools.CRYSTAL, 1.5f,-3.0f)));
    public static final DeferredItem<Item> CRYSTAL_HOE = DUSTITEMS.registerItem("crystal_hoe",
            properties -> new Item(properties.hoe(DustMaterialTools.CRYSTAL, 0.0f,-3.0f)));
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



    // Writes signs, books and item names in the ancient script (see Features/AncientLanguage)
    public static final DeferredItem<Item> ANCIENT_INK = DUSTITEMS.registerItem("ancient_ink", AncientInkItem::new);

    // Ancient words to decipher (hangman): found in chests and ruins, or rubbed off glyph walls with Parchment
    public static final DeferredItem<Item> ANCIENT_TABLET = DUSTITEMS.registerItem("ancient_tablet",
            properties -> new AncientWritingItem(properties.stacksTo(1)));
    public static final DeferredItem<Item> TRANSCRIPTION = DUSTITEMS.registerItem("transcription",
            properties -> new AncientWritingItem(properties.stacksTo(1)));
    public static final DeferredItem<Item> PARCHMENT = DUSTITEMS.registerItem("parchment", ParchmentItem::new);

    public static final DeferredItem<Item> SPELL_WAND_FIRE = DUSTITEMS.registerItem("wands_stick_fire", properties -> new FireStickItem(properties.durability(8)));
    public static final DeferredItem<Item> SPELL_WAND_EARTH = DUSTITEMS.registerItem("wands_stick_earth", properties -> new EarthStickItem(properties.durability(8)));




    public static void register(IEventBus eventBus)
    {
        DUSTITEMS.register(eventBus);
    }

}
