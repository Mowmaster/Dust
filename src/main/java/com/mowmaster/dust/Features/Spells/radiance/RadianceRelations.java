package com.mowmaster.dust.Features.Spells.radiance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// A player's standing with other players (attachment RADIANCE_RELATIONS), synced to that player only: their client
// draws the red and green arrows from it (Client/RelationArrows), and Radiance reads it on the server.
//   hostile  players they fought (either way round) recently, until a game time: both have each other here
//   marked   players they've marked friendly with magic (the other gets the Friendly effect), until a game time
//   friends  permanent friends (/friend), both ways
//   allies   allies for this session (/ally), both ways, gone when either leaves
//   enemies  players they've declared enemies (/enemy), permanent, one way
// friends, allies and enemies are a copy of the server's SocialData / Social, refreshed whenever they change and on
// login (Social.refresh); hostile and marked live only here.
public record RadianceRelations(Map<UUID, Long> hostile, Map<UUID, Long> marked, Set<UUID> friends, Set<UUID> allies, Set<UUID> enemies)
{
    private static final Codec<Map<UUID, Long>> TIMED_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.LONG);
    // A set saved as a list: Codec.xmap turns a List<UUID> codec into a Set<UUID> one
    private static final Codec<Set<UUID>> SET_CODEC = UUIDUtil.CODEC.listOf().xmap(HashSet::new, java.util.List::copyOf);

    public static final Codec<RadianceRelations> CODEC = RecordCodecBuilder.create(i -> i.group(
            TIMED_CODEC.optionalFieldOf("hostile", Map.of()).forGetter(RadianceRelations::hostile),
            TIMED_CODEC.optionalFieldOf("marked", Map.of()).forGetter(RadianceRelations::marked),
            SET_CODEC.optionalFieldOf("friends", Set.of()).forGetter(RadianceRelations::friends),
            SET_CODEC.optionalFieldOf("allies", Set.of()).forGetter(RadianceRelations::allies),
            SET_CODEC.optionalFieldOf("enemies", Set.of()).forGetter(RadianceRelations::enemies)
    ).apply(i, RadianceRelations::new));

    // ByteBufCodecs.map / .collection: maps and sets over the network, given how to make them and send their parts
    private static final StreamCodec<RegistryFriendlyByteBuf, Map<UUID, Long>> TIMED_STREAM =
            ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.VAR_LONG);
    private static final StreamCodec<RegistryFriendlyByteBuf, Set<UUID>> SET_STREAM =
            ByteBufCodecs.collection(HashSet::new, UUIDUtil.STREAM_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, RadianceRelations> STREAM_CODEC = StreamCodec.composite(
            TIMED_STREAM, RadianceRelations::hostile,
            TIMED_STREAM, RadianceRelations::marked,
            SET_STREAM, RadianceRelations::friends,
            SET_STREAM, RadianceRelations::allies,
            SET_STREAM, RadianceRelations::enemies,
            RadianceRelations::new);

    public static final RadianceRelations NONE = new RadianceRelations(Map.of(), Map.of(), Set.of(), Set.of(), Set.of());

    // Red: declared an enemy, or fought recently
    public boolean isHostile(UUID other, long now)
    {
        Long until = hostile.get(other);
        return enemies.contains(other) || (until != null && until > now);
    }

    // Green: a friend, an ally, or marked friendly (and not hostile: hostility wins)
    public boolean isFriendly(UUID other, long now)
    {
        if (isHostile(other, now)) return false;
        Long until = marked.get(other);
        return friends.contains(other) || allies.contains(other) || (until != null && until > now);
    }

    // Friends or allies: the pair can't hurt each other (Social)
    public boolean isProtected(UUID other)
    {
        return friends.contains(other) || allies.contains(other);
    }

    // The latest time a fight's hostility runs out (0 if none)
    public long hostileUntil()
    {
        return hostile.values().stream().mapToLong(Long::longValue).max().orElse(0);
    }

    // Copies with one change (records can't be changed), dropping timed entries that have run out
    public RadianceRelations withHostile(UUID other, long until, long now)
    {
        Map<UUID, Long> h = live(hostile, now);
        h.put(other, until);
        return new RadianceRelations(h, live(marked, now), friends, allies, enemies);
    }

    public RadianceRelations withMarked(UUID other, long until, long now)
    {
        Map<UUID, Long> m = live(marked, now);
        m.put(other, until);
        return new RadianceRelations(live(hostile, now), m, friends, allies, enemies);
    }

    public RadianceRelations withSocial(Set<UUID> friends, Set<UUID> allies, Set<UUID> enemies)
    {
        return new RadianceRelations(hostile, marked, Set.copyOf(friends), Set.copyOf(allies), Set.copyOf(enemies));
    }

    private static Map<UUID, Long> live(Map<UUID, Long> map, long now)
    {
        Map<UUID, Long> copy = new HashMap<>(map);
        copy.values().removeIf(until -> until <= now);
        return copy;
    }
}
