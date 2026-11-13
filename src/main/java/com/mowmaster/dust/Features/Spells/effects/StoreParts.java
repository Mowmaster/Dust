package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

// Self effects about the player's stores of mana and dust:
//   Crystal  Crystallized for duration x 36 (3 minutes): mana and dust hold half again as much a level
//            (PlayerMagicHelper.manaCap / dustCap). Anything over the normal cap when it ends stays, but can't be topped up.
//   Seeds    takes 10% of your max mana (or what you have, if less) and stores it as a Seed of Life (its level is the
//            amount). Recasting adds to it. Drop under 30% health and it bursts, once: Instant Health II, and
//            Regeneration III for 2 seconds a stored mana. If it's never needed, the mana comes back when it ends.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class StoreParts
{
    public static final int CRYSTAL_TIME_SCALE = 36;
    // How long a seed waits: duration x 120 (10 minutes at the default)
    public static final int SEED_TIME_SCALE = 120;
    public static final float SEED_SHARE = 0.10F;
    public static final float BURST_HEALTH = 0.30F;
    public static final int REGEN_TICKS_PER_MANA = 40;
    // The most a seed can hold (an effect's level only goes so high)
    private static final int MAX_SEED = 255;

    private StoreParts() {}

    // ---- Crystal ----

    public static void crystallize(SpellCast cast, LivingEntity target)
    {
        target.addEffect(new MobEffectInstance(DustEffectsRegistry.CRYSTALLIZED, cast.stats().duration() * CRYSTAL_TIME_SCALE, cast.stats().amplifier()), cast.caster());
        if (target.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.END_ROD, target.getX(), target.getY() + 1, target.getZ(), 15, 0.4, 0.6, 0.4, 0.02);
        }
    }

    // ---- Seeds ----

    public static void plantSeed(SpellCast cast, LivingEntity target)
    {
        if (!(target instanceof ServerPlayer player)) return;
        int share = Math.max(1, (int) Math.ceil(PlayerMagicHelper.maxMana(player) * SEED_SHARE));
        int taken = Math.min(share, PlayerMagicHelper.mana(player));
        if (taken <= 0) {
            player.sendOverlayMessage(Component.translatable("dust.seed.no_mana"));
            return;
        }
        PlayerMagicHelper.setMana(player, PlayerMagicHelper.mana(player) - taken);
        MobEffectInstance seed = player.getEffect(DustEffectsRegistry.SEED_OF_LIFE);
        int stored = Math.min(MAX_SEED, (seed == null ? 0 : seed.getAmplifier() + 1) + taken);
        // Removed first (a lower level wouldn't replace it, and the new one should have the full wait again), quietly:
        // its mana is carried into the new seed, not handed back
        quiet = true;
        player.removeEffect(DustEffectsRegistry.SEED_OF_LIFE);
        quiet = false;
        player.addEffect(new MobEffectInstance(DustEffectsRegistry.SEED_OF_LIFE, cast.stats().duration() * SEED_TIME_SCALE, stored - 1));
        player.level().sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1, player.getZ(), 10, 0.4, 0.5, 0.4, 0);
    }

    // Hurt below 30%: the seed bursts
    @SubscribeEvent
    public static void onDamaged(LivingDamageEvent.Post event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player) || !player.isAlive()) return;
        MobEffectInstance seed = player.getEffect(DustEffectsRegistry.SEED_OF_LIFE);
        if (seed == null || player.getHealth() >= player.getMaxHealth() * BURST_HEALTH) return;
        int stored = seed.getAmplifier() + 1;
        // Removed quietly: the mana went into the burst, so it isn't handed back
        quiet = true;
        player.removeEffect(DustEffectsRegistry.SEED_OF_LIFE);
        quiet = false;
        player.addEffect(new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 1));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, stored * REGEN_TICKS_PER_MANA, 2));
        player.level().sendParticles(ParticleTypes.HEART, player.getX(), player.getY() + 1.5, player.getZ(), 8, 0.5, 0.5, 0.5, 0);
        player.level().playSound(null, player.blockPosition(), SoundEvents.BIG_DRIPLEAF_TILT_UP, SoundSource.PLAYERS, 1.0F, 1.4F);
    }

    // Set while Dust itself removes a seed (bursting, or replacing it with a bigger one), so the mana isn't handed back.
    // The server runs one thing at a time, so a plain flag is enough.
    private static boolean quiet;

    // A seed that runs out (or is cleared by milk or a cleanse) gives its mana back, up to the cap
    @SubscribeEvent
    public static void onSeedExpired(MobEffectEvent.Expired event)
    {
        seedGone(event.getEntity(), event.getEffectInstance());
    }

    @SubscribeEvent
    public static void onSeedRemoved(MobEffectEvent.Remove event)
    {
        if (!quiet) seedGone(event.getEntity(), event.getEffectInstance());
    }

    private static void seedGone(LivingEntity entity, MobEffectInstance seed)
    {
        if (seed == null || !seed.getEffect().is(DustEffectsRegistry.SEED_OF_LIFE) || !(entity instanceof ServerPlayer player)) return;
        PlayerMagicHelper.addMana(player, seed.getAmplifier() + 1);
    }
}
