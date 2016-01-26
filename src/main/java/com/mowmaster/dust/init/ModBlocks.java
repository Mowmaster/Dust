package com.mowmaster.dust.init;


import com.mowmaster.dust.block.*;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks
{
    public static Block RedCrystalOre;
    public static Block BlueCrystalOre;
    public static Block YellowCrystalOre;
    public static Block GreenCrystalOre;
    public static Block OrangeCrystalOre;
    public static Block PurpleCrystalOre;
    public static Block BlackCrystalOre;
    public static Block WhiteCrystalOre;

    public static void dustBlocks()
    {
        GameRegistry.registerBlock(RedCrystalOre = new BlockOreRed("crystalore_red"), "crystalore_red");
        GameRegistry.registerBlock(BlueCrystalOre = new BlockOreBlue("crystalore_blue"), "crystalore_blue");
        GameRegistry.registerBlock(YellowCrystalOre = new BlockOreYellow("crystalore_yellow"), "crystalore_yellow");
        GameRegistry.registerBlock(GreenCrystalOre = new BlockOreGreen("crystalore_green"), "crystalore_green");
        GameRegistry.registerBlock(OrangeCrystalOre = new BlockOreOrange("crystalore_orange"), "crystalore_orange");
        GameRegistry.registerBlock(PurpleCrystalOre = new BlockOrePurple("crystalore_purple"), "crystalore_purple");
        GameRegistry.registerBlock(BlackCrystalOre = new BlockOreBlack("crystalore_black"), "crystalore_black");
        GameRegistry.registerBlock(WhiteCrystalOre = new BlockOreWhite("crystalore_white"), "crystalore_white");

    }

}
