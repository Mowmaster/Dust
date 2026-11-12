package com.mowmaster.dust.Features.Machines;

import net.minecraft.world.item.Item;

// Alchemical Dust: stable dust of one element, made from an alchemical compound on the Transformation Table. Its
// element is a data component (AlchemicalDust); the heap is tinted the element's colour (Client/AlchemicalDustTint)
// and its tooltip shows the element's icon and phase (Client/AlchemicalDustTooltips). It doesn't remember what it was
// made from.
public class AlchemicalDustItem extends Item
{
    public AlchemicalDustItem(Properties properties)
    {
        super(properties);
    }
}
