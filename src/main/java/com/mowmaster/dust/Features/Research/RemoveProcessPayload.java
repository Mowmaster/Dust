package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Client -> server: the player crossed out a process step in their book (the "x" on the Processes page)
public record RemoveProcessPayload(Identifier tab, Identifier id, ProcessRecord process) implements CustomPacketPayload
{
    public static final Type<RemoveProcessPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "remove_process"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveProcessPayload> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, RemoveProcessPayload::tab,
            Identifier.STREAM_CODEC, RemoveProcessPayload::id,
            ProcessRecord.STREAM_CODEC, RemoveProcessPayload::process,
            RemoveProcessPayload::new);

    @Override
    public Type<RemoveProcessPayload> type()
    {
        return TYPE;
    }

    // Runs on the server's main thread. Only with Research Notes carried, like editing notes.
    public static void handle(RemoveProcessPayload payload, IPayloadContext context)
    {
        if (!(context.player() instanceof ServerPlayer player) || !Research.carriesNotes(player)) return;
        ResearchEntry entry = Research.byKey(payload.tab(), payload.id());
        if (entry != null) Research.removeProcess(player, entry, payload.process());
    }
}
