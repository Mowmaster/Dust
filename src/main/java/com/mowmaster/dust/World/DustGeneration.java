package com.mowmaster.dust.World;

import com.mowmaster.dust.Configs.DustGenerationConfig;
import com.mowmaster.dust.World.GeodeGen.GeodeFeatures;
import com.mowmaster.dust.World.GeodeGen.GeodeFeaturesRegistry;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static com.mowmaster.dust.References.ColorReference.getRandomGeodeFeature;
import static com.mowmaster.dust.World.GeodeGen.GeodeFeatures.*;

public class DustGeneration
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void biomeLoadingEvent(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder builder = event.getGeneration();

        if(DustGenerationConfig.COMMON.generate333.get()) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_333.getGeodeFeature().rarity(DustGenerationConfig.COMMON.geodeRarity333.get()).decorated(GeodeFeatures.RNG_DECORATOR));
        }
        if(DustGenerationConfig.COMMON.generate000.get()) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GEODE_000.getGeodeFeature().rarity(DustGenerationConfig.COMMON.geodeRarity000.get()).decorated(GeodeFeatures.RNG_DECORATOR));
        }
        if(DustGenerationConfig.COMMON.generate300.get()) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_300.getGeodeFeature().rarity(DustGenerationConfig.COMMON.geodeRarity300.get()).decorated(GeodeFeatures.RNG_DECORATOR));
        }
        if(DustGenerationConfig.COMMON.generate030.get()) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_030.getGeodeFeature().rarity(DustGenerationConfig.COMMON.geodeRarity030.get()).decorated(GeodeFeatures.RNG_DECORATOR));
        }
        if(DustGenerationConfig.COMMON.generate003.get()) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.GEODE_003.getGeodeFeature().rarity(DustGenerationConfig.COMMON.geodeRarity003.get()).decorated(GeodeFeatures.RNG_DECORATOR));
        }
        if(DustGenerationConfig.COMMON.generateGeodeRandom.get()) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, getRandomGeodeFeature(new Random().nextInt(63)).getGeodeFeature().rarity(DustGenerationConfig.COMMON.geodeRarityGeodeRandom.get()).decorated(GeodeFeatures.RNG_DECORATOR));
        }
        /*if(DustGenerationConfig.COMMON.generateWorldRandom.get()) {
            builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GeodeFeatures.NODE_RANDOM.getGeodeFeature().rarity(DustGenerationConfig.COMMON.geodeRarityWorldRandom.get()).decorated(GeodeFeatures.RNG_DECORATOR));
        }*/

    }
}


