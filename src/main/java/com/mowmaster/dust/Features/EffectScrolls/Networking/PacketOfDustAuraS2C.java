package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustReferences;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;


public record PacketOfDustAuraS2C(int oldValue, int newValue) implements CustomPacketPayload
{
    public static final Type<PacketOfDustAuraS2C> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_packet"));
    //SIMILAR TO RECIPES IN 1.19 your coding the packet send data, bytebufcodec type first then the value second
    public static final StreamCodec<ByteBuf, PacketOfDustAuraS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            PacketOfDustAuraS2C::oldValue,

            ByteBufCodecs.VAR_INT,
            PacketOfDustAuraS2C::newValue,

            PacketOfDustAuraS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
