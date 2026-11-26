package com.mowmaster.dust.Features.Bosses;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.EffectScrolls.DustEffects.DustDamageTypes;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Spells.api.SpellCast;
import com.mowmaster.dust.Features.Spells.shapes.SpellProjectileEntity;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// How much an element boss (ArchonEntity) takes from a hit, by the element the hit came from:
//   its own element                      nothing (immune)
//   its complementary element            double
//   an element made partly from its own  less, by how much of it is its element (Energy, half Fire: half damage to
//                                        the Fire Archon; Light, two thirds Fire: a third)
//   anything else (a sword, an arrow)    the usual
// A hit's element (elementOf): the spell landing (SpellCast.landing), a spell bolt's own element, or for a few kinds of
// damage the element they plainly are (burning: Fire, drowning: Water, freezing: Ice, explosions: Explosion,
// lightning: Lightning).
// The complementary element (complement): the one whose colour is the opposite of its own (each channel turned
// around; the nearest element's colour to that). Fire and Life, Water and Metal, Earth and Air, Order and Chaos;
// Energy's is Death, Explosion's Healing, Ice's Damage, Abyss's Tool, Stone's Vacuum, Mud's Motion.
public final class BossDamage
{
    private static final Map<DustElement, DustElement> COMPLEMENTS = new ConcurrentHashMap<>();

    private BossDamage() {}

    // The multiplier for a hit of this element (null: no element) on a boss of that element
    public static float factor(DustElement boss, @Nullable DustElement from)
    {
        if (from == null) return 1F;
        if (from.equals(boss)) return 0F;
        if (from.equals(complement(boss))) return 2F;
        return 1F - share(from, boss);
    }

    public static DustElement complement(DustElement element)
    {
        return COMPLEMENTS.computeIfAbsent(element, BossDamage::findComplement);
    }

    private static DustElement findComplement(DustElement element)
    {
        int opposite = 0xFFFFFF ^ element.color();
        DustElement best = element;
        long bestDistance = Long.MAX_VALUE;
        for (DustElement other : DustElements.all()) {
            if (other.equals(element)) continue;
            long distance = distance(opposite, other.color());
            if (distance < bestDistance) {
                best = other;
                bestDistance = distance;
            }
        }
        return best;
    }

    // Squared distance between two 0xRRGGBB colours
    private static long distance(int a, int b)
    {
        long red = ((a >> 16) & 0xFF) - ((b >> 16) & 0xFF), green = ((a >> 8) & 0xFF) - ((b >> 8) & 0xFF), blue = (a & 0xFF) - (b & 0xFF);
        return red * red + green * green + blue * blue;
    }

    // How much of element is made from part (0-1), by weight of primal dust: 1 for itself, 0 if part isn't in what
    // it's mixed from at all. Light (Fire + Air, Air = Fire + Water) is 2/3 Fire and 2/3 Air.
    public static float share(DustElement element, DustElement part)
    {
        if (element.equals(part)) return 1F;
        if (element.isPrimal()) return 0F;
        int total = total(element);
        if (total <= 0) return 0F;
        float share = 0F;
        for (DustElement made : element.parts()) share += (float) total(made) / total * share(made, part);
        return Math.min(1F, share);
    }

    private static int total(DustElement element)
    {
        int total = 0;
        for (EnumElement primal : EnumElement.values()) total += element.primal(primal);
        return total;
    }

    // The element a hit came from, or null
    public static @Nullable DustElement elementOf(DamageSource source)
    {
        SpellCast landing = SpellCast.landing;
        if (landing != null) return landing.element();
        if (source.getDirectEntity() instanceof SpellProjectileEntity bolt) return bolt.element();
        if (source.is(DamageTypeTags.IS_FIRE) || source.is(DustDamageTypes.SCORCH_KEY) || source.is(DustDamageTypes.IGNITION_KEY)) return DustElements.FIRE;
        if (source.is(DamageTypeTags.IS_DROWNING)) return DustElements.WATER;
        if (source.is(DamageTypeTags.IS_FREEZING)) return DustElements.ICE;
        if (source.is(DamageTypeTags.IS_EXPLOSION)) return DustElements.EXPLOSION;
        if (source.is(DamageTypeTags.IS_LIGHTNING)) return DustElements.LIGHTNING;
        return null;
    }
}
