package com.mowmaster.dust.Features.Spells.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

// Short-lived marks a spell leaves on blocks: "this block breaks faster", "this block gives more experience"... Kept in
// memory only (they last a spell's duration, seconds), by dimension and position, until a game time. Read by the
// event hooks in SpellMobEffects.
public final class BlockMarks
{
    public static final String QUICK_BREAK = "quick_break";
    public static final String WISE = "wise";
    public static final String LOWER_TIER = "lower_tier";

    private record Key(ResourceKey<Level> dimension, BlockPos pos, String mark) {}

    private static final Map<Key, Long> MARKS = new HashMap<>();

    private BlockMarks() {}

    public static void mark(Level level, BlockPos pos, String mark, int ticks)
    {
        // Tidy away the expired ones now and then, so the map doesn't grow forever
        if (MARKS.size() > 256) MARKS.values().removeIf(until -> until < level.getGameTime());
        MARKS.put(new Key(level.dimension(), pos.immutable(), mark), level.getGameTime() + ticks);
    }

    public static boolean has(Level level, BlockPos pos, String mark)
    {
        Long until = MARKS.get(new Key(level.dimension(), pos, mark));
        return until != null && until >= level.getGameTime();
    }
}
