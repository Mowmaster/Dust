package com.mowmaster.dust.Features.Spells.api;

// Which spells a modifier suits, by how their effect plays out (EffectType): Damage only means something for effects
// that hit once, Duration only for ones that linger. ANY suits every spell.
public enum ModifierFit
{
    ANY, INSTANT, DOT;

    // Whether a spell whose effect can be instant / lingering (on what it lands on) takes this
    public boolean suits(boolean instant, boolean dot)
    {
        return switch (this) {
            case ANY -> true;
            case INSTANT -> instant;
            case DOT -> dot;
        };
    }
}
