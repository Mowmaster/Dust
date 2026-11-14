package com.mowmaster.dust.Features.Spells.pylons;

import net.minecraft.util.StringRepresentable;

// Which of a pylon's three free sides have the next pylons of the tree on them (the block state property "links"),
// seen from the pylon looking away from its parent: straight on, to its left, to its right. The models (facing with
// their parent to the south) have the matching connection arms:
//   blank_pylon_<suffix>_level_<n>   base: none, 1: left, 2: straight, 3: right, 4: straight + left,
//                                    5: straight + right, 6: left + right, 7: all three
//   shape_pylon<suffix>              (none), _1: straight, _2: left, _3: right (the Shape Pylon only ever has one)
public enum PylonLinks implements StringRepresentable
{
    NONE("none", false, false, false, "base", ""),
    LEFT("left", false, true, false, "1", "_2"),
    STRAIGHT("straight", true, false, false, "2", "_1"),
    RIGHT("right", false, false, true, "3", "_3"),
    STRAIGHT_LEFT("straight_left", true, true, false, "4", "_1"),
    STRAIGHT_RIGHT("straight_right", true, false, true, "5", "_1"),
    LEFT_RIGHT("left_right", false, true, true, "6", "_2"),
    ALL("all", true, true, true, "7", "_1");

    private final String name;
    private final boolean straight;
    private final boolean left;
    private final boolean right;
    private final String blankModel;
    private final String shapeModel;

    PylonLinks(String name, boolean straight, boolean left, boolean right, String blankModel, String shapeModel)
    {
        this.name = name;
        this.straight = straight;
        this.left = left;
        this.right = right;
        this.blankModel = blankModel;
        this.shapeModel = shapeModel;
    }

    public static PylonLinks of(boolean straight, boolean left, boolean right)
    {
        for (PylonLinks links : values()) {
            if (links.straight == straight && links.left == left && links.right == right) return links;
        }
        return NONE;
    }

    // The part of the model name for a blank pylon ("base", "1"... "7") and for the Shape Pylon ("", "_1", "_2", "_3")
    public String blankModel() { return blankModel; }
    public String shapeModel() { return shapeModel; }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
