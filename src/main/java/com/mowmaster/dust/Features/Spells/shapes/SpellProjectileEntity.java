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
//   falloff      no gravity until it's flown the spell's falloff distance (Falloff)
//   splits       Multishot: at the falloff point it splits, this many more bolts fanning out beside it (they don't split)
//   impact       a creature it hits takes the spell's impact damage as well as the effect (Impact Damage)
//   pierce       Piercing: how many more creatures / blocks it can go through, affecting each, before it's spent (1,
//                plus half a level of Depth); it won't hit the same one twice
public class SpellProjectileEntity extends ThrowableProjectile
{
    // The element's id, synced so the client can colour the trail
    private static final EntityDataAccessor<String> ELEMENT = SynchedEntityData.defineId(SpellProjectileEntity.class, EntityDataSerializers.STRING);

    private @Nullable Spell spell;
    private SpellStats stats = SpellStats.defaults();
    private Vec3 start = Vec3.ZERO;
    private int splits;
    private int pierce;
    // The Spell Caster that fired it, if one did (SpellCast.casterBlock), for the cast where it lands
    private net.minecraft.core.@Nullable BlockPos casterBlock;
    // Not saved: what it's already gone through (so it doesn't hit them again)
    private final java.util.Set<java.util.UUID> pierced = new java.util.HashSet<>();
    private final java.util.Set<net.minecraft.core.BlockPos> piercedBlocks = new java.util.HashSet<>();
    // Set while handling a hit it goes on through
    private boolean goesOn;
    // Degrees between the bolts of a Multishot split
    private static final float SPREAD_DEGREES = 8F;

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
        bolt.splits = cast.spell().levelsOf(com.mowmaster.dust.Features.Spells.SpellRegistries.MULTISHOT.getId());
        bolt.pierce = piercing(cast.spell());
        bolt.casterBlock = cast.casterBlock();
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

    // No gravity until past the falloff distance
    @Override
    protected double getDefaultGravity()
    {
        return pastFalloff() ? stats.gravity() : 0;
    }

    private boolean pastFalloff()
    {
        return position().distanceToSqr(start) >= stats.falloff() * stats.falloff();
    }

