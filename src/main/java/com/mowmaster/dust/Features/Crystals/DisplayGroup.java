package com.mowmaster.dust.Features.Crystals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Crystal Displays working together. Displays side by side at the same height make one group: if the group is exactly
// a 2x2 or 3x3 square (no other display touching it) and every one of them holds a crystal, they act as one, under one
// larger crystal (Client/CrystalDisplayRenderer); otherwise each display works on its own.
//   master   the square's north-west corner (the lowest x and z), which runs the group (CrystalDisplayBlockEntity)
//   size     1 (on its own), 2 or 3
//   members  the displays in it, master first
// The area it gives off its effects in: 9 x 9 blocks round the middle of the group, from the displays' own height up 9
// blocks (never below them).
public record DisplayGroup(BlockPos master, int size, List<BlockPos> members)
{
    public static final int RADIUS = 4;
    public static final int HEIGHT = 9;

    public static DisplayGroup single(BlockPos pos)
    {
        return new DisplayGroup(pos.immutable(), 1, List.of(pos.immutable()));
    }

    public static DisplayGroup find(Level level, BlockPos pos)
    {
        Set<BlockPos> seen = new HashSet<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        seen.add(pos.immutable());
        queue.add(pos.immutable());
        while (!queue.isEmpty()) {
            BlockPos at = queue.poll();
            for (Direction side : Direction.Plane.HORIZONTAL) {
                BlockPos next = at.relative(side);
                if (seen.contains(next) || !(level.getBlockState(next).getBlock() instanceof CrystalDisplayBlock)) continue;
                seen.add(next);
                if (seen.size() > 9) return single(pos);
                queue.add(next);
            }
        }
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
        for (BlockPos at : seen) {
            minX = Math.min(minX, at.getX());
            maxX = Math.max(maxX, at.getX());
            minZ = Math.min(minZ, at.getZ());
            maxZ = Math.max(maxZ, at.getZ());
        }
        int width = maxX - minX + 1, depth = maxZ - minZ + 1;
        if (width != depth || (width != 2 && width != 3) || seen.size() != width * depth) return single(pos);
        for (BlockPos at : seen) {
            if (!(level.getBlockEntity(at) instanceof CrystalDisplayBlockEntity display) || display.element() == null) return single(pos);
        }
        BlockPos master = new BlockPos(minX, pos.getY(), minZ);
        List<BlockPos> members = new ArrayList<>(seen);
        members.sort(Comparator.comparingInt((BlockPos at) -> at.equals(master) ? 0 : 1).thenComparingInt(BlockPos::getX).thenComparingInt(BlockPos::getZ));
        return new DisplayGroup(master, width, List.copyOf(members));
    }

    // The middle of the group, at the displays' feet
    public Vec3 centre()
    {
        return new Vec3(master.getX() + size / 2.0, master.getY(), master.getZ() + size / 2.0);
    }

    public AABB area()
    {
        Vec3 centre = centre();
        return new AABB(centre.x - RADIUS - 0.5, centre.y, centre.z - RADIUS - 0.5, centre.x + RADIUS + 0.5, centre.y + HEIGHT, centre.z + RADIUS + 0.5);
    }
}
