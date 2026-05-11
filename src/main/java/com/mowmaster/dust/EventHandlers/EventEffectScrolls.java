package com.mowmaster.dust.EventHandlers;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.EffectScrolls.AttachmentTypes.DustAttachmentTypes;
import com.mowmaster.dust.Features.EffectScrolls.Networking.DustPacketTestC2S;
import com.mowmaster.dust.Features.EffectScrolls.Networking.ServerPacketHandler;
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

        registrar.playToServer(DustPacketTestC2S.TYPE, DustPacketTestC2S.STREAM_CODEC, ServerPacketHandler::handleTestPacket);
    }

    @SubscribeEvent
    public static void setPlayerManaOnSpawn(PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = event.getEntity();
        if(player.hasData(DustAttachmentTypes.DUST_AURA))
        {
            player.setData(DustAttachmentTypes.DUST_AURA, player.getData(DustAttachmentTypes.DUST_AURA));
        }
        else {
            player.setData(DustAttachmentTypes.DUST_AURA, 5);
        }
    }

    @SubscribeEvent
    public static void setPlayersManaOnClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
        newPlayer.setData(DustAttachmentTypes.DUST_AURA, event.getOriginal().getData(DustAttachmentTypes.DUST_AURA));
    }

    @SubscribeEvent
    public static void setPlayersManaOnDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        player.setData(DustAttachmentTypes.DUST_AURA, player.getData(DustAttachmentTypes.DUST_AURA));
    }

    @SubscribeEvent
    public static void setPlayersManaOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        player.setData(DustAttachmentTypes.DUST_AURA, player.getData(DustAttachmentTypes.DUST_AURA));
    }
}
