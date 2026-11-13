package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

// Energy on a mob: charge stacks up (the Charged effect's level is the number of stacks), STACKS_PER_POTENCY each cast
// at potency 1. At MAX_STACKS it bursts: a shockwave that hurts and throws back everything around it (it included).
// Like Scorch, the stacks build over several hits.
public final class EnergyCharge
{
    public static final int STACKS_PER_POTENCY = 10;
    public static final int MAX_STACKS = 100;
    private static final double SHOCKWAVE_RADIUS = 4.0;
    // The charge lasts this long since the last hit
    private static final int CHARGE_TICKS = 20 * 20;

    private EnergyCharge() {}

    public static void charge(SpellCast cast, Entity target)
    {
        if (!(target instanceof LivingEntity living)) return;
        MobEffectInstance existing = living.getEffect(DustEffectsRegistry.CHARGED);
        int stacks = (existing == null ? 0 : existing.getAmplifier() + 1) + Math.max(1, Math.round(STACKS_PER_POTENCY * cast.stats().potency()));
        if (stacks < MAX_STACKS) {
            // A lower-duration instance with a higher level replaces the old one
            living.removeEffect(DustEffectsRegistry.CHARGED);
            living.addEffect(new MobEffectInstance(DustEffectsRegistry.CHARGED, CHARGE_TICKS, stacks - 1), cast.caster());
            return;
        }
        living.removeEffect(DustEffectsRegistry.CHARGED);
        shockwave(cast, living);
    }

    private static void shockwave(SpellCast cast, LivingEntity centre)
    {
        ServerLevel level = cast.level();
        Vec3 at = centre.position().add(0, centre.getBbHeight() / 2, 0);
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, at.x, at.y, at.z, 1, 0, 0, 0, 0);
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, at.x, at.y, at.z, 40, SHOCKWAVE_RADIUS / 2, 0.5, SHOCKWAVE_RADIUS / 2, 0.2);
        level.playSound(null, centre.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.8F, 1.6F);
        for (LivingEntity hit : cast.around(at, SHOCKWAVE_RADIUS)) {
            hit.hurtServer(level, level.damageSources().lightningBolt(), cast.stats().damage() * 3);
            Vec3 away = hit.position().subtract(centre.position());
            Vec3 push = (away.lengthSqr() < 1.0E-4 ? new Vec3(0, 0, 0) : away.normalize().scale(1.2)).add(0, 0.5, 0);
            hit.push(push.x, push.y, push.z);
            hit.needsSync = true;
        }
    }
}
