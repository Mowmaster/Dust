package com.mowmaster.dust.misc;

import net.minecraftforge.common.config.Configuration;

import java.io.File;


public class DustConfigurationFile
{
    public static Configuration dustConfig;


    public static int hotBiome;
    public static int warmBiome;
    public static int coldBiome;
    public static int crystalBiome;
    public static int biomeWeightChunks;

    public static int structuresInCrystal;
    public static int structuresInWorld;
    public static int treeGenChance;
    public static int anyBiomeOreSpawnChance;
    public static int oreClusterSize;
    public static int oreRegrowthRate;
    public static boolean funhaters;


    public static void InitConfig(File file)
    {
        dustConfig = new Configuration(file);
        SyncConfig();
    }

    public static void SyncConfig()
    {
        String category;

        category = "Biome Generation";
        dustConfig.addCustomCategoryComment(category,"Biome Generation Tweaks");
        hotBiome = dustConfig.getInt("1.Hot Crystal Biome ID", category,200,200,255,"Hot Biome");
        warmBiome = dustConfig.getInt("2.Warm Crystal Biome ID", category,201,200,255,"Warm Biome");
        coldBiome = dustConfig.getInt("3.Cold Crystal Biome ID", category,202,200,255,"Cold Biome");
        crystalBiome = dustConfig.getInt("4.Crystal Biome ID", category,203,200,255,"Crystal Biome");
        biomeWeightChunks = dustConfig.getInt("5.Crystal Biome Weight", category,20,1,200,"Larger Number is less often");

        category = "World Generation";
        dustConfig.addCustomCategoryComment(category,"World Generation Tweaks");
        structuresInCrystal = dustConfig.getInt("1.Crystal Structure Generation", category,150,1,9999,"Structures in Crystal Biomes");
        structuresInWorld = dustConfig.getInt("2.Structure Generation", category,300,1,9999,"Structures in any biome");
        treeGenChance = dustConfig.getInt("3.Trees Generation", category,9,1,9999,"1 in X chunks a tree will spawn in a crystal biome");
        anyBiomeOreSpawnChance = dustConfig.getInt("4.Ore Spawn Rate", category,4,1,9999,"Specifically in Normal Biomes");
        oreClusterSize = dustConfig.getInt("5.Cluster Size", category,3,1,20,"Size of Crystal Ore CLusters");
        oreRegrowthRate = dustConfig.getInt("6.Regrow Rate", category,100,1,999,"Higher is faster regeneration of crystal ore");

        category = "Misc";
        dustConfig.addCustomCategoryComment(category,"Player Interaction");
        funhaters = dustConfig.getBoolean("1.Fun Hater?",category,false,"Do you hate fun and Explosions?");





        dustConfig.save();
    }
}
