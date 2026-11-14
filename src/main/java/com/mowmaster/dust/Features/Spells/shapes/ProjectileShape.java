package com.mowmaster.dust.Features.Spells.shapes;

import com.mowmaster.dust.Features.Spells.SpellRegistries;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellShape;

// Projectile: a bolt of the element flies the way the caster is looking (SpellProjectileEntity), dropping a little
// and slowing as it goes, and dissipates after the spell's range. Whatever it hits gets the matching effect.
// With Hitscan there's no bolt: a ray reaches the first creature or block along the view at once (as far as the
// range), drawn as a line of dust. Impact Damage hits as a bolt's would.
public class ProjectileShape implements SpellShape
{
    private static final double PARTICLE_STEP = 0.35;

    @Override
    public boolean cast(SpellCast cast)
    {
        if (cast.spell().modifiers().contains(SpellRegistries.HITSCAN.getId())) return hitscan(cast);
        SpellProjectileEntity bolt = SpellProjectileEntity.create(cast);
        return bolt != null && cast.level().addFreshEntity(bolt);
    }

    private static boolean hitscan(SpellCast cast)
    {
        Vec3 from = cast.origin();
        Vec3 direction = cast.direction().normalize();
        Entity caster = cast.caster();
        Vec3 end = from.add(direction.scale(cast.stats().range()));
        // The first block in the way, then the nearest creature before it (ProjectileUtil.getEntityHitResult: the first
        // entity a line passes through)
        BlockHitResult block = cast.level().clip(new ClipContext(from, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                caster == null ? CollisionContext.empty() : CollisionContext.of(caster)));
        Vec3 stop = block.getType() == HitResult.Type.MISS ? end : block.getLocation();
        EntityHitResult entity = ProjectileUtil.getEntityHitResult(cast.level(), caster, from, stop, new AABB(from, stop).inflate(1.0),
                target -> target instanceof LivingEntity && target != caster && target.isAlive() && !target.isSpectator(), 0.3F);
        Vec3 hitAt = entity != null ? entity.getLocation() : stop;

        DustParticleOptions dust = new DustParticleOptions(cast.element().color(), 0.9F);
        double length = hitAt.distanceTo(from);
        for (double along = 0.5; along < length; along += PARTICLE_STEP) {
            Vec3 at = from.add(direction.scale(along));
            cast.level().sendParticles(dust, at.x, at.y, at.z, 1, 0, 0, 0, 0);
        }
        if (entity != null && entity.getEntity() instanceof LivingEntity target) {
            if (cast.stats().impactDamage() > 0) {
                target.hurtServer(cast.level(), cast.level().damageSources().indirectMagic(target, caster), cast.stats().impactDamage());
            }
            cast.applyEntity(target);
            return true;
        }
        if (block.getType() == HitResult.Type.BLOCK) {
            cast.applyBlock(block.getBlockPos(), block.getDirection());
            return true;
        }
        return false;
    }
}
