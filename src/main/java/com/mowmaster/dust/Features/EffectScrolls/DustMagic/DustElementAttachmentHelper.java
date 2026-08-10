package com.mowmaster.dust.Features.EffectScrolls.DustMagic;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class DustElementAttachmentHelper
{

    public static void sync(ServerPlayer player)
    {
        player.syncData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS);
    }
    public static void sync(Player player)
    {
        player.syncData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS);
    }
    //Gets elements max including affinity
    public static int getElementMaximum(Player player, ElementEnum element)
    {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).effectiveCapacity(element);}

    public static int getElementAmount(Player player, ElementEnum element)
    {return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).get(element);}


    //Sets the base amount for all elements(affinity can increase this for one or two elements later)
    public static void setElementBaseMaximum(Player player, int val)
    {
        player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).setBaseCapacity(val);
        if(player instanceof ServerPlayer serverPlayer)sync(serverPlayer);
        else sync(player);
    }

    public static void addToElement(Player player, ElementEnum element, int val)
    {
        player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).add(element,val);
        if(player instanceof ServerPlayer serverPlayer)sync(serverPlayer);
        else sync(player);
    }

    public static void removeFromElement(Player player, ElementEnum element, int val)
    {
        player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).consume(element,val);
        if(player instanceof ServerPlayer serverPlayer)sync(serverPlayer);
        else sync(player);
    }

    public static void addToElementWithAffinityMultiplier(Player player, ElementEnum element, int val)
    {
        float multiplier = getAffinityMultiplier(player,element);
        player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).add(element,Math.round(multiplier * val));
        if(player instanceof ServerPlayer serverPlayer)sync(serverPlayer);
        else sync(player);
    }

    public static boolean hasAffinityWithElement(Player player, ElementEnum element)
    {
        return DustElementAttachmentHelper.hasAnyAffinityWithElement(player, element);
    }

    public static float getAffinityMultiplier(Player player, ElementEnum element)
    {
        if(hasAffinityWithElement(player, element))
        {
            //for a single affinity 2.0 - 0.5(reduction multi) gives us 1.5 bonus multiplier
            return 2.0f - DustElementAttachmentHelper.getAffinityCostReduction(player);
        }
        //returns 100% or 1.0 bonus multiplier
        return 1.0f;
    }

    public static Optional<DustMagicAffinityData> getAffinity(Player player)
    {
        return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).getAffinity();
    }

    public static boolean hasAnyAffinityWithElement(Player player, ElementEnum element)
    {
        return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).hasAnyAffinityTo(element);
    }

    public static void setAffinity(Player player, DustMagicAffinityData method)
    {
        player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).setAffinity(method);
        if(player instanceof ServerPlayer serverPlayer)sync(serverPlayer);
        else sync(player);
    }

    public static void setAffinityCapacityBonus(Player player, boolean isSingleAffinity)
    {
        //All elements have the same base values, affinities can boost these a bit, but we need to calculate out the bonuses
        int base = player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).getBaseElementCapacity();
        float affinityModifier = (isSingleAffinity)?(1.5f):(1.2f);
        int affinityCapacityValue = Math.round(base*affinityModifier);
        player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).setAffinityCapacityBonus(affinityCapacityValue);
        if(player instanceof ServerPlayer serverPlayer)sync(serverPlayer);
        else sync(player);
    }

    public static void setAffinityCostReduction(Player player, boolean isSingleAffinity)
    {
        //50% reduction for single affinity and 20% for each for dual affinity, a slight loss for multi affinities seems balanced
        float affinityModifier = (isSingleAffinity)?(0.5f):(0.8f);
        player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).setAffinityCostMultiplier(affinityModifier);
        if(player instanceof ServerPlayer serverPlayer)sync(serverPlayer);
        else sync(player);
    }

    public static float getAffinityCostReduction(Player player)
    {
        return player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTS).getAffinityCostMultiplier();
    }

}
