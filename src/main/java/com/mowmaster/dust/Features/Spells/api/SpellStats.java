package com.mowmaster.dust.Features.Spells.api;

// The numbers a cast works with, after the spell's attributes (and modifiers) have changed them from the defaults
// (SpellConfig):
//   duration      ticks an effect lasts (5 seconds by default; instant effects ignore it)
//   potency       how strong: damage is SpellConfig.BASE_DAMAGE x potency (1 = one heart), and an effect's level is
//                 amplifier() (potency 1 = level I, 2 = level II...)
//   size          how far around the target it reaches, in blocks (0 = just the target block / entity)
//   range         how far a projectile flies before it dissipates, and how far Touch reaches past normal reach
//   speed         how fast a projectile flies (blocks per tick)
//   gravity       how much a projectile drops each tick
// A record: immutable; the with... methods give a changed copy (attributes and modifiers chain them).
public record SpellStats(int duration, float potency, int size, double range, float speed, float gravity)
{
    public static SpellStats defaults()
    {
        return new SpellStats(SpellConfig.BASE_DURATION, 1.0F, 0, SpellConfig.BASE_RANGE, SpellConfig.BASE_SPEED, SpellConfig.BASE_GRAVITY);
    }

    // A mob effect's amplifier for this potency (0 = level I)
    public int amplifier()
    {
        return Math.max(0, Math.round(potency) - 1);
    }

    // Damage for this potency, in health points (2 = one heart)
    public float damage()
    {
        return SpellConfig.BASE_DAMAGE * potency;
    }

    public SpellStats withDuration(int duration) { return new SpellStats(duration, potency, size, range, speed, gravity); }
    public SpellStats withPotency(float potency) { return new SpellStats(duration, potency, size, range, speed, gravity); }
    public SpellStats withSize(int size) { return new SpellStats(duration, potency, size, range, speed, gravity); }
    public SpellStats withRange(double range) { return new SpellStats(duration, potency, size, range, speed, gravity); }
    public SpellStats withSpeed(float speed) { return new SpellStats(duration, potency, size, range, speed, gravity); }
    public SpellStats withGravity(float gravity) { return new SpellStats(duration, potency, size, range, speed, gravity); }
}
