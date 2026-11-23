package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustStorage.DustClouds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

// Dust Chutes fixed to a block's sides letting its dust out (a Still Pot's, a Dust Centrifuge's): each chute facing away
// from the block (its back against it) whose filter lets the element through pours it out as Dust Piles under it, no
// higher than the chute
public final class DustChutes
{
    private DustChutes() {}

    // Pours this element out of source's chutes, up to perChute through each, available in all; returns how much landed
    public static int pour(ServerLevel level, BlockPos source, DustElement element, int perChute, int available)
    {
        int left = available;
        for (Direction side : Direction.Plane.HORIZONTAL) {
            if (left <= 0) break;
            BlockPos at = source.relative(side);
            DustChuteBlockEntity chute = chute(level, source, side);
            if (chute == null || (chute.filter() != null && !chute.filter().equals(element))) continue;
            BlockPos under = at.below();
            int landed = DustClouds.deposit(level, under, element, Math.min(perChute, left), under.getY());
            if (landed <= 0) continue;
            left -= landed;
            level.sendParticles(new DustParticleOptions(element.color(), 1.0F), under.getX() + 0.5, under.getY() + 0.9, under.getZ() + 0.5, 4, 0.12, 0.2, 0.12, 0);
        }
        return available - left;
    }

    // Whether any chute on source would let this element out
    public static boolean anyFor(ServerLevel level, BlockPos source, DustElement element)
    {
        for (Direction side : Direction.Plane.HORIZONTAL) {
            DustChuteBlockEntity chute = chute(level, source, side);
            if (chute != null && (chute.filter() == null || chute.filter().equals(element))) return true;
        }
        return false;
    }

    // The chute fixed to source on this side (facing away from it), if there is one
    public static @Nullable DustChuteBlockEntity chute(ServerLevel level, BlockPos source, Direction side)
    {
        BlockPos at = source.relative(side);
        BlockState state = level.getBlockState(at);
        if (!(state.getBlock() instanceof DustChuteBlock) || state.getValue(DustChuteBlock.FACING) != side) return null;
        return level.getBlockEntity(at) instanceof DustChuteBlockEntity chute ? chute : null;
    }
}
