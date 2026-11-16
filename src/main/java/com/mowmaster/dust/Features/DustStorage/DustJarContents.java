package com.mowmaster.dust.Features.DustStorage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.Optional;

// What a Dust Jar holds: one element (none: an empty jar with no colour), how much of it, and which sides have a Jar
// Label. Kept on the block entity and, as the dust:dust_jar data component, on the jar's item when it's picked up or
// broken, so the dust and labels go with it. Optional is Java's "maybe a value": Optional.empty() for no element.
public record DustJarContents(Optional<DustElement> element, int amount, List<Direction> labels)
{
    public static final int CAPACITY = 1000;
    public static final DustJarContents EMPTY = new DustJarContents(Optional.empty(), 0, List.of());

    public DustJarContents
    {
        labels = List.copyOf(labels);
    }

    public static final Codec<DustJarContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DustElements.CODEC.optionalFieldOf("element").forGetter(DustJarContents::element),
            Codec.intRange(0, CAPACITY).optionalFieldOf("amount", 0).forGetter(DustJarContents::amount),
            Direction.CODEC.listOf().optionalFieldOf("labels", List.of()).forGetter(DustJarContents::labels)
    ).apply(instance, DustJarContents::new));

    public static final StreamCodec<ByteBuf, DustJarContents> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public boolean isEmpty()
    {
        return element.isEmpty() && amount == 0 && labels.isEmpty();
    }
}
