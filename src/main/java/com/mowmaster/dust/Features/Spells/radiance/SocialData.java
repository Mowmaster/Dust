package com.mowmaster.dust.Features.Spells.radiance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// The permanent side of who's friends and enemies with whom, for the whole server (data/dust_social.dat). Kept here,
// not on the players, so it can change while one of them is offline:
//   friends   both ways: each friend is in the other's set
//   enemies   one way: who each player has declared an enemy
//   requests  friend requests waiting for an answer: target -> who asked, oldest first. Delivered on login.
//   names     the last name seen for each player, so requests to and from offline players can be shown by name
// Allies are for one session only, so they're kept in memory instead (Social).
public class SocialData extends SavedData
{
    private static final Codec<Map<UUID, Set<UUID>>> GRAPH_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC,
            UUIDUtil.CODEC.listOf().xmap(list -> (Set<UUID>) new LinkedHashSet<>(list), List::copyOf));

    private static final Codec<SocialData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GRAPH_CODEC.optionalFieldOf("friends", Map.of()).forGetter(data -> data.friends),
            GRAPH_CODEC.optionalFieldOf("enemies", Map.of()).forGetter(data -> data.enemies),
            GRAPH_CODEC.optionalFieldOf("requests", Map.of()).forGetter(data -> data.requests),
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.STRING).optionalFieldOf("names", Map.of()).forGetter(data -> data.names)
    ).apply(i, SocialData::new));

    private static final SavedDataType<SocialData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "social"), SocialData::new, CODEC);

    private final Map<UUID, Set<UUID>> friends;
    private final Map<UUID, Set<UUID>> enemies;
    private final Map<UUID, Set<UUID>> requests;
    private final Map<UUID, String> names;

    public SocialData()
    {
        this(Map.of(), Map.of(), Map.of(), Map.of());
    }

    private SocialData(Map<UUID, Set<UUID>> friends, Map<UUID, Set<UUID>> enemies, Map<UUID, Set<UUID>> requests, Map<UUID, String> names)
    {
        this.friends = copy(friends);
        this.enemies = copy(enemies);
        this.requests = copy(requests);
        this.names = new HashMap<>(names);
    }

    // Deep copies, so the sets can be changed (the codec hands over ones that can't)
    private static Map<UUID, Set<UUID>> copy(Map<UUID, Set<UUID>> graph)
    {
        Map<UUID, Set<UUID>> copy = new HashMap<>();
        graph.forEach((key, set) -> copy.put(key, new LinkedHashSet<>(set)));
        return copy;
    }

    public static SocialData get(MinecraftServer server)
    {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    private static Set<UUID> of(Map<UUID, Set<UUID>> graph, UUID player)
    {
        return graph.getOrDefault(player, Set.of());
    }

    // computeIfAbsent: the set for player, made (empty) if there isn't one yet
    private static Set<UUID> edit(Map<UUID, Set<UUID>> graph, UUID player)
    {
        return graph.computeIfAbsent(player, key -> new LinkedHashSet<>());
    }

    // ---- names ----

    public void rememberName(UUID player, String name)
    {
        if (!name.equals(names.put(player, name))) setDirty();
    }

    public String nameOf(UUID player)
    {
        return names.getOrDefault(player, player.toString());
    }

    // ---- friends ----

    public Set<UUID> friendsOf(UUID player) { return Set.copyOf(of(friends, player)); }
    public boolean areFriends(UUID a, UUID b) { return of(friends, a).contains(b); }

    public void addFriends(UUID a, UUID b)
    {
        edit(friends, a).add(b);
        edit(friends, b).add(a);
        setDirty();
    }

    public boolean removeFriends(UUID a, UUID b)
    {
        boolean removed = edit(friends, a).remove(b) | edit(friends, b).remove(a);
        if (removed) setDirty();
        return removed;
    }

    // ---- enemies ----

    public Set<UUID> enemiesOf(UUID player) { return Set.copyOf(of(enemies, player)); }

    public boolean addEnemy(UUID player, UUID enemy)
    {
        boolean added = edit(enemies, player).add(enemy);
        if (added) setDirty();
        return added;
    }

    public boolean removeEnemy(UUID player, UUID enemy)
    {
        boolean removed = edit(enemies, player).remove(enemy);
        if (removed) setDirty();
        return removed;
    }

    // ---- friend requests ----

    // Who has asked target, oldest first
    public List<UUID> requestsTo(UUID target) { return List.copyOf(of(requests, target)); }
    public boolean hasRequest(UUID target, UUID from) { return of(requests, target).contains(from); }

    public void addRequest(UUID target, UUID from)
    {
        if (edit(requests, target).add(from)) setDirty();
    }

    public boolean removeRequest(UUID target, UUID from)
    {
        boolean removed = edit(requests, target).remove(from);
        if (removed) setDirty();
        return removed;
    }
}
