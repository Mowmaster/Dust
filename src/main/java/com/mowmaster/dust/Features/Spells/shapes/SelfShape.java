package com.mowmaster.dust.Features.Spells.shapes;

import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellShape;

// Self: the spell's self effect on the caster (a block casting it has no self, so nothing happens)
public class SelfShape implements SpellShape
{
    @Override
    public boolean cast(SpellCast cast)
    {
        if (cast.caster() == null) return false;
        cast.applySelf(cast.caster());
        return true;
    }
}
