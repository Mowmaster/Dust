package com.mowmaster.dust.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static com.mowmaster.dust.misc.DustConfigurationFile.*;


public class BiomeRegistry
{
    private static int biomeWeight = biomeWeightChunks;

    public static void BiomeReg()
    {
        initBiome();
        regBiome();
    }

    public static Biome crystal_hot;
    public static Biome crystal_warm;
    public static Biome crystal_cold;
    public static Biome crystal_crystal;
    public static void initBiome()
    {
        crystal_hot = new CrystalHot(new Biome.BiomeProperties("Crystal Hot").setWaterColor(0).setRainDisabled().setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(2.0F));
        crystal_hot.setRegistryName("crystalhot");
        ForgeRegistries.BIOMES.register(crystal_hot);
        crystal_warm = new CrystalWarm(new Biome.BiomeProperties("Crystal Warm").setWaterColor(0).setRainDisabled().setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(0.8F));
        crystal_warm.setRegistryName("crystalwarm");
        ForgeRegistries.BIOMES.register(crystal_warm);
        crystal_cold = new CrystalCold(new Biome.BiomeProperties("Crystal Cold").setWaterColor(0).setRainDisabled().setBaseHeight(0.2F).setHeightVariation(0.2F).setTemperature(0.25F));
        crystal_cold.setRegistryName("crystalcold");
        ForgeRegistries.BIOMES.register(crystal_cold);
        crystal_crystal = new Crystal(new Biome.BiomeProperties("Crystal").setWaterColor(0).setRainDisabled().setBaseHeight(2.0F).setHeightVariation(0.125F).setTemperature(0.8F));
        crystal_crystal.setRegistryName("crystal");
        ForgeRegistries.BIOMES.register(crystal_crystal);
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
