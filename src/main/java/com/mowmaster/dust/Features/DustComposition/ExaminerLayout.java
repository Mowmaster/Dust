package com.mowmaster.dust.Features.DustComposition;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

// Where things are on the Element Tester's models (models/block/station/element_tester_<count>.json), for 1 to 5
// crystals, in pixels with the model facing north: the plate the item sits on, each crystal, and how many gauge lines
// run from the plate to each crystal. Read off the models; if a model changes, change these to match.
//
// The models' tint indices (used by Client/ElementTesterTint):
//   0-4                    the crystals, in the order they were put in (their element's colour)
//   5 + crystal * 10 + k   gauge line k of that crystal, k = 0 next to the plate
public record ExaminerLayout(Vec3 plate, Vec3[] crystals, int lines)
{
    public static final int MAX_CRYSTALS = 5;
    public static final int MAX_LINES = 10;

    private static final ExaminerLayout[] LAYOUTS = {
            null,
            new ExaminerLayout(new Vec3(8, 11.25, 11.85), new Vec3[] {new Vec3(8, 13, 3.5)}, 10),
            new ExaminerLayout(new Vec3(8.5, 11.25, 10.6), new Vec3[] {new Vec3(13, 13, 2.95), new Vec3(3, 13, 3)}, 10),
            new ExaminerLayout(new Vec3(8, 11.25, 8.6), new Vec3[] {new Vec3(8, 13, 2.5), new Vec3(3.5, 13, 12.8), new Vec3(13.25, 13, 12.25)}, 5),
            new ExaminerLayout(new Vec3(8, 11.25, 8.1), new Vec3[] {new Vec3(2.75, 13, 4), new Vec3(12.5, 13, 3.45),
                    new Vec3(3.5, 13, 12.55), new Vec3(13.25, 13, 12)}, 5),
            new ExaminerLayout(new Vec3(8, 11.25, 8.6), new Vec3[] {new Vec3(8, 13, 2.5), new Vec3(2.75, 13, 5.75),
                    new Vec3(13.5, 13, 6), new Vec3(3.5, 13, 13.05), new Vec3(13.25, 13, 12.5)}, 5),
    };

    // The layout for this many crystals (null for none: the plain workstation)
    public static ExaminerLayout of(int crystals)
    {
        return crystals < 1 || crystals > MAX_CRYSTALS ? null : LAYOUTS[crystals];
    }

    // How many of a crystal's gauge lines are lit for this percent: none for 0%, then one per 10% (or 20% with 5
    // lines), so 0-9% lights the first line and 90-99% the tenth. 100% lights them all (and the crystal sparkles).
    public int litLines(int percent)
    {
        if (percent <= 0) return 0;
        return Math.min(lines, percent * lines / 100 + 1);
    }

    // A point from the model (pixels, facing north) turned to the block's facing, in block units (0-1)
    public static Vec3 turned(Vec3 pixels, Direction facing)
    {
        double x = pixels.x, z = pixels.z;
        // Each quarter turn clockwise (seen from above) takes (x, z) to (16 - z, x), the same turn the blockstate gives
        // the model for east, south and west
        int turns = switch (facing) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
        for (int i = 0; i < turns; i++) {
            double oldX = x;
            x = 16 - z;
            z = oldX;
        }
        return new Vec3(x / 16, pixels.y / 16, z / 16);
    }
}
