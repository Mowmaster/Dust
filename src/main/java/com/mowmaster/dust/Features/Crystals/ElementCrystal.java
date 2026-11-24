package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.Features.DustComposition.DustElement;

// A block made of one element's crystal (a crystal of any size, a crystal block, a budding crystal): its element, for
// its tint (Client/ElementCrystalTint) and its drops
public interface ElementCrystal
{
    DustElement element();
}
