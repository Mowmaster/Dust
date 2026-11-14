package com.mowmaster.dust.Features.Spells.shapes;

import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellShape;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Comparator;
import java.util.List;

// Beam: an instant ray along the caster's view, as far as the spell's range (24 blocks to start with). It passes
// through every creature on the way (each gets the entity effect, nearest first) and stops at the first block (which
// gets the block effect). Drawn as a line of dust in the element's colour.
// Players unlock it the first time one of their spell trees reaches PylonConfig.BEAM_LEVEL (SpellScrollItem).
public class BeamShape implements SpellShape
{
    // How close to the line a creature's box has to come to be hit (blocks)
    private static final double WIDTH = 0.3;
    // Dust particles along the line: one every this many blocks
    private static final double PARTICLE_STEP = 0.35;

    @Override
    public boolean cast(SpellCast cast)
    {
        Vec3 from = cast.origin();
        Vec3 direction = cast.direction().normalize();
        Entity caster = cast.caster();
        Vec3 end = from.add(direction.scale(cast.stats().range()));

        // The first block in the way (ClipContext: a ray through the world, stopping at block outlines, ignoring fluids)
        BlockHitResult block = cast.level().clip(new ClipContext(from, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE,
                caster == null ? CollisionContext.empty() : CollisionContext.of(caster)));
        Vec3 stop = block.getType() == HitResult.Type.MISS ? end : block.getLocation();

        // Every creature whose box (a little wider) the line passes through before the block. AABB.clip: where a line
        // enters the box, if it does at all (an Optional, so isPresent says whether it does).
        List<LivingEntity> hits = cast.level().getEntitiesOfClass(LivingEntity.class, new AABB(from, stop).inflate(1.0),
                target -> target != caster && target.isAlive() && !target.isSpectator()
                        && target.getBoundingBox().inflate(WIDTH).clip(from, stop).isPresent());
        hits.sort(Comparator.comparingDouble(target -> target.distanceToSqr(from)));

        DustParticleOptions dust = new DustParticleOptions(cast.element().color(), 1.0F);
        double length = stop.distanceTo(from);
        for (double along = 0.5; along < length; along += PARTICLE_STEP) {
            Vec3 at = from.add(direction.scale(along));
            cast.level().sendParticles(dust, at.x, at.y, at.z, 1, 0.02, 0.02, 0.02, 0);
        }

        for (LivingEntity target : hits) cast.applyEntity(target);
        boolean hitBlock = block.getType() == HitResult.Type.BLOCK;
        if (hitBlock) cast.applyBlock(block.getBlockPos(), block.getDirection());
        // Nothing at all along it: refunded, like Touch with nothing in reach
        return hitBlock || !hits.isEmpty();
    }
}
