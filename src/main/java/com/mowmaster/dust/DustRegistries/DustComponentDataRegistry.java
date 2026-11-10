package com.mowmaster.dust.DustRegistries;


import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Research.ResearchData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DustComponentDataRegistry
{
    public static final DeferredRegister<DataComponentType<?>> DUST_DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, DustReferences.MODID);

    // The ancient word written on a tablet or transcription (lowercase a-z)
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> ANCIENT_WORD = register("ancient_word",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

    // Decipher progress, kept on the tablet so closing the screen doesn't reset the game:
    // letters guessed so far (bit 0 = a) and mistakes made
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DECIPHER_GUESSES = register("decipher_guesses",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DECIPHER_MISTAKES = register("decipher_mistakes",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    // A Shared Research Guide's copy of its author's research, and the author's name
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResearchData>> RESEARCH_SNAPSHOT = register("research_snapshot",
            builder -> builder.persistent(ResearchData.CODEC).networkSynchronized(ResearchData.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> RESEARCH_AUTHOR = register("research_author",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                          UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DUST_DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DUST_DATA_COMPONENT_TYPES.register(eventBus);
    }
}
