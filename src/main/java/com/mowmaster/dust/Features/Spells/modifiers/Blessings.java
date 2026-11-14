package com.mowmaster.dust.Features.Spells.modifiers;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.ModifierTier;
import com.mowmaster.dust.Features.Spells.api.Spell;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.radiance.Social;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

import java.util.Set;

// The blessed modifiers: the twelve signs. Each doubles the potency (and bonus damage) of spells of its own elements;
// on a spell of any other element it does something else instead (a buff, a trick). One use each: once one has been
// drawn it's gone from the tree's pool. Only a Neutral pylon can take one (SpellPylonBlockEntity).
public final class Blessings
{
    // How long the caster's buffs last (ticks)
    private static final int BUFF_TICKS = 20 * 10;
    private static final int SWIM_TICKS = 20 * 30;
    private static final int REBUKE_TICKS = 20 * 60;
    // How far around where it landed items are gathered (Taurus) / pets come from (Leo)
    private static final double GATHER_RADIUS = 3;
    private static final double PET_RADIUS = 16;

    private Blessings() {}

    // A sign of these elements: double potency for them (the base of its damage and the level of its effects), and
    // double the bonus damage
    private static HookModifier sign(DustElement... elements)
    {
        Set<DustElement> ours = Set.of(elements);
        return new HookModifier(ModifierTier.BLESSED).stats((spell, stats) -> ours.contains(spell.element())
                ? stats.withPotency(stats.potency() * 2).withBonusDamage(stats.bonusDamage() * 2) : stats);
    }

    // Whether the cast is of one of these elements (the sign's own: its alternative doesn't apply)
    private static boolean ours(SpellCast cast, DustElement... elements)
    {
        return Set.of(elements).contains(cast.element());
    }

    private static void buff(SpellCast cast, Holder<MobEffect> effect, int ticks, int amplifier)
    {
        if (cast.caster() != null) cast.caster().addEffect(new MobEffectInstance(effect, ticks, amplifier, false, true, true));
    }

    // Aries: fire, energy. Otherwise, a projectile flies twice as fast
    public static HookModifier aries()
    {
        HookModifier sign = sign(DustElements.FIRE, DustElements.ENERGY);
        return sign.stats((spell, stats) -> spell.element() == DustElements.FIRE || spell.element() == DustElements.ENERGY
                ? stats.withPotency(stats.potency() * 2).withBonusDamage(stats.bonusDamage() * 2)
                : spell.shape().equals(SpellRegistries.PROJECTILE.getId()) ? stats.withSpeed(stats.speed() * 2) : stats);
    }

    // Taurus: protection, armor. Otherwise, the items lying around where it lands come to the caster
    public static HookModifier taurus()
    {
        return sign(DustElements.PROTECTION, DustElements.ARMOR).onImpact((cast, at, target) -> {
            if (ours(cast, DustElements.PROTECTION, DustElements.ARMOR) || cast.caster() == null) return;
            for (ItemEntity item : cast.level().getEntitiesOfClass(ItemEntity.class, new AABB(at, at).inflate(GATHER_RADIUS))) {
                item.teleportTo(cast.caster().getX(), cast.caster().getY(), cast.caster().getZ());
                item.setNoPickUpDelay();
            }
        });
    }

    // Gemini: air, motion, flight. Otherwise, the caster gets Speed II
    public static HookModifier gemini()
    {
        return sign(DustElements.AIR, DustElements.MOTION, DustElements.FLIGHT).onCast(cast -> {
            if (!ours(cast, DustElements.AIR, DustElements.MOTION, DustElements.FLIGHT)) buff(cast, MobEffects.SPEED, BUFF_TICKS, 1);
        });
    }

    // Cancer: water, life, healing. Otherwise, the caster gets Regeneration I for 5 seconds
    public static HookModifier cancer()
    {
        return sign(DustElements.WATER, DustElements.LIFE, DustElements.HEALING).onCast(cast -> {
            if (!ours(cast, DustElements.WATER, DustElements.LIFE, DustElements.HEALING)) buff(cast, MobEffects.REGENERATION, 20 * 5, 0);
        });
    }

