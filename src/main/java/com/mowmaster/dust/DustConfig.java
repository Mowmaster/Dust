package com.mowmaster.dust;

import net.neoforged.neoforge.common.ModConfigSpec;

// Dust's game settings (config/dust-synced.toml: loaded with the server, sent to clients). Registered in Dust
// as SYNCED (FML 12's config types: LOCAL, CLIENT, SYNCED, STARTUP; there's no COMMON any more).
// [spells.metal]: what Metal's block effect drops from blocks that aren't ores: a nugget, picked by these weights (the
// chance of each is its weight out of the total).
public final class DustConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue METAL_COPPER_WEIGHT;
    public static final ModConfigSpec.IntValue METAL_IRON_WEIGHT;
    public static final ModConfigSpec.IntValue METAL_GOLD_WEIGHT;

    static {
        BUILDER.push("spells").comment("Metal on a block that isn't an ore: the nugget it drops when broken, by weight").push("metal");
        METAL_COPPER_WEIGHT = BUILDER.comment("Copper nugget").defineInRange("copper", 70, 0, 1000);
        METAL_IRON_WEIGHT = BUILDER.comment("Iron nugget").defineInRange("iron", 20, 0, 1000);
        METAL_GOLD_WEIGHT = BUILDER.comment("Gold nugget").defineInRange("gold", 10, 0, 1000);
        BUILDER.pop(2);
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private DustConfig() {}
}
