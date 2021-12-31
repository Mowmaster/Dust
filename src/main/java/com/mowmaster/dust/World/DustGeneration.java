package com.mowmaster.dust.World;

import com.mowmaster.dust.Configs.DustConfig;
import com.mowmaster.dust.World.GeodeGen.GeodeFeatures;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DustGeneration
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void biomeLoadingEvent(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder builder = event.getGeneration();


        if(DustConfig.COMMON.generate000.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_000.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_001.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_002.getPlacedFeature()); }
        if(DustConfig.COMMON.generate003.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_003.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_010.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_011.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_012.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_013.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_020.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_021.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_022.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_023.getPlacedFeature()); }
        if(DustConfig.COMMON.generate030.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_030.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_031.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_032.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_033.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_100.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_101.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_102.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_103.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_110.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_111.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_112.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_113.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_120.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_121.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_122.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_123.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_130.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_131.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_132.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_133.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_200.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_201.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_202.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_203.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_210.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_211.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_212.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_213.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_220.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_221.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_222.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_223.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_230.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_231.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_232.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_233.getPlacedFeature()); }
        if(DustConfig.COMMON.generate300.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_300.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_301.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_302.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_303.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_310.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_311.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_312.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_313.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_320.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_321.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_322.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_323.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_330.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_331.getPlacedFeature()); }
        if(DustConfig.COMMON.generateGeodeRandom.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_332.getPlacedFeature()); }
        if(DustConfig.COMMON.generate333.get()) { builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_333.getPlacedFeature()); }




    }
}


