package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.Features.DustComposition.DustElement;

// A block entity that holds element dust other things can take out as well as pour in: the Dust Jars. Dust Tubes
// (DustNetwork) treat these as the storage of their network: the alchemical apparatus pour what they make into them
// and draw what they need out of them. Implement it on any other dust store to put it on a tube network too (its block
// then needs DustTubeConnectable, so the tubes join on).
public interface IDustHolder extends IDustReceiver
{
    // How much of this element it holds that can be taken out (0: none)
    int dustAmount(DustElement element);

    // Takes up to amount of this element out; returns how much came out. simulate: only work it out, change nothing.
    int extractDust(DustElement element, int amount, boolean simulate);

    // Whether it's set to this element: it holds some, or keeps the element while empty (a jar keeps its element until
    // it's cleared). The tubes fill these first, so each element gathers in its own jars.
    default boolean prefersDust(DustElement element)
    {
        return dustAmount(element) > 0;
    }
}
