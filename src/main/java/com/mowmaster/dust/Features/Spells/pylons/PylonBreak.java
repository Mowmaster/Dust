package com.mowmaster.dust.Features.Spells.pylons;

import net.minecraft.util.StringRepresentable;

// Whether a pylon is a spell break, and if so whether redstone powers it (the block state property "break", for the
// model: models/block/pylon/pylon_break_broken / pylon_break_connected):
//   none        not a spell break (the blank pylon models)
//   broken      a spell break without power: the spell ends here (the default break model)
//   connected   a spell break with redstone power: the line carries on past it
//   fusion      not a break: a Fusion / Spell Stack pylon (models/block/pylon/pylon_fusionstack_<links>: SpellLinks)
public enum PylonBreak implements StringRepresentable
{
    NONE("none"), BROKEN("broken"), CONNECTED("connected"), FUSION("fusion");

    private final String name;

    PylonBreak(String name)
    {
        this.name = name;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
