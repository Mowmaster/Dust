package com.mowmaster.dust.init;

import com.mowmaster.dust.item.ItemDust;
import com.mowmaster.dust.item.Tools.Swords.*;
import net.minecraft.item.Item;
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

// Material name
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
    }

}
