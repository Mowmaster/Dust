package com.mowmaster.dust.Features.Spells.mobeffects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.jspecify.annotations.Nullable;

// The movement effects that need more than a mixin:
//   Soaring, Spirit Form   the player may fly while they last (and Spirit Form starts them flying); when they end, a
//                          player who isn't in creative or spectator comes down
//   Ghostly                a mob's AI stops and it can't be hurt; when it ends, it carries on as before
@EventBusSubscriber(modid = DustReferences.MODID)
public final class MovementEffects
{
    private static final String GHOST_HAD_NO_AI = "dust_ghost_had_no_ai";

    private MovementEffects() {}

    // Every half second while Soaring / in Spirit Form: make sure the player may fly
    public static void keepFlight(ServerLevel level, LivingEntity entity, int amplifier)
    {
        if (!(entity instanceof ServerPlayer player)) return;
        boolean spirit = player.hasEffect(DustEffectsRegistry.SPIRIT_FORM);
        if (!player.getAbilities().mayfly || (spirit && !player.getAbilities().flying)) {
            player.getAbilities().mayfly = true;
            if (spirit) player.getAbilities().flying = true;
            // Sends the abilities to the player's client
            player.onUpdateAbilities();
        }
    }

    private static boolean grantsFlight(@Nullable Holder<MobEffect> effect)
    {
        return effect != null && (effect.is(DustEffectsRegistry.SOARING) || effect.is(DustEffectsRegistry.SPIRIT_FORM));
    }

    // Flight ends with the effect (unless something else still gives it)
    private static void effectGone(LivingEntity entity, @Nullable Holder<MobEffect> effect)
    {
        if (grantsFlight(effect) && entity instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator()) {
            boolean other = effect.is(DustEffectsRegistry.SOARING) ? player.hasEffect(DustEffectsRegistry.SPIRIT_FORM) : player.hasEffect(DustEffectsRegistry.SOARING);
            if (!other) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
        if (effect != null && effect.is(DustEffectsRegistry.GHOSTLY) && entity instanceof Mob mob) {
            mob.setNoAi(mob.getPersistentData().getBooleanOr(GHOST_HAD_NO_AI, false));
            mob.getPersistentData().remove(GHOST_HAD_NO_AI);
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event)
    {
        MobEffectInstance added = event.getEffectInstance();
        if (added == null) return;
        if (added.getEffect().is(DustEffectsRegistry.GHOSTLY) && event.getEntity() instanceof Mob mob && !mob.getPersistentData().contains(GHOST_HAD_NO_AI)) {
            mob.getPersistentData().putBoolean(GHOST_HAD_NO_AI, mob.isNoAi());
            mob.setNoAi(true);
            if (mob.level() instanceof ServerLevel level) {
                level.sendParticles(ParticleTypes.SOUL, mob.getX(), mob.getY() + mob.getBbHeight() / 2, mob.getZ(), 12, 0.3, 0.4, 0.3, 0.02);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event)
    {
        effectGone(event.getEntity(), event.getEffectInstance() == null ? null : event.getEffectInstance().getEffect());
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event)
    {
        effectGone(event.getEntity(), event.getEffect());
    }

    // A ghost can't be hurt
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event)
    {
        if (event.getEntity().hasEffect(DustEffectsRegistry.GHOSTLY)) event.setCanceled(true);
    }
}
