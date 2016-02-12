package com.mowmaster.dust.world.biomes;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;


public class biomeRegistration {


    public static BiomeGenBase Crystal;
    public static BiomeGenBase CrystalRed;
    public static BiomeGenBase CrystalBlue;
    public static BiomeGenBase CrystalYellow;
    public static BiomeGenBase CrystalGreen;
    public static BiomeGenBase CrystalOrange;
    public static BiomeGenBase CrystalPurple;
    public static BiomeGenBase CrystalBlack;
    public static BiomeGenBase CrystalWhite;

    public static void biomeReg()
    {
        Crystal = new biomeCrystal(117).setTopFillerBlock(Blocks.grass.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(2.0F,0.025F).setBiomeName("Crystal");
        CrystalRed = new biomeCrystal(118).setTopFillerBlock(Blocks.sand.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(0.2F,0.2F).setBiomeName("Crystal").setTemperatureRainfall(1.8F, 0.0F).setDisableRain();
        CrystalBlue = new biomeCrystal(119).setTopFillerBlock(Blocks.grass.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(0.2F,0.2F).setBiomeName("Crystal").setTemperatureRainfall(0.25F, .8F);
        CrystalYellow = new biomeCrystal(120).setTopFillerBlock(Blocks.sand.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(0.2F,0.2F).setBiomeName("Crystal").setTemperatureRainfall(2.0F, 0.0F).setDisableRain();
        CrystalGreen = new biomeCrystal(121).setTopFillerBlock(Blocks.grass.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(0.2F,0.2F).setBiomeName("Crystal");
        CrystalOrange = new biomeCrystal(122).setTopFillerBlock(Blocks.grass.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(0.2F,0.2F).setBiomeName("Crystal").setTemperatureRainfall(1.3F, 0.1F);
        CrystalPurple = new biomeCrystal(123).setTopFillerBlock(Blocks.grass.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(0.2F,0.2F).setBiomeName("Crystal").setTemperatureRainfall(0.1F, 1.0F);
        CrystalBlack = new biomeCrystal(124).setTopFillerBlock(Blocks.grass.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(2.0F,0.025F).setBiomeName("Crystal").setTemperatureRainfall(-0.5F, 0.4F);
        CrystalWhite = new biomeCrystal(125).setTopFillerBlock(Blocks.grass.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(2.0F,0.025F).setBiomeName("Crystal").setTemperatureRainfall(0.2F, 0.3F);




        BiomeManager.addBiome(BiomeManager.BiomeType.WARM,new BiomeManager.BiomeEntry(Crystal,1));
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT,new BiomeManager.BiomeEntry(CrystalRed,5));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL,new BiomeManager.BiomeEntry(CrystalBlue,5));
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT,new BiomeManager.BiomeEntry(CrystalYellow,5));
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM,new BiomeManager.BiomeEntry(CrystalGreen,5));
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT,new BiomeManager.BiomeEntry(CrystalOrange,5));
        BiomeManager.addBiome(BiomeManager.BiomeType.ICY,new BiomeManager.BiomeEntry(CrystalPurple,5));
        BiomeManager.addBiome(BiomeManager.BiomeType.ICY,new BiomeManager.BiomeEntry(CrystalBlack,2));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL,new BiomeManager.BiomeEntry(CrystalWhite,1));

    }
}
