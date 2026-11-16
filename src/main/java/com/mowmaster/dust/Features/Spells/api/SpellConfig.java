package com.mowmaster.dust.Features.Spells.api;

// The spell defaults, in one place to tune
public final class SpellConfig
{
    // Effects last 5 seconds unless they're instant
    public static final int BASE_DURATION = 20 * 5;
    // One heart
    public static final float BASE_DAMAGE = 2.0F;
    // "Nearby": 5 blocks around the caster / target
    public static final double NEARBY = 5.0;

    // A projectile: flies this far before dissipating,
    public static final double BASE_RANGE = 8.0;
    //at this many blocks per tick,
    public static final float BASE_SPEED = 0.5F;
    // dropping this much per tick,
    public static final float BASE_GRAVITY = 0.01F;
    // keeping this much of its speed each tick (air drag)
    public static final float AIR_DRAG = 0.99F;
    // A spell's dust cost: this much of its element, plus each modifier's cost (1 of its element unless it says otherwise)
    public static final int BASE_DUST_COST = 1;

    private SpellConfig() {}

    // ---- shape modifiers (SpellRegistries) ----
    // AOE: the radius before Size (blocks); Size adds this much a level
    public static final double AOE_RADIUS = 1.5;
    public static final double SIZE_PER_LEVEL = 0.25;
    // Chaining: how far it looks for the next creature before Search Distance (blocks)
    public static final double CHAIN_SEARCH = 2.0;
    // Wall: how far it reaches either side before Wall Width, and what Width and Depth add a level (blocks)
    public static final double WALL_HALF = 1.0;
    public static final double WALL_PER_LEVEL = 0.5;
    // Rune: how long a rune lasts before Rune Duration, and how long it has to last for Permanent (seconds)
    public static final int RUNE_SECONDS = 20;
    public static final int PERMANENT_SECONDS = 600;
    // Permanent: casting one costs this many times the mana
    public static final int PERMANENT_MANA = 4;
    // Beam: ticks between pulses before Casting Speed (never below BEAM_MIN_INTERVAL); the most Efficiency takes off (percent)
    public static final int BEAM_INTERVAL = 20;
    public static final int BEAM_MIN_INTERVAL = 2;
    public static final int MAX_EFFICIENCY = 75;
    // Lingering: how often the pool puts the effect on what's in it (ticks)
    public static final int LINGER_INTERVAL = 20;
}
