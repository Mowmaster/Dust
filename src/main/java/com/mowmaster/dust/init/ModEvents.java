package com.mowmaster.dust.init;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModEvents {


    @SubscribeEvent
    public void xp(PlayerPickupXpEvent event) {
        if (event.entityPlayer.getCurrentArmor(3) != null && event.entityPlayer.getCurrentArmor(2) != null && event.entityPlayer.getCurrentArmor(1) != null && event.entityPlayer.getCurrentArmor(0) != null) {
            if (event.entityPlayer.getCurrentArmor(3).getItem().equals(ModItems.GreenCrystalHelmet) && event.entityPlayer.getCurrentArmor(2).getItem().equals(ModItems.GreenCrystalChestplate) && event.entityPlayer.getCurrentArmor(1).getItem().equals(ModItems.GreenCrystalLeggings) && event.entityPlayer.getCurrentArmor(0).getItem().equals(ModItems.GreenCrystalBoots)) {
                event.orb.xpValue *= 2;
                //System.out.println("BLARG!");
            }
        }
    }

    @SubscribeEvent
    public void fire(TickEvent.PlayerTickEvent event) {

      if (event.player.isBurning()) {
          if (event.player.getCurrentArmor(3) != null && event.player.getCurrentArmor(2) != null && event.player.getCurrentArmor(1) != null && event.player.getCurrentArmor(0) != null) {
              if (event.player.getCurrentArmor(3).getItem().equals(ModItems.RedCrystalHelmet) && event.player.getCurrentArmor(2).getItem().equals(ModItems.RedCrystalChestplate) && event.player.getCurrentArmor(1).getItem().equals(ModItems.RedCrystalLeggings) && event.player.getCurrentArmor(0).getItem().equals(ModItems.RedCrystalBoots)) {
                  //System.out.println("STOP DROP AND ROLL!!!");
                  event.player.addPotionEffect(new PotionEffect(12, 100, 1));

              }
          }
      }
    }

    @SubscribeEvent
    public void water(TickEvent.PlayerTickEvent event) {

        if (event.player.isInWater()){
            if (event.player.getCurrentArmor(3) != null && event.player.getCurrentArmor(2) != null && event.player.getCurrentArmor(1) != null && event.player.getCurrentArmor(0) != null) {
                if (event.player.getCurrentArmor(3).getItem().equals(ModItems.BlueCrystalHelmet) && event.player.getCurrentArmor(2).getItem().equals(ModItems.BlueCrystalChestplate) && event.player.getCurrentArmor(1).getItem().equals(ModItems.BlueCrystalLeggings) && event.player.getCurrentArmor(0).getItem().equals(ModItems.BlueCrystalBoots)) {
                    //System.out.println("Glug Glug");
                    event.player.addPotionEffect(new PotionEffect(13, 100, 1));

                }
            }
        }
    }

    @SubscribeEvent
    public void sneaking(TickEvent.PlayerTickEvent event) {

        if (event.player.isSneaking()) {
            if (event.player.getCurrentArmor(3) != null && event.player.getCurrentArmor(2) != null && event.player.getCurrentArmor(1) != null && event.player.getCurrentArmor(0) != null) {
                if (event.player.getCurrentArmor(3).getItem().equals(ModItems.BlackCrystalHelmet) && event.player.getCurrentArmor(2).getItem().equals(ModItems.BlackCrystalChestplate) && event.player.getCurrentArmor(1).getItem().equals(ModItems.BlackCrystalLeggings) && event.player.getCurrentArmor(0).getItem().equals(ModItems.BlackCrystalBoots)) {
                    event.player.addPotionEffect(new PotionEffect(14, 20, 1));

                }
            }
        }
    }

}
