package com.mowmaster.dust.Features.Bosses;

import com.mowmaster.dust.Features.Armor.ElementalArmorSet;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Spells.SpellRegistries;
import com.mowmaster.dust.Features.Spells.api.Spell;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// What each Archon (ArchonEntity) casts: its kit, built from ordinary spells of its element and of elements made from
// it. The first spell is its bolt (cast whenever nothing else is ready); the others are specials, each resting for a
// while after it's cast. Every spell has Prevent Block Action and Silky, so nothing an Archon casts changes or breaks
// blocks (a missed bolt or an explosion leaves the ground alone).
//   BOLT   a projectile aimed at its target
//   SELF   cast on itself (a heal), only once it's below SELF_BELOW of its health
// Archons pay nothing for their spells, but cast half as often as a player could and their bolts fly half as fast
// (ArchonEntity.release).
public final class ArchonSpells
{
    public enum Kind { BOLT, SELF }

    // spell: what's cast; rest: ticks before it can be cast again (the bolt: 0, only the cast delay)
    public record ArchonSpell(Spell spell, Kind kind, int rest) {}

    public static final float SELF_BELOW = 0.6F;
    // Range attribute levels on every bolt (8 + 8 per level: 32 blocks) and Speed levels (0.5 + 0.4: 0.9 blocks a
    // tick for a player, halved for an Archon)
    private static final int RANGE_LEVELS = 3;
    private static final int SPEED_LEVELS = 1;

    private static final Map<ElementalArmorSet, List<ArchonSpell>> KITS = new EnumMap<>(ElementalArmorSet.class);

    private ArchonSpells() {}

    public static List<ArchonSpell> kit(ElementalArmorSet set)
    {
        if (KITS.isEmpty()) build();
        return KITS.get(set);
    }

    // Built the first time it's asked for (the registries' ids are known by then)
    private static synchronized void build()
    {
        if (!KITS.isEmpty()) return;
        Identifier multishot = SpellRegistries.MULTISHOT.getId(), chaining = SpellRegistries.CHAINING.getId(), chainCount = SpellRegistries.CHAIN_COUNT.getId(),
                aoe = SpellRegistries.AOE.getId(), targets = SpellRegistries.TARGET_COUNT.getId(), lingering = SpellRegistries.LINGERING.getId();
        kit(ElementalArmorSet.PYREBORN, bolt(DustElements.FIRE), bolt(DustElements.FIRE, 80, multishot, 2), bolt(DustElements.EXPLOSION, 140));
        kit(ElementalArmorSet.TIDEWOVEN, bolt(DustElements.WATER), bolt(DustElements.WATER, 80, chaining, 1, chainCount, 2), bolt(DustElements.SLIME, 120, aoe, 1, targets, 2));
        kit(ElementalArmorSet.ROOTBOUND, bolt(DustElements.EARTH), bolt(DustElements.STONE, 80, multishot, 2), bolt(DustElements.PLANTS, 140, aoe, 1, targets, 2));
        kit(ElementalArmorSet.LUMEN, bolt(DustElements.ORDER), bolt(DustElements.PROTECTION, 80, multishot, 2), bolt(DustElements.LIGHT, 120, chaining, 1, chainCount, 2), heal(300));
        kit(ElementalArmorSet.RIFTBORN, bolt(DustElements.VOID), bolt(DustElements.DARKNESS, 120, aoe, 1, targets, 2), bolt(DustElements.CHAOS, 500));
        kit(ElementalArmorSet.SPARKWRIGHT, bolt(DustElements.ENERGY), bolt(DustElements.LIGHTNING, 80, chaining, 1, chainCount, 2), bolt(DustElements.MAGIC, 120, multishot, 2));
        kit(ElementalArmorSet.BLASTWARD, bolt(DustElements.EXPLOSION), bolt(DustElements.FIRE, 80, multishot, 2), bolt(DustElements.EXPLOSION, 160, multishot, 1));
        kit(ElementalArmorSet.RIMEFROST, bolt(DustElements.ICE), bolt(DustElements.ICE, 80, multishot, 2), bolt(DustElements.ICE, 160, lingering, 1));
        kit(ElementalArmorSet.DEEPWATCH, bolt(DustElements.WATER), bolt(DustElements.ABYSS, 200), bolt(DustElements.DARKNESS, 160, chaining, 1, chainCount, 1));
        kit(ElementalArmorSet.BASTION, bolt(DustElements.STONE), bolt(DustElements.STONE, 80, multishot, 2), bolt(DustElements.EARTH, 140, aoe, 1, targets, 2));
        kit(ElementalArmorSet.MIREWALKER, bolt(DustElements.MUD), bolt(DustElements.MUD, 140, lingering, 1), bolt(DustElements.EARTH, 80, multishot, 1));
        kit(ElementalArmorSet.ZEPHYR, bolt(DustElements.AIR), bolt(DustElements.LIGHTNING, 80, chaining, 1, chainCount, 2), bolt(DustElements.AIR, 120, multishot, 2));
        kit(ElementalArmorSet.VERDANT, bolt(DustElements.POISON), bolt(DustElements.PLANTS, 80, multishot, 2), heal(300));
        kit(ElementalArmorSet.GILDED, bolt(DustElements.METAL), bolt(DustElements.DAMAGE, 80, multishot, 2), bolt(DustElements.CRYSTAL, 140));
    }

    private static void kit(ElementalArmorSet set, ArchonSpell... spells)
    {
        KITS.put(set, Collections.unmodifiableList(List.of(spells)));
    }

    private static ArchonSpell bolt(DustElement element)
    {
        return bolt(element, 0);
    }

    // A bolt of this element with these modifiers (id, levels, id, levels...)
    private static ArchonSpell bolt(DustElement element, int rest, Object... modifiers)
    {
        Map<Identifier, Integer> attributes = new LinkedHashMap<>();
        attributes.put(SpellRegistries.RANGE.getId(), RANGE_LEVELS);
        attributes.put(SpellRegistries.SPEED.getId(), SPEED_LEVELS);
        return new ArchonSpell(spell(element, SpellRegistries.PROJECTILE.getId(), attributes, modifiers), Kind.BOLT, rest);
    }

    // Healing on itself: Potency 4 (healSelf heals 4 health per point of potency, times the Archon's power)
    private static ArchonSpell heal(int rest)
    {
        return new ArchonSpell(spell(DustElements.HEALING, SpellRegistries.SELF.getId(), Map.of(SpellRegistries.POTENCY.getId(), 4)), Kind.SELF, rest);
    }

    private static Spell spell(DustElement element, Identifier shape, Map<Identifier, Integer> attributes, Object... modifiers)
    {
        List<Identifier> ids = new ArrayList<>();
        List<Integer> levels = new ArrayList<>();
        for (int i = 0; i + 1 < modifiers.length; i += 2) {
            ids.add((Identifier) modifiers[i]);
            levels.add((Integer) modifiers[i + 1]);
        }
        ids.add(SpellRegistries.PREVENT_BLOCK.getId());
        levels.add(1);
        ids.add(SpellRegistries.SILKY.getId());
        levels.add(1);
        return new Spell(element, shape, attributes, ids, levels);
    }
}
