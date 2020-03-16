package com.mowmaster.dust.events;

import com.mowmaster.dust.effects.PotionBasePlayerRClickBlock;
import com.mowmaster.dust.effects.PotionRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PotionEvent
{
    @SubscribeEvent
    public void onEntityUpdate(PlayerInteractEvent.RightClickBlock event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity == null) {
            return;
        }
        for (PotionBasePlayerRClickBlock effect : PotionRegistry.PotionEffects.effects) {
            if (effect != null && entity.isPotionActive(effect)) {
                effect.tick(event);
            }
        }
    }
}
