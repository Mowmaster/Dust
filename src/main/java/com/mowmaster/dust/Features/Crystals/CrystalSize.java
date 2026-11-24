package com.mowmaster.dust.Features.Crystals;

import net.minecraft.util.StringRepresentable;

// The three sizes a crystal grows to (models/block/crystal/crystal_<name>.json), and what each is worth:
//   height, inset  its outline: how tall it stands and how far in from the block's sides (pixels), as for
//                  vanilla's amethyst buds
//   value          the dust it gives back as an element orb when broken without Silk Touch (a small crystal is worth
//                  one dust, as a crystal made from Alchemical Dust is)
//   light          the light it gives off
public enum CrystalSize implements StringRepresentable
{
    SMALL("small", 4, 6, 1, 2),
    MEDIUM("medium", 8, 5, 4, 3),
    LARGE("large", 12, 3, 16, 5);

    private final String name;
    public final float height;
    public final float inset;
    public final int value;
    public final int light;

    CrystalSize(String name, float height, float inset, int value, int light)
    {
        this.name = name;
        this.height = height;
        this.inset = inset;
        this.value = value;
        this.light = light;
    }

    // The next size up, or null for the largest
    public CrystalSize next()
    {
        return this == SMALL ? MEDIUM : this == MEDIUM ? LARGE : null;
    }

    @Override
    public String getSerializedName()
    {
        return name;
    }
}
