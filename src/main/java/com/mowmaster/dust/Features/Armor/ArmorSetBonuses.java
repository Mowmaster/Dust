package com.mowmaster.dust.Features.Armor;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.function.ToIntFunction;

// What wearing the elemental armor sets does (ElementalArmorSet lists the bonuses). Counted from the four armour slots;
// players are checked every TICK_EVERY ticks, bosses call tick() themselves. Attribute bonuses are transient modifiers
// (dust:set_<set>_<pieces>), effects are refreshed short ones (hidden particles); damage, deaths and spell hits have their
// own hooks below, and the spell cost, power, mana and free-cast bonuses are read by PlayerMagicHelper and SpellCasting.
@EventBusSubscriber(modid = DustReferences.MODID)
public final class ArmorSetBonuses
{
    private static final int TICK_EVERY = 10;
    // Long enough that night vision never drops to where it flickers (200 ticks) between refreshes
    private static final int EFFECT_TICKS = 400;
    private static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    // How many pieces of a set the local player wears (for tooltips; set by the client: Client/ElementalArmorClient)
    public static ToIntFunction<ElementalArmorSet> localCount = set -> 0;

    private record Bonus(ElementalArmorSet set, int pieces, Holder<Attribute> attribute, double amount, AttributeModifier.Operation operation)
    {
        Identifier id()
        {
            return Identifier.fromNamespaceAndPath(DustReferences.MODID, "set_" + set.id + "_" + pieces + "_" + attribute.unwrapKey().map(k -> k.identifier().getPath()).orElse("x"));
        }
    }

    private static final List<Bonus> ATTRIBUTES = List.of(
            new Bonus(ElementalArmorSet.ROOTBOUND, 2, Attributes.KNOCKBACK_RESISTANCE, 0.5, AttributeModifier.Operation.ADD_VALUE),
            new Bonus(ElementalArmorSet.ROOTBOUND, 2, Attributes.ARMOR_TOUGHNESS, 2, AttributeModifier.Operation.ADD_VALUE),
            new Bonus(ElementalArmorSet.BASTION, 2, Attributes.ARMOR, 4, AttributeModifier.Operation.ADD_VALUE),
            new Bonus(ElementalArmorSet.GILDED, 2, Attributes.ARMOR_TOUGHNESS, 2, AttributeModifier.Operation.ADD_VALUE),
            new Bonus(ElementalArmorSet.GILDED, 2, Attributes.ATTACK_DAMAGE, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new Bonus(ElementalArmorSet.SPARKWRIGHT, 2, Attributes.MOVEMENT_SPEED, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
            new Bonus(ElementalArmorSet.ZEPHYR, 4, Attributes.MOVEMENT_SPEED, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    private ArmorSetBonuses() {}

    // ---- counting ----

    public static int count(LivingEntity entity, ElementalArmorSet set)
    {
        int count = 0;
        for (EquipmentSlot slot : SLOTS) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.getItem() instanceof ElementalArmorItem piece && piece.set() == set) count++;
        }
        return count;
    }

    public static boolean has(LivingEntity entity, ElementalArmorSet set, int pieces)
    {
        return count(entity, set) >= pieces;
    }

    // ---- attributes and effects ----

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event)
    {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % TICK_EVERY == 0) tick(player);
    }

