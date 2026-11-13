package com.mowmaster.dust.Features.Spells.shapes;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellConfig;
import com.mowmaster.dust.Features.Spells.api.SpellStats;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// A spell in flight (the Projectile shape). It carries its spell and stats; ThrowableProjectile does the moving and
// hit detection, using our gravity and air drag. It dissipates in a puff once it's flown the spell's range. Drawn as
// a trail of dust in its element's colour (client side, in tick; the entity itself has no model: NoopRenderer).
public class SpellProjectileEntity extends ThrowableProjectile
{
    // The element's id, synced so the client can colour the trail
    private static final EntityDataAccessor<String> ELEMENT = SynchedEntityData.defineId(SpellProjectileEntity.class, EntityDataSerializers.STRING);

    private @Nullable Spell spell;
    private SpellStats stats = SpellStats.defaults();
    private Vec3 start = Vec3.ZERO;

    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> type, Level level)
    {
        super(type, level);
    }

    // A bolt for this cast, at its origin, flying its way at the spell's speed (not yet added to the level)
    public static @Nullable SpellProjectileEntity create(SpellCast cast)
    {
        SpellProjectileEntity bolt = DustEntityRegistry.SPELL_PROJECTILE.get().create(cast.level(), EntitySpawnReason.TRIGGERED);
        if (bolt == null) return null;
        bolt.spell = cast.spell();
        bolt.stats = cast.stats();
        bolt.entityData.set(ELEMENT, cast.element().id().toString());
        if (cast.caster() != null) bolt.setOwner(cast.caster());
        Vec3 from = cast.origin().add(cast.direction().scale(0.5));
        bolt.setPos(from.x, from.y - 0.1, from.z);
        bolt.start = bolt.position();
        // shoot: sets the velocity along a direction at this speed, with no spread (inaccuracy 0)
        bolt.shoot(cast.direction().x, cast.direction().y, cast.direction().z, cast.stats().speed(), 0);
        return bolt;
    }

    public DustElement element()
    {
        DustElement element = DustElements.get(Identifier.parse(entityData.get(ELEMENT)));
        return element == null ? DustElements.FIRE : element;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        builder.define(ELEMENT, DustElements.FIRE.id().toString());
    }

    @Override
    protected double getDefaultGravity()
    {
        return stats.gravity();
    }

    @Override
    protected float getAirDrag()
    {
        return SpellConfig.AIR_DRAG;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (isRemoved()) return;
        if (level().isClientSide()) {
            // The trail: a few motes in its colour
            int color = element().color();
            for (int i = 0; i < 2; i++) {
                level().addParticle(new DustParticleOptions(color, 0.8F), getX() + (random.nextDouble() - 0.5) * 0.15,
                        getY() + (random.nextDouble() - 0.5) * 0.15, getZ() + (random.nextDouble() - 0.5) * 0.15, 0, 0, 0);
            }
            return;
        }
        // Gone too far, or too slow to matter: it fades out
        if (position().distanceTo(start) > stats.range() || getDeltaMovement().lengthSqr() < 0.0004 || tickCount > 20 * 30) dissipate();
    }

    private void dissipate()
    {
        if (level() instanceof ServerLevel server) {
            server.sendParticles(new DustParticleOptions(element().color(), 1.0F), getX(), getY(), getZ(), 10, 0.15, 0.15, 0.15, 0.01);
        }
        discard();
    }

    // What it hit gets the matching effect, cast as if from where the bolt is now
    private @Nullable SpellCast castHere()
    {
        if (spell == null || !(level() instanceof ServerLevel server)) return null;
        LivingEntity caster = getOwner() instanceof LivingEntity living ? living : null;
        return new SpellCast(server, caster, spell, stats, position(), getDeltaMovement().normalize());
    }

    @Override
    protected void onHitEntity(EntityHitResult hit)
    {
        super.onHitEntity(hit);
        SpellCast cast = castHere();
        if (cast != null && hit.getEntity() instanceof LivingEntity) cast.applyEntity(hit.getEntity());
    }

    @Override
    protected void onHitBlock(BlockHitResult hit)
    {
        super.onHitBlock(hit);
        SpellCast cast = castHere();
        if (cast != null) cast.applyBlock(hit.getBlockPos(), hit.getDirection());
    }

    // Whatever it hit, it's spent
    @Override
    protected void onHit(net.minecraft.world.phys.HitResult hit)
    {
        super.onHit(hit);
        if (!level().isClientSide()) dissipate();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        super.addAdditionalSaveData(output);
        if (spell != null) output.store("spell", Spell.CODEC, spell);
        output.putInt("duration", stats.duration());
        output.putFloat("potency", stats.potency());
        output.putInt("size", stats.size());
        output.putDouble("range", stats.range());
        output.putFloat("speed", stats.speed());
        output.putFloat("gravity", stats.gravity());
        output.store("start", Vec3.CODEC, start);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        super.readAdditionalSaveData(input);
        spell = input.read("spell", Spell.CODEC).orElse(null);
        SpellStats defaults = SpellStats.defaults();
        stats = new SpellStats(input.getIntOr("duration", defaults.duration()), input.getFloatOr("potency", defaults.potency()),
                input.getIntOr("size", 0), input.getDoubleOr("range", defaults.range()), input.getFloatOr("speed", defaults.speed()),
                input.getFloatOr("gravity", defaults.gravity()));
        start = input.read("start", Vec3.CODEC).orElse(position());
        if (spell != null) entityData.set(ELEMENT, spell.element().id().toString());
    }
}
