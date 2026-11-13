package com.mowmaster.dust.Features.soul;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// A player's soul anchor (attachment SOUL_ANCHOR, kept through death): where their ghost stands, what they carried when
// they cast it, and until when it holds. On death the things in snapshot are kept back (kept) instead of dropped, and
// respawning is set; they respawn at the ghost and get kept back (SoulEvents).
public record SoulAnchor(ResourceKey<Level> dimension, Vec3 pos, float yaw, List<ItemStack> snapshot, long expiresAt,
                         Optional<UUID> ghost, List<ItemStack> kept, boolean respawning)
{
    public static final Codec<SoulAnchor> CODEC = RecordCodecBuilder.create(i -> i.group(
            Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(SoulAnchor::dimension),
            Vec3.CODEC.fieldOf("pos").forGetter(SoulAnchor::pos),
            Codec.FLOAT.fieldOf("yaw").forGetter(SoulAnchor::yaw),
            ItemStack.CODEC.listOf().fieldOf("snapshot").forGetter(SoulAnchor::snapshot),
            Codec.LONG.fieldOf("expires_at").forGetter(SoulAnchor::expiresAt),
            UUIDUtil.CODEC.optionalFieldOf("ghost").forGetter(SoulAnchor::ghost),
            ItemStack.CODEC.listOf().optionalFieldOf("kept", List.of()).forGetter(SoulAnchor::kept),
            Codec.BOOL.optionalFieldOf("respawning", false).forGetter(SoulAnchor::respawning)
    ).apply(i, SoulAnchor::new));

    public static final SoulAnchor NONE = new SoulAnchor(Level.OVERWORLD, Vec3.ZERO, 0, List.of(), 0, Optional.empty(), List.of(), false);

    // A record's fields can't change: "with" methods make a copy with one thing different
    public SoulAnchor withDeath(List<ItemStack> kept)
    {
        return new SoulAnchor(dimension, pos, yaw, List.of(), expiresAt, ghost, kept, true);
    }
}
