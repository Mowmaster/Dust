package com.mowmaster.dust.Features.Spells.pylons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

// What a broken Spell Pylon remembers (the dust:pylon_memory data component on its item): the element its spell was,
// and the experience it keeps (PylonConfig.KEPT_LEVELS of its levels). Placed again and given a crystal of the same
// element, it starts from there; any other element starts from nothing.
public record PylonMemory(DustElement element, int experience)
{
    public static final Codec<PylonMemory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DustElements.CODEC.fieldOf("element").forGetter(PylonMemory::element),
            Codec.INT.fieldOf("experience").forGetter(PylonMemory::experience)
    ).apply(instance, PylonMemory::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PylonMemory> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
