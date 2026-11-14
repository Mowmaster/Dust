package com.mowmaster.dust.Features.Crafting;

import com.mowmaster.mowlib.util.CBPixelBox;
import net.minecraft.world.phys.Vec3;

// Where things are on the Element Crafter's model (block/station/element_crafter.json), in pixels, model facing north:
//   the 3x3 grid on top    cells between the dividers: x and z in [2,5], [6.5,9.5], [11,14]; the floor is at y 15
//   the four jars          one on each side (north, east, south, west), glass from y 4 to 10, lid above
// Grid cell i is row i / 3 (from the north edge) and column i % 3 (from the west edge), as a crafting grid reads.
public final class ElementCrafterLayout
{
    public static final int CELLS = 9;
    public static final int JARS = 4;
    public static final int JAR_CAPACITY = 1000;

    private static final double[] CELL_LO = {2, 6.5, 11};
    private static final double[] CELL_HI = {5, 9.5, 14};
    // Grid items lie just above the cell floor
    private static final double ITEM_Y = 15.05;

    // Jars 0-3: north, east, south, west (the lids' model tint indices are 2 + jar)
    private static final CBPixelBox[] JAR_BOXES = {
            new CBPixelBox(5.5, 4, -0.5, 10.5, 11.5, 4.5),
            new CBPixelBox(11.5, 4, 5.5, 16.5, 11.5, 10.5),
            new CBPixelBox(5.5, 4, 11.5, 10.5, 11.5, 16.5),
            new CBPixelBox(-0.5, 4, 5.5, 4.5, 11.5, 10.5)
    };

    // Inside each jar (inside the glass, between the jar's bottom and its lid), as {x0, z0, x1, z1}: the dust fills
    // this from FILL_BOTTOM up to FILL_TOP when the jar is full
    private static final double[][] JAR_INSIDES = {
            {6, 0, 10, 4},
            {12, 6, 16, 10},
            {6, 12, 10, 16},
            {0, 6, 4, 10}
    };
    public static final double FILL_BOTTOM = 4.5;
    public static final double FILL_TOP = 9.5;

    private ElementCrafterLayout() {}

    public static double[] jarInside(int jar)
    {
        return JAR_INSIDES[jar];
    }

    // How high the dust stands in a jar holding this much (a thin layer at least, so a few dust still show)
    public static double fillHeight(int amount)
    {
        if (amount <= 0) return FILL_BOTTOM;
        double share = Math.min(1.0, amount / (double) JAR_CAPACITY);
        return FILL_BOTTOM + Math.max(0.15, (FILL_TOP - FILL_BOTTOM) * share);
    }

    // The click area of grid cell i (a little above the floor too, so clicking an item lying there counts)
    public static CBPixelBox cell(int i)
    {
        int row = i / 3, column = i % 3;
        return new CBPixelBox(CELL_LO[column], 14.9, CELL_LO[row], CELL_HI[column], 16.5, CELL_HI[row]);
    }

    // Where grid cell i's item lies
    public static Vec3 cellCentre(int i)
    {
        int row = i / 3, column = i % 3;
        return new Vec3((CELL_LO[column] + CELL_HI[column]) / 2, ITEM_Y, (CELL_LO[row] + CELL_HI[row]) / 2);
    }

    public static CBPixelBox jar(int jar)
    {
        return JAR_BOXES[jar];
    }
}
