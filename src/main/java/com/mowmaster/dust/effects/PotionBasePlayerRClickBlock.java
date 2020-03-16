package com.mowmaster.dust.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public abstract class PotionBasePlayerRClickBlock extends Effect
{
    protected PotionBasePlayerRClickBlock(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    public abstract void tick(PlayerInteractEvent.RightClickBlock event);
}
