package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustReferences;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.List;

// Dust running along Glass Dust Tubes (DustNetwork), as a server -> client message: the blocks it passes through, in
// order (where it came from first, where it's going last, the tubes between), and the element's colour. The client
// sends a glowing trail of dust motes along the tubes (Client/DustTubeFlows). Registered in DustMagicEvents, its
// handler in DustClient.
public record DustTubeFlow(List<BlockPos> path, int color) implements CustomPacketPayload
{
    public static final Type<DustTubeFlow> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_tube_flow"));
    // A list of block positions (at most 512, as DustNetwork sends), then the colour
    public static final StreamCodec<RegistryFriendlyByteBuf, DustTubeFlow> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list(512)), DustTubeFlow::path,
            ByteBufCodecs.INT, DustTubeFlow::color,
            DustTubeFlow::new);

    @Override
    public Type<DustTubeFlow> type()
    {
        return TYPE;
    }
}
