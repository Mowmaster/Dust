package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Magic.MagicConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

// Fruit affinities: one mob effect for each elemental fruit's element (dust:affinity_<element>, "Fire Affinity"...).
// While one runs, the player has an affinity with that element on top of any attunement they have (PlayerMagicHelper:
// more dust from orbs, cheaper and stronger spells of it). How strong it is goes by the attunement tiers
// (MagicConfig.ATTUNEMENT) for how many fruit affinities run at once:
//   one     50% more dust from orbs, spells 50% cheaper, 25% stronger
//   two     20%, 20%, 10%
//   three   10%, 10%, no power bonus
// The two stack by multiplying (attuned to Fire and eating an Ember Plum: dust x1.5 x1.5, cost x0.5 x0.5).
// An elemental fruit gives its element's affinity for BASE_TICKS (5 minutes), a Fruit Medley up to three (BASE_TICKS for
// each fruit of that kind in it). Either one replaces any fruit affinities already running: only one fruit's (or one
// medley's) at a time.
public final class FruitAffinity
{
    public static final int BASE_TICKS = 6000;
    public static final Map<DustElement, Holder<MobEffect>> EFFECTS;

    static {
        Map<DustElement, Holder<MobEffect>> effects = new LinkedHashMap<>();
        for (ElementalWood wood : ElementalWood.values()) {
            DustElement element = wood.element();
            effects.put(element, DustEffectsRegistry.MOB_EFFECTS.register("affinity_" + element.id().getPath(),
                    () -> new Effect(element)));
        }
        EFFECTS = Collections.unmodifiableMap(effects);
    }

    private FruitAffinity() {}

    // Loads the class (and so registers the effects) from the mod constructor (Dust)
    public static void init() {}

    // A fruit affinity: does nothing by itself; PlayerMagicHelper checks for it
    public static class Effect extends MobEffect
    {
        private final DustElement element;

        public Effect(DustElement element)
        {
            super(MobEffectCategory.BENEFICIAL, element.color());
            this.element = element;
        }

        public DustElement element()
        {
            return element;
        }
    }

    // Takes away every fruit affinity the entity has
    public static void clear(LivingEntity entity)
    {
        for (Holder<MobEffect> effect : EFFECTS.values()) entity.removeEffect(effect);
    }

    // Replaces the entity's fruit affinities with these: each element for BASE_TICKS times its count
    public static void give(LivingEntity entity, Map<DustElement, Integer> counts)
    {
        clear(entity);
        counts.forEach((element, count) -> {
            Holder<MobEffect> effect = EFFECTS.get(element);
            if (effect != null && count > 0) entity.addEffect(new MobEffectInstance(effect, BASE_TICKS * count, 0));
        });
    }

    // How many fruit affinities the entity has running
    public static int count(LivingEntity entity)
    {
        int count = 0;
        for (Holder<MobEffect> effect : EFFECTS.values()) if (entity.hasEffect(effect)) count++;
        return count;
    }

    // [gain bonus, cost reduction, power bonus] the entity's fruit affinities give this element (all zero without one)
    public static float[] bonus(LivingEntity entity, DustElement element)
    {
        Holder<MobEffect> effect = EFFECTS.get(element);
        if (effect == null || !entity.hasEffect(effect)) return MagicConfig.ATTUNEMENT[0];
        return tier(count(entity));
    }

    // The bonuses for this many affinities at once
    public static float[] tier(int affinities)
    {
        return MagicConfig.ATTUNEMENT[Math.max(0, Math.min(affinities, MagicConfig.ATTUNEMENT.length - 1))];
    }
}
