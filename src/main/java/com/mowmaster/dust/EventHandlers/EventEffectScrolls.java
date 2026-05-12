package com.mowmaster.dust.EventHandlers;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.EffectScrolls.Networking.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = DustReferences.MODID)
public class EventEffectScrolls {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(PacketOfDustAuraC2S.TYPE, PacketOfDustAuraC2S.STREAM_CODEC, PacketReceiverServer::handleTestPacket);
        registrar.playToClient(PacketOfDustAuraS2C.TYPE, PacketOfDustAuraS2C.STREAM_CODEC, PacketReceiverClient::handleClientPacket);
    }

    @SubscribeEvent
    public static void setPlayerManaOnSpawn(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getEntity();
        if(player.hasData(DustAttachmentTypeRegistry.DUST_AURA))
        {
            DustAuraPacketHelper.setAura((ServerPlayer) player, player.getData(DustAttachmentTypeRegistry.DUST_AURA));
        }
        else {
            DustAuraPacketHelper.setAura((ServerPlayer) player, 0);
        }
    }

    @SubscribeEvent
    public static void setPlayersManaOnClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
        DustAuraPacketHelper.setAura((ServerPlayer) newPlayer, event.getOriginal().getData(DustAttachmentTypeRegistry.DUST_AURA));
    }

    @SubscribeEvent
    public static void setPlayersManaOnDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        DustAuraPacketHelper.setAura((ServerPlayer) player, player.getData(DustAttachmentTypeRegistry.DUST_AURA));
    }

    @SubscribeEvent
    public static void setPlayersManaOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        DustAuraPacketHelper.setAura((ServerPlayer) player, player.getData(DustAttachmentTypeRegistry.DUST_AURA));
    }
}
