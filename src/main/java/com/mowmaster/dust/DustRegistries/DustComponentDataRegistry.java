package com.mowmaster.dust.DustRegistries;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.DustReferences;
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


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FOCUSEDBOOK_BOOK_COLOR = register("fb_book_color",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FOCUSEDBOOK_BOOKMODEL_TYPE = register("fb_bookmodel_type",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FOCUSEDBOOK_BOOKCOVER_TYPE = register("fb_bookcover_type",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                          UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DUST_DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DUST_DATA_COMPONENT_TYPES.register(eventBus);
    }
}
