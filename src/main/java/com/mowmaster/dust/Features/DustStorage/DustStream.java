package com.mowmaster.dust.Features.DustStorage;

import com.mowmaster.dust.DustReferences;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

// Dust flowing from one place to another, as a server -> client message:
//   from, to   where it comes from and goes to
//   color      the element's colour
//   amount     how much moved: more dust, a thicker stream
//   travel     how long it takes to get there (ticks)
//   hold       how long it then keeps flowing (ticks). 0: a single ribbon of dust that runs from one end to the other
//              (a Dust Jar or an element orb into a lamp). More: a steady stream from end to end for that long, whose
//              tail then runs in after it (the Element Replicator drawing on its jars), so it all ends at
//              travel + hold + travel ticks.
// The client draws it as a winding ribbon of dust (Client/DustStreams); the server sends one message.
// Registered in DustMagicEvents, its handler in DustClient.
public record DustStream(Vec3 from, Vec3 to, int color, int amount, int travel, int hold) implements CustomPacketPayload
{
    public static final Type<DustStream> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_stream"));
    // composite: writes and reads each field in turn (up to a dozen or so fields; this has six)
    public static final StreamCodec<RegistryFriendlyByteBuf, DustStream> STREAM_CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC, DustStream::from,
            Vec3.STREAM_CODEC, DustStream::to,
            ByteBufCodecs.INT, DustStream::color,
            ByteBufCodecs.VAR_INT, DustStream::amount,
            ByteBufCodecs.VAR_INT, DustStream::travel,
            ByteBufCodecs.VAR_INT, DustStream::hold,
            DustStream::new);

    // Players this near see it (blocks)
    private static final double SEEN_FROM = 48;
    // How long a ribbon takes to get there (ticks): MIN_TRAVEL, plus a tick for each TRAVEL_PER units, up to MAX_TRAVEL
    private static final int MIN_TRAVEL = 18;
    private static final int MAX_TRAVEL = 44;
    private static final int TRAVEL_PER = 4;

    @Override
    public Type<DustStream> type()
    {
        return TYPE;
    }

    // Shows a ribbon of dust of this colour flowing from there to there to the players nearby (more dust takes longer)
    public static void send(ServerLevel level, Vec3 from, Vec3 to, int color, int amount)
    {
        send(level, from, to, color, amount, Mth.clamp(MIN_TRAVEL + amount / TRAVEL_PER, MIN_TRAVEL, MAX_TRAVEL), 0);
    }

    // The same, taking travel ticks to get there and then flowing on for hold ticks (see above)
    public static void send(ServerLevel level, Vec3 from, Vec3 to, int color, int amount, int travel, int hold)
    {
        Vec3 middle = from.add(to).scale(0.5);
        PacketDistributor.sendToPlayersNear(level, null, middle.x, middle.y, middle.z, SEEN_FROM,
                new DustStream(from, to, color, amount, Math.max(1, travel), Math.max(0, hold)));
    }
}
