package com.mowmaster.dust.handlers;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by KingMowmaster on 3/26/2017.
 */
public class PlaceableCrystals
{
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event)
    {
        if(event.getEntityPlayer().isSneaking())
        {
            System.out.println("sneaky");



        }
    }
}
