package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.EffectScrolls.Networking.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class DustElementAttachmentHelper
{
    //ELEMENT HANDLER

    //AFFINITY
    private static boolean hasAffinity(Player player) {return player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY);}
    public static EnumAffinity getAffinity(Player player)
    {
        if(hasAffinity(player)) return EnumAffinity.valueOf(player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY));
        else return EnumAffinity.valueOf("none");
    }
    private static List<EnumAffinity> getAffinityBreakdown(Player player, EnumAffinity affinity)
    {
        List<String> composition = affinity.getAffinityComposition();
        List<EnumAffinity> returnList = new ArrayList<>();
        for(String comp : composition) {returnList.add(EnumAffinity.valueOf(comp));}
        return returnList;
    }
    private static float getAffinityDiscount(EnumAffinity affinity) {return affinity.getElementDiscount();}
    private static float getAffinityMultiplier(EnumAffinity affinity) {return affinity.getElementMultiplier();}

    private static void setAffinity(Player player, EnumAffinity affinity) {player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_AFFINITY, affinity.name());}
    private static EnumAffinity composeAffinity(EnumAffinity first, EnumAffinity second)
    {
        EnumAffinity newAffinity = EnumAffinity.NONE;
        for(EnumAffinity aff : EnumAffinity.values())
        {
            if(aff.getAffinityTier() >= 2)
            {
                if(aff.getAffinityComposition().contains(first.name()))
                {
                    if(aff.getAffinityComposition().contains(second.name()))return aff;
                }
            }
        }
        return newAffinity;
    }
    private static EnumAffinity decomposeTieredAffinity(EnumAffinity tieredAffinity, EnumAffinity affinityToRemove)
    {
        EnumAffinity newAffinity = EnumAffinity.NONE;
        List<String> composition = tieredAffinity.getAffinityComposition();
        if(EnumAffinity.valueOf(composition.getFirst()).equals(affinityToRemove))return EnumAffinity.valueOf(composition.getLast());
        else if(EnumAffinity.valueOf(composition.getLast()).equals(affinityToRemove)){
            return EnumAffinity.valueOf(composition.getFirst());
        }
        return newAffinity;
    }

    public static boolean addAffinity(Player player, EnumAffinity affinityToAdd)
    {
        EnumAffinity currentPlayerAffinity = getAffinity(player);
        List<EnumAffinity> decomposeAffinity = getAffinityBreakdown(player,currentPlayerAffinity);
        if(decomposeAffinity.size() > 1)
        {
            return false;
        }
        else if(decomposeAffinity.getFirst().equals(EnumAffinity.NONE))
        {
            setAffinity(player,affinityToAdd);
            return true;
        }
        else
        {
            EnumAffinity currentAffinity = decomposeAffinity.getFirst();
            EnumAffinity newAffinity = composeAffinity(currentAffinity, affinityToAdd);
            if(!newAffinity.equals(EnumAffinity.NONE)){
                setAffinity(player,newAffinity);
                return true;
            }
            else return false;
        }
    }

    public static boolean removeAffinity(Player player, EnumAffinity affinityToRemove)
    {
        EnumAffinity currentPlayerAffinity = getAffinity(player);
        List<EnumAffinity> decomposeAffinity = getAffinityBreakdown(player,currentPlayerAffinity);
        if(decomposeAffinity.size() > 1)
        {
            EnumAffinity newAffinity = decomposeTieredAffinity(currentPlayerAffinity,affinityToRemove);
            if(!newAffinity.equals(EnumAffinity.NONE)){
                setAffinity(player,newAffinity);
                return true;
            }
            else return false;
        }
        else
        {
            setAffinity(player,EnumAffinity.NONE);
            return true;
        }
    }


    public record AffinityInfo(List<EnumAffinity> affinity, float disount, float multipler) {}
    public AffinityInfo getAffinityInfo(Player player)
    {
        List<EnumAffinity> aff = new ArrayList<>();
        //1.0 = 100% aka no discount or multiplier
        float dis = 1.0f;
        float mult = 1.0f;
        if(hasAffinity(player))
        {
            EnumAffinity affinity = getAffinity(player);
            aff = getAffinityBreakdown(player, affinity);
            dis = getAffinityDiscount(affinity);
            mult = getAffinityMultiplier(affinity);
        }
        else {
            aff.add(EnumAffinity.NONE);
        }
        return new AffinityInfo(aff, dis, mult);
    }
    /*
    In order to retrieve this info
    ElementFireInfo result = getElementFireInfo(player);
    int count = result.count();  // Direct access via accessor methods
    int max = result.max();
    */

    //FIRE
    private static boolean hasElementFire(Player player) {return player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE);}
    private static int getElementFireCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE);}
    private static int getElementFireMaxCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE);}
    private static void setElementFireCount(Player player, int val) {
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_FIRE, val);
        PacketDistributor.sendToPlayer((ServerPlayer)player, new S2CPacketElementFire(0, val));
    }

    public record ElementFireInfo(int count, int max) {}
    public static ElementFireInfo getElementFireInfo(Player player) {
        return new ElementFireInfo(getElementFireCount(player), getElementFireMaxCount(player));
    }

    public static int addToElementFire(Player player, int val, boolean simulate)
    {
        if(!hasElementFire(player))return 0;
        int currentCount = getElementFireCount(player);
        int newValue = currentCount + val;
        int max = getElementFireMaxCount(player);
        if(newValue > max) newValue = max;
        if(!simulate)setElementFireCount(player, newValue);
        //Return Count Added
        return newValue - currentCount;
    }
    public static int removeFromElementFire(Player player, int val, boolean simulate)
    {
        if(!hasElementFire(player))return 0;
        int currentCount = getElementFireCount(player);
        int newValue = currentCount - val;
        if(val > currentCount) newValue = 0;
        if(!simulate)setElementFireCount(player, newValue);
        //Return Count removed
        return currentCount - newValue;
    }
    //WATER
    private static boolean hasElementWater(Player player) {return player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER);}
    private static int getElementWaterCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER);}
    private static int getElementWaterMaxCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER);}
    private static void setElementWaterCount(Player player, int val) {
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_WATER, val);
        PacketDistributor.sendToPlayer((ServerPlayer)player, new S2CPacketElementWater(0, val));
    }

    public record ElementWaterInfo(int count, int max) {}
    public static ElementWaterInfo getElementWaterInfo(Player player) {
        return new ElementWaterInfo(getElementWaterCount(player), getElementWaterMaxCount(player));
    }

    public static int addToElementWater(Player player, int val, boolean simulate)
    {
        if(!hasElementWater(player))return 0;
        int currentCount = getElementWaterCount(player);
        int newValue = currentCount + val;
        int max = getElementWaterMaxCount(player);
        if(newValue > max) newValue = max;
        if(!simulate)setElementWaterCount(player, newValue);
        return newValue - currentCount;
    }
    public static int removeFromElementWater(Player player, int val, boolean simulate)
    {
        if(!hasElementWater(player))return 0;
        int currentCount = getElementWaterCount(player);
        int newValue = currentCount - val;
        if(val > currentCount) newValue = 0;
        if(!simulate)setElementWaterCount(player, newValue);
        return currentCount - newValue;
    }
    //EARTH
    private static boolean hasElementEarth(Player player) {return player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH);}
    private static int getElementEarthCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH);}
    private static int getElementEarthMaxCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH);}
    private static void setElementEarthCount(Player player, int val) {
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_EARTH, val);
        PacketDistributor.sendToPlayer((ServerPlayer)player, new S2CPacketElementEarth(0, val));
    }

    public record ElementEarthInfo(int count, int max) {}
    public static ElementEarthInfo getElementEarthInfo(Player player) {
        return new ElementEarthInfo(getElementEarthCount(player), getElementEarthMaxCount(player));
    }

    public static int addToElementEarth(Player player, int val, boolean simulate)
    {
        if(!hasElementEarth(player))return 0;
        int currentCount = getElementEarthCount(player);
        int newValue = currentCount + val;
        int max = getElementEarthMaxCount(player);
        if(newValue > max) newValue = max;
        if(!simulate)setElementEarthCount(player, newValue);
        return newValue - currentCount;
    }
    public static int removeFromElementEarth(Player player, int val, boolean simulate)
    {
        if(!hasElementEarth(player))return 0;
        int currentCount = getElementEarthCount(player);
        int newValue = currentCount - val;
        if(val > currentCount) newValue = 0;
        if(!simulate)setElementEarthCount(player, newValue);
        return currentCount - newValue;
    }
    //CHAOS
    private static boolean hasElementChaos(Player player) {return player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS);}
    private static int getElementChaosCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS);}
    private static int getElementChaosMaxCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS);}
    private static void setElementChaosCount(Player player, int val) {
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_CHAOS, val);
        PacketDistributor.sendToPlayer((ServerPlayer)player, new S2CPacketElementChaos(0, val));
    }

    public record ElementChaosInfo(int count, int max) {}
    public static ElementChaosInfo getElementChaosInfo(Player player) {
        return new ElementChaosInfo(getElementChaosCount(player), getElementChaosMaxCount(player));
    }

    public static int addToElementChaos(Player player, int val, boolean simulate)
    {
        if(!hasElementChaos(player))return 0;
        int currentCount = getElementChaosCount(player);
        int newValue = currentCount + val;
        int max = getElementChaosMaxCount(player);
        if(newValue > max) newValue = max;
        if(!simulate)setElementChaosCount(player, newValue);
        return newValue - currentCount;
    }
    public static int removeFromElementChaos(Player player, int val, boolean simulate)
    {
        if(!hasElementChaos(player))return 0;
        int currentCount = getElementChaosCount(player);
        int newValue = currentCount - val;
        if(val > currentCount) newValue = 0;
        if(!simulate)setElementChaosCount(player, newValue);
        return currentCount - newValue;
    }
    //ORDER
    private static boolean hasElementOrder(Player player) {return player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER);}
    private static int getElementOrderCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER);}
    private static int getElementOrderMaxCount(Player player) {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER);}
    private static void setElementOrderCount(Player player, int val) {
        player.setData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTCOUNT_ORDER, val);
        PacketDistributor.sendToPlayer((ServerPlayer)player, new S2CPacketElementOrder(0, val));
    }

    public record ElementOrderInfo(int count, int max) {}
    public static ElementOrderInfo getElementOrderInfo(Player player) {
        return new ElementOrderInfo(getElementOrderCount(player), getElementOrderMaxCount(player));
    }

    public static int addToElementOrder(Player player, int val, boolean simulate)
    {
        if(!hasElementOrder(player))return 0;
        int currentCount = getElementOrderCount(player);
        int newValue = currentCount + val;
        int max = getElementOrderMaxCount(player);
        if(newValue > max) newValue = max;
        if(!simulate)setElementOrderCount(player, newValue);
        return newValue - currentCount;
    }
    public static int removeFromElementOrder(Player player, int val, boolean simulate)
    {
        if(!hasElementOrder(player))return 0;
        int currentCount = getElementOrderCount(player);
        int newValue = currentCount - val;
        if(val > currentCount) newValue = 0;
        if(!simulate)setElementOrderCount(player, newValue);
        return currentCount - newValue;
    }
}