    public static void tick(LivingEntity entity)
    {
        for (Bonus bonus : ATTRIBUTES) {
            AttributeInstance attribute = entity.getAttribute(bonus.attribute());
            if (attribute == null) continue;
            boolean on = has(entity, bonus.set(), bonus.pieces());
            Identifier id = bonus.id();
            if (on && !attribute.hasModifier(id)) attribute.addTransientModifier(new AttributeModifier(id, bonus.amount(), bonus.operation()));
            else if (!on && attribute.hasModifier(id)) attribute.removeModifier(id);
        }
        float half = entity.getMaxHealth() / 2;
        if (has(entity, ElementalArmorSet.PYREBORN, 2)) give(entity, MobEffects.FIRE_RESISTANCE, 0);
        if (has(entity, ElementalArmorSet.TIDEWOVEN, 2)) {
            give(entity, MobEffects.WATER_BREATHING, 0);
            if (entity.isInWater()) give(entity, MobEffects.DOLPHINS_GRACE, 0);
        }
        if (has(entity, ElementalArmorSet.TIDEWOVEN, 4) && entity.isInWaterOrRain() && entity.getHealth() < half) give(entity, MobEffects.REGENERATION, 1);
        if (has(entity, ElementalArmorSet.DEEPWATCH, 2)) {
            give(entity, MobEffects.NIGHT_VISION, 0);
            give(entity, MobEffects.WATER_BREATHING, 0);
        }
        if (has(entity, ElementalArmorSet.RIMEFROST, 2)) {
            entity.removeEffect(MobEffects.SLOWNESS);
            entity.setTicksFrozen(0);
        }
        if (has(entity, ElementalArmorSet.LUMEN, 4)) {
            entity.removeEffect(MobEffects.WITHER);
            entity.removeEffect(MobEffects.POISON);
        }
        if (has(entity, ElementalArmorSet.MIREWALKER, 4)) {
            var below = entity.level().getBlockState(entity.blockPosition().below());
            if (below.is(BlockTags.DIRT) || below.is(BlockTags.SAND)) give(entity, MobEffects.REGENERATION, 0);
        }
        if (has(entity, ElementalArmorSet.VERDANT, 2) && entity.getHealth() < half) give(entity, MobEffects.REGENERATION, 0);
        if (has(entity, ElementalArmorSet.ZEPHYR, 4)) give(entity, MobEffects.JUMP_BOOST, 1);
        // Rootbound 4: a shell of absorption every 30 seconds, when there's none
        if (has(entity, ElementalArmorSet.ROOTBOUND, 4) && entity.tickCount % 600 < TICK_EVERY && entity.getAbsorptionAmount() <= 0) {
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 1, true, false, true));
        }
    }

    private static void give(LivingEntity entity, Holder<MobEffect> effect, int amplifier)
    {
        MobEffectInstance current = entity.getEffect(effect);
        if (current != null && current.getAmplifier() > amplifier) return;
        if (current != null && current.getDuration() > EFFECT_TICKS - TICK_EVERY * 4 && current.getAmplifier() == amplifier) return;
        entity.addEffect(new MobEffectInstance(effect, EFFECT_TICKS, amplifier, true, false, true));
    }

    // ---- damage ----

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event)
    {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();
        if (has(target, ElementalArmorSet.PYREBORN, 2) && source.is(DamageTypeTags.IS_FIRE)) {
            event.setCanceled(true);
            return;
        }
        if (has(target, ElementalArmorSet.ZEPHYR, 2) && source.is(DamageTypeTags.IS_FALL)) {
            event.setCanceled(true);
            return;
        }
        if (has(target, ElementalArmorSet.BLASTWARD, 2) && source.is(DamageTypeTags.IS_EXPLOSION)) event.setAmount(event.getAmount() * 0.4F);
        if (has(target, ElementalArmorSet.BASTION, 4) && target.isShiftKeyDown()) event.setAmount(event.getAmount() * 0.6F);
        // Struck in melee: the attacker's own hand (not an arrow or a spell)
        Entity direct = source.getDirectEntity();
        if (direct instanceof LivingEntity attacker && direct == source.getEntity() && attacker != target && !source.is(DamageTypeTags.IS_PROJECTILE)
                && SpellCast.landing == null) {
            if (has(target, ElementalArmorSet.PYREBORN, 4)) attacker.igniteForSeconds(5);
            if (has(target, ElementalArmorSet.RIMEFROST, 4)) attacker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 1));
            if (has(target, ElementalArmorSet.MIREWALKER, 2)) attacker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 0));
            if (has(target, ElementalArmorSet.GILDED, 4) && target.level() instanceof ServerLevel level && !source.is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)) {
                attacker.hurtServer(level, target.damageSources().thorns(target), event.getAmount() * 0.25F);
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event)
    {
        if (!(event.getSource().getEntity() instanceof LivingEntity killer) || killer == event.getEntity()) return;
        if (!(killer.level() instanceof ServerLevel level)) return;
        if (has(killer, ElementalArmorSet.VERDANT, 4)) killer.heal(2);
        // Blastward 4: a spell kill bursts, hurting what's round it (never the killer, never blocks)
        if (has(killer, ElementalArmorSet.BLASTWARD, 4) && SpellCast.landing != null) {
            LivingEntity victim = event.getEntity();
            level.sendParticles(ParticleTypes.EXPLOSION, victim.getX(), victim.getY() + 0.5, victim.getZ(), 1, 0, 0, 0, 0);
            level.playSound(null, victim.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.8F, 1.3F);
            for (LivingEntity near : level.getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(3))) {
                if (near == killer || near == victim || near.isAlliedTo(killer)) continue;
                near.hurtServer(level, killer.damageSources().explosion(killer, killer), 4);
            }
        }
    }

    // A spell landing on an entity (SpellCast.entityEffect)
    public static void onSpellHit(SpellCast cast, Entity target)
    {
        LivingEntity caster = cast.caster();
        if (caster == null || !(target instanceof LivingEntity living) || living == caster) return;
        if (has(caster, ElementalArmorSet.RIFTBORN, 4)) living.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
    }

    // ---- spells ----

    // How much stronger the caster's spells of this element are for the sets they wear
    public static float powerFactor(LivingEntity caster, DustElement element)
    {
        float factor = 1;
        for (ElementalArmorSet set : ElementalArmorSet.values()) {
            int pieces = count(caster, set);
            if (pieces < 2 || set.element() != element) continue;
            factor *= 1 + set.twoPower + (pieces >= 4 ? set.fourPower : 0);
        }
        if (has(caster, ElementalArmorSet.DEEPWATCH, 4) && caster.level().getMaxLocalRawBrightness(caster.blockPosition()) <= 7) factor *= 1.2F;
        return factor;
    }

    // What share of a spell's dust of this element the caster still pays for the sets they wear
    public static float costFactor(LivingEntity caster, DustElement element)
    {
        float factor = 1;
        for (ElementalArmorSet set : ElementalArmorSet.values()) {
            if (set.fourCost > 0 && set.element() == element && has(caster, set, 4)) factor *= 1 - set.fourCost;
        }
        if (has(caster, ElementalArmorSet.LUMEN, 4)) factor *= 0.85F;
        return factor;
    }

    // Riftborn 2: one cast in ten costs nothing
    public static boolean freeCast(Player player)
    {
        return has(player, ElementalArmorSet.RIFTBORN, 2) && player.getRandom().nextInt(10) == 0;
    }

    // Extra steps of mana regeneration this tick: Lumen 2 (half again as fast), Sparkwright 4 (twice as fast on the move)
    public static int regenBonus(Player player)
    {
        int steps = 0;
        if (has(player, ElementalArmorSet.LUMEN, 2) && player.tickCount % 2 == 0) steps++;
        if (has(player, ElementalArmorSet.SPARKWRIGHT, 4) && player.getDeltaMovement().horizontalDistanceSqr() > 0.0025) steps++;
        return steps;
    }
}
