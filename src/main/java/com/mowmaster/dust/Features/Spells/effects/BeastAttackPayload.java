package com.mowmaster.dust.Features.Spells.effects;

import com.mowmaster.dust.DustReferences;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Client -> server: a player turned into a creature (Beastification) swung a weapon at the air (Client/
// BeastificationClient). The server checks the form and the weapon again, then does the creature's attack.
public record BeastAttackPayload() implements CustomPacketPayload
{
    public static final Type<BeastAttackPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "beast_attack"));
    public static final StreamCodec<ByteBuf, BeastAttackPayload> STREAM_CODEC = StreamCodec.unit(new BeastAttackPayload());

    @Override
    public Type<BeastAttackPayload> type()
    {
        return TYPE;
    }

    // Runs on the server's main thread
    public static void handle(BeastAttackPayload payload, IPayloadContext context)
    {
        if (!(context.player() instanceof ServerPlayer player) || player.isSpectator()) return;
        if (Beastification.formOf(player) == null || !player.getMainHandItem().has(DataComponents.WEAPON)) return;
        BeastAttacks.attack(player, null);
    }
}
