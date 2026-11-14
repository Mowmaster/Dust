package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.Spells.api.SpellAspect;
import net.minecraft.resources.Identifier;

// An aspect whose effect is read by its fragment (EchoFragment counts its Resonance and Quickening aspects): it only
// says which fragment it belongs to and how likely it is
public class BasicAspect implements SpellAspect
{
    private final Identifier fragment;
    private final int weight;

    public BasicAspect(Identifier fragment, int weight)
    {
        this.fragment = fragment;
        this.weight = weight;
    }

    @Override
    public Identifier fragment() { return fragment; }

    @Override
    public int weight() { return weight; }
}
