package com.mowmaster.dust.Features.Machines;

import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.world.phys.Vec3;

// Where things are on the Element Replicator model (models/block/station/element_replicator.json), in pixels with the
// model facing north, and the run's timing (shared by the block entity and its renderer). If the model changes, change
// these.
//   the bottom sigil (scroll_copy) on the base slab: the item to copy stands on it
//   the top sigil (scroll_paste) on the top slab, inside the rails: the copy appears on it
public final class ReplicatorLayout
{
    // The sigils: their middles (just above their faces) and size
    public static final Vec3 BOTTOM_SIGIL = new Vec3(8, 4.3, 8);
    public static final Vec3 TOP_SIGIL = new Vec3(8, 15.3, 8);
    public static final double SIGIL_SIZE = 8;
    // How high the bottom sigil's glyph rises while scanning: to just under the top slab
    public static final double SCAN_HEIGHT = 7.3;
    // The items stand on the sigils, sized to ITEM_HEIGHT pixels tall, or less if that would make them wider than
    // ITEM_WIDTH (so a block and a flat item come out the same height, and either clears the posts as it turns)
    public static final double ITEM_HEIGHT = 6;
    public static final double ITEM_WIDTH = 7;

    // ---- the run (ticks) ----
    //   scan    the bottom glyph spins and rises up through the item
    //   draw    dust streams in from the jars to the top sigil while the bottom glyph sinks back
    //   print   the top glyph spins (PRINT_SPIN_UP ticks), then rises, the copy appearing beneath it as if printed
    public static final int SCAN_TICKS = 40;
    public static final int DRAW_TICKS = 50;
    public static final int PRINT_TICKS = 60;
    public static final int PRINT_SPIN_UP = 10;
    public static final int DRAW_START = SCAN_TICKS;
    public static final int PRINT_START = SCAN_TICKS + DRAW_TICKS;
    public static final int RUN_TICKS = PRINT_START + PRINT_TICKS;
    // Each jar's stream (DustStream): STREAM_TRAVEL ticks to reach the top sigil, then flowing on for STREAM_HOLD, then
    // its tail running in after it, so the last of the dust arrives just as the bottom glyph is back down
    public static final int STREAM_TRAVEL = 15;
    public static final int STREAM_HOLD = DRAW_TICKS - 2 * STREAM_TRAVEL;

    // How far up the bottom glyph is, 0-1 (progress: ticks into the run): up over the scan, down over the draw (eased:
    // smoothstep, slow at either end)
    public static float scanLift(float progress)
    {
        if (progress < 0 || progress >= PRINT_START) return 0;
        float t = progress < DRAW_START ? progress / SCAN_TICKS : 1 - (progress - DRAW_START) / DRAW_TICKS;
        return smooth(t);
    }

    // How much of the copy is printed, 0-1 (and how far up the top glyph is): nothing while it spins up, then all of it
    // by the end of the run
    public static float printed(float progress)
    {
        float start = PRINT_START + PRINT_SPIN_UP;
        if (progress < start) return 0;
        return smooth(Math.min(1, (progress - start) / (RUN_TICKS - start)));
    }

    private static float smooth(float t)
    {
        t = Math.clamp(t, 0F, 1F);
        return t * t * (3 - 2 * t);
    }

    // Click areas (StationInteractions): the item to copy, on the bottom sigil between the posts (the block's shape has
    // a box there, so clicks into the gap land on it); the copy, on top
    public static final CBPixelBox TEMPLATE_AREA = new CBPixelBox(3.5, 3.9, 3.5, 12.5, 12.1, 12.5);
    public static final CBPixelBox OUTPUT_AREA = new CBPixelBox(2, 14.9, 2, 14, 17, 14);

    private ReplicatorLayout() {}
}
