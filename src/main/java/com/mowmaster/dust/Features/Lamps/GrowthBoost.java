package com.mowmaster.dust.Features.Lamps;

import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.WeakHashMap;

// How many blocks each lit Growth Lamp gets to try this tick. Each lamp picks blocks at random in its area (as
// vanilla's random ticks do) and gives the ones that grow an extra random tick, so more lamps over the same place grow
// it faster. To keep a crowd of lamps from costing too much, a level's lamps share MAX_SAMPLES tries a tick between
// them: up to SAMPLES_PER_LAMP each, fewer each once there are more than MAX_SAMPLES / SAMPLES_PER_LAMP (32) lamps lit.
// Each lamp counts itself in as it ticks; until all have this tick, the count from the tick before stands in.
public final class GrowthBoost
{
    // Tries a lamp makes each tick. Its area holds about 3000 blocks, so each block there gets an extra random tick
    // about every 19 seconds (vanilla gives each block one about every 68 seconds): roughly 4 to 5 times the speed.
    public static final int SAMPLES_PER_LAMP = 8;
    // Tries a level's lamps share each tick
    public static final int MAX_SAMPLES = 256;

    // For each level: {the tick these counts are for, lamps counted so far this tick, lamps counted last tick}.
    // WeakHashMap: an unloaded level's entry goes once nothing else holds the level.
    private static final Map<ServerLevel, long[]> COUNTS = new WeakHashMap<>();

    private GrowthBoost() {}

    // A lamp's tries for this tick (it's counted in by asking)
    public static int share(ServerLevel level)
    {
        long[] counts = COUNTS.computeIfAbsent(level, key -> new long[] {-1, 0, 0});
        long now = level.getGameTime();
        if (counts[0] != now) {
            counts[2] = counts[0] == now - 1 ? counts[1] : 0;
            counts[1] = 0;
            counts[0] = now;
        }
        counts[1]++;
        long lamps = Math.max(counts[1], counts[2]);
        return (int) Math.max(1, Math.min(SAMPLES_PER_LAMP, MAX_SAMPLES / lamps));
    }
}
