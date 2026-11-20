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
    // The blackstone plinth every apparatus stands on
    private static final VoxelShape BASE = Block.box(0, 0, 0, 16, 3, 16);

    public static final VoxelShape CRUSHING_MILL = Shapes.or(BASE, Block.box(1, 3, 1, 15, 13, 15), Block.box(3, 12, 3, 13, 15, 13));
    public static final VoxelShape SEPARATOR = Shapes.or(BASE, Block.box(1, 3, 1, 15, 13, 15), Block.box(3, 13, 3, 13, 16, 13));
    public static final VoxelShape KETTLE = Shapes.or(BASE, Block.box(2, 3, 2, 14, 13, 14), Block.box(4.5, 13, 4.5, 11.5, 16, 11.5));
    public static final VoxelShape TRANSMUTER = Shapes.or(BASE, Block.box(3, 3, 3, 13, 16, 13));
    public static final VoxelShape CRUCIBLE = Shapes.or(BASE, Block.box(2.5, 3, 2.5, 13.5, 13.5, 13.5));
    // The retort's belly with its side outlets, and its neck
    public static final VoxelShape RETORT = Shapes.or(BASE, Block.box(1, 3, 3, 15, 12, 13), Block.box(6, 12, 6, 10, 15.5, 10));
    // The vat, and its stirrer's gantry across it (west to east)
    public static final VoxelShape MIXING_VAT = Shapes.or(BASE, Block.box(1.75, 3, 1.75, 14.25, 12, 14.25), Block.box(0.5, 3, 7, 15.5, 16, 9));

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
