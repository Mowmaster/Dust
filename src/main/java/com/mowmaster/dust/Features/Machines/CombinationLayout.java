package com.mowmaster.dust.Features.Machines;

import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.world.phys.Vec3;

// Where things are on the Combination Table model (models/block/station/research_combination.json), in pixels with
// the model facing north (the output towards the player, at the south). If the model changes, change these.
public final class CombinationLayout
{
    // The two white input pads either side of the scroll (left and right as the player sees them), the red output pad
    public static final Vec3 LEFT_INPUT = new Vec3(3, 15.6, 8);
    public static final Vec3 RIGHT_INPUT = new Vec3(13, 15.6, 8);
    public static final Vec3 OUTPUT = new Vec3(8, 15.6, 13);
    // The scroll's glyph: its middle and size, and how high it lifts while combining
    public static final Vec3 ARRAY = new Vec3(7.94, 15.26, 8);
    public static final double GLYPH_WIDTH = 6.15;
    public static final double GLYPH_DEPTH = 5.74;
    public static final double ARRAY_LIFT = 4;

    // Click areas (StationInteractions)
    public static final CBPixelBox LEFT_AREA = new CBPixelBox(1, 15, 6, 4.5, 17, 10);
    public static final CBPixelBox RIGHT_AREA = new CBPixelBox(11.5, 15, 6, 15, 17, 10);
    public static final CBPixelBox OUTPUT_AREA = new CBPixelBox(6, 15, 11, 10, 17, 15);

    private CombinationLayout() {}
}