    // Leo: stone, gem, crystal. Otherwise, the caster's pets, and their friends' and allies' pets, go after the creature hit
    public static HookModifier leo()
    {
        return sign(DustElements.STONE, DustElements.GEM, DustElements.CRYSTAL).onImpact((cast, at, target) -> {
            if (ours(cast, DustElements.STONE, DustElements.GEM, DustElements.CRYSTAL) || cast.caster() == null || !(target instanceof LivingEntity victim)) return;
            var server = cast.level().getServer();
            for (Mob pet : cast.level().getEntitiesOfClass(Mob.class, new AABB(at, at).inflate(PET_RADIUS), mob -> mob instanceof OwnableEntity)) {
                var owner = ((OwnableEntity) pet).getOwnerReference();
                if (owner == null || pet == victim) continue;
                boolean side = owner.getUUID().equals(cast.caster().getUUID()) || Social.areProtected(server, owner.getUUID(), cast.caster().getUUID());
                if (side && !owner.getUUID().equals(victim.getUUID())) pet.setTarget(victim);
            }
        });
    }

    // Virgo: mud (and earth's dirt). Otherwise, a projectile homes in on the nearest creature (SpellProjectileEntity)
    public static HookModifier virgo()
    {
        return sign(DustElements.MUD);
    }

    // Whether a spell's bolt homes in (Virgo on a spell of another element)
    public static boolean homes(Spell spell)
    {
        return spell.modifiers().contains(SpellRegistries.VIRGO.getId()) && spell.element() != DustElements.MUD;
    }

    // Libra: warp, sound. Otherwise, 10% more luck on the spell's chances
    public static HookModifier libra()
    {
        return new HookModifier(ModifierTier.BLESSED).stats((spell, stats) -> spell.element() == DustElements.WARP || spell.element() == DustElements.SOUND
                ? stats.withPotency(stats.potency() * 2).withBonusDamage(stats.bonusDamage() * 2) : stats.withLuck(stats.luck() + 0.1F));
    }

    // Scorpio: magic, knowledge. Otherwise, the caster gets Strength II
    public static HookModifier scorpio()
    {
        return sign(DustElements.MAGIC, DustElements.KNOWLEDGE).onCast(cast -> {
            if (!ours(cast, DustElements.MAGIC, DustElements.KNOWLEDGE)) buff(cast, MobEffects.STRENGTH, BUFF_TICKS, 1);
        });
    }

    // Sagittarius: earth, seeds, plants, trees. Otherwise, the caster steps up full blocks for a while (Step Assist)
    public static HookModifier sagittarius()
    {
        return sign(DustElements.EARTH, DustElements.SEEDS, DustElements.PLANTS, DustElements.TREES).onCast(cast -> {
            if (!ours(cast, DustElements.EARTH, DustElements.SEEDS, DustElements.PLANTS, DustElements.TREES)) buff(cast, DustEffectsRegistry.STEP_ASSIST, BUFF_TICKS, 0);
        });
    }

    // Capricorn: light, darkness, radiance. Otherwise, the next attack on the caster is turned back on the attacker (Rebuke)
    public static HookModifier capricorn()
    {
        return sign(DustElements.LIGHT, DustElements.DARKNESS, DustElements.RADIANCE).onCast(cast -> {
            if (!ours(cast, DustElements.LIGHT, DustElements.DARKNESS, DustElements.RADIANCE)) buff(cast, DustEffectsRegistry.REBUKE, REBUKE_TICKS, 0);
        });
    }

    // Aquarius: lightning, slime. Otherwise, the caster swims fast and breathes underwater
    public static HookModifier aquarius()
    {
        return sign(DustElements.LIGHTNING, DustElements.SLIME).onCast(cast -> {
            if (ours(cast, DustElements.LIGHTNING, DustElements.SLIME)) return;
            buff(cast, MobEffects.DOLPHINS_GRACE, SWIM_TICKS, 0);
            buff(cast, MobEffects.WATER_BREATHING, SWIM_TICKS, 0);
        });
    }

    // Pisces: vacuum, void. Otherwise, the creature hit is confused (Nausea)
    public static HookModifier pisces()
    {
        return sign(DustElements.VACUUM, DustElements.VOID).onImpact((cast, at, target) -> {
            if (!ours(cast, DustElements.VACUUM, DustElements.VOID) && target instanceof LivingEntity victim) {
                victim.addEffect(new MobEffectInstance(MobEffects.NAUSEA, BUFF_TICKS, 0), cast.caster());
            }
        });
    }
}
