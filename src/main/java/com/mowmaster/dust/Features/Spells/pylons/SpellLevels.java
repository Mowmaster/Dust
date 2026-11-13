package com.mowmaster.dust.Features.Spells.pylons;

// A spell's level from its experience, on the same curve as a player's: levels 0-15 take 2L+7 each, 16-30 take
// 5L-38, beyond that 9L-158 (vanilla's Player.getXpNeededForNextLevel)
public final class SpellLevels
{
    private SpellLevels() {}

    public static int xpForNextLevel(int level)
    {
        if (level >= 30) return 112 + (level - 30) * 9;
        if (level >= 15) return 37 + (level - 15) * 5;
        return 7 + level * 2;
    }

    public static int levelForXp(int xp)
    {
        int level = 0;
        while (xp >= xpForNextLevel(level)) {
            xp -= xpForNextLevel(level);
            level++;
        }
        return level;
    }

    // Total experience to reach this level from 0
    public static int xpForLevel(int level)
    {
        int total = 0;
        for (int l = 0; l < level; l++) total += xpForNextLevel(l);
        return total;
    }

    // How far into the current level, 0-1
    public static float progress(int xp)
    {
        int level = levelForXp(xp);
        return (xp - xpForLevel(level)) / (float) xpForNextLevel(level);
    }
}
