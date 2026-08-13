package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustReferences;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;


public record S2CPacketElementEarth(int oldValue, int newValue) implements CustomPacketPayload
{
    public static final Type<@NotNull S2CPacketElementEarth> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "packet_element_earth"));
    //SIMILAR TO RECIPES IN 1.19 your coding the packet send data, bytebufcodec type first then the value second
    public static final StreamCodec<ByteBuf, S2CPacketElementEarth> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            S2CPacketElementEarth::oldValue,

            ByteBufCodecs.VAR_INT,
            S2CPacketElementEarth::newValue,

            S2CPacketElementEarth::new);

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }
}
