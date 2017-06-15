package com.mowmaster.dust.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
    public static final int biomeIdHot = 200;
    public static Biome crystal_warm;
    public static final int biomeIdWarm = 201;
    public static Biome crystal_cold;
    public static final int biomeIdCold = 202;
    public static Biome crystal_crystal;
    public static final int biomeIdCrystal = 203;

    public static void initBiome()
    {
        crystal_hot = new CrystalHot(new Biome.BiomeProperties("CrystalHot").setWaterColor(0).setRainDisabled().setTemperature(3.0f).setBaseHeight(1.5f).setHeightVariation(0.75f));
        Biome.registerBiome(biomeIdHot,"CrystalHot",crystal_hot);
        crystal_warm = new CrystalWarm(new Biome.BiomeProperties("CrystalWarm").setWaterColor(0).setRainDisabled().setTemperature(3.0f).setBaseHeight(1.5f).setHeightVariation(0.75f));
        Biome.registerBiome(biomeIdWarm,"CrystalWarm",crystal_warm);
        crystal_cold = new CrystalCold(new Biome.BiomeProperties("CrystalCold").setWaterColor(0).setRainDisabled().setTemperature(3.0f).setBaseHeight(1.5f).setHeightVariation(0.75f));
        Biome.registerBiome(biomeIdCold,"CrystalCold",crystal_cold);
        crystal_crystal = new Crystal(new Biome.BiomeProperties("Crystal").setWaterColor(0).setRainDisabled().setTemperature(3.0f).setBaseHeight(1.5f).setHeightVariation(0.75f));
        Biome.registerBiome(biomeIdCrystal,"Crystal",crystal_crystal);
    }
    public static void regBiome()
    {
        BiomeDictionary.hasType(crystal_hot,BiomeDictionary.Type.HOT);
        BiomeDictionary.hasType(crystal_warm,BiomeDictionary.Type.PLAINS);
        BiomeDictionary.hasType(crystal_cold,BiomeDictionary.Type.COLD);
        BiomeDictionary.hasType(crystal_crystal,BiomeDictionary.Type.MAGICAL);
        BiomeManager.addSpawnBiome(crystal_hot);
        BiomeManager.addSpawnBiome(crystal_warm);
        BiomeManager.addSpawnBiome(crystal_cold);
        BiomeManager.addSpawnBiome(crystal_crystal);
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(crystal_hot,50));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(crystal_warm,50));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(crystal_cold,50));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(crystal_crystal,50));

    }
}
