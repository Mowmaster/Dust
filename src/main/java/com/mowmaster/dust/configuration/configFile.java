package com.mowmaster.dust.configuration;

import com.mowmaster.dust.dust;
import net.minecraftforge.fml.common.FMLCommonHandler;


public class configFile
{
    public static boolean craftableCrystals;
    public static final boolean CRAFTABLECRYSTALS = false;
    public static final String CRAFTABLECRYSTALS_NAME = "Enable The Crafting Of Crystals";

    public static boolean lotsofBiomes;
    public static final boolean LOTSOFBIOMES = false;
    public static final String LOTSOFBIOMES_NAME = "Do you want lots of crystal biomes to spawn???";

    public static int biomeMixed;
    public static int biomeRed;
    public static int biomeBlue;
    public static int biomeYellow;
    public static int biomeGreen;
    public static int biomeOrange;
    public static int biomePurple;
    public static int biomeBlack;
    public static int biomeWhite;
    public static final int BIOMECRYSTAL = 117;
    public static final int BIOMECRYSTALRED = 118;
    public static final int BIOMECRYSTALBLUE = 119;
    public static final int BIOMECRYSTALYELLOW = 120;
    public static final int BIOMECRYSTALGREEN = 121;
    public static final int BIOMECRYSTALORANGE = 122;
    public static final int BIOMECRYSTALPURPLE = 123;
    public static final int BIOMECRYSTALBLACK = 124;
    public static final int BIOMECRYSTALWHITE = 125;
    public static final String BIOMECRYSTAL_NAME = "Mixed Biome";
    public static final String BIOMECRYSTALRED_NAME = "Red Biome";
    public static final String BIOMECRYSTALBLUE_NAME = "Blue Biome";
    public static final String BIOMECRYSTALYELLOW_NAME = "Yellow Biome";
    public static final String BIOMECRYSTALGREEN_NAME = "Green Biome";
    public static final String BIOMECRYSTALORANGE_NAME = "Orange Biome";
    public static final String BIOMECRYSTALPURPLE_NAME = "Purple Biome";
    public static final String BIOMECRYSTALBLACK_NAME = "Black Biome";
    public static final String BIOMECRYSTALWHITE_NAME = "White Biome";

    public static void syncConfig()
    {
        FMLCommonHandler.instance().bus().register(dust.instance);

        final String RECIPIES = dust.config.CATEGORY_GENERAL + dust.config.CATEGORY_SPLITTER + "Recipies";
        dust.config.addCustomCategoryComment(RECIPIES, "Craftable Crystals???");
        craftableCrystals = dust.config.get(RECIPIES, CRAFTABLECRYSTALS_NAME, CRAFTABLECRYSTALS).getBoolean(CRAFTABLECRYSTALS);

        final String LOTSOFBIOME = dust.config.CATEGORY_GENERAL + dust.config.CATEGORY_SPLITTER + "Lots O' Biomes";
        dust.config.addCustomCategoryComment(LOTSOFBIOME, "How Many Is Too Much?");
        lotsofBiomes = dust.config.get(LOTSOFBIOME, LOTSOFBIOMES_NAME, LOTSOFBIOMES).getBoolean(LOTSOFBIOMES);

        final String BIOMES = dust.config.CATEGORY_GENERAL + dust.config.CATEGORY_SPLITTER + "Biomes";
        dust.config.addCustomCategoryComment(BIOMES, "Biome Mixed");
        biomeMixed = dust.config.get(BIOMES, BIOMECRYSTAL_NAME, BIOMECRYSTAL).getInt(BIOMECRYSTAL);
        dust.config.addCustomCategoryComment(BIOMES, "Biome Red");
        biomeRed = dust.config.get(BIOMES, BIOMECRYSTALRED_NAME, BIOMECRYSTALRED).getInt(BIOMECRYSTALRED);
        dust.config.addCustomCategoryComment(BIOMES, "Biome Blue");
        biomeBlue = dust.config.get(BIOMES, BIOMECRYSTALBLUE_NAME, BIOMECRYSTALBLUE).getInt(BIOMECRYSTALBLUE);
        dust.config.addCustomCategoryComment(BIOMES, "Biome Yellow");
        biomeYellow = dust.config.get(BIOMES, BIOMECRYSTALYELLOW_NAME, BIOMECRYSTALYELLOW).getInt(BIOMECRYSTALYELLOW);
        dust.config.addCustomCategoryComment(BIOMES, "Biome Green");
        biomeGreen = dust.config.get(BIOMES, BIOMECRYSTALGREEN_NAME, BIOMECRYSTALGREEN).getInt(BIOMECRYSTALGREEN);
        dust.config.addCustomCategoryComment(BIOMES, "Biome Orange");
        biomeOrange = dust.config.get(BIOMES, BIOMECRYSTALORANGE_NAME, BIOMECRYSTALORANGE).getInt(BIOMECRYSTALORANGE);
        dust.config.addCustomCategoryComment(BIOMES, "Biome Purple");
        biomePurple = dust.config.get(BIOMES, BIOMECRYSTALPURPLE_NAME, BIOMECRYSTALPURPLE).getInt(BIOMECRYSTALPURPLE);
        dust.config.addCustomCategoryComment(BIOMES, "Biome Black");
        biomeBlack = dust.config.get(BIOMES, BIOMECRYSTALBLACK_NAME, BIOMECRYSTALBLACK).getInt(BIOMECRYSTALBLACK);
        dust.config.addCustomCategoryComment(BIOMES, "Biome White");
        biomeWhite = dust.config.get(BIOMES, BIOMECRYSTALWHITE_NAME, BIOMECRYSTALWHITE).getInt(BIOMECRYSTALWHITE);






        if (dust.config.hasChanged())
        {
            dust.config.save();
        }
    }

}
