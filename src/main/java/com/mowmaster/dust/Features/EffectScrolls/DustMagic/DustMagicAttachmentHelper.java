package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import net.minecraft.world.entity.player.Player;

public class DustMagicAttachmentHelper
{
    // Mana now lives in the player's PlayerMagic (Features/Magic); these keep the older callers (the HUD, the crystal
    // sword, the tool spells, the commands) working on it
    public static int getManaMaximum(Player player) {return PlayerMagicHelper.maxMana(player);}

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
        return true;
    }

    public static int getManaLevel(Player player)
    {
        return PlayerMagicHelper.mana(player);
    }

    public static void setMana(Player player, int val)
    {
        PlayerMagicHelper.setMana(player, val);
    }

    public static void addMana(Player player, int val)
    {
        PlayerMagicHelper.addMana(player, val);
    }

    public static int addManaWithConsumedCount(Player player, int val)
    {
        return PlayerMagicHelper.addMana(player, val);
    }

    public static boolean canAddMana(Player player, int val)
    {
        return getManaLevel(player) + val <= getManaMaximum(player);
    }

    public static void removeMana(Player player, int val)
    {
        PlayerMagicHelper.setMana(player, getManaLevel(player) - val);
    }

    public static boolean canRemoveMana(Player player, int val)
    {
        return getManaLevel(player) - val >= 0;
    }
}
