package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustReferences;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

// Dust flowing from one place to another (a Dust Jar or an element orb into a lamp), as a server -> client message:
// where from, where to, the element's colour, and how much moved (more dust, a longer, thicker stream). The client draws
// it as a winding ribbon of dust over a second or two (Client/DustStreams); the server sends one message.
// Registered in DustMagicEvents, its handler in DustClient.
public record DustStream(Vec3 from, Vec3 to, int color, int amount) implements CustomPacketPayload
{
    public static final Type<DustStream> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_stream"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DustStream> STREAM_CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC, DustStream::from,
            Vec3.STREAM_CODEC, DustStream::to,
            ByteBufCodecs.INT, DustStream::color,
            ByteBufCodecs.VAR_INT, DustStream::amount,
            DustStream::new);

    // Players this near see it (blocks)
    private static final double SEEN_FROM = 48;

    @Override
    public Type<DustStream> type()
    {
        return TYPE;
    }

    // Shows dust of this colour flowing from there to there to the players nearby
    public static void send(ServerLevel level, Vec3 from, Vec3 to, int color, int amount)
    {
        Vec3 middle = from.add(to).scale(0.5);
        PacketDistributor.sendToPlayersNear(level, null, middle.x, middle.y, middle.z, SEEN_FROM, new DustStream(from, to, color, amount));
    }
}
