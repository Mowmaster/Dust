package com.mowmaster.dust.Features.Spells.effects;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

// What a player turned into a creature (Beastification) does when they cast or strike with a weapon: the creature's
// own attack, at most once every COOLDOWN ticks.
//   ghast            a fireball (explodes like a ghast's)
//   blaze            three small fireballs
//   creeper          its fuse hisses, and FUSE_TICKS later it explodes, killing the player
//   skeleton, stray, bogged, pillager   an arrow (it can't be picked up)
//   snow golem       a snowball
//   wither           a wither skull
//   shulker          a homing bullet at the creature looked at (else its sound)
//   evoker           a line of fangs along the ground
//   breeze           a wind charge
//   warden           a sonic boom at the first creature in line (up to SONIC_RANGE)
//   anything else with attack damage (zombies, spiders, wolves, iron golems...)   a hit with its attack damage on the
//                    creature struck or looked at (within MELEE_RANGE); nothing there: its sound
//   no attack at all (pigs, cows...)   one of its sounds (its ambient sound, as it makes idly)
public final class BeastAttacks
{
    private static final int COOLDOWN = 20;
    private static final int FUSE_TICKS = 30;
    private static final double MELEE_RANGE = 4;
    private static final double LOOK_RANGE = 32;
    private static final double SONIC_RANGE = 15;

    // When each player may attack again (game time), and when a turned creeper player goes off
    private static final Map<UUID, Long> READY = new HashMap<>();
    private static final Map<UUID, Long> FUSES = new HashMap<>();
    // One creature of each kind per level, never added to it: what makes the sounds (its own sound methods)
    private static final Map<Level, Map<EntityType<?>, Entity>> VOICES = new WeakHashMap<>();

    private BeastAttacks() {}

