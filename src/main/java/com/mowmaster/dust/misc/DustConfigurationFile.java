package com.mowmaster.dust.misc;

import net.minecraftforge.common.config.Configuration;

import java.io.File;


public class DustConfigurationFile
{
    public static Configuration dustConfig;


    public static int overworldOreSpawnChance;


    public static void InitConfig(File file)
    {
        dustConfig = new Configuration(file);
        SyncConfig();
    }

    public static void SyncConfig()
    {
        String category;

        category = "World Generation";
        dustConfig.addCustomCategoryComment(category,"World Generation Tweaks");
        overworldOreSpawnChance = dustConfig.getInt("Over World Ore Spawn Chance", category,19,1,999,"(0 and 1) 2/x chance ore will spawn in a chunk");




        dustConfig.save();
    }
}
