package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Client -> server: the player edited their notes for an entry in the book screen
public record ResearchNotesPayload(Identifier tab, Identifier id, String notes) implements CustomPacketPayload
{
    public static final Type<ResearchNotesPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "research_notes"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResearchNotesPayload> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ResearchNotesPayload::tab,
            Identifier.STREAM_CODEC, ResearchNotesPayload::id,
            ByteBufCodecs.stringUtf8(EntryResearch.MAX_NOTES_LENGTH * 4), ResearchNotesPayload::notes,
            ResearchNotesPayload::new);

    @Override
    public Type<ResearchNotesPayload> type()
    {
        return TYPE;
    }

    // Runs on the server's main thread. Only changes entries the player has started, and only with notes carried.
    public static void handle(ResearchNotesPayload payload, IPayloadContext context)
    {
        if (!(context.player() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        ResearchEntry entry = Research.byKey(payload.tab(), payload.id());
        if (entry != null) Research.setNotes(player, entry, payload.notes());
    }
}
