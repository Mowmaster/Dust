package com.mowmaster.dust.Features.Spells.shapes;

import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellShape;

// Projectile: a bolt of the element flies the way the caster is looking (SpellProjectileEntity), dropping a little
// and slowing as it goes, and dissipates after the spell's range. Whatever it hits gets the matching effect.
public class ProjectileShape implements SpellShape
{
    @Override
    public boolean cast(SpellCast cast)
    {
        SpellProjectileEntity bolt = SpellProjectileEntity.create(cast);
        return bolt != null && cast.level().addFreshEntity(bolt);
    }
}
