package com.mowmaster.dust.Features.soul;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

// A mob's soul, pulled out of its body by Soul. It drifts through blocks looking for its body: every second it picks a
// heading, toward the body half the time and anywhere the other half, so whether it makes it back is down to luck and
// how far it was thrown. Touching the body puts it back. Hitting the wisp destroys it, and the body dies with it.
// Drawn as soul particles.
public class SoulWispEntity extends Entity
{
    private static final double SPEED = 0.1;
    private static final int LOST_TICKS = 40;

    private @Nullable UUID body;
    private Vec3 heading = Vec3.ZERO;
    private int bodyMissing;

    public SoulWispEntity(EntityType<? extends SoulWispEntity> type, Level level)
    {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    public static @Nullable SoulWispEntity spawn(ServerLevel level, LivingEntity body, Vec3 at)
    {
        SoulWispEntity wisp = DustEntityRegistry.SOUL_WISP.get().create(level, EntitySpawnReason.TRIGGERED);
        if (wisp == null) return null;
        wisp.body = body.getUUID();
        wisp.setPos(at.x, at.y, at.z);
        level.addFreshEntity(wisp);
        return wisp;
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder)
    {
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!(level() instanceof ServerLevel level)) {
            level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY() + 0.25, getZ(), 0, 0.01, 0);
            if (random.nextInt(3) == 0) level().addParticle(ParticleTypes.SOUL, getX(), getY() + 0.25, getZ(), 0, 0.02, 0);
            return;
        }
        LivingEntity owner = body == null ? null : level.getEntity(body) instanceof LivingEntity living ? living : null;
        // Its body has to be there, alive and still waiting for it (tethered to this wisp)
        boolean waiting = owner != null && owner.isAlive()
                && owner.getExistingData(com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry.SOUL_TETHER)
                        .map(tether -> tether.spirit().equals(getUUID())).orElse(false);
        if (!waiting) {
            if (++bodyMissing > LOST_TICKS) discard();
            return;
        }
        bodyMissing = 0;

        Vec3 target = owner.getBoundingBox().getCenter();
        if (position().distanceTo(target) < 1.0) {
            SoulEvents.reuniteMob(level, owner, this);
            return;
        }
        if (tickCount % 20 == 1) {
            Vec3 toward = target.subtract(position()).normalize();
            Vec3 wander = new Vec3(random.nextDouble() - 0.5, (random.nextDouble() - 0.5) * 0.4, random.nextDouble() - 0.5).normalize();
            heading = random.nextBoolean() ? toward : wander;
        }
        // A little wobble on top, so it doesn't fly in dead straight lines
        Vec3 wobble = new Vec3(Mth.sin(tickCount * 0.3F) * 0.02, Mth.cos(tickCount * 0.25F) * 0.02, 0);
        Vec3 next = position().add(heading.scale(SPEED)).add(wobble);
        setPos(next.x, next.y, next.z);
    }

    // Any hit destroys it, and its body dies (SoulEvents sees the body's Ghostly gone with the tether still there)
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount)
    {
        if (!(source.getEntity() instanceof Player)) return false;
        if (body != null && level.getEntity(body) instanceof LivingEntity owner) owner.removeEffect(DustEffectsRegistry.GHOSTLY);
        level.sendParticles(ParticleTypes.SOUL, getX(), getY() + 0.25, getZ(), 15, 0.2, 0.2, 0.2, 0.05);
        level.playSound(null, blockPosition(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.NEUTRAL, 1.0F, 0.6F);
        discard();
        return true;
    }

    @Override
    public boolean isPickable()
    {
        return !isRemoved();
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        if (body != null) output.store("body", net.minecraft.core.UUIDUtil.CODEC, body);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        body = input.read("body", net.minecraft.core.UUIDUtil.CODEC).orElse(null);
    }
}
