package com.mowmaster.dust.Features.Machines;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

// Where things are on the Research Crusher model (models/block/station/research_crusher*.json), in pixels with the
// model facing north (its output tray towards north). If the model changes, change these to match.
public final class CrusherLayout
{
    // The block being crushed: from the top of the funnel down to where the two wheels meet
    public static final Vec3 FUNNEL_TOP = new Vec3(8, 15, 12);
    public static final Vec3 WHEELS_NIP = new Vec3(8, 7.5, 11.4);
    // The two grinding wheels' axles (they turn about the x axis; the second is tilted 45 degrees in the model)
    public static final Vec3 WHEEL_A_AXLE = new Vec3(8, 6, 10);
    public static final Vec3 WHEEL_B_AXLE = new Vec3(8, 6.4858, 12.75);
    // Where the pebbles sit in the output tray
    public static final Vec3 TRAY = new Vec3(8, 1, 3);

    private CrusherLayout() {}

    // A point from the model (pixels, facing north) turned to the block's facing, in block units (0-1)
    public static Vec3 turned(Vec3 pixels, Direction facing)
    {
        double x = pixels.x, z = pixels.z;
        // Each quarter turn clockwise (seen from above) takes (x, z) to (16 - z, x), the same turn the blockstate gives
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

    // The block on its way down, progress 0-1
    public static Vec3 descent(float progress)
    {
        return FUNNEL_TOP.lerp(WHEELS_NIP, progress);
    }
}
