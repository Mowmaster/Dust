package com.mowmaster.dust;

import net.neoforged.neoforge.common.ModConfigSpec;

// Dust's game settings (config/dust-synced.toml: loaded with the server, sent to clients). Registered in Dust
// as SYNCED (FML 12's config types: LOCAL, CLIENT, SYNCED, STARTUP; there's no COMMON any more).
// [spells.metal]: what Metal's block effect drops from blocks that aren't ores: a nugget, picked by these weights (the
// chance of each is its weight out of the total).
// [dust_clouds]: the dusty air round spilled dust (Features/DustStorage/DustClouds): whether it explodes, how much dust
// it takes, how big the blasts are, how far a pile's cloud reaches.
// [worldgen]: how rare Dust's geodes, ore boulders, flowers and trees are in new terrain (Features/Worldgen).
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

    public static final ModConfigSpec.IntValue GEODE_RARITY;
    public static final ModConfigSpec.IntValue BOULDER_RARITY;
    public static final ModConfigSpec.IntValue FLOWER_RARITY;
    public static final ModConfigSpec.IntValue TREE_RARITY;
    public static final ModConfigSpec.IntValue RUIN_RARITY;
    public static final ModConfigSpec.IntValue SPAWNER_COOLDOWN;
    public static final ModConfigSpec.BooleanValue LABYRINTH_KEEP_INVENTORY;
    public static final ModConfigSpec.DoubleValue SPAWNER_KEY_CHANCE;

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

        BUILDER.comment("What Dust adds to newly generated terrain (Features/Worldgen): about one in this many chunks, where the biome suits it").push("worldgen");
        GEODE_RARITY = BUILDER.comment("Element geodes, underground in biomes rich in a primal (vanilla's amethyst geodes are one in 24)")
                .defineInRange("geode_rarity", 48, 1, 100000);
        BOULDER_RARITY = BUILDER.comment("Ore boulders on the surface (64: about one in a small biome, 4 in a medium one, 16 in a large one)")
                .defineInRange("boulder_rarity", 64, 1, 100000);
        FLOWER_RARITY = BUILDER.comment("Patches of elemental flowers, in biomes rich in their element")
                .defineInRange("flower_rarity", 32, 1, 100000);
        TREE_RARITY = BUILDER.comment("Elemental trees, in biomes rich in their element")
                .defineInRange("tree_rarity", 24, 1, 100000);
        RUIN_RARITY = BUILDER.comment("Ancient ruins (glyph walls) on the surface, anywhere in the overworld (128: half as common as ore boulders)")
                .defineInRange("ruin_rarity", 128, 1, 100000);
        BUILDER.pop();

        BUILDER.comment("Sanctums and their labyrinths (Features/Sanctums, Features/Labyrinth)").push("sanctums");
        SPAWNER_COOLDOWN = BUILDER.comment("Ticks an Arcane Spawner rests once its wave is beaten (36000: half an hour, as a trial spawner)")
                .defineInRange("spawner_cooldown", 36000, 20, 10000000);
        SPAWNER_KEY_CHANCE = BUILDER.comment("The chance that each player who helped beat an Arcane Spawner's wave also gets a Vault Key of its element (0.2: one in five)")
                .defineInRange("spawner_key_chance", 0.2, 0.0, 1.0);
        LABYRINTH_KEEP_INVENTORY = BUILDER.comment("Whether players who die in a labyrinth keep everything they had (they wake in front of the portal they came through either way)")
                .define("labyrinth_keep_inventory", true);
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    // A worldgen rarity, safe to read before the config has loaded (its default then)
    public static int rarity(ModConfigSpec.IntValue value)
    {
        try {
            return Math.max(1, value.get());
        } catch (IllegalStateException notLoaded) {
            return Math.max(1, value.getDefault());
        }
    }

    private DustConfig() {}
}
