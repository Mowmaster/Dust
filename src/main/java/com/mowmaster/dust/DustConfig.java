package com.mowmaster.dust;

import net.neoforged.neoforge.common.ModConfigSpec;

// Dust's game settings (config/dust-synced.toml: loaded with the server, sent to clients). Registered in Dust
// as SYNCED (FML 12's config types: LOCAL, CLIENT, SYNCED, STARTUP; there's no COMMON any more).
// [spells.metal]: what Metal's block effect drops from blocks that aren't ores: a nugget, picked by these weights (the
// chance of each is its weight out of the total).
// [dust_clouds]: the dusty air round spilled dust (Features/DustStorage/DustClouds): whether it explodes, how much dust
// it takes, how big the blasts are, how far a pile's cloud reaches.
public final class DustConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue METAL_COPPER_WEIGHT;
    public static final ModConfigSpec.IntValue METAL_IRON_WEIGHT;
    public static final ModConfigSpec.IntValue METAL_GOLD_WEIGHT;

    public static final ModConfigSpec.BooleanValue DUST_EXPLOSIONS;
    public static final ModConfigSpec.IntValue DUST_TO_EXPLODE;
    public static final ModConfigSpec.DoubleValue DUST_EXPLOSION_POWER;
    public static final ModConfigSpec.DoubleValue DUST_EXPLOSION_MAX_POWER;
    public static final ModConfigSpec.IntValue DUST_CLOUD_SIZE;

    static {
        BUILDER.push("spells").comment("Metal on a block that isn't an ore: the nugget it drops when broken, by weight").push("metal");
        METAL_COPPER_WEIGHT = BUILDER.comment("Copper nugget").defineInRange("copper", 70, 0, 1000);
        METAL_IRON_WEIGHT = BUILDER.comment("Iron nugget").defineInRange("iron", 20, 0, 1000);
        METAL_GOLD_WEIGHT = BUILDER.comment("Gold nugget").defineInRange("gold", 10, 0, 1000);
        BUILDER.pop(2);

        BUILDER.comment("The dusty air round Dust Piles (spilled out of tubes with no jar)").push("dust_clouds");
        DUST_EXPLOSIONS = BUILDER.comment("Whether a spark (a flame, a creeper, a spell, a flint and steel) in a thick enough cloud makes it explode")
                .define("explosions", true);
        DUST_TO_EXPLODE = BUILDER.comment("How much dust has to be in the air at a spark for it to explode (the haze is half see-through at this much)")
                .defineInRange("dust_to_explode", 500, 1, 100000);
        DUST_EXPLOSION_POWER = BUILDER.comment("Explosion power with dust_to_explode dust (TNT is 4); it grows with the square root of the dust")
                .defineInRange("explosion_power", 8.0, 0.0, 64.0);
        DUST_EXPLOSION_MAX_POWER = BUILDER.comment("The most power one blast can have")
                .defineInRange("max_power", 16.0, 0.0, 64.0);
        DUST_CLOUD_SIZE = BUILDER.comment("How wide the cloud round each pile is, in blocks (9: 9x9; even numbers round down to odd)")
                .defineInRange("cloud_size", 9, 1, 17);
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private DustConfig() {}
}
