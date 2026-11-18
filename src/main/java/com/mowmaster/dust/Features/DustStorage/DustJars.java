package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// The Dust Jars round a machine that draws on them (the lamps, the Element Replicator): found among the block entities
// of the chunks the area covers, only chunks already loaded (getChunkNow: looking never loads one), nearest first.
public final class DustJars
{
    private DustJars() {}

    // The jars within range blocks of centre (a cube), nearest first
    public static List<DustJarBlockEntity> nearby(ServerLevel level, BlockPos centre, int range)
    {
        List<DustJarBlockEntity> jars = new ArrayList<>();
        int minX = (centre.getX() - range) >> 4, maxX = (centre.getX() + range) >> 4;
        int minZ = (centre.getZ() - range) >> 4, maxZ = (centre.getZ() + range) >> 4;
        for (int cx = minX; cx <= maxX; cx++) {
            for (int cz = minZ; cz <= maxZ; cz++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
                if (chunk == null) continue;
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    if (!(blockEntity instanceof DustJarBlockEntity jar)) continue;
                    BlockPos at = jar.getBlockPos();
                    if (Math.abs(at.getX() - centre.getX()) > range || Math.abs(at.getY() - centre.getY()) > range
                            || Math.abs(at.getZ() - centre.getZ()) > range) continue;
                    jars.add(jar);
                }
            }
        }
        jars.sort(java.util.Comparator.comparingDouble(jar -> jar.getBlockPos().distSqr(centre)));
        return jars;
    }

    // How much of each element those jars hold together
    public static Map<DustElement, Integer> totals(List<DustJarBlockEntity> jars)
    {
        Map<DustElement, Integer> totals = new LinkedHashMap<>();
        for (DustJarBlockEntity jar : jars) {
            if (jar.element() != null && jar.amount() > 0) totals.merge(jar.element(), jar.amount(), Integer::sum);
        }
        return totals;
    }

    // Whether those jars hold at least this much of every element
    public static boolean hold(List<DustJarBlockEntity> jars, Map<DustElement, Integer> needed)
    {
        Map<DustElement, Integer> totals = totals(jars);
        for (Map.Entry<DustElement, Integer> need : needed.entrySet()) {
            if (totals.getOrDefault(need.getKey(), 0) < need.getValue()) return false;
        }
        return true;
    }
}
