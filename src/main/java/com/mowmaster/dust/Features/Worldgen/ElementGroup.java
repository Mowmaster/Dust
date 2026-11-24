package com.mowmaster.dust.Features.Worldgen;

import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import com.mowmaster.dust.Features.Magic.BiomeElements;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// The families elements fall into for where they form in the world (element geodes): by which of Fire, Water and Earth
// they hold most of (one of the three strictly the most, and at least as much as their Order or Chaos). The rest (Order
// and Chaos themselves, the ones balanced between two of the three, the ones made mostly of Order or Chaos) don't sit
// well with any of the three: they form where the white and black crystal ores do, in biomes rich in Order or Chaos.
//   FIRE         Fire, Energy, Explosion, Light, Darkness, Radiance, Crystal, Gem, Fracture
//   WATER        Water, Ice, Abyss, Lightning, Flight, Change, Slime, Poison, Spirit, Knowledge, Soul, Magic, Warp
//   EARTH        Earth, Stone, Mud, Seeds, Growth, Fungus, Armor, Protection, Rust, Creature, Flesh, Plants, Trees
//   ORDER_CHAOS  Order, Chaos, Air, Vacuum, Motion, Life, Death, Healing, Metal, Tool, Damage, Void, Sound
// A biome's family comes from the primals it's innately rich in (BiomeElements): a desert's is FIRE, a jungle's WATER or
// EARTH, a snowy biome's ORDER_CHAOS. Within a family the simpler elements are commoner: an element's weight is its
// share of the family's primal over how many primals it holds in all (ORDER_CHAOS: just one over how many), so the
// family's primal itself turns up most.
public enum ElementGroup
{
    FIRE(EnumElement.FIRE), WATER(EnumElement.WATER), EARTH(EnumElement.EARTH), ORDER_CHAOS(null);

    // The primal the family is named for (null: Order and Chaos)
    private final @Nullable EnumElement primal;
    private @Nullable List<DustElement> members;

    ElementGroup(@Nullable EnumElement primal)
    {
        this.primal = primal;
    }

    public static ElementGroup of(DustElement element)
    {
        int fire = element.primal(EnumElement.FIRE), water = element.primal(EnumElement.WATER), earth = element.primal(EnumElement.EARTH);
        int most = Math.max(fire, Math.max(water, earth));
        int ties = (fire == most ? 1 : 0) + (water == most ? 1 : 0) + (earth == most ? 1 : 0);
        int other = Math.max(element.primal(EnumElement.ORDER), element.primal(EnumElement.CHAOS));
        if (most <= 0 || ties > 1 || most < other) return ORDER_CHAOS;
        return fire == most ? FIRE : water == most ? WATER : EARTH;
    }

    public List<DustElement> members()
    {
        if (members == null) {
            List<DustElement> list = new ArrayList<>();
            for (DustElement element : DustElements.all()) if (of(element) == this) list.add(element);
            members = List.copyOf(list);
        }
        return members;
    }

    private static int total(DustElement element)
    {
        int total = 0;
        for (EnumElement each : EnumElement.values()) total += element.primal(each);
        return Math.max(1, total);
    }

    private float weight(DustElement element)
    {
        float total = total(element);
        if (primal == null) return 1F / total;
        return element.primal(primal) / total / total;
    }

    // An element of the family, the simpler ones commoner
    public DustElement pickElement(RandomSource random)
    {
        List<DustElement> list = members();
        float sum = 0;
        for (DustElement element : list) sum += weight(element);
        float pick = random.nextFloat() * sum;
        for (DustElement element : list) {
            pick -= weight(element);
            if (pick <= 0) return element;
        }
        return list.getLast();
    }

    // How strongly a biome belongs to the family: how much richer it is than ordinary in the family's primal(s)
    public float strength(Holder<Biome> biome)
    {
        if (primal != null) return BiomeElements.richness(biome, primal);
        return BiomeElements.richness(biome, EnumElement.ORDER) + BiomeElements.richness(biome, EnumElement.CHAOS);
    }

    // A family for something forming in this biome, picked by how strongly the biome belongs to each; null for a biome
    // rich in no primal
    public static @Nullable ElementGroup pick(Holder<Biome> biome, RandomSource random)
    {
        float sum = 0;
        for (ElementGroup group : values()) sum += group.strength(biome);
        if (sum <= 0) return null;
        float pick = random.nextFloat() * sum;
        for (ElementGroup group : values()) {
            pick -= group.strength(biome);
            if (pick <= 0 && group.strength(biome) > 0) return group;
        }
        return null;
    }
}
