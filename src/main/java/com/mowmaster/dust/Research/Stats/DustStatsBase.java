package com.mowmaster.dust.Research.Stats;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;

public class DustStatsBase
{
    public static final StatType<ResourceLocation> CUSTOM = makeRegistryStatType("custom", Registry.CUSTOM_STAT);

    private static ResourceLocation makeCustomStat(String p_13008_, StatFormatter p_13009_) {
        ResourceLocation resourcelocation = new ResourceLocation(p_13008_);
        Registry.register(Registry.CUSTOM_STAT, p_13008_, resourcelocation);
        CUSTOM.get(resourcelocation, p_13009_);
        return resourcelocation;
    }

    private static <T> StatType<T> makeRegistryStatType(String p_13011_, Registry<T> p_13012_) {
        return Registry.register(Registry.STAT_TYPE, p_13011_, new StatType<>(p_13012_));
    }
}
