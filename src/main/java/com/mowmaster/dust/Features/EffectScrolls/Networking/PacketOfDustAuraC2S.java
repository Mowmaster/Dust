package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustReferences;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PacketOfDustAuraC2S(String name, int value) implements CustomPacketPayload
{
    public static final Type<PacketOfDustAuraC2S> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "test_packet"));
    //SIMILAR TO RECIPES IN 1.19 your coding the packet send data, bytebufcodec type first then the value second
    public static final StreamCodec<ByteBuf, PacketOfDustAuraC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            PacketOfDustAuraC2S::name,

            ByteBufCodecs.VAR_INT,
            PacketOfDustAuraC2S::value,

            PacketOfDustAuraC2S::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
