package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.Spells.api.ModifierCategory;
import com.mowmaster.dust.Features.Spells.api.SpellAspect;
import net.minecraft.resources.Identifier;

// An aspect whose effect is read by its fragment (EchoFragment counts its Resonance and Quickening aspects): it only
// says which fragment it belongs to, its category and how likely it is
public class BasicAspect implements SpellAspect
{
    private final Identifier fragment;
    private final ModifierCategory category;
    private final int weight;

    public BasicAspect(Identifier fragment, ModifierCategory category, int weight)
    {
        this.fragment = fragment;
        this.category = category;
        this.weight = weight;
    }

    @Override
    public Identifier fragment() { return fragment; }

    @Override
    public ModifierCategory category() { return category; }

    @Override
    public int weight() { return weight; }
}
