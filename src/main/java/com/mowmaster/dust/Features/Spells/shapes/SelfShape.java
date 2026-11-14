package com.mowmaster.dust.Features.Spells.shapes;

import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellShape;

// Self: the spell's self effect on the caster (a block casting it has no self, so nothing happens)
public class SelfShape implements SpellShape
{
    // How far in front of a Spell Caster its "self" can be
    private static final double BLOCK_SELF_REACH = 1.5;

    @Override
    public boolean cast(SpellCast cast)
    {
        if (cast.caster() != null) {
            cast.applySelf(cast.caster());
            return true;
        }
        // Cast by a block (a Spell Caster): "self" is whoever stands at its face, the nearest within reach
        var nearest = cast.around(cast.origin(), BLOCK_SELF_REACH).stream()
                .min(java.util.Comparator.comparingDouble(entity -> entity.distanceToSqr(cast.origin())));
        if (nearest.isEmpty()) return false;
        cast.applySelf(nearest.get());
        return true;
    }
}
