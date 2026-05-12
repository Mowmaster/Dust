package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustDataGen.DustTags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

//Server Side Packet Handler
public class PacketReceiverServer
{

    public static void handleTestPacket(PacketOfDustAuraC2S dustTestPacket, IPayloadContext context) {
        Player player = context.player();
        ServerLevel level = ((ServerLevel) player.level());
        if(player.getOffhandItem().is(DustTags.Items.MAGICAL_DUST_ITEMS) && DustAuraPacketHelper.canAddAura(((ServerPlayer) player),dustTestPacket.value()))
        {
            DustAuraPacketHelper.addAura(((ServerPlayer) player),dustTestPacket.value());
            player.getOffhandItem().shrink(dustTestPacket.value());
            player.sendOverlayMessage(Component.literal("Aura Replentished by: " + dustTestPacket.value()));
        }

    }
}
