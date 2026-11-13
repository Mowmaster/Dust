package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

// Life and pets: a player's pet that dies doesn't die. It becomes a ghost for 5 minutes: it floats, nothing can hurt it
// (and it can't hurt anything, or be targeted), and it keeps following its owner. Life cast on the ghost brings it
// back at full health; if nobody does in time, it dies for good (dropping what it carried, as a normal death).
// The pet stays the same entity the whole time, so its name, collar, saddle and inventory are all still there. The
// ghost's deadline is the PET_GHOST attachment (the game time it runs out), saved with the pet.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class PetGhosts
{
    public static final int GHOST_TICKS = 20 * 60 * 5;

    private PetGhosts() {}

    public static boolean isGhost(Entity entity)
    {
        return entity.hasData(DustAttachmentTypeRegistry.PET_GHOST);
    }

    // Life on a creature: a pet's ghost comes back to life; otherwise an undead creature is cured (MobParts.cure)
    public static void reviveOrCure(SpellCast cast, Entity target)
    {
        if (target instanceof LivingEntity pet && isGhost(pet)) revive(cast.level(), pet);
        else MobParts.cure(cast, target);
    }

    private static void revive(ServerLevel level, LivingEntity pet)
    {
        pet.removeData(DustAttachmentTypeRegistry.PET_GHOST);
        pet.setNoGravity(false);
        pet.setHealth(pet.getMaxHealth());
        level.sendParticles(ParticleTypes.HEART, pet.getX(), pet.getY() + pet.getBbHeight(), pet.getZ(), 6, 0.4, 0.3, 0.4, 0);
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pet.getX(), pet.getY() + pet.getBbHeight() / 2, pet.getZ(), 15, 0.4, 0.5, 0.4, 0);
        level.playSound(null, pet.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 0.8F, 1.4F);
    }

    // A player's pet: something ownable, with an owner that's a player (or an owner who isn't loaded right now: only
    // players own pets in vanilla, and an offline owner can't be looked at)
    private static boolean isPlayersPet(LivingEntity entity)
    {
        if (!(entity instanceof OwnableEntity ownable) || ownable.getOwnerReference() == null) return false;
        LivingEntity owner = ownable.getOwner();
        return owner == null || owner instanceof Player;
    }

    // Dying: cancelling the death event stops the death (no drops, no death message), and the pet becomes a ghost.
    // HIGH: before other mods react to the death. A ghost that dies (its time ran out) isn't stopped.
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDeath(LivingDeathEvent event)
    {
        LivingEntity pet = event.getEntity();
        if (!(pet.level() instanceof ServerLevel level) || isGhost(pet) || !isPlayersPet(pet)) return;
        if (event.getSource().is(DamageTypes.GENERIC_KILL)) return; // /kill still kills
        event.setCanceled(true);
        pet.setHealth(pet.getMaxHealth());
        pet.removeAllEffects();
        pet.clearFire();
        pet.setNoGravity(true);
        pet.setDeltaMovement(new Vec3(0, 0.15, 0));
        if (pet instanceof Mob mob) mob.setTarget(null);
        pet.setData(DustAttachmentTypeRegistry.PET_GHOST, level.getGameTime() + GHOST_TICKS);
        level.sendParticles(ParticleTypes.SOUL, pet.getX(), pet.getY() + pet.getBbHeight() / 2, pet.getZ(), 20, 0.4, 0.5, 0.4, 0.03);
        level.playSound(null, pet.blockPosition(), SoundEvents.SOUL_ESCAPE.value(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        if (((OwnableEntity) pet).getOwner() instanceof Player owner) {
            owner.sendOverlayMessage(Component.translatable("dust.pet_ghost.ghosted", pet.getDisplayName()));
        }
    }

    // Ghosts: drifting, glowing faintly with souls, harmless; dying for good when their time is up
    @SubscribeEvent
    public static void onTick(EntityTickEvent.Post event)
    {
        if (!(event.getEntity() instanceof LivingEntity pet) || !(pet.level() instanceof ServerLevel level)) return;
        Long until = pet.getExistingDataOrNull(DustAttachmentTypeRegistry.PET_GHOST);
        if (until == null) return;
        if (level.getGameTime() >= until) {
            level.sendParticles(ParticleTypes.SOUL, pet.getX(), pet.getY() + pet.getBbHeight() / 2, pet.getZ(), 25, 0.4, 0.5, 0.4, 0.05);
            // kill() deals generic kill damage, which a ghost doesn't stop, and onDeath leaves ghosts alone
            pet.kill(level);
            return;
        }
        if (pet instanceof Mob mob && mob.getTarget() != null) mob.setTarget(null);
        pet.clearFire();
        pet.setAirSupply(pet.getMaxAirSupply());
        // A gentle bob, and slowly rising off the ground if it's sitting on it
        Vec3 motion = pet.getDeltaMovement();
        double lift = (pet.onGround() ? 0.04 : 0) + Math.sin(pet.tickCount * 0.1) * 0.005;
        pet.setDeltaMovement(motion.x, motion.y * 0.8 + lift, motion.z);
        if (pet.tickCount % 10 == 0) {
            level.sendParticles(ParticleTypes.SOUL, pet.getRandomX(0.6), pet.getRandomY(), pet.getRandomZ(0.6), 1, 0, 0.02, 0, 0.01);
        }
    }

    // Nothing hurts a ghost (except /kill and its time running out), and a ghost hurts nothing
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event)
    {
        boolean ghostHit = isGhost(event.getEntity()) && !event.getSource().is(DamageTypes.GENERIC_KILL);
        boolean ghostHitting = event.getSource().getEntity() != null && isGhost(event.getSource().getEntity());
        if (ghostHit || ghostHitting) event.setCanceled(true);
    }

    // Nothing hunts a ghost
    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event)
    {
        if (event.getNewAboutToBeSetTarget() != null && isGhost(event.getNewAboutToBeSetTarget())) event.setCanceled(true);
    }
}
