package com.mowmaster.dust.Features.Spells.pylons;

// The numbers behind the spell pylons, in one place to tune (the deck's own are in SpellDeck)
public final class PylonConfig
{
    // Spell breaks: a spell break card goes in the deck at this level, and another every BREAK_EVERY levels after
    public static final int BREAK_MIN_LEVEL = 30;
    public static final int BREAK_EVERY = 20;

    // A Legendary pylon: instead of a Blessed one this often (twice as often once no blessed modifier is left), with
    // this many upgrade nodes; its modifier counts double
    public static final float LEGENDARY_CHANCE = 0.05F;
    public static final int LEGENDARY_MIN_NODES = 5;
    public static final int LEGENDARY_MAX_NODES = 10;

    // A Neutral pylon takes a blessed card in the deck this often (otherwise it draws from the rest)
    public static final float BLESSED_DRAW_CHANCE = 1.0F;
    // Levelling a pylon: a step of blessing at this node (if it has more), and at its last node, besides the one for
    // locking it
    public static final int BLESSED_NODE = 4;

    // The most upgrade nodes any pylon has (the models go up to level_10)
    public static final int MAX_NODES = 10;

    // A broken main pylon keeps this share of its levels (for the same element)
    public static final float KEPT_LEVELS = 0.1F;

    // Fusions can be drawn once the placer has two elements' spells at FUSION_LEVEL (SpellMastery), on a spell of at
    // least FUSION_TREE_LEVEL, once per branch
    public static final int FUSION_LEVEL = 30;
    public static final int FUSION_TREE_LEVEL = 50;

    // Reduced Requirements: every modifier unlocks this many levels sooner
    public static final int REDUCED_REQUIREMENT = 10;

    // At each of these spell levels a Fragment slot can be drawn (one fragment per milestone, each after its own
    // spell break); Fragment Progress brings them sooner
    public static final int[] FRAGMENT_LEVELS = {25, 50, 75, 100};

    public static int fragmentSlots(int level)
    {
        int slots = 0;
        for (int milestone : FRAGMENT_LEVELS) if (level >= milestone) slots++;
        return slots;
    }

    private PylonConfig() {}
}
