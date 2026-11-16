package com.mowmaster.dust.Features.Magic;

// The numbers behind the player's mana and dust store, in one place to tune
public final class MagicConfig
{
    // ---- mana ----
    // Max mana at Magic Level 0; each level adds more (PlayerMagicHelper.maxManaFor: +1 a level for levels 1-9, +2 for
    // 10-19, +3 for 20-29...)
    public static final int START_MAX_MANA = 10;
    // Regeneration: REGEN_PERCENT of max mana back every REGEN_TICKS (10 max: 1 mana a minute; 21 max: about every 29s)
    public static final int REGEN_TICKS = 120;
    public static final int REGEN_PERCENT = 1;
    // Damage per mana point a spell needs that the player doesn't have (1 = half a heart)
    public static final float DAMAGE_PER_MISSING_MANA = 1.0F;
    // A spell's mana cost: 1 per this much dust it uses, rounded up (10 dust: 1 mana, 11 dust: 2)
    public static final int DUST_PER_MANA = 10;

    // ---- dust store ----
    // How many different elements the player can carry at once, and how much of each
    public static final int START_DUST_TYPES = 3;
    public static final int START_DUST_CAPACITY = 64;

    // ---- attunement: [gain bonus, cost reduction, spell power bonus] by how many elements the player is attuned to ----
    public static final float[][] ATTUNEMENT = {
            {0F, 0F, 0F},          // none
            {0.5F, 0.5F, 0.25F},   // one: 50% more dust from orbs, spells cost 50% less, 25% more powerful
            {0.2F, 0.2F, 0.1F},    // two: 20%, 20%, 10%
            {0.1F, 0.1F, 0F},      // three: 10%, 10%, no power bonus
    };

    private MagicConfig() {}
}
