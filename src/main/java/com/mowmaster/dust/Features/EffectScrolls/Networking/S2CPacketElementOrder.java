package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustReferences;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;


public record S2CPacketElementOrder(int oldValue, int newValue) implements CustomPacketPayload
{
    public static final Type<S2CPacketElementOrder> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "packet_element_order"));
    //SIMILAR TO RECIPES IN 1.19 your coding the packet send data, bytebufcodec type first then the value second
    public static final StreamCodec<ByteBuf, S2CPacketElementOrder> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            S2CPacketElementOrder::oldValue,

            ByteBufCodecs.VAR_INT,
            S2CPacketElementOrder::newValue,

            S2CPacketElementOrder::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
