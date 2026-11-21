package com.mowmaster.dust.Features.Apparatus.still;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import org.jspecify.annotations.Nullable;

// Something with an element filter set by hand (a Dust Chute, a Still Pot): right-clicked with Research Notes (or a
// Shared Research Guide) open at an element's page, the client says which element (DustChuteFilterPayload); nothing is
// used up. null clears it.
public interface ElementFilterable
{
    void setFilter(@Nullable DustElement element);
}
