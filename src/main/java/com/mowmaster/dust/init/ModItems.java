package com.mowmaster.dust.init;

import com.mowmaster.dust.item.Armor.*;
import com.mowmaster.dust.item.ItemDust;
import com.mowmaster.dust.item.Tools.Axes.*;
import com.mowmaster.dust.item.Tools.Shovel.*;
import com.mowmaster.dust.item.Tools.Pickaxes.*;
import com.mowmaster.dust.item.Tools.Swords.*;
import com.mowmaster.dust.reference.reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems
{
    public static Item ItemCrystalRed;
    public static Item ItemCrystalBlue;
    public static Item ItemCrystalYellow;
    public static Item ItemCrystalGreen;
    public static Item ItemCrystalOrange;
    public static Item ItemCrystalPurple;
    public static Item ItemCrystalBlack;
    public static Item ItemCrystalWhite;

    public static void dustItems()
    {
        // Crystal Types
        GameRegistry.registerItem(ItemCrystalRed = new ItemDust("crystal_red"), "crystal_red");
        GameRegistry.registerItem(ItemCrystalBlue = new ItemDust("crystal_blue"), "crystal_blue");
        GameRegistry.registerItem(ItemCrystalYellow = new ItemDust("crystal_yellow"), "crystal_yellow");
        GameRegistry.registerItem(ItemCrystalGreen = new ItemDust("crystal_green"), "crystal_green");
        GameRegistry.registerItem(ItemCrystalOrange = new ItemDust("crystal_orange"), "crystal_orange");
        GameRegistry.registerItem(ItemCrystalPurple = new ItemDust("crystal_purple"), "crystal_purple");
        GameRegistry.registerItem(ItemCrystalBlack = new ItemDust("crystal_black"), "crystal_black");
        GameRegistry.registerItem(ItemCrystalWhite = new ItemDust("crystal_white"), "crystal_white");

        // Dust Types
    }

// Tool Material

// "Material name"
// Harvest Level: Wood/Gold: 0 Stone: 1 Iron: 2 Diamond: 3
// Durability: Gold 32 Wood 59 Stone 131 Iron 250 Diamond 1561
// Mining Speed: No Tool 1.0F - Wood 2.0F - Stone 4.0F - Iron 6.0F - Diamond 8.0F - Gold 12.0F
// Damage to Entities: Wood/Gold 0.0F - Stone 1.0F - Iron 2.0F - Diamond 3.0F  ** All Swords add 4.0 to Base Damage ~ ex. Iron Sword does 2.0+4.0 = 6.0 damage
// Enchant ability: Stone 5 - Diamond 10 - Iron 14 - Wood 15 - Gold 22
    public static Item.ToolMaterial RedCrystal = EnumHelper.addToolMaterial("RedCrystal", 2, 250, 6.0F, 2.0F, 14);
    public static Item.ToolMaterial BlueCrystal = EnumHelper.addToolMaterial("BlueCrystal", 2, 250, 6.0F, 2.0F, 14);
    public static Item.ToolMaterial YellowCrystal = EnumHelper.addToolMaterial("YellowCrystal", 2, 250, 6.0F, 2.0F, 14);
    public static Item.ToolMaterial GreenCrystal = EnumHelper.addToolMaterial("GreenCrystal", 2, 250, 6.0F, 2.0F, 14);
    public static Item.ToolMaterial OrangeCrystal = EnumHelper.addToolMaterial("OrangeCrystal", 2, 250, 6.0F, 2.0F, 14);
    public static Item.ToolMaterial PurpleCrystal = EnumHelper.addToolMaterial("PurpleCrystal", 2, 250, 6.0F, 2.0F, 14);

// White and Black are rarer and thus better tool materials
    public static Item.ToolMaterial BlackCrystal = EnumHelper.addToolMaterial("BlackCrystal", 3, 1600, 9.0F, 3.0F, 16);
    public static Item.ToolMaterial WhiteCrystal = EnumHelper.addToolMaterial("WhiteCrystal", 3, 1600, 9.0F, 3.0F, 25);


    public static Item crystalSwordRed;
    public static Item crystalSwordBlue;
    public static Item crystalSwordYellow;
    public static Item crystalSwordGreen;
    public static Item crystalSwordOrange;
    public static Item crystalSwordPurple;
    public static Item crystalSwordBlack;
    public static Item crystalSwordWhite;

    public static Item crystalAxeRed;
    public static Item crystalAxeBlue;
    public static Item crystalAxeYellow;
    public static Item crystalAxeGreen;
    public static Item crystalAxeOrange;
    public static Item crystalAxePurple;
    public static Item crystalAxeBlack;
    public static Item crystalAxeWhite;

    public static Item crystalPickaxeRed;
    public static Item crystalPickaxeBlue;
    public static Item crystalPickaxeYellow;
    public static Item crystalPickaxeGreen;
    public static Item crystalPickaxeOrange;
    public static Item crystalPickaxePurple;
    public static Item crystalPickaxeBlack;
    public static Item crystalPickaxeWhite;

    public static Item crystalShovelRed;
    public static Item crystalShovelBlue;
    public static Item crystalShovelYellow;
    public static Item crystalShovelGreen;
    public static Item crystalShovelOrange;
    public static Item crystalShovelPurple;
    public static Item crystalShovelBlack;
    public static Item crystalShovelWhite;

    public static void dustTools()
    {
        GameRegistry.registerItem(crystalSwordRed = new CrystalSwordRed("crystalsword_red",RedCrystal), "crystalsword_red");
        GameRegistry.registerItem(crystalSwordBlue = new CrystalSwordBlue("crystalsword_blue",BlueCrystal), "crystalsword_blue");
        GameRegistry.registerItem(crystalSwordYellow = new CrystalSwordYellow("crystalsword_yellow",YellowCrystal), "crystalsword_yellow");
        GameRegistry.registerItem(crystalSwordGreen = new CrystalSwordGreen("crystalsword_green",GreenCrystal), "crystalsword_green");
        GameRegistry.registerItem(crystalSwordOrange = new CrystalSwordOrange("crystalsword_orange",OrangeCrystal), "crystalsword_orange");
        GameRegistry.registerItem(crystalSwordPurple = new CrystalSwordPurple("crystalsword_purple",PurpleCrystal), "crystalsword_purple");
        GameRegistry.registerItem(crystalSwordBlack = new CrystalSwordBlack("crystalsword_black",BlackCrystal), "crystalsword_black");
        GameRegistry.registerItem(crystalSwordWhite = new CrystalSwordWhite("crystalsword_white",WhiteCrystal), "crystalsword_white");


        GameRegistry.registerItem(crystalAxeRed = new CrystalAxeRed("crystalaxe_red",RedCrystal), "crystalaxe_red");
        GameRegistry.registerItem(crystalAxeBlue = new CrystalAxeBlue("crystalaxe_blue",BlueCrystal), "crystalaxe_blue");
        GameRegistry.registerItem(crystalAxeYellow = new CrystalAxeYellow("crystalaxe_yellow",YellowCrystal), "crystalaxe_yellow");
        GameRegistry.registerItem(crystalAxeGreen = new CrystalAxeGreen("crystalaxe_green",GreenCrystal), "crystalaxe_green");
        GameRegistry.registerItem(crystalAxeOrange = new CrystalAxeOrange("crystalaxe_orange",OrangeCrystal), "crystalaxe_orange");
        GameRegistry.registerItem(crystalAxePurple = new CrystalAxePurple("crystalaxe_purple",PurpleCrystal), "crystalaxe_purple");
        GameRegistry.registerItem(crystalAxeBlack = new CrystalAxeBlack("crystalaxe_black",BlackCrystal), "crystalaxe_black");
        GameRegistry.registerItem(crystalAxeWhite = new CrystalAxeWhite("crystalaxe_white",WhiteCrystal), "crystalaxe_white");


        GameRegistry.registerItem(crystalPickaxeRed = new CrystalPickaxeRed("crystalpickaxe_red",RedCrystal), "crystalpickaxe_red");
        GameRegistry.registerItem(crystalPickaxeBlue = new CrystalPickaxeBlue("crystalpickaxe_blue",BlueCrystal), "crystalpickaxe_blue");
        GameRegistry.registerItem(crystalPickaxeYellow = new CrystalPickaxeYellow("crystalpickaxe_yellow",YellowCrystal), "crystalpickaxe_yellow");
        GameRegistry.registerItem(crystalPickaxeGreen = new CrystalPickaxeGreen("crystalpickaxe_green",GreenCrystal), "crystalpickaxe_green");
        GameRegistry.registerItem(crystalPickaxeOrange = new CrystalPickaxeOrange("crystalpickaxe_orange",OrangeCrystal), "crystalpickaxe_orange");
        GameRegistry.registerItem(crystalPickaxePurple = new CrystalPickaxePurple("crystalpickaxe_purple",PurpleCrystal), "crystalpickaxe_purple");
        GameRegistry.registerItem(crystalPickaxeBlack = new CrystalPickaxeBlack("crystalpickaxe_black",BlackCrystal), "crystalpickaxe_black");
        GameRegistry.registerItem(crystalPickaxeWhite = new CrystalPickaxeWhite("crystalpickaxe_white",WhiteCrystal), "crystalpickaxe_white");

        GameRegistry.registerItem(crystalShovelRed = new CrystalShovelRed("crystalshovel_red",RedCrystal), "crystalshovel_red");
        GameRegistry.registerItem(crystalShovelBlue = new CrystalShovelBlue("crystalshovel_blue",BlueCrystal), "crystalshovel_blue");
        GameRegistry.registerItem(crystalShovelYellow = new CrystalShovelYellow("crystalshovel_yellow",YellowCrystal), "crystalshovel_yellow");
        GameRegistry.registerItem(crystalShovelGreen = new CrystalShovelGreen("crystalshovel_green",GreenCrystal), "crystalshovel_green");
        GameRegistry.registerItem(crystalShovelOrange = new CrystalShovelOrange("crystalshovel_orange",OrangeCrystal), "crystalshovel_orange");
        GameRegistry.registerItem(crystalShovelPurple = new CrystalShovelPurple("crystalshovel_purple",PurpleCrystal), "crystalshovel_purple");
        GameRegistry.registerItem(crystalShovelBlack = new CrystalShovelBlack("crystalshovel_black",BlackCrystal), "crystalshovel_black");
        GameRegistry.registerItem(crystalShovelWhite = new CrystalShovelWhite("crystalshovel_white",WhiteCrystal), "crystalshovel_white");

    }


    // Armor Material

// "Material Name"
// next is a texture name for the armor
// Each type has a value and each piece has a multiplier. Leather 5, Gold 7, Chain/Iron 15, Diamond 33 ~~~ Helmet *11, Chestplate *16, Leggings *15, Boots *13
// damageReduction (Array) int values for the armor protection (shows on armor bar in game) 0-25 (0-20 render) at 25 the player is invulnerable...
    //  Leather - new int[]{1,3,2,1} (7 Total)
    //  Gold - new int[]{2,5,3,1} (11 Total)
    //  Chain - new int[]{2,5,4,1} (12 Total)
    //  Iron - new int[]{2,6,5,2} (15 Total)
    //  Diamond - new int[]{3,8,6,3} (20 Total)
// enchantability Iron - 9, Diamond - 10, Chain - 12, Leather - 15, Gold - 25

    public static ItemArmor.ArmorMaterial RedArmorCrystal = EnumHelper.addArmorMaterial("RedCrystal", "dust:RedCrystalArmor", 15, new int[]{2,6,5,3}, 15);
    public static ItemArmor.ArmorMaterial BlueArmorCrystal = EnumHelper.addArmorMaterial("BlueCrystal", "dust:BlueCrystalArmor", 15, new int[]{2,6,5,3}, 15);
    public static ItemArmor.ArmorMaterial YellowArmorCrystal = EnumHelper.addArmorMaterial("YellowCrystal", "dust:YellowCrystalArmor", 15, new int[]{2,6,5,3}, 15);
    public static ItemArmor.ArmorMaterial GreenArmorCrystal = EnumHelper.addArmorMaterial("GreenCrystal", "dust:GreenCrystalArmor", 15, new int[]{2,6,5,3}, 15);
    public static ItemArmor.ArmorMaterial OrangeArmorCrystal = EnumHelper.addArmorMaterial("OrangeCrystal", "dust:OrangeCrystalArmor", 15, new int[]{2,6,5,3}, 15);
    public static ItemArmor.ArmorMaterial PurpleArmorCrystal = EnumHelper.addArmorMaterial("PurpleCrystal", "dust:PurpleCrystalArmor", 15, new int[]{2,6,5,3}, 15);

    // White and Black are rarer and thus better tool materials
    public static ItemArmor.ArmorMaterial BlackArmorCrystal = EnumHelper.addArmorMaterial("BlackCrystal", "dust:BlackCrystalArmor", 33, new int[]{3,8,6,4}, 20);
    public static ItemArmor.ArmorMaterial WhiteArmorCrystal = EnumHelper.addArmorMaterial("WhiteCrystal", "dust:WhiteCrystalArmor", 33, new int[]{3,8,6,4}, 20);





    public static Item RedCrystalHelmet;
    public static Item RedCrystalChestplate;
    public static Item RedCrystalLeggings;
    public static Item RedCrystalBoots;

    public static Item BlueCrystalHelmet;
    public static Item BlueCrystalChestplate;
    public static Item BlueCrystalLeggings;
    public static Item BlueCrystalBoots;

    public static Item YellowCrystalHelmet;
    public static Item YellowCrystalChestplate;
    public static Item YellowCrystalLeggings;
    public static Item YellowCrystalBoots;

    public static Item GreenCrystalHelmet;
    public static Item GreenCrystalChestplate;
    public static Item GreenCrystalLeggings;
    public static Item GreenCrystalBoots;

    public static Item OrangeCrystalHelmet;
    public static Item OrangeCrystalChestplate;
    public static Item OrangeCrystalLeggings;
    public static Item OrangeCrystalBoots;

    public static Item PurpleCrystalHelmet;
    public static Item PurpleCrystalChestplate;
    public static Item PurpleCrystalLeggings;
    public static Item PurpleCrystalBoots;

    public static Item BlackCrystalHelmet;
    public static Item BlackCrystalChestplate;
    public static Item BlackCrystalLeggings;
    public static Item BlackCrystalBoots;

    public static Item WhiteCrystalHelmet;
    public static Item WhiteCrystalChestplate;
    public static Item WhiteCrystalLeggings;
    public static Item WhiteCrystalBoots;



    public static void dustArmors()
    {

        //GameRegistry.registerItem( ItemName = new ArmorMaterialName("nameof_item", ArmorClassFile, #1, #2));
        //#1 is the texture layer Default values --> Helm = 1, Chest = 1, Leggs = 2, Boots = 1
        //#2 is the value that tells the game what type of armor it is --> Helm = 0, Chest = 1, Leggs = 2, Boots = 3

        GameRegistry.registerItem(RedCrystalHelmet = new RedCrystalArmor("crystalhelmet_red", RedArmorCrystal, 1, 0), "crystalhelmet_red");
        GameRegistry.registerItem(RedCrystalChestplate = new RedCrystalArmor("crystalchestplate_red", RedArmorCrystal, 1, 1), "crystalchestplate_red");
        GameRegistry.registerItem(RedCrystalLeggings = new RedCrystalArmor("crystalleggings_red", RedArmorCrystal, 2, 2), "crystalleggings_red");
        GameRegistry.registerItem(RedCrystalBoots = new RedCrystalArmor("crystalboots_red", RedArmorCrystal, 1, 3), "crystalboots_red");

        GameRegistry.registerItem(BlueCrystalHelmet = new BlueCrystalArmor("crystalhelmet_blue", BlueArmorCrystal, 1, 0), "crystalhelmet_blue");
        GameRegistry.registerItem(BlueCrystalChestplate = new BlueCrystalArmor("crystalchestplate_blue", BlueArmorCrystal, 1, 1), "crystalchestplate_blue");
        GameRegistry.registerItem(BlueCrystalLeggings = new BlueCrystalArmor("crystalleggings_blue", BlueArmorCrystal, 2, 2), "crystalleggings_blue");
        GameRegistry.registerItem(BlueCrystalBoots = new BlueCrystalArmor("crystalboots_blue", BlueArmorCrystal, 1, 3), "crystalboots_blue");

        GameRegistry.registerItem(YellowCrystalHelmet = new YellowCrystalArmor("crystalhelmet_yellow", YellowArmorCrystal, 1, 0), "crystalhelmet_yellow");
        GameRegistry.registerItem(YellowCrystalChestplate = new YellowCrystalArmor("crystalchestplate_yellow", YellowArmorCrystal, 1, 1), "crystalchestplate_yellow");
        GameRegistry.registerItem(YellowCrystalLeggings = new YellowCrystalArmor("crystalleggings_yellow", YellowArmorCrystal, 2, 2), "crystalleggings_yellow");
        GameRegistry.registerItem(YellowCrystalBoots = new YellowCrystalArmor("crystalboots_yellow", YellowArmorCrystal, 1, 3), "crystalboots_yellow");

        GameRegistry.registerItem(GreenCrystalHelmet = new GreenCrystalArmor("crystalhelmet_green", GreenArmorCrystal, 1, 0), "crystalhelmet_green");
        GameRegistry.registerItem(GreenCrystalChestplate = new GreenCrystalArmor("crystalchestplate_green", GreenArmorCrystal, 1, 1), "crystalchestplate_green");
        GameRegistry.registerItem(GreenCrystalLeggings = new GreenCrystalArmor("crystalleggings_green", GreenArmorCrystal, 2, 2), "crystalleggings_green");
        GameRegistry.registerItem(GreenCrystalBoots = new GreenCrystalArmor("crystalboots_green", GreenArmorCrystal, 1, 3), "crystalboots_green");

        GameRegistry.registerItem(OrangeCrystalHelmet = new OrangeCrystalArmor("crystalhelmet_orange", OrangeArmorCrystal, 1, 0), "crystalhelmet_orange");
        GameRegistry.registerItem(OrangeCrystalChestplate = new OrangeCrystalArmor("crystalchestplate_orange", OrangeArmorCrystal, 1, 1), "crystalchestplate_orange");
        GameRegistry.registerItem(OrangeCrystalLeggings = new OrangeCrystalArmor("crystalleggings_orange", OrangeArmorCrystal, 2, 2), "crystalleggings_orange");
        GameRegistry.registerItem(OrangeCrystalBoots = new OrangeCrystalArmor("crystalboots_orange", OrangeArmorCrystal, 1, 3), "crystalboots_orange");

        GameRegistry.registerItem(PurpleCrystalHelmet = new PurpleCrystalArmor("crystalhelmet_purple", PurpleArmorCrystal, 1, 0), "crystalhelmet_purple");
        GameRegistry.registerItem(PurpleCrystalChestplate = new PurpleCrystalArmor("crystalchestplate_purple", PurpleArmorCrystal, 1, 1), "crystalchestplate_purple");
        GameRegistry.registerItem(PurpleCrystalLeggings = new PurpleCrystalArmor("crystalleggings_purple", PurpleArmorCrystal, 2, 2), "crystalleggings_purple");
        GameRegistry.registerItem(PurpleCrystalBoots = new PurpleCrystalArmor("crystalboots_purple", PurpleArmorCrystal, 1, 3), "crystalboots_purple");

        GameRegistry.registerItem(BlackCrystalHelmet = new BlackCrystalArmor("crystalhelmet_black", BlackArmorCrystal, 1, 0), "crystalhelmet_black");
        GameRegistry.registerItem(BlackCrystalChestplate = new BlackCrystalArmor("crystalchestplate_black", BlackArmorCrystal, 1, 1), "crystalchestplate_black");
        GameRegistry.registerItem(BlackCrystalLeggings = new BlackCrystalArmor("crystalleggings_black", BlackArmorCrystal, 2, 2), "crystalleggings_black");
        GameRegistry.registerItem(BlackCrystalBoots = new BlackCrystalArmor("crystalboots_black", BlackArmorCrystal, 1, 3), "crystalboots_black");

        GameRegistry.registerItem(WhiteCrystalHelmet = new WhiteCrystalArmor("crystalhelmet_white", WhiteArmorCrystal, 1, 0), "crystalhelmet_white");
        GameRegistry.registerItem(WhiteCrystalChestplate = new WhiteCrystalArmor("crystalchestplate_white", WhiteArmorCrystal, 1, 1), "crystalchestplate_white");
        GameRegistry.registerItem(WhiteCrystalLeggings = new WhiteCrystalArmor("crystalleggings_white", WhiteArmorCrystal, 2, 2), "crystalleggings_white");
        GameRegistry.registerItem(WhiteCrystalBoots = new WhiteCrystalArmor("crystalboots_white", WhiteArmorCrystal, 1, 3), "crystalboots_white");

    }
}
