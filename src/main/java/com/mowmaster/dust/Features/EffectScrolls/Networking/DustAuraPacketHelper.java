package com.mowmaster.dust.Features.EffectScrolls.Networking;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class DustAuraPacketHelper
{
    public static final int maxAura = 50;
    public static final int maxMagic = 20;

    public static int getAuraLimit(Player player)
    {
        return maxAura;
    }

    public static int getMagicLimit(Player player, int type)
    {
        return maxMagic;
    }

    public static void setAura(ServerPlayer player, int type, int val)
    {
        if(type==0)player.setData(DustAttachmentTypeRegistry.DUST_AURA, val);
        else if(type==1)player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE, val);
        else if(type==2)player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER, val);
        else if(type==3)player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH, val);
        else if(type==4)player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS, val);
        else if(type==5)player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER, val);

        //THIS ISNT NEEDED IF WE USE THE .sync Call in the Attachment Types Class
        PacketDistributor.sendToPlayer(player, new PacketOfDustAuraS2C(0, val));
    }

    public static void addAura(ServerPlayer player, int type, int val)
    {
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) + val;
        int max = maxMagic;
        if(type==0){
            newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) + val;
            max = maxAura;
        }
        else if(type==1)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE) + val;
        else if(type==2)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER) + val;
        else if(type==3)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH) + val;
        else if(type==4)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS) + val;
        else if(type==5)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER) + val;

        if(newValue > max) newValue = max;
        setAura(player, type, newValue);
    }

    public static int addAuraWithConsumedCount(ServerPlayer player, int type, int val)
    {
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) + val;
        int max = maxMagic;
        if(type==0){
            newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) + val;
            max = maxAura;
        }
        else if(type==1)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE) + val;
        else if(type==2)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER) + val;
        else if(type==3)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH) + val;
        else if(type==4)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS) + val;
        else if(type==5)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER) + val;
        int consumedCount = 0;
        if(newValue > max)
        {
            newValue = max;
            consumedCount = max - getAuraLevel(player, type);
        }
        else consumedCount = val;

        if(consumedCount>0)setAura(player, type, newValue);
        return consumedCount;
    }

    public static boolean canAddAura(ServerPlayer player, int type, int val)
    {
        if(!player.hasData(DustAttachmentTypeRegistry.DUST_AURA))return false;
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) + val;
        int max = maxMagic;
        if(type==0){
            newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) + val;
            max = maxAura;
        }
        else if(type==1)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE) + val;
        else if(type==2)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER) + val;
        else if(type==3)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH) + val;
        else if(type==4)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS) + val;
        else if(type==5)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER) + val;
        if(newValue <= max)
        {
            return true;
        }

        return false;
    }

    public static void removeAura(ServerPlayer player, int type, int val)
    {
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) - val;
        if(type==0)newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) - val;
        else if(type==1)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE) - val;
        else if(type==2)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER) - val;
        else if(type==3)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH) - val;
        else if(type==4)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS) - val;
        else if(type==5)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER) - val;
        setAura(player, type, newValue);
    }

    public static boolean canRemoveAura(ServerPlayer player, int type, int val)
    {
        if(!player.hasData(DustAttachmentTypeRegistry.DUST_AURA))return false;
        int newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) - val;
        if(type==0)newValue = player.getData(DustAttachmentTypeRegistry.DUST_AURA) - val;
        else if(type==1)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE) - val;
        else if(type==2)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER) - val;
        else if(type==3)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH) - val;
        else if(type==4)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS) - val;
        else if(type==5)newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER) - val;
        if(newValue >= 0)
        {
            return true;
        }

        return false;
    }

    public static int getAuraLevel(ServerPlayer player, int type)
    {
        if(!player.hasData(DustAttachmentTypeRegistry.DUST_AURA))return 0;
        if(type==0)return player.getData(DustAttachmentTypeRegistry.DUST_AURA);
        else if(type==1)return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE);
        else if(type==2)return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER);
        else if(type==3)return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH);
        else if(type==4)return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS);
        else if(type==5)return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER);

        return player.getData(DustAttachmentTypeRegistry.DUST_AURA);
    }

    public static boolean hasAura(ServerPlayer player, int type)
    {
        if(!player.hasData(DustAttachmentTypeRegistry.DUST_AURA))return false;
        return (getAuraLevel(player, type)>0)?(true):(false);
    }
}