    // The form's attack. struck: the creature hit by a weapon, or null (a spell, or a swing at the air).
    public static void attack(ServerPlayer player, @Nullable Entity struck)
    {
        EntityType<?> form = Beastification.formOf(player);
        if (form == null || FUSES.containsKey(player.getUUID())) return;
        ServerLevel level = player.level();
        long now = level.getGameTime();
        if (READY.getOrDefault(player.getUUID(), 0L) > now) return;
        READY.put(player.getUUID(), now + COOLDOWN);

        Vec3 look = player.getLookAngle();
        Vec3 muzzle = player.getEyePosition().add(look.scale(1.2));
        if (is(form, "ghast")) {
            LargeFireball fireball = new LargeFireball(level, player, look, 1);
            fireball.setPos(muzzle);
            level.addFreshEntity(fireball);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (is(form, "blaze")) {
            for (int i = 0; i < 3; i++) {
                Vec3 spread = look.add(player.getRandom().triangle(0, 0.1), player.getRandom().triangle(0, 0.1), player.getRandom().triangle(0, 0.1));
                SmallFireball fireball = new SmallFireball(level, player, spread.normalize());
                fireball.setPos(muzzle);
                level.addFreshEntity(fireball);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (is(form, "creeper")) {
            FUSES.put(player.getUUID(), now + FUSE_TICKS);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CREEPER_PRIMED, SoundSource.PLAYERS, 1.0F, 0.5F);
        } else if (is(form, "skeleton") || is(form, "stray") || is(form, "bogged") || is(form, "pillager")) {
            Arrow arrow = new Arrow(level, player, new ItemStack(Items.ARROW), null);
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
            level.addFreshEntity(arrow);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    is(form, "pillager") ? SoundEvents.CROSSBOW_SHOOT : SoundEvents.SKELETON_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (is(form, "snow_golem")) {
            Snowball snowball = new Snowball(level, player, new ItemStack(Items.SNOWBALL));
            snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.6F, 1.0F);
            level.addFreshEntity(snowball);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (is(form, "wither")) {
            WitherSkull skull = new WitherSkull(level, player, look);
            skull.setPos(muzzle);
            level.addFreshEntity(skull);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (is(form, "shulker")) {
            Entity target = struck != null ? struck : lookedAt(player, LOOK_RANGE);
            if (target == null) {
                voice(level, player, form);
                return;
            }
            level.addFreshEntity(new ShulkerBullet(level, player, target, Direction.Axis.Y));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHULKER_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (is(form, "evoker")) {
            Vec3 flat = new Vec3(look.x, 0, look.z).normalize();
            float yaw = (float) Math.atan2(flat.z, flat.x);
            for (int i = 1; i <= 8; i++) {
                Vec3 at = player.position().add(flat.scale(i * 1.25));
                level.addFreshEntity(new EvokerFangs(level, at.x, player.getY(), at.z, yaw, i, player));
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (is(form, "breeze")) {
            WindCharge charge = new WindCharge(player, level, muzzle.x, muzzle.y, muzzle.z);
            charge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(charge);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BREEZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else if (is(form, "warden")) {
            sonicBoom(level, player, look);
        } else {
            melee(level, player, form, struck);
        }
    }

    // Whether the form is this vanilla creature (by id: 26.3 keeps the vanilla types on EntityTypes, 26.1.2 on EntityType)
    private static boolean is(EntityType<?> form, String vanillaId)
    {
        return net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(form).equals(net.minecraft.resources.Identifier.withDefaultNamespace(vanillaId));
    }

    // Each tick for each player: a turned creeper's fuse burns down, and it goes off
    public static void tick(ServerPlayer player)
    {
        Long at = FUSES.get(player.getUUID());
        if (at == null) return;
        ServerLevel level = player.level();
        if (Beastification.formOf(player) == null || !player.isAlive()) {
            FUSES.remove(player.getUUID());
            return;
        }
        if (level.getGameTime() < at) return;
        FUSES.remove(player.getUUID());
        level.explode(player, player.getX(), player.getY(), player.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
        if (player.isAlive()) player.hurtServer(level, level.damageSources().explosion(player, player), Float.MAX_VALUE);
    }

    // A hit with the form's attack damage on what was struck, or what's looked at close by; nothing: its sound
    private static void melee(ServerLevel level, ServerPlayer player, EntityType<?> form, @Nullable Entity struck)
    {
        double damage = attackDamage(form);
        if (damage <= 0) {
            voice(level, player, form);
            return;
        }
        Entity target = struck != null ? struck : lookedAt(player, MELEE_RANGE);
        if (!(target instanceof LivingEntity living)) {
            voice(level, player, form);
            return;
        }
        living.hurtServer(level, level.damageSources().playerAttack(player), (float) damage);
        // An iron golem throws what it hits into the air, as it does
        if (is(form, "iron_golem")) living.setDeltaMovement(living.getDeltaMovement().add(0, 0.4, 0));
        voice(level, player, form);
    }

    private static double attackDamage(EntityType<?> form)
    {
        if (!DefaultAttributes.hasSupplier(form)) return 0;
        @SuppressWarnings("unchecked")
        AttributeSupplier supplier = DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) form);
        return supplier.hasAttribute(Attributes.ATTACK_DAMAGE) ? supplier.getBaseValue(Attributes.ATTACK_DAMAGE) : 0;
    }

    // A warden's sonic boom: the first creature in line takes 10 damage that armour doesn't stop, and is knocked back
    private static void sonicBoom(ServerLevel level, ServerPlayer player, Vec3 look)
    {
        Vec3 from = player.getEyePosition();
        for (int i = 1; i <= SONIC_RANGE; i++) {
            Vec3 at = from.add(look.scale(i));
            level.sendParticles(ParticleTypes.SONIC_BOOM, at.x, at.y, at.z, 1, 0, 0, 0, 0);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 3.0F, 1.0F);
        Entity target = lookedAt(player, SONIC_RANGE);
        if (target instanceof LivingEntity living) {
            living.hurtServer(level, level.damageSources().sonicBoom(player), 10.0F);
            living.push(look.x * 2.5, 0.5, look.z * 2.5);
        }
    }

    // The creature the player looks at, within range, or null
    private static @Nullable Entity lookedAt(ServerPlayer player, double range)
    {
        Vec3 eyes = player.getEyePosition();
        Vec3 end = eyes.add(player.getLookAngle().scale(range));
        AABB box = player.getBoundingBox().expandTowards(player.getLookAngle().scale(range)).inflate(1.0);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(player, eyes, end, box,
                entity -> entity instanceof LivingEntity && entity.isAlive() && entity != player && !entity.isSpectator(), range * range);
        return hit == null ? null : hit.getEntity();
    }

    // One of the form's sounds, from the player
    private static void voice(ServerLevel level, ServerPlayer player, EntityType<?> form)
    {
        Entity voice = VOICES.computeIfAbsent(level, key -> new HashMap<>()).computeIfAbsent(form, type -> type.create(level, EntitySpawnReason.TRIGGERED));
        if (!(voice instanceof Mob mob)) return;
        mob.setPos(player.getX(), player.getY(), player.getZ());
        mob.playAmbientSound();
    }
}
