package com.mowmaster.dust.Features.Spells.radiance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// A player's standing with other players (attachment RADIANCE_RELATIONS), each until a game time:
//   hostile  players they fought (either way round) in the last 5 minutes: both have each other here
//   friends  players they've marked friendly (the other player gets the Friendly effect)
// Synced to its own player only: their client draws the red and green arrows from it (Client/RelationArrows).
public record RadianceRelations(Map<UUID, Long> hostile, Map<UUID, Long> friends)
{
    private static final Codec<Map<UUID, Long>> MAP_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.LONG);

    public static final Codec<RadianceRelations> CODEC = RecordCodecBuilder.create(i -> i.group(
            MAP_CODEC.optionalFieldOf("hostile", Map.of()).forGetter(RadianceRelations::hostile),
            MAP_CODEC.optionalFieldOf("friends", Map.of()).forGetter(RadianceRelations::friends)
    ).apply(i, RadianceRelations::new));

    // ByteBufCodecs.map: a map over the network, given how to make the map and how to send keys and values
    private static final StreamCodec<RegistryFriendlyByteBuf, Map<UUID, Long>> MAP_STREAM_CODEC =
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.VAR_LONG);

    public static final StreamCodec<RegistryFriendlyByteBuf, RadianceRelations> STREAM_CODEC = StreamCodec.composite(
            MAP_STREAM_CODEC, RadianceRelations::hostile,
            MAP_STREAM_CODEC, RadianceRelations::friends,
            RadianceRelations::new);

    public static final RadianceRelations NONE = new RadianceRelations(Map.of(), Map.of());

    public boolean isHostile(UUID other, long now)
    {
        Long until = hostile.get(other);
        return until != null && until > now;
    }

    public boolean isFriend(UUID other, long now)
    {
        Long until = friends.get(other);
        return until != null && until > now;
    }

    // The latest time any hostility runs out (0 if none)
    public long hostileUntil()
    {
        return hostile.values().stream().mapToLong(Long::longValue).max().orElse(0);
    }

    // Copies with one change, dropping anything that has run out. Fighting someone ends a friendship with them.
    public RadianceRelations withHostile(UUID other, long until, long now)
    {
        Map<UUID, Long> h = live(hostile, now), f = live(friends, now);
        h.put(other, until);
        f.remove(other);
        return new RadianceRelations(h, f);
    }

    public RadianceRelations withFriend(UUID other, long until, long now)
    {
        Map<UUID, Long> f = live(friends, now);
        f.put(other, until);
        return new RadianceRelations(live(hostile, now), f);
    }

    private static Map<UUID, Long> live(Map<UUID, Long> map, long now)
    {
        Map<UUID, Long> copy = new HashMap<>(map);
        copy.values().removeIf(until -> until <= now);
        return copy;
    }
}
