package com.mowmaster.dust.Features.Machines;

import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.world.phys.Vec3;

// Where things are on the Evaporation Table model (models/block/station/research_evaporation*.json), in pixels with the
// model facing north (the output towards the player, at the south). If the model changes, change these to match.
public final class EvaporationLayout
{
    // The blue pad at the back (paper) and the red pad at the front (the output)
    public static final Vec3 PAPER_PAD = new Vec3(8, 15.6, 3.35);
    public static final Vec3 OUTPUT = new Vec3(8, 15.6, 12.5);
    // The pot: the middle of its opening, its inside bottom, and its liquid's surface when half full
    public static final Vec3 POT = new Vec3(7.9, 16.6, 8.05);
    public static final double POT_INSIDE = 3.0;
    public static final double LIQUID_HALF = 18.35;
    // The glyph: spinning just over the pot's rim, then lowered into it
    public static final Vec3 GLYPH_TOP = new Vec3(7.9, 21.2, 8.05);
    public static final double GLYPH_SIZE = 4.5;

    // Click areas (StationInteractions)
    public static final CBPixelBox PAPER_AREA = new CBPixelBox(6, 15, 1, 10, 17, 5);
    // Only the top of the pot (its opening): clicking its sides does nothing special
    public static final CBPixelBox POT_AREA = new CBPixelBox(5.5, 20.2, 6, 10.5, 21, 10);
    public static final CBPixelBox OUTPUT_AREA = new CBPixelBox(6, 15, 10.5, 10, 17, 14.5);

    private EvaporationLayout() {}
}
