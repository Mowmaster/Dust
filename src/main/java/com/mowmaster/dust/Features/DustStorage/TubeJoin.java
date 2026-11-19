package com.mowmaster.dust.Features.DustStorage;

import net.minecraft.util.StringRepresentable;

// How a Glass Dust Tube is joined on one side (DustTubeBlock): not at all, to another tube (a plain arm to the edge), or
// to a block that takes tubes (a jar, an apparatus...: a longer arm reaching into that block's space, so it meets the
// block's body, with its collar there). StringRepresentable: how the value is written in the blockstate ("block").
public enum TubeJoin implements StringRepresentable
{
    NONE("none"), TUBE("tube"), BLOCK("block");

    private final String name;

    TubeJoin(String name)
    {
        this.name = name;
    }

    public boolean joined()
    {
        return this != NONE;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
