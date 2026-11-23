package com.mowmaster.dust.Features.DustStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

// How far in from its block's edge a block's body is on a side (pixels), so things fixed to that side meet it: a Glass
// Dust Tube's arm reaches that far in (TubeJoin.reaching: flush, 2 or 3 pixels), a Dust Chute sits that far back
// (DustChuteBlock.INSET).
//   hard-coded   a block that takes tubes says (DustTubeConnectable.attachInset), measured off its model; for a block
//                that turns, as for its model facing north (Insets, turned with turned)
//   from shape   otherwise worked out from the block's shape: the nearest of its boxes to that side, among the ones
//                where an attachment meets it (the middle, pixels 5 to 11 across: fromShape)
public final class AttachInsets
{
    // Where an attachment meets a side (pixels across the face, both ways)
    private static final double BAND_LOW = 5 / 16.0, BAND_HIGH = 11 / 16.0;

    private AttachInsets() {}

    // A block's inset on this side (side: its side facing the attachment)
    public static double of(BlockState state, Direction side)
    {
        if (state.getBlock() instanceof DustTubeConnectable connectable) return connectable.attachInset(state, side);
        return fromShape(state, side);
    }

    // Worked out from the block's shape (0: flush, or no shape there)
    public static double fromShape(BlockState state, Direction side)
    {
        VoxelShape shape = state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
        if (shape.isEmpty()) return 0;
        double best = Double.MAX_VALUE;
        for (AABB box : shape.toAabbs()) {
            if (!meets(box, side)) continue;
            best = Math.min(best, distance(box, side));
        }
        if (best == Double.MAX_VALUE) {
            // Nothing in the middle of that side: the shape's nearest box at all
            for (AABB box : shape.toAabbs()) best = Math.min(best, distance(box, side));
        }
        return Math.max(0, best * 16);
    }

    // Whether a box covers the middle of the side (where an attachment meets it)
    private static boolean meets(AABB box, Direction side)
    {
        return switch (side.getAxis()) {
            case X -> overlaps(box.minY, box.maxY) && overlaps(box.minZ, box.maxZ);
            case Y -> overlaps(box.minX, box.maxX) && overlaps(box.minZ, box.maxZ);
            case Z -> overlaps(box.minX, box.maxX) && overlaps(box.minY, box.maxY);
        };
    }

    private static boolean overlaps(double min, double max)
    {
        return max > BAND_LOW && min < BAND_HIGH;
    }

    // How far a box sits in from the block's edge on that side (blocks)
    private static double distance(AABB box, Direction side)
    {
        return switch (side) {
            case NORTH -> box.minZ;
            case SOUTH -> 1 - box.maxZ;
            case WEST -> box.minX;
            case EAST -> 1 - box.maxX;
            case DOWN -> box.minY;
            case UP -> 1 - box.maxY;
        };
    }

    // A block's insets on its four sides, as for its model facing north (the bottom and top from its shape)
    public record Insets(double north, double east, double south, double west)
    {
        public static Insets all(double inset)
        {
            return new Insets(inset, inset, inset, inset);
        }

        // The inset on a world side of a block turned to face this way (the model faces north)
        public double turned(Direction facing, Direction side)
        {
            if (!side.getAxis().isHorizontal()) return 0;
            // Turn the world side back into the model's frame: as many quarter turns anticlockwise as the block was
            // turned clockwise from north
            Direction model = side;
            int turns = switch (facing) {
                case EAST -> 1;
                case SOUTH -> 2;
                case WEST -> 3;
                default -> 0;
            };
            for (int i = 0; i < turns; i++) model = model.getCounterClockWise();
            return switch (model) {
                case NORTH -> north;
                case EAST -> east;
                case SOUTH -> south;
                default -> west;
            };
        }
    }
}
