package com.mowmaster.dust.Features.Spells.pylons;

import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import org.jspecify.annotations.Nullable;

// The kinds of pylon. SHAPE goes right after the Spell Pylon and holds the spell's shape (no level needed). The others
// draw a modifier from the tree's deck (SpellPylonBlockEntity):
//   NEUTRAL       anything in the deck (the only kind that can take a blessed card or a spell break); takes 3-7 nodes,
//                 2 fewer (down to 1-3) each time one is broken and replaced at the same spot
//   DAMAGE / GIANT / THRIFTY / EXPERIENCED   a modifier of their category if the deck has one, otherwise any; locking
//                 adds another of the category to the deck. 3-5 nodes (Experienced 1-3), 1 fewer each reroll (down to 1)
// minNodes / maxNodes: the range; step: how much a reroll at the spot takes off; floorMin / floorMax: how low they go.
public enum PylonType
{
    SHAPE("shape", null, 0, 0, 0, 0, 0),
    NEUTRAL("neutral", null, 3, 7, 2, 1, 3),
    DAMAGE("damage", ModifierCategory.DAMAGE, 3, 5, 1, 1, 1),
    GIANT("giant", ModifierCategory.GIANT, 3, 5, 1, 1, 1),
    THRIFTY("thrifty", ModifierCategory.THRIFTY, 3, 5, 1, 1, 1),
    EXPERIENCED("experienced", ModifierCategory.EXPERIENCED, 1, 3, 1, 1, 1);

    private final String name;
    private final @Nullable ModifierCategory category;
    private final int minNodes, maxNodes, step, floorMin, floorMax;

    PylonType(String name, @Nullable ModifierCategory category, int minNodes, int maxNodes, int step, int floorMin, int floorMax)
    {
        this.name = name;
        this.category = category;
        this.minNodes = minNodes;
        this.maxNodes = maxNodes;
        this.step = step;
        this.floorMin = floorMin;
        this.floorMax = floorMax;
    }

    public String serializedName() { return name; }
    public @Nullable ModifierCategory category() { return category; }

    // The upgrade node range after this many rerolls at the spot: {min, max}
    public int[] nodeRange(int rerolls)
    {
        int min = Math.max(floorMin, minNodes - step * rerolls);
        int max = Math.max(Math.max(floorMax, min), maxNodes - step * rerolls);
        return new int[] {min, max};
    }
}
