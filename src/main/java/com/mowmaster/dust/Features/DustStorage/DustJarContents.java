package com.mowmaster.dust.Features.DustStorage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

// What a Dust Jar holds: one element (none: an empty jar with no colour) and how much of it. Kept on the block entity
// and, as the dust:dust_jar data component, on the jar's item when it's picked up or broken, so the dust goes with it.
// Optional is Java's "maybe a value": Optional.empty() for no element.
public record DustJarContents(Optional<DustElement> element, int amount)
{
    public static final int CAPACITY = 1000;
    public static final DustJarContents EMPTY = new DustJarContents(Optional.empty(), 0);

    public static final Codec<DustJarContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DustElements.CODEC.optionalFieldOf("element").forGetter(DustJarContents::element),
            Codec.intRange(0, CAPACITY).optionalFieldOf("amount", 0).forGetter(DustJarContents::amount)
    ).apply(instance, DustJarContents::new));

    public static final StreamCodec<ByteBuf, DustJarContents> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public boolean isEmpty()
    {
        return element.isEmpty() && amount == 0;
    }
}
