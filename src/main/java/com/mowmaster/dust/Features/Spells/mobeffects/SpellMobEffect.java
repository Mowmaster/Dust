package com.mowmaster.dust.Features.Spells.mobeffects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// A plain mob effect for spells: what it does is its attribute modifiers (added where it's registered, in
// DustEffectsRegistry) or code that checks for it (SpellMobEffects). MobEffect's constructor is protected, so even a
// do-nothing effect needs its own class.
public class SpellMobEffect extends MobEffect
{
    public SpellMobEffect(MobEffectCategory category, int color)
    {
        super(category, color);
    }
}
