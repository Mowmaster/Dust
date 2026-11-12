package com.mowmaster.dust.Features.Machines;

import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.world.phys.Vec3;

// Where things are on the Transformation Table model (models/block/station/research_transformational.json), in pixels
// with the model facing north (the output towards the player, at the south). If the model changes, change these.
public final class TransformationLayout
{
    // The blue pad at the back (an inert crystal / nether quartz), the scroll the item sits on, the white output pad
    public static final Vec3 CRYSTAL_PAD = new Vec3(8, 15.6, 3);
    public static final Vec3 SCROLL = new Vec3(7.94, 15.6, 8.35);
    public static final Vec3 OUTPUT = new Vec3(8, 15.6, 13);
    // The scroll's glyph: its middle and size, and how high it lifts while transforming
    public static final Vec3 ARRAY = new Vec3(7.94, 15.26, 8);
    public static final double GLYPH_WIDTH = 6.15;
    public static final double GLYPH_DEPTH = 5.74;
    public static final double ARRAY_LIFT = 4;

    // Click areas (StationInteractions)
    public static final CBPixelBox CRYSTAL_AREA = new CBPixelBox(6, 15, 1, 10, 17, 5);
    public static final CBPixelBox SCROLL_AREA = new CBPixelBox(4.5, 15, 5, 11.5, 17, 11);
    public static final CBPixelBox OUTPUT_AREA = new CBPixelBox(6, 15, 11, 10, 17, 15);

    private TransformationLayout() {}
}
