package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.Features.DustComposition.DustElement;

// A block entity that machines can pour element dust into (the Dust Grinder fills the Dust Jars next to it).
// Implement it on any other dust holder to let machines fill it too.
public interface IDustReceiver
{
    // Puts up to amount of this element in; returns how much went in. simulate: only work it out, change nothing.
    int insertDust(DustElement element, int amount, boolean simulate);
}
