/*
package com.mowmaster.dust.World.GeodeGen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.mowmaster.dust.References.Constants.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class GeodeGenerator
{
    @SubscribeEvent
    public static void generateOres(final BiomeLoadingEvent event) {
        for (GeodeTypes ore : GeodeTypes.values()) {
            if (!(event.getCategory().equals(Biome.BiomeCategory.NETHER) || event.getCategory().equals(Biome.BiomeCategory.THEEND))) {
                if (ore.getGenerateOreToggle()) {
                    ImmutableList<OreConfiguration.TargetBlockState> ORE_TARGET_LIST = ImmutableList.of(OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, ore.getBlock().get().defaultBlockState()), OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, ore.getDeepslateBlock().get().defaultBlockState()));
                    ConfiguredFeature<?, ?> ORE_TYPE = register(ore.getBlock().get(), Feature.REPLACE_SINGLE_BLOCK.configured(new ReplaceBlockConfiguration(ORE_TARGET_LIST)).rangeUniform(VerticalAnchor.absolute(ore.getMinHeight()), VerticalAnchor.absolute(ore.getMaxHeight())).squared().count(UniformInt.of(ore.getMinVeinSize(), ore.getMaxVeinSize())));
                    generateOre(event.getGeneration(), ORE_TYPE);
                }
            }
        }

    }

    private static <FC extends FeatureConfiguration> ConfiguredFeature<?, ?> register(Block ore, ConfiguredFeature<FC, ?> pConfiguredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ore.getRegistryName(), pConfiguredFeature);
    }

    public static void generateOre(BiomeGenerationSettings.Builder pBuilder, ConfiguredFeature<?, ?> ore) {
        pBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ore);
    }
}
*/
