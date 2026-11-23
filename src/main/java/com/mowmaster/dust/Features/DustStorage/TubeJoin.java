package com.mowmaster.dust.Features.DustStorage;

import net.minecraft.util.StringRepresentable;

// How a Glass Dust Tube is joined on one side (DustTubeBlock): not at all, to another tube (a plain arm to the edge), or
// to a block that takes tubes (a jar, an apparatus...: a longer arm reaching into that block's space, so it meets the
// block's body, with its collar there): 3 pixels in (BLOCK), or 2 (NEAR: the Still Tower's barrel). The block says which
// (DustTubeConnectable.tubeJoin). SEALED: something it would join is there, but it's been cut off from it (shears, or
// a waxed tube beside it: DustTubeBlock); drawn and walked as NONE, and kept while that neighbour stays. PULL: joined to
// a jar as BLOCK is, with redstone on that end: the tube pulls dust out of the jar there (DustTubeBlock).
// StringRepresentable: how the value is written in the blockstate ("block").
public enum TubeJoin implements StringRepresentable
{
    NONE("none"), TUBE("tube"), BLOCK("block"), NEAR("near"), SEALED("sealed"), PULL("pull");

    private final String name;

    TubeJoin(String name)
    {
        this.name = name;
    }

    // The join that reaches as far into a block as its body is in (pixels: AttachInsets): flush (a plain arm to the
    // edge), 2 pixels (NEAR) or 3 (BLOCK), whichever's nearest
    public static TubeJoin reaching(double inset)
    {
        if (inset < 0.75) return TUBE;
        if (inset < 2.5) return NEAR;
        return BLOCK;
    }

    public boolean joined()
    {
        return this != NONE && this != SEALED;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
