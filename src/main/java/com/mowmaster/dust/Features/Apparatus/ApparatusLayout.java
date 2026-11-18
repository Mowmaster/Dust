package com.mowmaster.dust.Features.Apparatus;

import com.mowmaster.mowlib.util.CBPixelBox;

// Where the alchemical apparatus are clicked (StationInteractions), in model pixels with the model facing north (the
// front, which faces the player who placed it). Every apparatus model keeps to this:
//   the intake    the middle of the top: right-click it with what the apparatus works on (or its pattern, for those
//                 that take one); hoppers and pipes feed it from the top (and the sides, for those that use nothing up)
//   the spout     the lower front: left-click it to take what it has handed on (sneak: a stack); hoppers and pipes take
//                 from the bottom
// Clicks anywhere else do their usual thing (a block in hand is placed against it).
public final class ApparatusLayout
{
    public static final CBPixelBox INTAKE = new CBPixelBox(4, 12, 4, 12, 16.5, 12);
    public static final CBPixelBox SPOUT = new CBPixelBox(3, 0, -0.5, 13, 8, 4);
    // Where a pattern sits on the apparatus that take one (the Transmuter, the Conjoining Crucible, the Mixing Vat): on
    // the front, above the spout
    public static final CBPixelBox PATTERN = new CBPixelBox(4, 8, -0.5, 12, 12, 4.5);
    // A slot that's never clicked (automation reaches it; players use another slot standing for it)
    public static final CBPixelBox NOWHERE = new CBPixelBox(-20, -20, -20, -19, -19, -19);

    private ApparatusLayout() {}
}
