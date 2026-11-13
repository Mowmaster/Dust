package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.DustComposition.DustElements;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

// The five primal dusts are now the primal elements in the player's dust store (Features/Magic/PlayerMagicHelper);
// the per-primal methods below are kept for the older callers (the HUD meters, the tool spells, the commands).
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
    private static int getElementFireCount(Player player) {return PlayerMagicHelper.dust(player, DustElements.FIRE);}
    private static int getElementFireMaxCount(Player player) {return PlayerMagicHelper.get(player).dustCapacity();}

    public record ElementFireInfo(int count, int max) {}
    public static ElementFireInfo getElementInfoFire(Player player) {
        return new ElementFireInfo(getElementFireCount(player), getElementFireMaxCount(player));
    }

    public static int addToElementFire(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.addDust(player, DustElements.FIRE, val, simulate);
    }
    public static int removeFromElementFire(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.removeDust(player, DustElements.FIRE, val, simulate);
    }
    //WATER
    private static int getElementWaterCount(Player player) {return PlayerMagicHelper.dust(player, DustElements.WATER);}
    private static int getElementWaterMaxCount(Player player) {return PlayerMagicHelper.get(player).dustCapacity();}

    public record ElementWaterInfo(int count, int max) {}
    public static ElementWaterInfo getElementInfoWater(Player player) {
        return new ElementWaterInfo(getElementWaterCount(player), getElementWaterMaxCount(player));
    }

    public static int addToElementWater(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.addDust(player, DustElements.WATER, val, simulate);
    }
    public static int removeFromElementWater(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.removeDust(player, DustElements.WATER, val, simulate);
    }
    //EARTH
    private static int getElementEarthCount(Player player) {return PlayerMagicHelper.dust(player, DustElements.EARTH);}
    private static int getElementEarthMaxCount(Player player) {return PlayerMagicHelper.get(player).dustCapacity();}

    public record ElementEarthInfo(int count, int max) {}
    public static ElementEarthInfo getElementInfoEarth(Player player) {
        return new ElementEarthInfo(getElementEarthCount(player), getElementEarthMaxCount(player));
    }

    public static int addToElementEarth(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.addDust(player, DustElements.EARTH, val, simulate);
    }
    public static int removeFromElementEarth(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.removeDust(player, DustElements.EARTH, val, simulate);
    }
    //CHAOS
    private static int getElementChaosCount(Player player) {return PlayerMagicHelper.dust(player, DustElements.CHAOS);}
    private static int getElementChaosMaxCount(Player player) {return PlayerMagicHelper.get(player).dustCapacity();}

    public record ElementChaosInfo(int count, int max) {}
    public static ElementChaosInfo getElementInfoChaos(Player player) {
        return new ElementChaosInfo(getElementChaosCount(player), getElementChaosMaxCount(player));
    }

    public static int addToElementChaos(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.addDust(player, DustElements.CHAOS, val, simulate);
    }
    public static int removeFromElementChaos(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.removeDust(player, DustElements.CHAOS, val, simulate);
    }
    //ORDER
    private static int getElementOrderCount(Player player) {return PlayerMagicHelper.dust(player, DustElements.ORDER);}
    private static int getElementOrderMaxCount(Player player) {return PlayerMagicHelper.get(player).dustCapacity();}

    public record ElementOrderInfo(int count, int max) {}
    public static ElementOrderInfo getElementInfoOrder(Player player) {
        return new ElementOrderInfo(getElementOrderCount(player), getElementOrderMaxCount(player));
    }

    public static int addToElementOrder(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.addDust(player, DustElements.ORDER, val, simulate);
    }
    public static int removeFromElementOrder(Player player, int val, boolean simulate)
    {
        return PlayerMagicHelper.removeDust(player, DustElements.ORDER, val, simulate);
    }
}
