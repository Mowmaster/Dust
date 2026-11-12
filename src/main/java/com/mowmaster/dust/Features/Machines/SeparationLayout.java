package com.mowmaster.dust.Features.Machines;

import net.minecraft.world.phys.Vec3;

// Where things are on the Alchemical Separation Table model (models/block/station/research_separation*.json), in
// pixels with the model facing north (the outputs towards the player, at the south). Turned like the crusher's
// (CrusherLayout.turned). If the model changes, change these to match.
public final class SeparationLayout
{
    // The blue pad at the back where the paper goes, the scroll the item sits on, and the red output pads at the front
    // (left and right as seen by the player standing in front)
    public static final Vec3 PAPER_PAD = new Vec3(8, 15.6, 3);
    public static final Vec3 SCROLL = new Vec3(7.94, 15.6, 8);
    public static final Vec3 LEFT_OUTPUT = new Vec3(6.5, 15.3, 12.5);
    public static final Vec3 RIGHT_OUTPUT = new Vec3(9.5, 15.3, 12.5);
    // The scroll's glyph: its middle and size (the scroll's top face, pixels), and how high it lifts while separating
    public static final Vec3 ARRAY = new Vec3(7.94, 15.26, 8);
    public static final double GLYPH_WIDTH = 6.15;
    public static final double GLYPH_DEPTH = 5.74;
    public static final double ARRAY_LIFT = 4;

    private SeparationLayout() {}
}
