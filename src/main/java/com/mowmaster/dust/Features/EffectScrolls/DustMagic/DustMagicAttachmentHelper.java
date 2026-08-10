package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.EffectScrolls.Networking.PacketOfDustAuraS2C;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class DustMagicAttachmentHelper
{
    public static int getManaMaximum(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA);}

    public static boolean hasUnlockedMana(Player player)
    {
        if(player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_UNLOCKED)) return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_UNLOCKED.get());
        else return false;
    }

    public static void unlockMana(Player player)
    {
        if (!hasUnlockedMana(player)) {
            {
                player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_UNLOCKED,Boolean.TRUE);
            }
        }
    }

    //Only used for initializations
    public static void lockMana(Player player)
    {
        if (hasUnlockedMana(player)) {
            player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_UNLOCKED,Boolean.FALSE);
        }
    }

    public static boolean hasMana(Player player)
    {
        return player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA);
    }

    public static int getManaLevel(Player player)
    {
        if(!hasMana(player))return 0;
        return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA);
    }

    public static void setMana(Player player, int val)
    {
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA, val);
        //THIS ISNT NEEDED IF WE USE THE .sync Call in the Attachment Types Class
        PacketDistributor.sendToPlayer((ServerPlayer)player, new PacketOfDustAuraS2C(0, val));
    }

    public static void addMana(Player player, int val)
    {
        int newValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA) + val;
        int max = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA);
        if(newValue > max) newValue = max;
        setMana(player, newValue);
    }

    public static int addManaWithConsumedCount(Player player, int val)
    {
        int currentValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA);
        int newValue = currentValue + val;
        int max = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA);

        int consumedCount = 0;
        if(newValue > max)
        {
            newValue = max;
            consumedCount = max - getManaLevel(player);
        }
        else consumedCount = val;

        if(consumedCount>0)setMana(player, newValue);
        return consumedCount;
    }

    public static boolean canAddMana(Player player, int val)
    {
        if(!hasMana(player))return false;
        int currentValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA);
        int newValue = currentValue + val;
        int max = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MAXMANA);

        return newValue <= max;
    }

    public static void removeMana(Player player, int val)
    {
        int currentValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA);
        int newValue = currentValue - val;
        setMana(player, newValue);
    }

    public static boolean canRemoveMana(Player player, int val)
    {
        if(!hasMana(player))return false;
        int currentValue = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_MANA);
        int newValue = currentValue - val;

        return newValue >= 0;
    }
}
