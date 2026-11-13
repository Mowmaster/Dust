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

    // LivingChangeTargetEvent: fired when a mob picks a new target; cancelling it keeps the old (none, once calmed).
    // A thrall won't pick its owner, or anything its owner owns.
    @SubscribeEvent
    public static void onChangeTarget(LivingChangeTargetEvent event)
    {
        LivingEntity mob = event.getEntity();
        LivingEntity target = event.getNewAboutToBeSetTarget();
        if (target == null) return;
        if (mob.hasEffect(DustEffectsRegistry.CALMED)) event.setCanceled(true);
        else if (mob.hasEffect(DustEffectsRegistry.ENTHRALLED) && isOwnersSide(mob, target)) event.setCanceled(true);
    }

    // ---- Vacuum and thralls (their ticks) ----

    // Vacuum: everything loose near the entity drifts in towards it
    public static void vacuumTick(ServerLevel level, LivingEntity entity, int amplifier)
    {
        double radius = 5 + amplifier;
        net.minecraft.world.phys.Vec3 centre = entity.position().add(0, entity.getBbHeight() / 2, 0);
        for (Entity loose : level.getEntities(entity, entity.getBoundingBox().inflate(radius),
                other -> other instanceof net.minecraft.world.entity.item.ItemEntity || other instanceof net.minecraft.world.entity.ExperienceOrb
                        || (other instanceof LivingEntity && !(other instanceof net.minecraft.world.entity.player.Player)))) {
            net.minecraft.world.phys.Vec3 pull = centre.subtract(loose.position());
            if (pull.lengthSqr() < 1) continue;
            net.minecraft.world.phys.Vec3 step = pull.normalize().scale(0.08);
            loose.push(step.x, step.y, step.z);
            loose.needsSync = true;
        }
    }

    // A thrall without a proper target looks for the nearest monster within 10 blocks that isn't on its owner's side
    public static void thrallTick(ServerLevel level, LivingEntity entity, int amplifier)
    {
        if (!(entity instanceof net.minecraft.world.entity.Mob mob)) return;
        LivingEntity current = mob.getTarget();
        if (current != null && current.isAlive() && !isOwnersSide(mob, current)) return;
        LivingEntity prey = level.getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(10),
                        other -> other != mob && other.isAlive() && other instanceof net.minecraft.world.entity.monster.Enemy && !isOwnersSide(mob, other))
                .stream().min(java.util.Comparator.comparingDouble(mob::distanceToSqr)).orElse(null);
        mob.setTarget(prey);
    }

    // Whether target is the thrall's owner, or a pet of theirs, or another of their thralls
    private static boolean isOwnersSide(LivingEntity thrall, LivingEntity target)
    {
        String owner = thrall.getPersistentData().getStringOr(com.mowmaster.dust.Features.Spells.effects.MobParts.THRALL_OWNER, "");
        if (owner.isEmpty()) return false;
        if (target.getStringUUID().equals(owner)) return true;
        if (target instanceof net.minecraft.world.entity.TamableAnimal pet && pet.getOwnerReference() != null
                && pet.getOwnerReference().getUUID().toString().equals(owner)) {
            return true;
        }
        return target.hasEffect(DustEffectsRegistry.ENTHRALLED)
                && owner.equals(target.getPersistentData().getStringOr(com.mowmaster.dust.Features.Spells.effects.MobParts.THRALL_OWNER, ""));
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
        int brittle = level(victim, DustEffectsRegistry.BRITTLE);
        if (brittle > 0 && source.is(DustDamageTypes.KINETIC_KEY)) amount *= 1 + 0.5F * brittle;
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
                || (entity.hasEffect(DustEffectsRegistry.ADAPTIVE) && effect.is(DustEffectsRegistry.FRACTURED))) {
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

    // ---- breaking blocks: Sure Footing mines off the ground at full speed (water and slowing ground are its attribute
    // modifiers); a block marked by a Damage spell breaks 10% faster ----

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        float speed = event.getNewSpeed();
        if (event.getEntity().hasEffect(DustEffectsRegistry.SURE_FOOTING) && !event.getEntity().onGround()) speed *= 5;
        if (event.getPosition().isPresent()
                && com.mowmaster.dust.Features.Spells.effects.BlockMarks.has(event.getEntity().level(), event.getPosition().get(),
                com.mowmaster.dust.Features.Spells.effects.BlockMarks.QUICK_BREAK)) {
            speed *= 1.1F;
        }
        event.setNewSpeed(speed);
    }

    // A block marked by a Knowledge spell gives 10% more experience (at least 1 more, if it gives any)
    @SubscribeEvent
    public static void onBlockDrops(net.neoforged.neoforge.event.level.BlockDropsEvent event)
    {
        if (event.getDroppedExperience() <= 0
                || !com.mowmaster.dust.Features.Spells.effects.BlockMarks.has(event.getLevel(), event.getPos(), com.mowmaster.dust.Features.Spells.effects.BlockMarks.WISE)) {
            return;
        }
        event.setDroppedExperience(Math.max(event.getDroppedExperience() + 1, Math.round(event.getDroppedExperience() * 1.1F)));
    }

    // ---- Butchered: twice the meat and hides ----

    @SubscribeEvent
    public static void onLivingDrops(net.neoforged.neoforge.event.entity.living.LivingDropsEvent event)
    {
        if (!event.getEntity().hasEffect(DustEffectsRegistry.BUTCHERED)) return;
        java.util.List<net.minecraft.world.entity.item.ItemEntity> extra = new java.util.ArrayList<>();
        for (net.minecraft.world.entity.item.ItemEntity drop : event.getDrops()) {
            ItemStack stack = drop.getItem();
            if (stack.has(DataComponents.FOOD) || stack.is(net.minecraft.world.item.Items.LEATHER) || stack.is(net.minecraft.world.item.Items.RABBIT_HIDE)
                    || stack.is(net.minecraft.world.item.Items.FEATHER) || stack.is(net.minecraft.world.item.Items.ROTTEN_FLESH)) {
                extra.add(new net.minecraft.world.entity.item.ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ(), stack.copy()));
            }
        }
        event.getDrops().addAll(extra);
    }

    // ---- Encased: breaking out ----

    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event)
    {
        MobEffectInstance instance = event.getEffectInstance();
        if (instance != null && instance.getEffect().is(DustEffectsRegistry.ENCASED) && event.getEntity().level() instanceof ServerLevel level) {
            com.mowmaster.dust.Features.Spells.effects.MobParts.breakOut(level, event.getEntity(), instance.getAmplifier());
        }
    }
}
