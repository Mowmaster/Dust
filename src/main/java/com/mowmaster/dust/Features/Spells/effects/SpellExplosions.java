package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustRegistries.DustSoundRegistry;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

// Spell explosions: the same as Level.explode's plain form (damage, knockback, particles), but with our quieter
// explosion sound (DustSoundRegistry.SPELL_EXPLOSION) instead of vanilla's full-volume one. Used by Explosion's
// creature effect and Scorch's burst.
public final class SpellExplosions
{
    // Vanilla's puffs and smoke from the blocks around an explosion (Level keeps its own copy private)
    private static final WeightedList<ExplosionParticleInfo> BLOCK_PARTICLES = WeightedList.<ExplosionParticleInfo>builder()
            .add(new ExplosionParticleInfo(ParticleTypes.POOF, 0.5F, 1.0F))
            .add(new ExplosionParticleInfo(ParticleTypes.SMOKE, 1.0F, 1.0F))
            .build();

    private SpellExplosions() {}

    public static void explode(Level level, @Nullable Entity source, double x, double y, double z, float radius, Level.ExplosionInteraction interaction)
    {
        level.explode(source, Explosion.getDefaultDamageSource(level, source), null, x, y, z, radius, false, interaction,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, BLOCK_PARTICLES, DustSoundRegistry.SPELL_EXPLOSION);
    }
}
