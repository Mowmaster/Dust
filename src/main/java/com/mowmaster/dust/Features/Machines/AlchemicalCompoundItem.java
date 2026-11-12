package com.mowmaster.dust.Features.Machines;

import net.minecraft.world.item.Item;

// A little pile of separated elements on a scrap of paper. What's in it and where it came from are data components
// (Compounds); the pile is tinted with the mix's colour (Client/CompoundTint), and its tooltip names the elements the
// player has discovered (Client/CompoundTooltips).
public class AlchemicalCompoundItem extends Item
{
    public AlchemicalCompoundItem(Properties properties)
    {
        super(properties);
    }
}
