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
    // A projectile: flies this far before dissipating, at this many blocks per tick, dropping this much per tick, and
    // keeping this much of its speed each tick (air drag)
    public static final double BASE_RANGE = 24.0;
    public static final float BASE_SPEED = 1.2F;
    public static final float BASE_GRAVITY = 0.01F;
    public static final float AIR_DRAG = 0.99F;
    // A spell's dust cost: this much of its element, plus each modifier's cost (1 of its element unless it says otherwise)
    public static final int BASE_DUST_COST = 1;

    private SpellConfig() {}
}
