package com.mowmaster.dust.Features.Spells.grimoire;

import com.mowmaster.dust.DustReferences;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Client -> server: a grimoire key was pressed (Client/GrimoireKeys). The server finds the player's grimoire itself
// (GrimoireItem.find) and checks everything again; the client only says which key.
//   action CAST    cast page `value`, or the selected page if value is -1
//   action TURN    turn to the next (+1) or previous (-1) spell, and say which it is now
public record GrimoirePayload(int action, int value) implements CustomPacketPayload
{
    public static final int CAST = 0;
    public static final int TURN = 1;
    public static final int SELECTED = -1;

    public static final Type<GrimoirePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(DustReferences.MODID, "grimoire"));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrimoirePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, GrimoirePayload::action,
            ByteBufCodecs.VAR_INT, GrimoirePayload::value,
            GrimoirePayload::new);

    @Override
    public Type<GrimoirePayload> type()
    {
        return TYPE;
    }

    // Runs on the server's main thread
    public static void handle(GrimoirePayload payload, IPayloadContext context)
    {
        if (!(context.player() instanceof ServerPlayer player) || player.isSpectator()) return;
        ItemStack stack = GrimoireItem.find(player);
        if (!(stack.getItem() instanceof GrimoireItem grimoire)) return;
        switch (payload.action()) {
            case CAST -> {
                int page = payload.value() == SELECTED ? grimoire.getPage(stack) : payload.value();
                if (page >= 0 && page < grimoire.getPageCount(stack)) grimoire.castPage(player, stack, page);
            }
            case TURN -> {
                int direction = payload.value() < 0 ? -1 : 1;
                grimoire.setPage(stack, grimoire.nextPage(stack, direction));
                player.sendOverlayMessage(grimoire.describe(stack, grimoire.getPage(stack)));
            }
            default -> {}
        }
    }
}
