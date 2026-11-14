package com.mowmaster.dust.Features.Spells.shapes;

import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

// Touch: whatever the caster is looking at within reach: an entity gets the entity effect, a block the block effect.
// Reach is the player's own (block / entity interaction range), or 4.5 blocks for anything else casting.
public class TouchShape implements SpellShape
{
    private static final double DEFAULT_REACH = 4.5;

    @Override
    public boolean cast(SpellCast cast)
    {
        Vec3 from = cast.origin();
        Entity caster = cast.caster();
        double blockReach = caster instanceof Player player ? player.blockInteractionRange() : DEFAULT_REACH;
        double entityReach = caster instanceof Player player ? player.entityInteractionRange() : DEFAULT_REACH;

        // The nearest block in reach, then any entity nearer than it. ClipContext: a ray through the world, here
        // stopping at blocks' outlines and ignoring fluids.
        Vec3 blockEnd = from.add(cast.direction().scale(blockReach));
        BlockHitResult block = cast.level().clip(new ClipContext(from, blockEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE,
                caster == null ? net.minecraft.world.phys.shapes.CollisionContext.empty() : net.minecraft.world.phys.shapes.CollisionContext.of(caster)));
        double blockDistance = block.getType() == HitResult.Type.MISS ? Double.MAX_VALUE : block.getLocation().distanceTo(from);

        Vec3 entityEnd = from.add(cast.direction().scale(entityReach));
        AABB sweep = new AABB(from, entityEnd).inflate(1.0);
        EntityHitResult entity = caster == null ? null : ProjectileUtil.getEntityHitResult(caster, from, entityEnd, sweep,
                target -> target instanceof LivingEntity && target != caster && target.isAlive() && !target.isSpectator(), entityReach * entityReach);
        if (entity != null && entity.getLocation().distanceTo(from) < blockDistance) {
            cast.applyEntity(entity.getEntity());
            return true;
        }
        // Cast by a block (a Spell Caster): there's no caster to look from, so the ray is tested against each creature
        // in the sweep (AABB.clip: where a line enters a box, if it does)
        if (caster == null) {
            LivingEntity nearest = null;
            double nearestDistance = blockDistance;
            for (LivingEntity target : cast.level().getEntitiesOfClass(LivingEntity.class, sweep, e -> e.isAlive() && !e.isSpectator())) {
                var hit = target.getBoundingBox().inflate(0.3).clip(from, entityEnd);
                if (hit.isPresent() && hit.get().distanceTo(from) < nearestDistance) {
                    nearest = target;
                    nearestDistance = hit.get().distanceTo(from);
                }
            }
            if (nearest != null) {
                cast.applyEntity(nearest);
                return true;
            }
        }
        if (block.getType() == HitResult.Type.BLOCK) {
            cast.applyBlock(block.getBlockPos(), block.getDirection());
            return true;
        }
        return false;
    }
}
