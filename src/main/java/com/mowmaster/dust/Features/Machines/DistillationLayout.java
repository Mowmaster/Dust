package com.mowmaster.dust.Features.Machines;

import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.world.phys.Vec3;

// Where things are on the Distillation Table model (models/block/station/research_distalation.json), in pixels with
// the model facing north (the outputs towards the player, at the south). If the model changes, change these to match.
public final class DistillationLayout
{
    // The container pads at the back: glass bottles (left), bowls (middle), paper for the soot (right), as the player
    // sees them
    public static final Vec3 BOTTLE_PAD = new Vec3(5, 15.6, 3.35);
    public static final Vec3 BOWL_PAD = new Vec3(8, 15.6, 3.35);
    public static final Vec3 PAPER_PAD = new Vec3(11, 15.6, 3.35);
    // The outputs, left to right: soot from the solids, the gas / liquid left in the flask, what boiled over into the jar
    public static final Vec3 SOOT_OUTPUT = new Vec3(5, 15.6, 12.5);
    public static final Vec3 FLASK_OUTPUT = new Vec3(8, 15.6, 12.5);
    public static final Vec3 JAR_OUTPUT = new Vec3(11, 15.6, 12.5);
    // The flask: the middle of its inside bottom, its inside size and height; filled a quarter of the way up
    public static final Vec3 FLASK = new Vec3(7.875, 16.25, 8);
    public static final double FLASK_WIDTH = 2.25;
    public static final double FLASK_DEPTH = 2.0;
    public static final double FLASK_HEIGHT = 2.0;
    // The neck from the flask's top to the jar, and the jar's inside bottom and size
    public static final Vec3 NECK_START = new Vec3(9.4, 17.3, 7.97);
    public static final Vec3 NECK_END = new Vec3(12.9, 17.3, 7.97);
    public static final Vec3 JAR = new Vec3(13, 15.25, 8);
    public static final double JAR_SIZE = 2.0;
    public static final double JAR_HEIGHT = 2.75;
    // The scroll's glyph: where it starts (the scroll's top, its size) and where it rises to (just inside the flask)
    public static final Vec3 GLYPH_SCROLL = new Vec3(7.94, 15.26, 8);
    public static final double GLYPH_SCROLL_WIDTH = 6.15;
    public static final double GLYPH_SCROLL_DEPTH = 5.74;
    public static final Vec3 GLYPH_FLASK = new Vec3(7.875, 16.4, 8);
    public static final double GLYPH_FLASK_SIZE = 2.0;

    // Click areas (StationInteractions)
    public static final CBPixelBox BOTTLE_AREA = new CBPixelBox(3.5, 15, 1, 6.5, 17, 5);
    public static final CBPixelBox BOWL_AREA = new CBPixelBox(6.5, 15, 1, 9.5, 17, 5);
    public static final CBPixelBox PAPER_AREA = new CBPixelBox(9.5, 15, 1, 12.5, 17, 5);
    // The top of the flask
    public static final CBPixelBox FLASK_AREA = new CBPixelBox(6, 18.2, 6, 10, 19, 10);
    public static final CBPixelBox SOOT_OUTPUT_AREA = new CBPixelBox(3.5, 15, 11, 6.5, 17, 14.5);
    public static final CBPixelBox FLASK_OUTPUT_AREA = new CBPixelBox(6.5, 15, 11, 9.5, 17, 14.5);
    public static final CBPixelBox JAR_OUTPUT_AREA = new CBPixelBox(9.5, 15, 11, 12.5, 17, 14.5);

    private DistillationLayout() {}
}
