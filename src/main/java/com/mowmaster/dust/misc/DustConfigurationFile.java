package com.mowmaster.dust.misc;

import net.minecraftforge.common.config.Configuration;

import java.io.File;


public class DustConfigurationFile
{
    public static Configuration dustConfig;

    public static int biomeWeightChunks;

    public static int structuresInCrystal;
    public static int structuresInWorld;
    public static int treeGenChance;
    public static int anyBiomeOreSpawnChance;
    public static int oreClusterSize;
    public static int oreRegrowthRate;
    public static boolean funhaters;
    public static boolean devBlocks;
    public static int effectMaximum;
    public static int dustToActivate;


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
        biomeWeightChunks = dustConfig.getInt("5.Crystal Biome Weight", category,500,1,999,"Larger Number is less often");

        category = "World Generation";
        dustConfig.addCustomCategoryComment(category,"World Generation Tweaks");
        structuresInCrystal = dustConfig.getInt("1.Crystal Structure Generation", category,1000,1,9999,"Structures in Crystal Biomes");
        structuresInWorld = dustConfig.getInt("2.Structure Generation", category,1500,1,9999,"Structures in any biome");
        treeGenChance = dustConfig.getInt("3.Trees Generation", category,9,1,9999,"1 in X chunks a tree will spawn in a crystal biome");
        anyBiomeOreSpawnChance = dustConfig.getInt("4.Ore Spawn Rate", category,50,1,9999,"Specifically in Normal Biomes");
        oreClusterSize = dustConfig.getInt("5.Cluster Size", category,3,1,20,"Size of Crystal Ore Clusters");
        oreRegrowthRate = dustConfig.getInt("6.Regrow Rate", category,40,1,999,"Higher is faster regeneration of crystal ore");

        category = "Misc";
        dustConfig.addCustomCategoryComment(category,"Player Interaction");
        funhaters = dustConfig.getBoolean("1.Fun Hater?",category,false,"Do you hate fun and Explosions?");
        devBlocks = dustConfig.getBoolean("2.Creative Use Dev Blocks?",category,false,"True is you want to use dev blocks while you build custom structures");

        category = "Effects";
        dustConfig.addCustomCategoryComment(category,"Spells/Effects");
        effectMaximum = dustConfig.getInt("1.Spell Potency", category,4,0,9,"How potent spells can be");
        dustToActivate = dustConfig.getInt("2.Spell Activation", category,5,0,9999,"Requirement for how much dust must be used to Activate spells/effects");


        dustConfig.save();
    }
}
