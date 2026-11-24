package com.mowmaster.dust.Features.Magic;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

// The shimmer of something drawing on an over-full aura (elemental flowers crystallizing it, magical fruit ripening on
// it): motes of the element's colour round a point, and now and then a white twinkle drifting up. Called from blocks'
// animateTick (the client's, so this only spawns particles there).
public final class AuraShimmer
{
    private AuraShimmer() {}

    public static void sparkle(Level level, RandomSource random, double x, double y, double z, double spreadXZ, double spreadY, int color)
    {
        level.addParticle(new DustParticleOptions(color, 0.55F + random.nextFloat() * 0.35F),
                x + (random.nextDouble() * 2 - 1) * spreadXZ, y + (random.nextDouble() * 2 - 1) * spreadY, z + (random.nextDouble() * 2 - 1) * spreadXZ,
                0, 0, 0);
        if (random.nextInt(5) == 0) {
            level.addParticle(ParticleTypes.END_ROD,
                    x + (random.nextDouble() * 2 - 1) * spreadXZ, y + (random.nextDouble() * 2 - 1) * spreadY, z + (random.nextDouble() * 2 - 1) * spreadXZ,
                    0, 0.01 + random.nextDouble() * 0.015, 0);
        }
    }
}
