package com.mowmaster.dust.World.GeodeGen;

import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.References.ColorReference;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
//import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
//import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;

import java.util.Arrays;

//Huge thanks to MyBysco/ShyNieke's https://github.com/Mrbysco/GeOre mod, I probably never would have been able to figure out the geodes without it.
public class GeodeFeatures
{
    public static void initialize() {}

    public static final PlacementModifier RNG_DECORATOR = HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top());
    //public static final ConfiguredDecorator<NoneDecoratorConfiguration> RNG_DECORATOR = DeferredRegisterBlocks.RNG_DECORATOR.get().configured(DecoratorConfiguration.NONE);

    public static GeodeFeaturesRegistry GEODE_000 = new GeodeFeaturesRegistry("gennode_000", ColorReference.getColor(Arrays.asList(0,0,0)), DustConfig.COMMON.geodeRarity000::get);
    public static GeodeFeaturesRegistry GEODE_001 = new GeodeFeaturesRegistry("gennode_001", ColorReference.getColor(Arrays.asList(0,0,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_002 = new GeodeFeaturesRegistry("gennode_002", ColorReference.getColor(Arrays.asList(0,0,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_003 = new GeodeFeaturesRegistry("gennode_003", ColorReference.getColor(Arrays.asList(0,0,3)), DustConfig.COMMON.geodeRarity003::get);
    public static GeodeFeaturesRegistry GEODE_010 = new GeodeFeaturesRegistry("gennode_010", ColorReference.getColor(Arrays.asList(0,1,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_011 = new GeodeFeaturesRegistry("gennode_011", ColorReference.getColor(Arrays.asList(0,1,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_012 = new GeodeFeaturesRegistry("gennode_012", ColorReference.getColor(Arrays.asList(0,1,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_013 = new GeodeFeaturesRegistry("gennode_013", ColorReference.getColor(Arrays.asList(0,1,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_020 = new GeodeFeaturesRegistry("gennode_020", ColorReference.getColor(Arrays.asList(0,2,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_021 = new GeodeFeaturesRegistry("gennode_021", ColorReference.getColor(Arrays.asList(0,2,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_022 = new GeodeFeaturesRegistry("gennode_022", ColorReference.getColor(Arrays.asList(0,2,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_023 = new GeodeFeaturesRegistry("gennode_023", ColorReference.getColor(Arrays.asList(0,2,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_030 = new GeodeFeaturesRegistry("gennode_030", ColorReference.getColor(Arrays.asList(0,3,0)), DustConfig.COMMON.geodeRarity030::get);
    public static GeodeFeaturesRegistry GEODE_031 = new GeodeFeaturesRegistry("gennode_031", ColorReference.getColor(Arrays.asList(0,3,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_032 = new GeodeFeaturesRegistry("gennode_032", ColorReference.getColor(Arrays.asList(0,3,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_033 = new GeodeFeaturesRegistry("gennode_033", ColorReference.getColor(Arrays.asList(0,3,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_100 = new GeodeFeaturesRegistry("gennode_100", ColorReference.getColor(Arrays.asList(1,0,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_101 = new GeodeFeaturesRegistry("gennode_101", ColorReference.getColor(Arrays.asList(1,0,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_102 = new GeodeFeaturesRegistry("gennode_102", ColorReference.getColor(Arrays.asList(1,0,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_103 = new GeodeFeaturesRegistry("gennode_103", ColorReference.getColor(Arrays.asList(1,0,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_110 = new GeodeFeaturesRegistry("gennode_110", ColorReference.getColor(Arrays.asList(1,1,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_111 = new GeodeFeaturesRegistry("gennode_111", ColorReference.getColor(Arrays.asList(1,1,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_112 = new GeodeFeaturesRegistry("gennode_112", ColorReference.getColor(Arrays.asList(1,1,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_113 = new GeodeFeaturesRegistry("gennode_113", ColorReference.getColor(Arrays.asList(1,1,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_120 = new GeodeFeaturesRegistry("gennode_120", ColorReference.getColor(Arrays.asList(1,2,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_121 = new GeodeFeaturesRegistry("gennode_121", ColorReference.getColor(Arrays.asList(1,2,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_122 = new GeodeFeaturesRegistry("gennode_122", ColorReference.getColor(Arrays.asList(1,2,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_123 = new GeodeFeaturesRegistry("gennode_123", ColorReference.getColor(Arrays.asList(1,2,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_130 = new GeodeFeaturesRegistry("gennode_130", ColorReference.getColor(Arrays.asList(1,3,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_131 = new GeodeFeaturesRegistry("gennode_131", ColorReference.getColor(Arrays.asList(1,3,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_132 = new GeodeFeaturesRegistry("gennode_132", ColorReference.getColor(Arrays.asList(1,3,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_133 = new GeodeFeaturesRegistry("gennode_133", ColorReference.getColor(Arrays.asList(1,3,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_200 = new GeodeFeaturesRegistry("gennode_200", ColorReference.getColor(Arrays.asList(2,0,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_201 = new GeodeFeaturesRegistry("gennode_201", ColorReference.getColor(Arrays.asList(2,0,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_202 = new GeodeFeaturesRegistry("gennode_202", ColorReference.getColor(Arrays.asList(2,0,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_203 = new GeodeFeaturesRegistry("gennode_203", ColorReference.getColor(Arrays.asList(2,0,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_210 = new GeodeFeaturesRegistry("gennode_210", ColorReference.getColor(Arrays.asList(2,1,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_211 = new GeodeFeaturesRegistry("gennode_211", ColorReference.getColor(Arrays.asList(2,1,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_212 = new GeodeFeaturesRegistry("gennode_212", ColorReference.getColor(Arrays.asList(2,1,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_213 = new GeodeFeaturesRegistry("gennode_213", ColorReference.getColor(Arrays.asList(2,1,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_220 = new GeodeFeaturesRegistry("gennode_220", ColorReference.getColor(Arrays.asList(2,2,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_221 = new GeodeFeaturesRegistry("gennode_221", ColorReference.getColor(Arrays.asList(2,2,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_222 = new GeodeFeaturesRegistry("gennode_222", ColorReference.getColor(Arrays.asList(2,2,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_223 = new GeodeFeaturesRegistry("gennode_223", ColorReference.getColor(Arrays.asList(2,2,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_230 = new GeodeFeaturesRegistry("gennode_230", ColorReference.getColor(Arrays.asList(2,3,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_231 = new GeodeFeaturesRegistry("gennode_231", ColorReference.getColor(Arrays.asList(2,3,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_232 = new GeodeFeaturesRegistry("gennode_232", ColorReference.getColor(Arrays.asList(2,3,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_233 = new GeodeFeaturesRegistry("gennode_233", ColorReference.getColor(Arrays.asList(2,3,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_300 = new GeodeFeaturesRegistry("gennode_300", ColorReference.getColor(Arrays.asList(3,0,0)), DustConfig.COMMON.geodeRarity300::get);
    public static GeodeFeaturesRegistry GEODE_301 = new GeodeFeaturesRegistry("gennode_301", ColorReference.getColor(Arrays.asList(3,0,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_302 = new GeodeFeaturesRegistry("gennode_302", ColorReference.getColor(Arrays.asList(3,0,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_303 = new GeodeFeaturesRegistry("gennode_303", ColorReference.getColor(Arrays.asList(3,0,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_310 = new GeodeFeaturesRegistry("gennode_310", ColorReference.getColor(Arrays.asList(3,1,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_311 = new GeodeFeaturesRegistry("gennode_311", ColorReference.getColor(Arrays.asList(3,1,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_312 = new GeodeFeaturesRegistry("gennode_312", ColorReference.getColor(Arrays.asList(3,1,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_313 = new GeodeFeaturesRegistry("gennode_313", ColorReference.getColor(Arrays.asList(3,1,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_320 = new GeodeFeaturesRegistry("gennode_320", ColorReference.getColor(Arrays.asList(3,2,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_321 = new GeodeFeaturesRegistry("gennode_321", ColorReference.getColor(Arrays.asList(3,2,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_322 = new GeodeFeaturesRegistry("gennode_322", ColorReference.getColor(Arrays.asList(3,2,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_323 = new GeodeFeaturesRegistry("gennode_323", ColorReference.getColor(Arrays.asList(3,2,3)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_330 = new GeodeFeaturesRegistry("gennode_330", ColorReference.getColor(Arrays.asList(3,3,0)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_331 = new GeodeFeaturesRegistry("gennode_331", ColorReference.getColor(Arrays.asList(3,3,1)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_332 = new GeodeFeaturesRegistry("gennode_332", ColorReference.getColor(Arrays.asList(3,3,2)), DustConfig.COMMON.geodeRarityGeodeRandom::get);
    public static GeodeFeaturesRegistry GEODE_333 = new GeodeFeaturesRegistry("gennode_333", ColorReference.getColor(Arrays.asList(3,3,3)), DustConfig.COMMON.geodeRarity333::get);

    public static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, name, configuredFeature);
    }
}
