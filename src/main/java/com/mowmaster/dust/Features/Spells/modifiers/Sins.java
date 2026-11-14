package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// The corrupted modifiers: the seven sins. Each gives something and takes something. One use each; the next pylon
// placed takes one from the deck, whatever its kind (SpellPylonBlockEntity).
public final class Sins
{
    // Pride: each caster's marked target (in memory: a restart forgets it)
    private static final Map<UUID, UUID> MARKED = new HashMap<>();
    // Lust: how far around the target mobs are stirred up, and for how long
    private static final double LUST_RADIUS = 5;
    private static final int LUST_TICKS = 20 * 10;

    private Sins() {}

    // Pride: the first creature hit is marked (one at a time, until it dies or is gone): double damage on it, a quarter
    // on anything else
    public static HookModifier pride()
    {
        return new HookModifier(ModifierTier.CORRUPTED).against((cast, target, stats) -> {
            if (cast.caster() == null) return stats;
            UUID mark = MARKED.get(cast.caster().getUUID());
            Entity marked = mark == null ? null : cast.level().getEntity(mark);
            if (marked == null || !marked.isAlive()) {
                MARKED.put(cast.caster().getUUID(), target.getUUID());
                marked = target;
            }
            if (marked == target) {
                if (target instanceof LivingEntity living) living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 10, 0, false, false));
                return stats.withDamageMultiplier(stats.damageMultiplier() * 2);
            }
            return stats.withDamageMultiplier(stats.damageMultiplier() * 0.25F);
        });
    }

    // Greed: four times the damage, but the spell no longer spares the caster or their friends and allies (SpellCast)
    public static HookModifier greed()
    {
        return new HookModifier(ModifierTier.CORRUPTED).stats((spell, stats) -> stats.withDamageMultiplier(stats.damageMultiplier() * 4));
    }

    public static boolean greedy(Spell spell)
    {
        return spell.modifiers().contains(SpellRegistries.GREED.getId());
    }

    // Wrath: double damage, triple the dust and mana
    public static HookModifier wrath()
    {
        return new HookModifier(ModifierTier.CORRUPTED).stats((spell, stats) -> stats.withDamageMultiplier(stats.damageMultiplier() * 2)).costChange(200);
    }

    // Lust: the creature hit gets Speed V, and the mobs around it turn on it, with Speed III
    public static HookModifier lust()
    {
        return new HookModifier(ModifierTier.CORRUPTED).onImpact((cast, at, target) -> {
            if (!(target instanceof LivingEntity victim)) return;
            victim.addEffect(new MobEffectInstance(MobEffects.SPEED, LUST_TICKS, 4));
            for (Mob mob : cast.level().getEntitiesOfClass(Mob.class, victim.getBoundingBox().inflate(LUST_RADIUS), mob -> mob != victim && mob.isAlive())) {
                mob.setTarget(victim);
                mob.addEffect(new MobEffectInstance(MobEffects.SPEED, LUST_TICKS, 2));
            }
        });
    }

    // Envy: a player hit (one whose magic is awakened) loses twice the spell's mana, and the caster gains it, but takes
    // half a heart for it
    public static HookModifier envy()
    {
        return new HookModifier(ModifierTier.CORRUPTED).onImpact((cast, at, target) -> {
            if (!(target instanceof ServerPlayer victim) || !PlayerMagicHelper.isAwakened(victim)) return;
            int mana = 2 * PlayerMagicHelper.manaForDust(cast.spell().dustCost().values().stream().mapToInt(Integer::intValue).sum());
            int taken = Math.min(mana, PlayerMagicHelper.mana(victim));
            PlayerMagicHelper.setMana(victim, PlayerMagicHelper.mana(victim) - taken);
            if (cast.caster() instanceof ServerPlayer caster) {
                PlayerMagicHelper.addMana(caster, taken);
                caster.hurtServer(cast.level(), cast.level().damageSources().magic(), 1.0F);
            }
        });
    }

    // Gluttony: double the AOE size, range, Beam distance and Piercing depth (SpellCast, BeamRay, SpellProjectileEntity),
    // but four times the dust and mana
    public static HookModifier gluttony()
    {
        return new HookModifier(ModifierTier.CORRUPTED).stats((spell, stats) -> stats.withRange(stats.range() * 2)).costChange(300);
    }

    public static boolean gluttonous(Spell spell)
    {
        return spell.modifiers().contains(SpellRegistries.GLUTTONY.getId());
    }

    // Sloth: the bolt flies half as fast with twice the falloff, but the caster waits only half as long to cast again
    // (SpellScrollItem.cooldownFor)
    public static HookModifier sloth()
    {
        return new HookModifier(ModifierTier.CORRUPTED).stats((spell, stats) -> stats.withSpeed(stats.speed() * 0.5F).withFalloff(stats.falloff() * 2));
    }

    public static boolean slothful(Spell spell)
    {
        return spell.modifiers().contains(SpellRegistries.SLOTH.getId());
    }
}
