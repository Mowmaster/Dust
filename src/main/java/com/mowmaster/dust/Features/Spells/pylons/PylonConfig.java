package com.mowmaster.dust.Features.Spells.pylons;

// The numbers behind the spell pylons, in one place to tune
public final class PylonConfig
{
    // A category pylon (Change / Cost / Count / Damage) adds this to its category's weight multiplier (1 + boost); every
    // pylon placed after takes DECAY off each category's boost, back down to nothing
    public static final float CATEGORY_BOOST = 2.0F;
    public static final float BOOST_DECAY = 0.5F;
    // Blessed / corrupted modifiers can only be rolled with at least this much blessing / corruption; rolling one uses
    // up TIER_USE of it. Their weight grows with how much there is (x the counter / TIER_MIN).
    public static final int TIER_MIN = 3;
    public static final int TIER_USE = 3;
    // Rerolling a pylon (breaking it) adds corruption; keeping one (placing the next pylon after it) adds blessing.
    // Breaking a blessed pylon loses BLESSED_LOSS blessing; breaking a corrupted one adds CORRUPTED_GAIN corruption.
    public static final int RESET_CORRUPTION = 1;
    public static final int KEEP_BLESSING = 1;
    public static final int BLESSED_LOSS = 5;
    public static final int CORRUPTED_GAIN = 5;
    // A spell break can turn up once the line has this many modifiers, with this chance per pylon
    public static final int BREAK_MIN_MODIFIERS = 5;
    public static final float BREAK_CHANCE = 0.12F;
    // A broken main pylon keeps this share of its levels (for the same element)
    public static final float KEPT_LEVELS = 0.1F;

    private PylonConfig() {}
}
