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

    // ---- the run's animation (shared by the block entity's particles and the renderer) ----

    // How far the glyph is up, 0-1 (progress: ticks into the run, -1 idle): up over its first 20 ticks, back down over
    // the last 10
    public static float lift(float progress)
    {
        if (progress < 0) return 0;
        float up = Math.clamp(progress / 20F, 0F, 1F);
        float down = Math.clamp((ResearchCombinationBlockEntity.COMBINE_TICKS - progress) / 10F, 0F, 1F);
        return Math.min(up, down);
    }

    // How far the inputs have been pulled into the glyph, 0-1: from PULL_START to DRAW_IN_TICKS, speeding up as they go
    // (t * t: slow at first, fast at the end, like being sucked in)
    public static float pull(float progress)
    {
        float t = Math.clamp((progress - ResearchCombinationBlockEntity.PULL_START)
                / (ResearchCombinationBlockEntity.DRAW_IN_TICKS - ResearchCombinationBlockEntity.PULL_START), 0F, 1F);
        return t * t;
    }

    // The middle of the spinning glyph, a little above it (where the inputs are pulled to and the result comes from)
    public static Vec3 glyphCentre(float lift)
    {
        return ARRAY.add(0, ARRAY_LIFT * lift + 0.8, 0);
    }

    // Where an input is while it's pulled in (t: pull 0-1): from its pad to the glyph's middle, arcing up on the way
    public static Vec3 pulled(Vec3 pad, float t, float lift)
    {
        return pad.lerp(glyphCentre(lift), t).add(0, Math.sin(Math.PI * t) * 2.0, 0);
    }

    // Click areas (StationInteractions)
    public static final CBPixelBox LEFT_AREA = new CBPixelBox(1, 15, 6, 4.5, 17, 10);
    public static final CBPixelBox RIGHT_AREA = new CBPixelBox(11.5, 15, 6, 15, 17, 10);
    public static final CBPixelBox OUTPUT_AREA = new CBPixelBox(6, 15, 11, 10, 17, 15);

    private CombinationLayout() {}
}
