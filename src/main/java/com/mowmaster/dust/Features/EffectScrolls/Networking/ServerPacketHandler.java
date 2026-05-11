package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustDataGen.DustTags;
import com.mowmaster.dust.Features.EffectScrolls.AttachmentTypes.DustAttachmentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

//Server Side Packet Handler
public class ServerPacketHandler
{

    public static void handleTestPacket(DustPacketTestC2S dustTestPacket, IPayloadContext context) {
        Player player = context.player();
        ServerLevel level = ((ServerLevel) player.level());
        if(player.getOffhandItem().is(DustTags.Items.MAGICAL_DUST_ITEMS))
        {
            player.setData(DustAttachmentTypes.DUST_AURA,player.getData(DustAttachmentTypes.DUST_AURA)+dustTestPacket.value());
            player.getOffhandItem().shrink(dustTestPacket.value());
            player.sendSystemMessage(Component.literal("Aura Replentished by: " + dustTestPacket.value() + " Current Aura is: " + player.getData(DustAttachmentTypes.DUST_AURA)));
            //player.sendSystemMessage(Component.literal(dustTestPacket.name() + ", Has just sent a packet with value: " + dustTestPacket.value()));
        }

    }
}
