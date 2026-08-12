package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

//Client Side Packet Handler
public class PacketReceiverClient
{

    public static void handleClientPacket(PacketOfDustAuraS2C dustClientPacket, IPayloadContext context) {
        Player player = context.player();
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA, dustClientPacket.newValue());
    }
    public static void handleCPElementFire(S2CPacketElementFire dustClientPacket, IPayloadContext context) {
        Player player = context.player();
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE, dustClientPacket.newValue());
    }
    public static void handleCPElementWater(S2CPacketElementWater dustClientPacket, IPayloadContext context) {
        Player player = context.player();
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER, dustClientPacket.newValue());
    }
    public static void handleCPElementEarth(S2CPacketElementEarth dustClientPacket, IPayloadContext context) {
        Player player = context.player();
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH, dustClientPacket.newValue());
    }
    public static void handleCPElementChaos(S2CPacketElementChaos dustClientPacket, IPayloadContext context) {
        Player player = context.player();
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS, dustClientPacket.newValue());
    }
    public static void handleCPElementOrder(S2CPacketElementOrder dustClientPacket, IPayloadContext context) {
        Player player = context.player();
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER, dustClientPacket.newValue());
    }
}
