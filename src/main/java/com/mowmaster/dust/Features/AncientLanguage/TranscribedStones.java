package com.mowmaster.dust.Features.AncientLanguage;

import com.mojang.serialization.Codec;
import com.mowmaster.dust.Features.AncientLanguage.Client.TranscribedStonesClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// The glyph stones a player has already transcribed (dimension + position). Each stone gives one transcription per
// player; afterwards it looks worn blank to that player only (Client/GlyphStoneTint) and Parchment won't work on it.
// Immutable: "adding" makes a new copy, and setting the copy on the player is what triggers the sync.
public record TranscribedStones(Set<GlobalPos> stones)
{
    public static final TranscribedStones EMPTY = new TranscribedStones(Set.of());

    // Saved as a list of {dimension, pos}
    public static final Codec<TranscribedStones> CODEC = GlobalPos.CODEC.listOf()
            .xmap(list -> new TranscribedStones(Set.copyOf(list)), stones -> List.copyOf(stones.stones()));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<GlobalPos>> LIST_STREAM_CODEC =
            GlobalPos.STREAM_CODEC.apply(ByteBufCodecs.list()).mapStream(buf -> buf);

    public boolean contains(Level level, BlockPos pos)
    {
        return stones.contains(GlobalPos.of(level.dimension(), pos.immutable()));
    }

    public TranscribedStones with(Level level, BlockPos pos)
    {
        Set<GlobalPos> copy = new HashSet<>(stones);
        copy.add(GlobalPos.of(level.dimension(), pos.immutable()));
        return new TranscribedStones(Set.copyOf(copy));
    }

    // How the attachment is sent to the client: only to the player it belongs to. When a new copy arrives on the client,
    // the stones that changed are re-drawn so they switch to the blank look straight away.
    public static final AttachmentSyncHandler<TranscribedStones> SYNC = new AttachmentSyncHandler<>()
    {
        @Override
        public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to)
        {
            return holder == to;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf, TranscribedStones attachment, boolean initialSync)
        {
            LIST_STREAM_CODEC.encode(buf, List.copyOf(attachment.stones()));
        }

        @Override
        public @Nullable TranscribedStones read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable TranscribedStones previousValue)
        {
            TranscribedStones received = new TranscribedStones(Set.copyOf(LIST_STREAM_CODEC.decode(buf)));
            // read() only ever runs on the client, so it's safe to call client code here
            TranscribedStonesClient.redrawChanged(previousValue == null ? EMPTY : previousValue, received);
            return received;
        }
    };
}
