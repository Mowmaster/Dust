package com.mowmaster.dust.Features.Trees;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// What's in a Fruit Medley (component dust:fruit_medley): how many of each elemental fruit went in (6 in all, of 1 to 3
// kinds: FruitMedleyRecipe)
public record FruitMedley(Map<ElementalWood, Integer> fruits)
{
    public static final Codec<FruitMedley> CODEC = Codec.unboundedMap(ElementalWood.CODEC, Codec.intRange(1, 64))
            .xmap(FruitMedley::new, FruitMedley::fruits);
    public static final StreamCodec<ByteBuf, FruitMedley> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public FruitMedley
    {
        fruits = Map.copyOf(fruits);
    }

    // The kinds of fruit, most first (then in wood order)
    public List<ElementalWood> kinds()
    {
        List<ElementalWood> kinds = new ArrayList<>(fruits.keySet());
        kinds.sort(Comparator.comparingInt((ElementalWood wood) -> -fruits.get(wood)).thenComparingInt(Enum::ordinal));
        return kinds;
    }

    // Each kind's element and how many of it went in: what eating it gives (FruitAffinity.give)
    public Map<DustElement, Integer> affinities()
    {
        Map<DustElement, Integer> out = new LinkedHashMap<>();
        for (ElementalWood wood : kinds()) out.merge(wood.element(), fruits.get(wood), Integer::sum);
        return out;
    }
}
