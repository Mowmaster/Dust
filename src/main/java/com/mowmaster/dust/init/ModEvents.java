package com.mowmaster.dust.init;

import ibxm.Player;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by KingMowmaster on 1/22/2016.
 */
public class ModEvents {

// many event priorities, Highest runs first, Lowest runs last,Normal is DEFAULT if none specified,this is a way to order events...
    // @SubscribeEvent(priority = EventPriority.NORMAL)
   // public void onToolUse(ItemTooltipEvent event)
   // {
    // EntityPlayer.addPotionEffect((new PotionEffect(Potion.moveSlowdown.getId(),1,5)));
   //     System.out.println("Test");
   // }
    @SubscribeEvent
    public void onToolUse(PlayerUseItemEvent.Tick event)
    {
        if(event.item.getItem() == Items.bow) {

            System.out.println("Test");
        }
    }
}
