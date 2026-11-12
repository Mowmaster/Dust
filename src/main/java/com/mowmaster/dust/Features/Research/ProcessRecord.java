package com.mowmaster.dust.Features.Research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// One step a player has taken a material through: what went in, which machine, what came out. Saved on the input
// material's entry, so the book maps out how each material was broken down. Records compare by value (two records of
// the same input, machine and outputs are equal), which is how the same step is never noted twice.
//   machine   the machine's item id (shown as its icon)
//   input     what went in (one of it, with its data: a crushed block remembers its block)
//   outputs   what came out (the crusher makes one thing, the separation table two)
//   with      anything else that went in alongside input (the Combination and Mixing Tables take two dusts); usually none
public record ProcessRecord(Identifier machine, ItemStackTemplate input, List<ItemStackTemplate> outputs, List<ItemStackTemplate> with)
{
    public ProcessRecord
    {
        outputs = List.copyOf(outputs);
        with = List.copyOf(with);
    }

    // A step with one input (every machine but the Combination and Mixing Tables)
    public ProcessRecord(Identifier machine, ItemStackTemplate input, List<ItemStackTemplate> outputs)
    {
        this(machine, input, outputs, List.of());
    }

    // Saved as "outputs": [...]. Steps saved before there could be several outputs have a single "output"; reading
    // takes either (both optional), writing only the list.
    public static final Codec<ProcessRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("machine").forGetter(ProcessRecord::machine),
            ItemStackTemplate.CODEC.fieldOf("input").forGetter(ProcessRecord::input),
            ItemStackTemplate.CODEC.listOf().optionalFieldOf("outputs", List.of()).forGetter(ProcessRecord::outputs),
            ItemStackTemplate.CODEC.optionalFieldOf("output").forGetter(record -> Optional.<ItemStackTemplate>empty()),
            ItemStackTemplate.CODEC.listOf().optionalFieldOf("with", List.of()).forGetter(ProcessRecord::with)
    ).apply(instance, (machine, input, outputs, oldOutput, with) -> {
        List<ItemStackTemplate> all = new ArrayList<>(outputs);
        oldOutput.ifPresent(all::add);
        return new ProcessRecord(machine, input, all, with);
    }));

    public static final StreamCodec<RegistryFriendlyByteBuf, ProcessRecord> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ProcessRecord::machine,
            ItemStackTemplate.STREAM_CODEC, ProcessRecord::input,
            ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()), ProcessRecord::outputs,
            ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()), ProcessRecord::with,
            ProcessRecord::new);
}
