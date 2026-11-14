package com.mowmaster.dust.Features.Spells.pylons;

// The numbers behind the spell pylons, in one place to tune
public final class PylonConfig
{
    // A category pylon (Change / Cost / Count / Damage) adds this to its category's weight multiplier (1 + boost); every
    // pylon placed after takes DECAY off each category's boost, back down to nothing
    public static final float CATEGORY_BOOST = 2.0F;
    public static final float BOOST_DECAY = 0.5F;
    // A pylon starting a branch (placed against one that already has a pylon after it) also favours the category of
    // the pylon it branches from, by this much, for its own roll
    public static final float BRANCH_BOOST = 0.5F;
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
    // Upgrade nodes: each modifier pylon gets a random number of them (MIN_NODES to MAX_NODES) when it joins a tree. Each
    // costs a level to fill and adds potency (and 1 dust per point of it) to the spell: 1 point while the spell is
    // below level 20, then 2 at 20-29, 3 at 30-39...
    public static final int MIN_NODES = 3;
    public static final int MAX_NODES = 10;

    public static int nodePoints(int spellLevel)
    {
        return spellLevel < 20 ? 1 : spellLevel / 10;
    }

    // Blessed / corrupted modifiers don't get upgrade nodes: their pylon rolls a strength in this range (percent)
    // instead (SpellModifier.usesStrength), and locks straight away
    public static final int MIN_STRENGTH = 10;
    public static final int MAX_STRENGTH = 50;

    // A spell tree reaching this level unlocks the Beam shape for whoever cast it (for every element)
    public static final int BEAM_LEVEL = 50;

    // A broken main pylon keeps this share of its levels (for the same element)
    public static final float KEPT_LEVELS = 0.1F;

    private PylonConfig() {}

    // Fusions join the pool once the placer has two elements' spells at this level (SpellMastery)
    public static final int FUSION_LEVEL = 30;
    // At each of these spell levels a Fragment slot joins the pool (one fragment per milestone, each after its own
    // spell break)
    public static final int[] FRAGMENT_LEVELS = {25, 50, 75, 100};

    public static int fragmentSlots(int level)
    {
        int slots = 0;
        for (int milestone : FRAGMENT_LEVELS) if (level >= milestone) slots++;
        return slots;
    }
}
