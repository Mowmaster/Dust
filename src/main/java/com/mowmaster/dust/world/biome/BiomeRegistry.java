package com.mowmaster.dust.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class BiomeRegistry
{
    private static int biomeWeight = 20;
    private static int  biomeCrystalHot = 200;
    private static int biomeCrystalWarm = 201;
    private static int biomeCrystalCold = 202;
    private static int biomeCrystal = 203;

    public static void BiomeReg()
    {
        initBiome();
        regBiome();
    }

    public static Biome crystal_hot;
    public static final int biomeIdHot = biomeCrystalHot;
    public static Biome crystal_warm;
    public static final int biomeIdWarm = biomeCrystalWarm;
    public static Biome crystal_cold;
    public static final int biomeIdCold = biomeCrystalCold;
    public static Biome crystal_crystal;
    public static final int biomeIdCrystal = biomeCrystal;

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
        BiomeDictionary.addTypes(crystal_hot,BiomeDictionary.Type.MAGICAL,BiomeDictionary.Type.HOT);
        BiomeDictionary.addTypes(crystal_warm,BiomeDictionary.Type.MAGICAL,BiomeDictionary.Type.PLAINS);
        BiomeDictionary.addTypes(crystal_cold,BiomeDictionary.Type.MAGICAL,BiomeDictionary.Type.COLD);
        BiomeDictionary.addTypes(crystal_crystal,BiomeDictionary.Type.MAGICAL,BiomeDictionary.Type.MOUNTAIN);
        BiomeManager.addSpawnBiome(crystal_hot);
        BiomeManager.addSpawnBiome(crystal_warm);
        BiomeManager.addSpawnBiome(crystal_cold);
        BiomeManager.addSpawnBiome(crystal_crystal);

        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(crystal_hot,biomeWeight));//Number is Biome Weight
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(crystal_warm,biomeWeight));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(crystal_cold,biomeWeight));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(crystal_crystal,(biomeWeight/2)));

        BiomeManager.addVillageBiome(crystal_crystal,true);


    }
}
