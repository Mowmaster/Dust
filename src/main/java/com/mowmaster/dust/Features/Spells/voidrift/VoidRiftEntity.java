package com.mowmaster.dust.Features.Spells.voidrift;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

// Void's block effect: a tear in space against the face hit. It draws loose items and experience in, and swallows
// whatever touches it into the void (VoidStorage, for research later):
//  - items and experience orbs vanish into the store
//  - ordinary creatures are killed outright; their drops and experience go to the store instead of the ground
//  - players and bosses take void damage while they touch it; if it kills them, their drops go to the void too
// Creative and spectator players are left alone. It doesn't break blocks. Drawn with portal particles only.
public class VoidRiftEntity extends Entity
{
    // How big the rift is (half its width, in blocks), synced so the client puts its particles in the right place
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(VoidRiftEntity.class, EntityDataSerializers.FLOAT);
    private static final double PULL_REACH = 3.0;
    private static final double PULL_STRENGTH = 0.06;
    private static final float TOUCH_DAMAGE = 4.0F;

    private int lifetime = 20 * 10;

    public VoidRiftEntity(EntityType<? extends VoidRiftEntity> type, Level level)
    {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    // Opens a rift centred at pos (a point in the world, usually the middle of the block space against the face hit)
    public static boolean open(ServerLevel level, Vec3 pos, float radius, int lifetime)
    {
        VoidRiftEntity rift = DustEntityRegistry.VOID_RIFT.get().create(level, EntitySpawnReason.TRIGGERED);
        if (rift == null) return false;
        rift.entityData.set(RADIUS, radius);
        rift.lifetime = lifetime;
        // The entity's position is the bottom middle of its box, so it sits half its height lower
        rift.setPos(pos.x, pos.y - rift.getBbHeight() / 2, pos.z);
        level.addFreshEntity(rift);
        level.playSound(null, rift.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 0.5F);
        return true;
    }

    public float radius()
    {
        return entityData.get(RADIUS);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        builder.define(RADIUS, 0.75F);
    }

    private Vec3 centre()
    {
        return getBoundingBox().getCenter();
    }

    // The part that swallows things: a cube radius out from the centre in every direction
    private AABB mouth()
    {
        return AABB.ofSize(centre(), radius() * 2, radius() * 2, radius() * 2);
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!(level() instanceof ServerLevel level)) {
            clientParticles();
            return;
        }
        Vec3 centre = centre();
        AABB mouth = mouth();
        VoidStorage storage = VoidStorage.get(level.getServer());

        // Draw loose items and experience in
        for (Entity loose : level.getEntities(this, mouth.inflate(PULL_REACH), e -> e instanceof ItemEntity || e instanceof ExperienceOrb)) {
            Vec3 toward = centre.subtract(loose.position()).normalize().scale(PULL_STRENGTH);
            loose.setDeltaMovement(loose.getDeltaMovement().add(toward));
            loose.needsSync = true;
        }
        // Swallow what touches it
        for (Entity touching : level.getEntities(this, mouth, Entity::isAlive)) {
            if (touching instanceof ItemEntity item) {
                storage.add(item.getItem());
                item.discard();
            } else if (touching instanceof ExperienceOrb orb) {
                storage.addExperience(orb.getValue());
                orb.discard();
            } else if (touching instanceof LivingEntity living) {
                swallow(level, living);
            }
        }

        if (tickCount % 40 == 0) level.playSound(null, blockPosition(), SoundEvents.PORTAL_AMBIENT, SoundSource.NEUTRAL, 0.3F, 1.5F);
        if (tickCount >= lifetime) {
            level.playSound(null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 0.8F, 1.5F);
            discard();
        }
    }

    private void swallow(ServerLevel level, LivingEntity living)
    {
        DamageSource voidDamage = level.damageSources().fellOutOfWorld();
        if (living instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) return;
            // Marked first, so if this hit kills them their drops are caught
            VoidCapture.mark(living);
            player.hurtServer(level, voidDamage, TOUCH_DAMAGE);
        } else if (living.is(Tags.EntityTypes.BOSSES)) {
            VoidCapture.mark(living);
            living.hurtServer(level, voidDamage, TOUCH_DAMAGE);
        } else {
            VoidCapture.mark(living);
            // Void damage gets past armour and invulnerability, so this kills anything that isn't a boss
            living.hurtServer(level, voidDamage, Float.MAX_VALUE);
        }
    }

    // Portal particles spiralling into the middle (the reverse portal particle drifts outward, so those go from the middle)
    private void clientParticles()
    {
        Vec3 centre = centre();
        double r = radius();
        for (int i = 0; i < 4; i++) {
            double dx = (random.nextDouble() - 0.5) * 2 * r;
            double dy = (random.nextDouble() - 0.5) * 2 * r;
            double dz = (random.nextDouble() - 0.5) * 2 * r;
            // The portal particle is given an offset and flies from there back to where it was spawned
            level().addParticle(ParticleTypes.PORTAL, centre.x, centre.y, centre.z, dx * 2, dy * 2, dz * 2);
        }
        if (random.nextInt(3) == 0) {
            level().addParticle(ParticleTypes.REVERSE_PORTAL, centre.x, centre.y, centre.z,
                    (random.nextDouble() - 0.5) * 0.05, (random.nextDouble() - 0.5) * 0.05, (random.nextDouble() - 0.5) * 0.05);
        }
        level().addParticle(ParticleTypes.SQUID_INK, centre.x + (random.nextDouble() - 0.5) * r, centre.y + (random.nextDouble() - 0.5) * r,
                centre.z + (random.nextDouble() - 0.5) * r, 0, 0, 0);
    }

    // Nothing can hit it, push it or stand on it
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount)
    {
        return false;
    }

    @Override
    public boolean isPickable()
    {
        return false;
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        output.putFloat("radius", radius());
        output.putInt("lifetime", lifetime);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        entityData.set(RADIUS, input.getFloatOr("radius", 0.75F));
        lifetime = input.getIntOr("lifetime", 20 * 10);
    }
}