    // Stonecutter: 26.1.2 has no getAirDrag to override; it always uses 0.99 in air (the same as AIR_DRAG now)
    //? if >=26.3 {
    @Override
    protected float getAirDrag()
    {
        return SpellConfig.AIR_DRAG;
    }
    //?}

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
        // A lightning bolt bends towards the nearest conductive thing (a Conductive creature, a block a Lightning spell
        // marked) within HOMING_RANGE
        if (element() == DustElements.LIGHTNING) homeIn();
        // Virgo (on a spell of another element): it homes in on the nearest creature
        if (spell != null && com.mowmaster.dust.Features.Spells.modifiers.Blessings.homes(spell)) homeOnCreature();
        // Multishot: at the falloff point it splits
        if (splits > 0 && pastFalloff()) split();
        // Gone too far, or too slow to matter: it fades out
        if (position().distanceTo(start) > stats.range() || getDeltaMovement().lengthSqr() < 0.0004 || tickCount > 20 * 30) dissipate();
    }

    private static final double HOMING_RANGE = 8;

    private void homeIn()
    {
        Vec3 here = position();
        Vec3 target = null;
        double best = HOMING_RANGE * HOMING_RANGE;
        for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class,
                getBoundingBox().inflate(HOMING_RANGE), other -> other != getOwner() && other.hasEffect(com.mowmaster.dust.DustRegistries.DustEffectsRegistry.CONDUCTIVE))) {
            Vec3 at = living.position().add(0, living.getBbHeight() / 2, 0);
            if (at.distanceToSqr(here) < best) {
                best = at.distanceToSqr(here);
                target = at;
            }
        }
        net.minecraft.core.BlockPos marked = com.mowmaster.dust.Features.Spells.effects.BlockMarks.nearest(level(), here, Math.sqrt(best),
                com.mowmaster.dust.Features.Spells.effects.BlockMarks.CONDUCTIVE);
        if (marked != null) target = Vec3.atCenterOf(marked);
        if (target == null) return;
        Vec3 velocity = getDeltaMovement();
        double speed = velocity.length();
        // Turn a fifth of the way towards it each tick, keeping the speed
        Vec3 turned = velocity.normalize().lerp(target.subtract(here).normalize(), 0.2).normalize().scale(speed);
        setDeltaMovement(turned);
    }

    // The split: this many more bolts beside this one, fanned out evenly to either side of its path (turned about the
    // vertical: Vec3.yRot, in radians), carrying on from here with its speed. They don't split again.
    private void split()
    {
        int extra = splits;
        splits = 0;
        Vec3 velocity = getDeltaMovement();
        for (int i = 1; i <= extra; i++) {
            // Alternate sides: +1, -1, +2, -2...
            int step = (i + 1) / 2 * (i % 2 == 1 ? 1 : -1);
            SpellProjectileEntity bolt = DustEntityRegistry.SPELL_PROJECTILE.get().create(level(), EntitySpawnReason.TRIGGERED);
            if (bolt == null) continue;
            bolt.spell = spell;
            bolt.stats = stats;
            bolt.entityData.set(ELEMENT, entityData.get(ELEMENT));
            bolt.setOwner(getOwner());
            bolt.setPos(getX(), getY(), getZ());
            bolt.start = start;
            bolt.pierce = pierce;
            bolt.casterBlock = casterBlock;
            bolt.setDeltaMovement(velocity.yRot((float) Math.toRadians(step * SPREAD_DEGREES)));
            level().addFreshEntity(bolt);
        }
    }

    // Turns a fifth of the way towards the nearest living creature (not its caster) within HOMING_RANGE each tick
    private void homeOnCreature()
    {
        Vec3 here = position();
        LivingEntity nearest = null;
        double best = HOMING_RANGE * HOMING_RANGE;
        for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(HOMING_RANGE),
                other -> other != getOwner() && other.isAlive() && !pierced.contains(other.getUUID()))) {
            double distance = living.position().distanceToSqr(here);
            if (distance < best) {
                best = distance;
                nearest = living;
            }
        }
        if (nearest == null) return;
        Vec3 velocity = getDeltaMovement();
        Vec3 aim = nearest.position().add(0, nearest.getBbHeight() / 2, 0).subtract(here).normalize();
        setDeltaMovement(velocity.normalize().lerp(aim, 0.2).normalize().scale(velocity.length()));
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
        return new SpellCast(server, caster, spell, stats, position(), getDeltaMovement().normalize()).fromBlock(casterBlock);
    }

    // Piercing: 1 to go through, plus half a level of Depth (0 without Piercing)
    private static int piercing(Spell spell)
    {
        if (!spell.modifiers().contains(com.mowmaster.dust.Features.Spells.SpellRegistries.PIERCING.getId())) return 0;
        int pierce = 1 + spell.levelsOf(com.mowmaster.dust.Features.Spells.SpellRegistries.DEPTH.getId()) / 2;
        // Gluttony: twice as deep
        return com.mowmaster.dust.Features.Spells.modifiers.Sins.gluttonous(spell) ? pierce * 2 : pierce;
    }

    // Not what it's already gone through
    @Override
    protected boolean canHitEntity(net.minecraft.world.entity.Entity target)
    {
        return super.canHitEntity(target) && !pierced.contains(target.getUUID());
    }

    @Override
    protected void onHitEntity(EntityHitResult hit)
    {
        super.onHitEntity(hit);
        SpellCast cast = castHere();
        if (cast != null && hit.getEntity() instanceof LivingEntity target) {
            // Impact Damage: the blow itself, then the effect
            if (stats.impactDamage() > 0) target.hurtServer(cast.level(), damageSources().thrown(this, getOwner()), stats.impactDamage());
            cast.applyEntity(target);
        }
        pierceThrough(() -> pierced.add(hit.getEntity().getUUID()));
    }

    @Override
    protected void onHitBlock(BlockHitResult hit)
    {
        // Still passing through a block it pierced: nothing new
        if (piercedBlocks.contains(hit.getBlockPos())) {
            goesOn = true;
            return;
        }
        super.onHitBlock(hit);
        SpellCast cast = castHere();
        if (cast != null) cast.applyBlock(hit.getBlockPos(), hit.getDirection());
        pierceThrough(() -> piercedBlocks.add(hit.getBlockPos().immutable()));
    }

    // With pierces left: one used, the thing remembered, and it carries on
    private void pierceThrough(Runnable remember)
    {
        if (pierce <= 0) return;
        pierce--;
        remember.run();
        goesOn = true;
    }

    // Whatever it hit, it's spent (unless it pierced through)
    @Override
    protected void onHit(net.minecraft.world.phys.HitResult hit)
    {
        goesOn = false;
        super.onHit(hit);
        if (!level().isClientSide() && !goesOn) dissipate();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        super.addAdditionalSaveData(output);
        if (spell != null) output.store("spell", Spell.CODEC, spell);
        output.store("stats", SpellStats.CODEC, stats);
        output.store("start", Vec3.CODEC, start);
        output.putInt("splits", splits);
        output.putInt("pierce", pierce);
        if (casterBlock != null) output.store("caster_block", net.minecraft.core.BlockPos.CODEC, casterBlock);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        super.readAdditionalSaveData(input);
        spell = input.read("spell", Spell.CODEC).orElse(null);
        stats = input.read("stats", SpellStats.CODEC).orElse(SpellStats.defaults());
        start = input.read("start", Vec3.CODEC).orElse(position());
        splits = input.getIntOr("splits", 0);
        pierce = input.getIntOr("pierce", 0);
        casterBlock = input.read("caster_block", net.minecraft.core.BlockPos.CODEC).orElse(null);
        if (spell != null) entityData.set(ELEMENT, spell.element().id().toString());
    }
}
