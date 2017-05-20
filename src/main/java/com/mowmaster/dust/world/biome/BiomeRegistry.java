package com.mowmaster.dust.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

/**
 * Created by KingMowmaster on 5/20/2017.
 */
public class BiomeRegistry
{
    public static void BiomeReg()
    {
        initBiome();
        regBiome();
    }

    public static Biome crystal_hot;
    public static final int biomeId = 200;

    public static void initBiome()
    {
        crystal_hot = new CrystalHot(new Biome.BiomeProperties("Crystal").setWaterColor(0).setRainDisabled().setTemperature(3.0f).setBaseHeight(1.5f).setHeightVariation(0.75f));
        Biome.registerBiome(biomeId,"Crystal",crystal_hot);
    }
    public static void regBiome()
    {
        BiomeDictionary.hasType(crystal_hot,BiomeDictionary.Type.HOT);
        BiomeManager.addSpawnBiome(crystal_hot);
    }
}
