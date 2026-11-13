package com.mowmaster.dust.Features.Spells.mobeffects;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.DustDamageTypes;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

import java.util.Set;

// What the spell mob effects that aren't just attribute changes or ticks do, hooked into the game's events:
//   Calmed        won't pick a target
//   Stone Skin    physical damage cut       Iron Skin    projectiles stopped     Magic Ward   magic damage cut
//   Marked        double damage taken       Pierced      more from projectiles   Conductive   more from lightning
//   Pack Tactics  double damage to what something else is fighting
//   Deathless     no withering, drowning or starving (and no Wither / Hunger effects)
//   Life Leech    heals from damage dealt   Energy Drain mana from hits          Thorns       hurts attackers
//   Reflecting    projectiles bounce back
//   Fungal / Deathless / Adaptive   effects that can't take hold, or only at half strength
//   Silenced      no shields, drinking or teleporting (no spells: SpellCasting)
//   Insight       more experience           Enlightened  drops more experience   Sure Footing mines at full speed in the air
@EventBusSubscriber(modid = DustReferences.MODID)
public final class SpellMobEffects
{
    private SpellMobEffects() {}

    private static int level(LivingEntity entity, Holder<MobEffect> effect)
    {
        MobEffectInstance instance = entity.getEffect(effect);
        return instance == null ? 0 : instance.getAmplifier() + 1;
    }

