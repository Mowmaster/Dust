package com.mowmaster.dust.Features.Machines;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import org.jspecify.annotations.Nullable;

// Putting two elements together into the element mixed from them (DustElements: air is fire + water), in either
// order. Two solids (crystalline / amorphous) go together on the Combination Table; if either is gaseous or aqueous,
// on the Mixing Table.
public final class ElementCombining
{
    private ElementCombining() {}

    // The element made of exactly these two, or null if there isn't one
    public static @Nullable DustElement combine(DustElement a, DustElement b)
    {
        for (DustElement element : DustElements.all()) {
            if (element.parts().size() != 2) continue;
            DustElement first = element.parts().get(0), second = element.parts().get(1);
            if ((first == a && second == b) || (first == b && second == a)) return element;
        }
        return null;
    }

    // Whether two elements are put together by the Combination Table (true: both solid) or the Mixing Table (false)
    public static boolean combinedBySolids(DustElement a, DustElement b)
    {
        return Separation.isSolid(a) && Separation.isSolid(b);
    }
}
