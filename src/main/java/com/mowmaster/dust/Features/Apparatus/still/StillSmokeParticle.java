package com.mowmaster.dust.Features.Apparatus.still;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.RandomSource;

// The Still Top's smoke (dust:still_smoke): a campfire's slow rising smoke, tinted the colour it's given (white while the
// still boils, an element's colour as it leaves into the aura). Client only; registered in DustClient.
public final class StillSmokeParticle
{
    private StillSmokeParticle() {}

    public static class Provider implements ParticleProvider<ColorParticleOption>
    {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites)
        {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed,
                                       RandomSource random)
        {
            CampfireSmokeParticle smoke = new CampfireSmokeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, false, sprites.get(random));
            smoke.setColor(options.getRed(), options.getGreen(), options.getBlue());
            return smoke;
        }
    }
}
