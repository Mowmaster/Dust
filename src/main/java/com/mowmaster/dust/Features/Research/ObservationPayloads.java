package com.mowmaster.dust.Features.Research;

import com.mowmaster.dust.DustReferences;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

// Server -> client messages about observing (handled in Client/ObservationClient, registered in DustClient).
public final class ObservationPayloads
{
    private ObservationPayloads() {}

    // How far along the current observation is: 0-1, or below 0 when the player stops observing it.
    // Sent every few ticks, only while there's something new to learn about what's being looked at.
    public record Progress(Identifier tab, Identifier id, float progress) implements CustomPacketPayload
    {
        public static final Type<Progress> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "observation_progress"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Progress> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, Progress::tab,
                Identifier.STREAM_CODEC, Progress::id,
                ByteBufCodecs.FLOAT, Progress::progress,
                Progress::new);

        public static Progress stop()
        {
            return new Progress(Identifier.withDefaultNamespace("none"), Identifier.withDefaultNamespace("none"), -1F);
        }

        @Override
        public Type<Progress> type()
        {
            return TYPE;
        }
    }

    // Something new was added to an entry that was already started: a recipe (recipe = true) or other information
    public record Noted(boolean recipe) implements CustomPacketPayload
    {
        public static final Type<Noted> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "research_noted"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Noted> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, Noted::recipe,
                Noted::new);

        @Override
        public Type<Noted> type()
        {
            return TYPE;
        }
    }

    // An observation finished: a new entry (newBiome false), or a known entry seen somewhere new (newBiome true)
    public record Discovered(Identifier tab, Identifier id, boolean newBiome) implements CustomPacketPayload
    {
        public static final Type<Discovered> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "research_discovered"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Discovered> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, Discovered::tab,
                Identifier.STREAM_CODEC, Discovered::id,
                ByteBufCodecs.BOOL, Discovered::newBiome,
                Discovered::new);

        @Override
        public Type<Discovered> type()
        {
            return TYPE;
        }
    }
}
