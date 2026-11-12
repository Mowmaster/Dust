package com.mowmaster.dust.Features.Machines;

import net.minecraft.world.phys.Vec3;

// Where things are on the Alchemical Separation Table model (models/block/station/research_separation*.json), in
// pixels with the model facing north (the outputs towards the player, at the south). Turned like the crusher's
// (CrusherLayout.turned). If the model changes, change these to match.
public final class SeparationLayout
{
    // The blue pad at the back where the item goes, and the red output pads at the front (left and right as seen by
    // the player standing in front)
    public static final Vec3 INPUT = new Vec3(8, 15.25, 3);
    public static final Vec3 LEFT_OUTPUT = new Vec3(6.5, 15.25, 12.5);
    public static final Vec3 RIGHT_OUTPUT = new Vec3(9.5, 15.25, 12.5);
    // The middle of the scroll's magical array, which lifts and spins while separating
    public static final Vec3 ARRAY = new Vec3(7.94, 15.14, 8);
    // How high the array lifts (pixels), and where the paper the table holds is shown
    public static final double ARRAY_LIFT = 4;
    public static final Vec3 PAPER = new Vec3(12.5, 15.1, 3);

    private SeparationLayout() {}
}
