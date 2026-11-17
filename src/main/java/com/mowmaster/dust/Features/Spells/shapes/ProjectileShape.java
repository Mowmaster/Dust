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
    // Telekinesis without AOE: a creature this close to where the ray hit a block is taken instead of the block
    private static final double TELEKINESIS_REACH = 1.5;
    // Telekinesis: the line takes a creature it passes this close to (blocks outside its box)
    private static final float TELEKINESIS_MARGIN = 0.75F;

    @Override
    public boolean cast(SpellCast cast)
    {
        // Hitscan, or Telekinesis (always a ray: it takes hold of what you look at)
        if (cast.spell().modifiers().contains(SpellRegistries.HITSCAN.getId())
                || com.mowmaster.dust.Features.Spells.effects.Telekinesis.applies(cast.spell())) return hitscan(cast);
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
        // The last number: how far outside a creature's box the line may pass and still hit it. Telekinesis aims more
        // generously, so small creatures (baby slimes) are easy to take rather than the ground behind them.
        float margin = com.mowmaster.dust.Features.Spells.effects.Telekinesis.applies(cast.spell()) ? TELEKINESIS_MARGIN : 0.3F;
        EntityHitResult entity = ProjectileUtil.getEntityHitResult(cast.level(), caster, from, stop, new AABB(from, stop).inflate(1.0 + margin),
                target -> target instanceof LivingEntity && target != caster && target.isAlive() && !target.isSpectator(), margin);
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
            // Telekinesis takes creatures over blocks: the ray just missed one (looking down past a mob onto the
            // ground), so a creature near where it landed is taken instead of the block (within the AOE radius with AOE)
            if (com.mowmaster.dust.Features.Spells.effects.Telekinesis.applies(cast.spell())) {
                double radius = cast.has(SpellRegistries.AOE.getId()) ? cast.aoeRadius() : TELEKINESIS_REACH;
                Vec3 landed = block.getLocation();
                LivingEntity near = cast.level().getEntitiesOfClass(LivingEntity.class, new AABB(landed, landed).inflate(radius),
                                target -> target != caster && target.isAlive() && !target.isSpectator() && target.distanceToSqr(landed) <= radius * radius)
                        .stream().min(java.util.Comparator.comparingDouble(target -> target.distanceToSqr(landed))).orElse(null);
                if (near != null) {
                    cast.applyEntity(near);
                    return true;
                }
            }
            cast.applyBlock(block.getBlockPos(), block.getDirection());
            return true;
        }
        return false;
    }
}
