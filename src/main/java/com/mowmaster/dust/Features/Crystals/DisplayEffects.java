package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.EffectType;
import com.mowmaster.dust.Features.Spells.api.ElementEffect;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.api.SpellTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

// What a Crystal Display gives off for its element (its element's effect: SpellRegistries.ELEMENT_EFFECTS), the first
// that works:
//   SELF     the effect it has on a caster, if it lingers (EffectType.DOT): given to every creature in the area that
//            isn't hostile (players, animals, villagers...), as if each had cast it on themselves
//   ENTITY   else the effect it has on a creature it hits, if that lingers: given to the hostile creatures in the area
//   BLOCK    else its effect on a block, if it has one: on one block of the area at a time
//   INERT    none of those: the crystal does nothing, and shows grey
// A spell set on the display (a self spell from a scroll or grimoire) always gives its self effect.
public final class DisplayEffects
{
    public enum Mode { SELF, ENTITY, BLOCK, INERT }

    private static final Map<Class<?>, Map<String, Boolean>> OVERRIDES = new HashMap<>();

    private DisplayEffects() {}

    public static ElementEffect effectOf(DustElement element)
    {
        ElementEffect effect = SpellRegistries.ELEMENT_EFFECT_REGISTRY.getValue(element.id());
        return effect == null ? ElementEffect.NONE : effect;
    }

    public static Mode mode(DustElement element)
    {
        ElementEffect effect = effectOf(element);
        if (effect.type(SpellTarget.SELF) == EffectType.DOT && overrides(effect, "onSelf", LivingEntity.class)) return Mode.SELF;
        if (effect.type(SpellTarget.ENTITY) == EffectType.DOT && overrides(effect, "onEntity", Entity.class)) return Mode.ENTITY;
        if (overrides(effect, "onBlock", BlockPos.class, Direction.class)) return Mode.BLOCK;
        return Mode.INERT;
    }

    // Whether the effect does anything for this kind of target: its class has its own version of the method (the
    // interface's default does nothing)
    private static boolean overrides(ElementEffect effect, String method, Class<?>... after)
    {
        Class<?> type = effect.getClass();
        return OVERRIDES.computeIfAbsent(type, key -> new HashMap<>()).computeIfAbsent(method, name -> {
            Class<?>[] params = new Class<?>[after.length + 1];
            params[0] = SpellCast.class;
            System.arraycopy(after, 0, params, 1, after.length);
            try {
                return type.getMethod(name, params).getDeclaringClass() != ElementEffect.class;
            } catch (NoSuchMethodException e) {
                return false;
            }
        });
    }
}
