package com.mowmaster.dust.Features.Machines;

import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.world.phys.Vec3;

// Where things are on the Mixing Table model (models/block/station/research_mixing.json), in pixels with the model
// facing north (the output towards the player, at the south). The pot, spoon and glyph are where the Evaporation
// Table's are (EvaporationLayout), so those are used for them. If the model changes, change these.
public final class MixingLayout
{
    // The two white input pads either side of the pot (left and right as the player sees them), the red output pad
    public static final Vec3 LEFT_INPUT = new Vec3(3, 15.6, 8);
    public static final Vec3 RIGHT_INPUT = new Vec3(13, 15.6, 8);
    public static final Vec3 OUTPUT = new Vec3(8, 15.6, 12.5);

    // Click areas (StationInteractions)
    public static final CBPixelBox LEFT_AREA = new CBPixelBox(1, 15, 6, 5, 17, 10);
    public static final CBPixelBox RIGHT_AREA = new CBPixelBox(11, 15, 6, 15, 17, 10);
    public static final CBPixelBox OUTPUT_AREA = new CBPixelBox(6, 15, 10.5, 10, 17, 14.5);

    private MixingLayout() {}
}
