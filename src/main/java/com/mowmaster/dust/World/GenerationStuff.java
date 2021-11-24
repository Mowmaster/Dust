package com.mowmaster.dust.World;

import com.google.common.collect.ImmutableList;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraftforge.fml.common.Mod;

import static com.mowmaster.dust.References.Constants.MODID;

//@Mod.EventBusSubscriber(modid = MODID)
public class GenerationStuff
{
    //public static final ConfiguredFeature<?, ?> CRYSTAL_GEODE_INERT = register(MODID + "_crystal_geode", Feature.GEODE.configured(new GeodeConfiguration(new GeodeBlockSettings(new SimpleStateProvider(Blocks.AIR.defaultBlockState()), new SimpleStateProvider(DeferredRegisterBlocks.CRYSTAL_BLOCK.get().defaultBlockState()), new SimpleStateProvider(DeferredRegisterBlocks.CRYSTAL_NODE.get().defaultBlockState()), new SimpleStateProvider(Blocks.CALCITE.defaultBlockState()), new SimpleStateProvider(Blocks.SMOOTH_BASALT.defaultBlockState()), ImmutableList.of(DeferredRegisterBlocks.CRYSTAL_CLUSTER_SMALL.get().defaultBlockState(), DeferredRegisterBlocks.CRYSTAL_CLUSTER_MEDIUM.get().defaultBlockState(), DeferredRegisterBlocks.CRYSTAL_CLUSTER_LARGE.get().defaultBlockState(), DeferredRegisterBlocks.CRYSTAL_CLUSTER_FULL.get().defaultBlockState()), BlockTags.FEATURES_CANNOT_REPLACE.getName(), BlockTags.GEODE_INVALID_BLOCKS.getName()), new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D), new GeodeCrackSettings(0.95D, 2.0D, 2), 0.35D, 0.083D, true, UniformInt.of(4, 6), UniformInt.of(3, 4), UniformInt.of(1, 2), -16, 16, 0.05D, 1)).rangeUniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(46)).squared().rarity(53));

    /*private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String p_127056_, ConfiguredFeature<FC, ?> p_127057_) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, p_127056_, p_127057_);
    }*/
}