    // LivingChangeTargetEvent: fired when a mob picks a new target; cancelling it keeps the old (none, once calmed)
    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event)
    {
        if (event.getNewAboutToBeSetTarget() != null && event.getEntity().hasEffect(DustEffectsRegistry.CALMED)) event.setCanceled(true);
    }

    // ---- damage taken (before armour): cut, raised or stopped ----

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event)
    {
        LivingEntity victim = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getAmount();

        if (victim.hasEffect(DustEffectsRegistry.DEATHLESS)
                && (source.is(DamageTypeTags.IS_DROWNING) || source.is(DamageTypes.WITHER) || source.is(DamageTypes.STARVE))) {
            event.setCanceled(true);
            return;
        }
        if (victim.hasEffect(DustEffectsRegistry.IRON_SKIN) && source.is(DamageTypeTags.IS_PROJECTILE)
                && !(source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0)) {
            event.setCanceled(true);
            return;
        }
        int stone = level(victim, DustEffectsRegistry.STONE_SKIN);
        if (stone > 0 && (!source.is(DamageTypeTags.BYPASSES_ARMOR) || source.is(DustDamageTypes.KINETIC_KEY))) {
            amount *= 1 - Math.min(0.8F, 0.2F * stone);
        }
        int ward = level(victim, DustEffectsRegistry.MAGIC_WARD);
        if (ward > 0 && (source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC))) amount *= 1 - Math.min(0.8F, 0.25F * ward);
        if (victim.hasEffect(DustEffectsRegistry.MARKED)) amount *= 2;
        int pierced = level(victim, DustEffectsRegistry.PIERCED);
        if (pierced > 0 && source.is(DamageTypeTags.IS_PROJECTILE)) amount *= 1 + 0.5F * pierced;
        int conductive = level(victim, DustEffectsRegistry.CONDUCTIVE);
        if (conductive > 0 && source.is(DamageTypeTags.IS_LIGHTNING)) amount *= 1 + 0.5F * conductive;
        // Pack tactics: something other than the attacker hurt the victim in the last 5 seconds
        if (source.getEntity() instanceof LivingEntity attacker && attacker.hasEffect(DustEffectsRegistry.PACK_TACTICS)) {
            LivingEntity other = victim.getLastHurtByMob();
            if (other != null && other != attacker && victim.tickCount - victim.getLastHurtByMobTimestamp() < 100) amount *= 2;
        }
        event.setAmount(amount);
    }

    // ---- damage dealt (after armour): healing, mana, thorns ----

    @SubscribeEvent
    public static void onDamageDealt(LivingDamageEvent.Post event)
    {
        LivingEntity victim = event.getEntity();
        DamageSource source = event.getSource();
        float dealt = event.getHealthDamage();
        if (dealt <= 0) return;
        if (source.getEntity() instanceof LivingEntity attacker) {
            int leech = level(attacker, DustEffectsRegistry.LIFE_LEECH);
            if (leech > 0) attacker.heal(dealt * Math.min(1F, 0.2F * leech));
            if (attacker instanceof ServerPlayer player && attacker.hasEffect(DustEffectsRegistry.ENERGY_DRAIN)) PlayerMagicHelper.addMana(player, 1);
            int thorns = level(victim, DustEffectsRegistry.THORNS);
            // Not thorns from thorns (two thorny things would hurt each other forever)
            if (thorns > 0 && !source.is(DamageTypes.THORNS) && victim.level() instanceof ServerLevel level) {
                attacker.hurtServer(level, level.damageSources().thorns(victim), thorns);
            }
        }
    }

    // ---- projectiles bouncing off ----

    // Reflecting: the projectile turns round (slower), and now belongs to whoever it bounced off
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event)
    {
        if (!(event.getRayTraceResult() instanceof EntityHitResult hit) || !(hit.getEntity() instanceof LivingEntity target)
                || !target.hasEffect(DustEffectsRegistry.REFLECTING)) {
            return;
        }
        Projectile projectile = event.getProjectile();
        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-0.6));
        projectile.setOwner(target);
        event.setCanceled(true);
    }

    // ---- effects that can't take hold ----

    private static final Set<Holder<MobEffect>> FUNGAL_IMMUNE = Set.of(MobEffects.POISON, MobEffects.MINING_FATIGUE, MobEffects.SLOWNESS);
    private static final Set<Holder<MobEffect>> DEATHLESS_IMMUNE = Set.of(MobEffects.WITHER, MobEffects.HUNGER);
    // Set while Adaptive adds its weakened copy, so that copy isn't weakened again
    private static final ThreadLocal<Boolean> ADAPTING = ThreadLocal.withInitial(() -> false);

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event)
    {
        LivingEntity entity = event.getEntity();
        MobEffectInstance incoming = event.getEffectInstance();
        Holder<MobEffect> effect = incoming.getEffect();
        if ((entity.hasEffect(DustEffectsRegistry.FUNGAL) && FUNGAL_IMMUNE.contains(effect))
                || (entity.hasEffect(DustEffectsRegistry.DEATHLESS) && DEATHLESS_IMMUNE.contains(effect))
                || (entity.hasEffect(DustEffectsRegistry.ADAPTIVE) && effect.equals(DustEffectsRegistry.FRACTURED))) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            return;
        }
        // Adaptive: a harmful effect takes hold at half strength (half its level, or half as long at level I)
        if (entity.hasEffect(DustEffectsRegistry.ADAPTIVE) && effect.value().getCategory() == MobEffectCategory.HARMFUL && !ADAPTING.get()
                && !incoming.isInfiniteDuration()) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            int amplifier = incoming.getAmplifier() / 2;
            int duration = incoming.getAmplifier() == 0 ? Math.max(1, incoming.getDuration() / 2) : incoming.getDuration();
            ADAPTING.set(true);
            try {
                entity.addEffect(new MobEffectInstance(effect, duration, amplifier, incoming.isAmbient(), incoming.isVisible(), incoming.showIcon()),
                        event.getEffectSource());
            } finally {
                ADAPTING.set(false);
            }
        }
    }

    // ---- Silenced: no shields, no drinking, no teleporting ----

    @SubscribeEvent
    public static void onStartUsingItem(LivingEntityUseItemEvent.Start event)
    {
        if (!event.getEntity().hasEffect(DustEffectsRegistry.SILENCED)) return;
        ItemStack stack = event.getItem();
        if (stack.has(DataComponents.BLOCKS_ATTACKS) || stack.getUseAnimation() == ItemUseAnimation.DRINK) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEnderTeleport(EntityTeleportEvent.EnderEntity event)
    {
        if (event.getEntityLiving().hasEffect(DustEffectsRegistry.SILENCED)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPearlTeleport(EntityTeleportEvent.EnderPearl event)
    {
        if (event.getPlayer().hasEffect(DustEffectsRegistry.SILENCED)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onItemTeleport(EntityTeleportEvent.ItemConsumption event)
    {
        if (event.getEntityLiving().hasEffect(DustEffectsRegistry.SILENCED)) event.setCanceled(true);
    }

    public static boolean isSilenced(Entity entity)
    {
        return entity instanceof LivingEntity living && living.hasEffect(DustEffectsRegistry.SILENCED);
    }

    // ---- experience ----

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event)
    {
        int insight = level(event.getEntity(), DustEffectsRegistry.INSIGHT);
        if (insight > 0 && event.getAmount() > 0) event.setAmount(Math.round(event.getAmount() * (1 + 0.5F * insight)));
    }

    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event)
    {
        int enlightened = level(event.getEntity(), DustEffectsRegistry.ENLIGHTENED);
        if (enlightened > 0) event.setDroppedExperience(Math.round(event.getDroppedExperience() * (1 + 0.5F * enlightened)));
    }

    // ---- Sure Footing: mining off the ground at full speed (water and slowing ground are its attribute modifiers) ----

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (event.getEntity().hasEffect(DustEffectsRegistry.SURE_FOOTING) && !event.getEntity().onGround()) event.setNewSpeed(event.getNewSpeed() * 5);
    }
}
