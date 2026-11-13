package com.mowmaster.dust.Features.Spells.pylons;

import net.minecraft.util.StringRepresentable;

// A modifier pylon's look (the block state property "state", so each can have its own model):
//   inert      not part of a spell tree
//   linked     part of one, with a normal modifier (or, for the Shape Pylon, its shape)
//   blessed / corrupted   holding a blessed / corrupted modifier
//   break_on / break_off  a spell break, powered by redstone (the line goes on) or not (the rest of the line is off)
public enum PylonState implements StringRepresentable
{
    INERT("inert"), LINKED("linked"), BLESSED("blessed"), CORRUPTED("corrupted"), BREAK_ON("break_on"), BREAK_OFF("break_off");

    private final String name;

    PylonState(String name)
    {
        this.name = name;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
