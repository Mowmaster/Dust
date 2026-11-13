package com.mowmaster.dust.Features.Spells.radiance;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// Radiance's self and entity parts (its block part spreads grass, mycelium, nylium... through the conversion table),
// and the player relations behind them:
//   Self    every effect the caster has is shared with the friendly creatures around them: players who aren't
//           hostile to them, and mobs that aren't monsters or after the caster
//   Entity  every effect the target has is shared with the mobs around it
//   Hostile players who hurt each other are both marked Hostile for 5 minutes (milk and cleansing can't remove it),
//           and each sees a red arrow pointing at the other (Client/RelationArrows)
//   Friendly  befriend(): the friend gets the Friendly effect, and the caster sees a green arrow pointing at them.
//           How it's given in play comes later; for now /dustmagic friend
@EventBusSubscriber(modid = DustReferences.MODID)
public final class Radiance
{
    public static final int HOSTILE_TICKS = 20 * 60 * 5;
    // Reach: SELF_RADIUS / MOB_RADIUS blocks, plus 2 a level of size
    public static final double SELF_RADIUS = 6;
    public static final double MOB_RADIUS = 4;

    private Radiance() {}

    // Effects that aren't shared: marks about who someone is, and soul states tied to one body.
    // Holder.is(holder) compares registry keys. Not equals: a DeferredHolder and the registry's own holder for the same
    // effect are different objects, and neither's equals treats them as the same.
    private static boolean shareable(Holder<MobEffect> effect)
    {
        return !(effect.is(DustEffectsRegistry.HOSTILE) || effect.is(DustEffectsRegistry.FRIENDLY)
                || effect.is(DustEffectsRegistry.SOUL_SEVERED) || effect.is(DustEffectsRegistry.GHOSTLY));
    }

    // ---- self ----

    public static void shareSelf(SpellCast cast, LivingEntity caster)
    {
        ServerLevel level = cast.level();
        long now = level.getGameTime();
        RadianceRelations relations = caster instanceof Player ? caster.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS) : RadianceRelations.NONE;
        Predicate<LivingEntity> friendly = other -> {
            if (other == caster || !other.isAlive()) return false;
            if (other instanceof Player player) return !player.isSpectator() && !relations.isHostile(player.getUUID(), now);
            if (other instanceof Enemy) return false;
            return !(other instanceof Mob mob && mob.getTarget() == caster);
        };
        share(level, caster, SELF_RADIUS + cast.stats().size() * 2, friendly);
    }

    // ---- entity ----

    public static void shareMob(SpellCast cast, Entity target)
    {
        if (!(target instanceof LivingEntity living)) return;
        share(cast.level(), living, MOB_RADIUS + cast.stats().size() * 2, other -> other != living && other.isAlive() && !(other instanceof Player));
    }

    // Gives copies of source's effects (same level, time left) to everything around it that passes the test
    private static void share(ServerLevel level, LivingEntity source, double radius, Predicate<LivingEntity> to)
    {
        List<MobEffectInstance> effects = new ArrayList<>();
        for (MobEffectInstance effect : source.getActiveEffects()) {
            if (shareable(effect.getEffect())) effects.add(effect);
        }
        if (effects.isEmpty()) return;
        level.sendParticles(ParticleTypes.END_ROD, source.getX(), source.getY() + source.getBbHeight() / 2, source.getZ(), 20, radius / 3, 0.5, radius / 3, 0.02);
        level.playSound(null, source.blockPosition(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.6F, 1.6F);
        for (LivingEntity other : level.getEntitiesOfClass(LivingEntity.class, source.getBoundingBox().inflate(radius), to)) {
            // new MobEffectInstance(effect): a copy, so each creature's runs down on its own
            for (MobEffectInstance effect : effects) other.addEffect(new MobEffectInstance(effect), source);
            level.sendParticles(ParticleTypes.END_ROD, other.getX(), other.getY() + other.getBbHeight() / 2, other.getZ(), 4, 0.3, 0.4, 0.3, 0.01);
        }
    }

    // ---- friendly ----

    // Marks friend as the caster's friend for ticks
    public static void befriend(ServerPlayer caster, ServerPlayer friend, int ticks)
    {
        long now = caster.level().getGameTime();
        caster.setData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS,
                caster.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS).withFriend(friend.getUUID(), now + ticks, now));
        friend.addEffect(new MobEffectInstance(DustEffectsRegistry.FRIENDLY, ticks, 0), caster);
    }

    // ---- hostile ----

    // A player hurt by another player (directly, or by their arrow, spell...): both are marked hostile to each other
    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event)
    {
        if (!(event.getEntity() instanceof ServerPlayer victim) || !(event.getSource().getEntity() instanceof ServerPlayer attacker)) return;
        if (victim == attacker || event.getInflictedDamage() <= 0) return;
        markHostile(victim, attacker);
        markHostile(attacker, victim);
    }

    private static void markHostile(ServerPlayer player, ServerPlayer other)
    {
        long now = player.level().getGameTime();
        player.setData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS,
                player.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS).withHostile(other.getUUID(), now + HOSTILE_TICKS, now));
        player.addEffect(new MobEffectInstance(DustEffectsRegistry.HOSTILE, HOSTILE_TICKS, 0));
    }

    // Milk, cleansing and anything else that removes effects can't take Hostile away while the fight is fresh. Removal
    // goes through this cancellable event; running out (Expired) doesn't, so it still ends on time.
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event)
    {
        if (!event.getEffect().is(DustEffectsRegistry.HOSTILE) || !(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isAlive() && player.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS).hostileUntil() > player.level().getGameTime()) {
            event.setCanceled(true);
        }
    }

    // Dying clears effects, but not who you fought (the relations are kept on death): Hostile comes back on respawn
    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        long left = player.getData(DustAttachmentTypeRegistry.RADIANCE_RELATIONS).hostileUntil() - player.level().getGameTime();
        if (left > 0) player.addEffect(new MobEffectInstance(DustEffectsRegistry.HOSTILE, (int) left, 0));
    }
}
