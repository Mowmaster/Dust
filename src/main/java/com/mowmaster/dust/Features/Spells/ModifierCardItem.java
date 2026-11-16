package com.mowmaster.dust.Features.Spells;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// A modifier card. Every modifier has one (DustItemRegistry.CARDS): pylons show the card of the modifier they hold, and
// the Spell Pylon's HUD shows the deck as cards. Shape modifiers' cards (AOE, Beam, Chaining, Piercing, Rune, Wall) and
// Transmutation's are playable: breaking a Spell Pylon drops one for each its tree had, and right-clicked on a Spell
// Pylon (one card per spell) it's laid on top and its modifier goes straight into the tree's deck, with it and its own
// modifiers unlocked for that spell whatever the level (SpellPylonBlockEntity.placeCard). The rest are for show.
public class ModifierCardItem extends Item
{
    private final Identifier modifier;
    private final boolean playable;

    public ModifierCardItem(Properties properties, Identifier modifier, boolean playable)
    {
        super(properties);
        this.modifier = modifier;
        this.playable = playable;
    }

    public Identifier modifier()
    {
        return modifier;
    }

    public boolean playable()
    {
        return playable;
    }

    // "<Modifier> Card"
    @Override
    public Component getName(ItemStack stack)
    {
        return Component.translatable("item.dust.modifier_card", Component.translatable("spell_modifier." + modifier.getNamespace() + "." + modifier.getPath()));
    }
}
