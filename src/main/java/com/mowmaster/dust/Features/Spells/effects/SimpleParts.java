package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.Features.EffectScrolls.DustEffects.DustDamageTypes;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

// The short effect parts that are more than a mob effect (BasicEffect builds elements out of these). Each is the
// shape of one of BasicEffect's part interfaces, so it can be passed as a method reference (SimpleParts::magicDamage).
public final class SimpleParts
{
    private SimpleParts() {}

    // ---- damage ----

    public static void magicDamage(SpellCast cast, Entity target)
    {
        var sources = cast.level().damageSources();
        target.hurtServer(cast.level(), cast.caster() == null ? sources.magic() : sources.indirectMagic(cast.caster(), cast.caster()), cast.stats().damage());
    }

    public static void voidDamage(SpellCast cast, Entity target)
    {
        target.hurtServer(cast.level(), cast.level().damageSources().fellOutOfWorld(), cast.stats().damage());
    }

    // A physical blow from magic (dust:damagetype_kinetic)
    public static void kineticDamage(SpellCast cast, Entity target)
    {
        target.hurtServer(cast.level(), DustDamageTypes.create(cast.level(), DustDamageTypes.KINETIC_KEY, cast.caster()), cast.stats().damage());
    }

    // An explosion where it stands (hurts what's around it, breaks no blocks), bigger with potency
    public static void explodeAt(SpellCast cast, Entity target)
    {
        cast.level().explode(cast.caster(), target.getX(), target.getY() + 0.5, target.getZ(), 1.0F + cast.stats().potency(), Level.ExplosionInteraction.NONE);
    }

    // Kills anything under 10% health, bosses too; the undead are healed to full instead
    public static void execute(SpellCast cast, Entity target)
    {
        if (!(target instanceof LivingEntity living) || living.getHealth() > living.getMaxHealth() * 0.1F) return;
        if (living.is(EntityTypeTags.UNDEAD)) {
            living.setHealth(living.getMaxHealth());
            cast.level().sendParticles(ParticleTypes.SOUL, living.getX(), living.getY() + 1, living.getZ(), 12, 0.3, 0.5, 0.3, 0.02);
        } else {
            cast.level().sendParticles(ParticleTypes.SCULK_SOUL, living.getX(), living.getY() + 1, living.getZ(), 12, 0.3, 0.5, 0.3, 0.02);
            living.kill(cast.level());
        }
    }

    // ---- healing ----

    // Heals it directly (2 hearts x potency): unlike Instant Health, this doesn't hurt the undead
    public static void heal(SpellCast cast, Entity target)
    {
        if (target instanceof LivingEntity living) {
            living.heal(4 * cast.stats().potency());
            cast.level().sendParticles(ParticleTypes.HEART, living.getX(), living.getY() + living.getBbHeight(), living.getZ(), 3, 0.3, 0.2, 0.3, 0);
        }
    }

    // Clears every effect, like drinking milk
    public static void cleanse(SpellCast cast, LivingEntity target)
    {
        target.removeAllEffects();
    }

    // ---- movement ----

    // Speed, and the same for whatever it's riding (a horse, a strider...)
    public static void speedWithMount(SpellCast cast, Entity target)
    {
        EffectHelper.give(cast, target, MobEffects.SPEED);
        if (target.getVehicle() instanceof LivingEntity mount) EffectHelper.give(cast, mount, MobEffects.SPEED);
    }

    public static void speedSelfWithMount(SpellCast cast, LivingEntity target)
    {
        speedWithMount(cast, target);
    }

    // Pushes it away from the caster (any direction if there's no caster), harder with potency
    public static void pushAway(SpellCast cast, Entity target)
    {
        Vec3 away = cast.caster() == null ? new Vec3(cast.level().getRandom().nextDouble() - 0.5, 0, cast.level().getRandom().nextDouble() - 0.5)
                : target.position().subtract(cast.caster().position());
        Vec3 flat = new Vec3(away.x, 0, away.z);
        if (flat.lengthSqr() < 1.0E-4) flat = new Vec3(1, 0, 0);
        Vec3 push = flat.normalize().scale(0.8 * cast.stats().potency()).add(0, 0.35, 0);
        target.push(push.x, push.y, push.z);
        // A player's own client moves them; needsSync tells the server to send them the new velocity (26.x's name for
        // what used to be hurtMarked)
        target.needsSync = true;
    }

    // ---- growing up ----

    // A baby grows up, an adult becomes a baby (animals, villagers, zombies)
    public static void toggleAge(SpellCast cast, Entity target)
    {
        if (target instanceof AgeableMob ageable) {
            ageable.setAge(ageable.isBaby() ? 0 : AgeableMob.BABY_START_AGE);
        } else if (target instanceof Zombie zombie) {
            zombie.setBaby(!zombie.isBaby());
        } else {
            return;
        }
        cast.level().sendParticles(ParticleTypes.HAPPY_VILLAGER, target.getX(), target.getY() + 0.5, target.getZ(), 8, 0.3, 0.3, 0.3, 0);
    }

    // ---- blocks ----

    // Blows the block apart (it drops nothing)
    public static void explodeBlock(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.getDestroySpeed(level, pos) < 0) return;
        level.destroyBlock(pos, false, cast.caster());
        level.sendParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, 0, 0, 0, 0);
        level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.6F, 1.2F);
    }

    // The sound a note block would make standing on this block (its instrument), at a random note
    public static void noteSound(SpellCast cast, BlockPos pos, Direction face)
    {
        ServerLevel level = cast.level();
        Holder<SoundEvent> sound = level.getBlockState(pos).instrument().getSoundEvent();
        int note = level.getRandom().nextInt(25);
        // A note block's pitch for note 0-24: two octaves from F#
        float pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
        level.playSound(null, pos.above(), sound.value(), SoundSource.RECORDS, 3.0F, pitch);
        level.sendParticles(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 0, note / 24.0, 0, 0, 1);
    }

    // ---- sounds ----

    // A random creature's ambient sound, from the target
    public static void mobSound(SpellCast cast, Entity target)
    {
        List<SoundEvent> ambient = BuiltInRegistries.SOUND_EVENT.stream()
                .filter(sound -> sound.location().getPath().startsWith("entity.") && sound.location().getPath().endsWith(".ambient"))
                .toList();
        if (ambient.isEmpty()) return;
        SoundEvent sound = ambient.get(cast.level().getRandom().nextInt(ambient.size()));
        cast.level().playSound(null, target.getX(), target.getY(), target.getZ(), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }
}
