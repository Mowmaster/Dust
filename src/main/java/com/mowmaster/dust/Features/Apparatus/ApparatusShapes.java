package com.mowmaster.dust.Features.Apparatus;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

// Each apparatus's outline (and collision): its model (models/block/apparatus/*.json) in a few boxes, facing north.
// ApparatusBlock turns it to the block's facing (turned), as the blockstate turns the model.
public final class ApparatusShapes
{
    private ApparatusShapes() {}

    // The shape for each facing: turned clockwise (seen from above) a quarter turn for east, two for south, three for
    // west, like the blockstate turns the model. Each box's corners are turned about the block's middle: a point at
    // (x, z) goes to (1 - z, x) each quarter turn.
    public static Map<Direction, VoxelShape> turned(VoxelShape north)
    {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int turns = switch (facing) {
                case EAST -> 1;
                case SOUTH -> 2;
                case WEST -> 3;
                default -> 0;
            };
            VoxelShape shape = Shapes.empty();
            for (AABB box : north.toAabbs()) {
                double x0 = box.minX, z0 = box.minZ, x1 = box.maxX, z1 = box.maxZ;
                for (int i = 0; i < turns; i++) {
                    double nx0 = 1 - z1, nx1 = 1 - z0;
                    z0 = x0;
                    z1 = x1;
                    x0 = nx0;
                    x1 = nx1;
                }
                shape = Shapes.or(shape, Shapes.create(x0, box.minY, z0, x1, box.maxY, z1));
            }
            shapes.put(facing, shape.optimize());
        }
        return shapes;
    }
}
