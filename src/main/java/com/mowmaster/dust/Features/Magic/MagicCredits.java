package com.mowmaster.dust.Features.Magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mowmaster.dust.DustReferences;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Which players got Magic Level experience from which spell tree (one point per level the tree gained from their
// casting), saved with the world (data/dust_magic_credits.dat). When a spell is broken its tree is forgotten
// (SpellTreeData.forget) and everyone it gave experience to loses it again: straight away if they're online, otherwise
// when they next log in (pending).
public class MagicCredits extends SavedData
{
    private record Stored(Map<UUID, Map<UUID, Integer>> credits, Map<UUID, Integer> pending)
    {
        static final Codec<Stored> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT))
                        .optionalFieldOf("credits", Map.of()).forGetter(Stored::credits),
                Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT).optionalFieldOf("pending", Map.of()).forGetter(Stored::pending)
        ).apply(instance, Stored::new));
    }

    private static final Codec<MagicCredits> CODEC = Stored.CODEC.xmap(MagicCredits::new, credits -> new Stored(credits.credits, credits.pending));
    private static final SavedDataType<MagicCredits> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "magic_credits"), MagicCredits::new, CODEC);

    // tree -> (player -> experience given)
    private final Map<UUID, Map<UUID, Integer>> credits = new HashMap<>();
    // player -> experience to take away when they log in
    private final Map<UUID, Integer> pending = new HashMap<>();

    public MagicCredits()
    {
    }

    private MagicCredits(Stored stored)
    {
        stored.credits().forEach((tree, players) -> credits.put(tree, new HashMap<>(players)));
        pending.putAll(stored.pending());
    }

    public static MagicCredits get(MinecraftServer server)
    {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    // The player's casting took the tree up this many levels: that much experience for them, remembered against it
    public void credit(ServerPlayer player, UUID tree, int levels)
    {
        if (levels <= 0) return;
        credits.computeIfAbsent(tree, id -> new HashMap<>()).merge(player.getUUID(), levels, Integer::sum);
        setDirty();
        PlayerMagicHelper.addMagicXp(player, levels);
    }

    // The tree is gone (its spell broken): everyone loses what it gave them
    public void revoke(MinecraftServer server, UUID tree)
    {
        Map<UUID, Integer> given = credits.remove(tree);
        if (given == null) return;
        given.forEach((id, amount) -> {
            ServerPlayer player = server.getPlayerList().getPlayer(id);
            if (player != null) PlayerMagicHelper.addMagicXp(player, -amount);
            else pending.merge(id, amount, Integer::sum);
        });
        setDirty();
    }

    // Takes away what spells broken while they were away gave them
    public void applyPending(ServerPlayer player)
    {
        Integer owed = pending.remove(player.getUUID());
        if (owed == null) return;
        PlayerMagicHelper.addMagicXp(player, -owed);
        setDirty();
    }
}
