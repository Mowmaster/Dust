package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class DustAuraPacketHelper
{
    public static final int maxAura = 10;

    public static void setAura(ServerPlayer player, int val)
    {
        player.setData(DustAttachmentTypeRegistry.DUST_AURA, val);
        //THIS ISNT NEEDED IF WE USE THE .sync Call in the Attachment Types Class
        PacketDistributor.sendToPlayer(player, new PacketOfDustAuraS2C(0, val));
    }

    public static void addAura(ServerPlayer player, int val)
    {
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) + val;
        if(newValue > maxAura) newValue = maxAura;
        setAura(player, newValue);
    }

    public static boolean canAddAura(ServerPlayer player, int val)
    {
        if(!player.hasData(DustAttachmentTypeRegistry.DUST_AURA))return false;
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) + val;
        if(newValue <= maxAura)
        {
            return true;
        }

        return false;
    }

    public static void removeAura(ServerPlayer player, int val)
    {
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) - val;
        setAura(player, newValue);
    }

    public static boolean canRemoveAura(ServerPlayer player, int val)
    {
        if(!player.hasData(DustAttachmentTypeRegistry.DUST_AURA))return false;
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) - val;
        if(newValue >= 0)
        {
            return true;
        }

        return false;
    }

    public static int getAuraLevel(ServerPlayer player)
    {
        if(!player.hasData(DustAttachmentTypeRegistry.DUST_AURA))return 0;
        return player.getData(DustAttachmentTypeRegistry.DUST_AURA);
    }

    public static boolean hasAura(ServerPlayer player)
    {
        if(!player.hasData(DustAttachmentTypeRegistry.DUST_AURA))return false;
        return (getAuraLevel(player)>0)?(true):(false);
    }
}
