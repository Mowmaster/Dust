package com.mowmaster.dust.Features.Spells.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// The numbers a cast works with, after the spell's attributes (and modifiers) have changed them from the defaults
// (SpellConfig):
//   duration          ticks an effect lasts (5 seconds by default; instant effects ignore it)
//   potency           how strong: an effect's level is amplifier() (potency 1 = level I, 2 = level II...)
//   size              how far around the target it reaches, in blocks (0 = just the target block / entity)
//   range             how far a projectile flies before it dissipates, and how far Touch reaches past normal reach
//   speed             how fast a projectile flies (blocks per tick)
//   gravity           how much a projectile drops each tick
//   bonusDamage       damage added on top of potency's (the Damage modifier), in health points
//   damageMultiplier  everything the damage comes to is multiplied by this (2 on a critical hit)
//   critChance        the chance (0-1) a cast is a critical hit (double damage), rolled as it's cast
//   luck              added to the chance (0-1) of an effect's chance-based results
//   cooldown          ticks added to the delay before the next cast (negative: sooner; Cast Speed)
//   manaPercent       change to the mana it costs, in percent (-5: 5% less)
//   xpPercent         change to the experience a cast gives its tree, in percent
//   falloff           how far a projectile flies before gravity starts pulling it down (blocks; Falloff)
//   impactDamage      damage a projectile does on hitting a creature, on top of its effect (Impact Damage)
// A record: immutable; the with... methods give a changed copy (attributes and modifiers chain them).
public record SpellStats(int duration, float potency, int size, double range, float speed, float gravity,
                         float bonusDamage, float damageMultiplier, float critChance, float luck, int cooldown, int manaPercent, int xpPercent,
                         double falloff, float impactDamage)
{
    public static SpellStats defaults()
    {
        return new SpellStats(SpellConfig.BASE_DURATION, 1.0F, 0, SpellConfig.BASE_RANGE, SpellConfig.BASE_SPEED, SpellConfig.BASE_GRAVITY,
                0F, 1F, 0F, 0F, 0, 0, 0, 0.0, 0F);
    }

    // Saved with a spell projectile in flight. optionalFieldOf(name, default): missing in older saves -> the default.
    public static final Codec<SpellStats> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.optionalFieldOf("duration", SpellConfig.BASE_DURATION).forGetter(SpellStats::duration),
            Codec.FLOAT.optionalFieldOf("potency", 1F).forGetter(SpellStats::potency),
            Codec.INT.optionalFieldOf("size", 0).forGetter(SpellStats::size),
            Codec.DOUBLE.optionalFieldOf("range", SpellConfig.BASE_RANGE).forGetter(SpellStats::range),
            Codec.FLOAT.optionalFieldOf("speed", SpellConfig.BASE_SPEED).forGetter(SpellStats::speed),
            Codec.FLOAT.optionalFieldOf("gravity", SpellConfig.BASE_GRAVITY).forGetter(SpellStats::gravity),
            Codec.FLOAT.optionalFieldOf("bonus_damage", 0F).forGetter(SpellStats::bonusDamage),
            Codec.FLOAT.optionalFieldOf("damage_multiplier", 1F).forGetter(SpellStats::damageMultiplier),
            Codec.FLOAT.optionalFieldOf("crit_chance", 0F).forGetter(SpellStats::critChance),
            Codec.FLOAT.optionalFieldOf("luck", 0F).forGetter(SpellStats::luck),
            Codec.INT.optionalFieldOf("cooldown", 0).forGetter(SpellStats::cooldown),
            Codec.INT.optionalFieldOf("mana_percent", 0).forGetter(SpellStats::manaPercent),
            Codec.INT.optionalFieldOf("xp_percent", 0).forGetter(SpellStats::xpPercent),
            Codec.DOUBLE.optionalFieldOf("falloff", 0.0).forGetter(SpellStats::falloff),
            Codec.FLOAT.optionalFieldOf("impact_damage", 0F).forGetter(SpellStats::impactDamage)
    ).apply(i, SpellStats::new));

    // A mob effect's amplifier for this potency (0 = level I)
    public int amplifier()
    {
        return Math.max(0, Math.round(potency) - 1);
    }

    // Damage for this potency, in health points (2 = one heart), with any bonus damage and multiplier
    public float damage()
    {
        return (SpellConfig.BASE_DAMAGE * potency + bonusDamage) * damageMultiplier;
    }

    public SpellStats withDuration(int duration) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withPotency(float potency) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withSize(int size) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withRange(double range) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withSpeed(float speed) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withGravity(float gravity) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withBonusDamage(float bonusDamage) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withDamageMultiplier(float damageMultiplier) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withCritChance(float critChance) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withLuck(float luck) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withCooldown(int cooldown) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withManaPercent(int manaPercent) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withFalloff(double falloff) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withImpactDamage(float impactDamage) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
    public SpellStats withXpPercent(int xpPercent) { return new SpellStats(duration, potency, size, range, speed, gravity, bonusDamage, damageMultiplier, critChance, luck, cooldown, manaPercent, xpPercent, falloff, impactDamage); }
}
