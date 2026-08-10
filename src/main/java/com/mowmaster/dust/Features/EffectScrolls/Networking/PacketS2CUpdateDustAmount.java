package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustReferences;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;


public record PacketS2CUpdateDustAmount(int oldValue, int newValue) implements CustomPacketPayload
{
    public static final Type<PacketS2CUpdateDustAmount> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_packet"));
    //SIMILAR TO RECIPES IN 1.19 your coding the packet send data, bytebufcodec type first then the value second
    public static final StreamCodec<ByteBuf, PacketS2CUpdateDustAmount> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            PacketS2CUpdateDustAmount::oldValue,

            ByteBufCodecs.VAR_INT,
            PacketS2CUpdateDustAmount::newValue,

            PacketS2CUpdateDustAmount::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
