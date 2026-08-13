package com.mowmaster.dust.Features.EffectScrolls.Particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class ParticleSpellFire extends SingleQuadParticle {

    private final SpriteSet spellSprite;
    private final float baseSize;
    private final float phaseX;
    private final float phaseZ;

    public ParticleSpellFire(ClientLevel level, double x, double y, double z,
                             double xSpeed, double ySpeed, double zSpeed, SpriteSet spellSprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spellSprite.first());
        this.spellSprite = spellSprite;
        // Burn for 5 seconds
        this.lifetime = 10;

        // Mostly stationary; ignore incoming motion, gentle upward drift in tick()
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;

        // No gravity, slight damping to kill any accidental motion
        this.gravity = 0.0f;
        this.friction = 0.96f;

        // No collisions to avoid getting stuck on blocks
        this.hasPhysics = false;

        // Starting size and color
        this.baseSize = 0.3f + this.random.nextFloat() * 0.15f; // 0.30–0.45
        this.quadSize = baseSize;

        // Initial warm color (will shift over time)
        this.rCol = 1.0f;
        this.gCol = 0.9f;
        this.bCol = 0.3f;

        // Start mostly opaque; will flicker and fade late
        this.alpha = 0.95f;

        // Random per-particle phases to desync flicker/wobble
        this.phaseX = this.random.nextFloat() * Mth.PI * 2f;
        this.phaseZ = this.random.nextFloat() * Mth.PI * 2f;


        this.setSpriteFromAge(spellSprite);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.removed) return;

        float t = this.age / (float) this.lifetime; // 0..1

        // Slight upward drift (gentle convection)
        this.yd += 0.003;

        // Tiny horizontal wobble so the flame "licks" but stays in place
        double wobbleSpeed = 0.25;  // lower = slower
        double wobbleAmp   = 0.002; // very small so it doesn't wander
        this.xd += Math.sin(this.age * wobbleSpeed + this.phaseX) * wobbleAmp;
        this.zd += Math.cos(this.age * wobbleSpeed + this.phaseZ) * wobbleAmp;

        // Scale flicker (size breathes a bit)
        float flicker = 0.12f * Mth.sin(this.age * 0.45f + this.phaseX) + (this.random.nextFloat() - 0.5f) * 0.03f;
        float sizeOverLife = 1.0f - 0.15f * t; // tiny shrink over time
        this.quadSize = this.baseSize * sizeOverLife * (1.0f + flicker);

        // Color shift: bright yellow -> deeper orange/red
        float rStart = 1.00f, gStart = 0.90f, bStart = 0.30f;
        float rEnd   = 0.85f, gEnd   = 0.30f, bEnd   = 0.08f;
        this.rCol = Mth.lerp(t, rStart, rEnd);
        this.gCol = Mth.lerp(t, gStart, gEnd);
        this.bCol = Mth.lerp(t, bStart, bEnd);

        // Alpha: steady with flicker, then fade out in last 20% of life
        float baseAlpha = 0.92f + 0.06f * Mth.sin(this.age * 0.5f + this.phaseZ);
        if (t < 0.8f) {
            this.alpha = baseAlpha;
        } else {
            float ft = (t - 0.8f) / 0.2f; // 0..1
            this.alpha = Mth.lerp(1.0f - ft, baseAlpha, 0.0f);
        }

        // Update sprite frame if multiple frames exist
        this.setSpriteFromAge(this.spellSprite);
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                                 double x, double y, double z,
                                                 double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
            return new ParticleSpellFire(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
