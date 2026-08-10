package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustDataGen.DustTags;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
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
        if(player.getOffhandItem().is(DustTags.Items.MAGICAL_DUST_ITEMS) && DustMagicAttachmentHelper.canAddMana(player,dustTestPacket.value()))
        {
            DustMagicAttachmentHelper.addMana(player,dustTestPacket.value());
            player.getOffhandItem().shrink(dustTestPacket.value());
            player.sendOverlayMessage(Component.literal("Aura Replentished by: " + dustTestPacket.value()));
        }

    }
}
