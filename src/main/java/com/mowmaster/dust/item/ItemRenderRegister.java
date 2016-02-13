package com.mowmaster.dust.item;

import com.mowmaster.dust.init.ModItems;
import com.mowmaster.dust.reference.reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;



public final class ItemRenderRegister {

    public static void registerItemRenderer()
    {
        reg(ModItems.ItemCrystalRed);
        reg(ModItems.ItemCrystalBlue);
        reg(ModItems.ItemCrystalYellow);
        reg(ModItems.ItemCrystalGreen);
        reg(ModItems.ItemCrystalOrange);
        reg(ModItems.ItemCrystalPurple);
        reg(ModItems.ItemCrystalBlack);
        reg(ModItems.ItemCrystalWhite);

        reg(ModItems.ItemCrystalRedDust);
        reg(ModItems.ItemCrystalBlueDust);
        reg(ModItems.ItemCrystalYellowDust);
        reg(ModItems.ItemCrystalGreenDust);
        reg(ModItems.ItemCrystalOrangeDust);
        reg(ModItems.ItemCrystalPurpleDust);
        reg(ModItems.ItemCrystalBlackDust);
        reg(ModItems.ItemCrystalWhiteDust);

// Tools
        reg(ModItems.crystalSwordRed);
        reg(ModItems.crystalSwordBlue);
        reg(ModItems.crystalSwordYellow);
        reg(ModItems.crystalSwordGreen);
        reg(ModItems.crystalSwordOrange);
        reg(ModItems.crystalSwordPurple);
        reg(ModItems.crystalSwordBlack);
        reg(ModItems.crystalSwordWhite);

        reg(ModItems.crystalAxeRed);
        reg(ModItems.crystalAxeBlue);
        reg(ModItems.crystalAxeYellow);
        reg(ModItems.crystalAxeGreen);
        reg(ModItems.crystalAxeOrange);
        reg(ModItems.crystalAxePurple);
        reg(ModItems.crystalAxeBlack);
        reg(ModItems.crystalAxeWhite);

        reg(ModItems.crystalPickaxeRed);
        reg(ModItems.crystalPickaxeBlue);
        reg(ModItems.crystalPickaxeYellow);
        reg(ModItems.crystalPickaxeGreen);
        reg(ModItems.crystalPickaxeOrange);
        reg(ModItems.crystalPickaxePurple);
        reg(ModItems.crystalPickaxeBlack);
        reg(ModItems.crystalPickaxeWhite);

        reg(ModItems.crystalShovelRed);
        reg(ModItems.crystalShovelBlue);
        reg(ModItems.crystalShovelYellow);
        reg(ModItems.crystalShovelGreen);
        reg(ModItems.crystalShovelOrange);
        reg(ModItems.crystalShovelPurple);
        reg(ModItems.crystalShovelBlack);
        reg(ModItems.crystalShovelWhite);

// Armor
        reg(ModItems.RedCrystalHelmet);
        reg(ModItems.RedCrystalChestplate);
        reg(ModItems.RedCrystalLeggings);
        reg(ModItems.RedCrystalBoots);

        reg(ModItems.BlueCrystalHelmet);
        reg(ModItems.BlueCrystalChestplate);
        reg(ModItems.BlueCrystalLeggings);
        reg(ModItems.BlueCrystalBoots);

        reg(ModItems.YellowCrystalHelmet);
        reg(ModItems.YellowCrystalChestplate);
        reg(ModItems.YellowCrystalLeggings);
        reg(ModItems.YellowCrystalBoots);

        reg(ModItems.GreenCrystalHelmet);
        reg(ModItems.GreenCrystalChestplate);
        reg(ModItems.GreenCrystalLeggings);
        reg(ModItems.GreenCrystalBoots);

        reg(ModItems.OrangeCrystalHelmet);
        reg(ModItems.OrangeCrystalChestplate);
        reg(ModItems.OrangeCrystalLeggings);
        reg(ModItems.OrangeCrystalBoots);

        reg(ModItems.PurpleCrystalHelmet);
        reg(ModItems.PurpleCrystalChestplate);
        reg(ModItems.PurpleCrystalLeggings);
        reg(ModItems.PurpleCrystalBoots);

        reg(ModItems.BlackCrystalHelmet);
        reg(ModItems.BlackCrystalChestplate);
        reg(ModItems.BlackCrystalLeggings);
        reg(ModItems.BlackCrystalBoots);

        reg(ModItems.WhiteCrystalHelmet);
        reg(ModItems.WhiteCrystalChestplate);
        reg(ModItems.WhiteCrystalLeggings);
        reg(ModItems.WhiteCrystalBoots);

    }

    public static void reg(Item item){
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }

}


