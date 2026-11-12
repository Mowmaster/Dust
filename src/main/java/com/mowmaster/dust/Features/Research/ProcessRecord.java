package com.mowmaster.dust.Features.Research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;

// One step a player has taken a material through: what went in, which machine, what came out. Saved on the input
// material's entry, so the book maps out how each material was broken down. Records compare by value (two records of
// the same input, machine and output are equal), which is how the same step is never noted twice.
//   machine   the machine's item id (shown as its icon)
//   input     what went in (one of it, with its data: a crushed block remembers its block)
//   output    what came out
public record ProcessRecord(Identifier machine, ItemStackTemplate input, ItemStackTemplate output)
{
    public static final Codec<ProcessRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("machine").forGetter(ProcessRecord::machine),
            ItemStackTemplate.CODEC.fieldOf("input").forGetter(ProcessRecord::input),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(ProcessRecord::output)
    ).apply(instance, ProcessRecord::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ProcessRecord> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ProcessRecord::machine,
            ItemStackTemplate.STREAM_CODEC, ProcessRecord::input,
            ItemStackTemplate.STREAM_CODEC, ProcessRecord::output,
            ProcessRecord::new);
}
