package com.mowmaster.dust.Features.Spells;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

// A modifier card: a shape modifier written down (AOE, Beam, Chaining, Piercing, Rune, Wall). Breaking a Spell Pylon
// drops one for each shape modifier its tree had. Right-clicked on a Spell Pylon (one card per spell), it's laid on
// top and its modifier goes straight into the tree's deck, with it and its own modifiers unlocked for that spell
// whatever the level (SpellPylonBlockEntity.placeCard).
public class ModifierCardItem extends Item
{
    private final Identifier modifier;

    public ModifierCardItem(Properties properties, Identifier modifier)
    {
        super(properties);
        this.modifier = modifier;
    }

    public Identifier modifier()
    {
        return modifier;
    }
}
