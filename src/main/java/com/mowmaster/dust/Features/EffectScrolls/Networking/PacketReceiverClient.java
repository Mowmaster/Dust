package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

//Client Side Packet Handler
public class PacketReceiverClient
{

    public static void handleClientPacket(PacketOfDustAuraS2C dustClientPacket, IPayloadContext context) {
        Player player = context.player();
        player.setData(DustAttachmentTypeRegistry.DUST_AURA, dustClientPacket.newValue());
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE, dustClientPacket.newValue());
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER, dustClientPacket.newValue());
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH, dustClientPacket.newValue());
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS, dustClientPacket.newValue());
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER, dustClientPacket.newValue());

    }
}
