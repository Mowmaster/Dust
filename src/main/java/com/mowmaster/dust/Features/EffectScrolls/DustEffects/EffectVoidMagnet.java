package com.mowmaster.dust.Features.EffectScrolls.DustEffects;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.chaosDust.ChaosOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.earthDust.EarthOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.fireDust.FireOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.orderDust.OrderOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.waterDust.WaterOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.Networking.DustAuraPacketHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EffectVoidMagnet extends MobEffect {

    public EffectVoidMagnet(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
        if(mob instanceof ServerPlayer player)
        {
            AABB range = player.getBoundingBox().inflate(amplification+1);
            List<Entity> entities = serverLevel.getEntities(player, range);
            for(Entity entity : entities)
            {
                if(entity instanceof ItemEntity itemEntity)
                {
                    player.getInventory().add(itemEntity.getItem());
                    itemEntity.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof ExperienceOrb experienceOrb)
                {
                    player.giveExperiencePoints(experienceOrb.getValue());
                    experienceOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof FireOrbEntity magicOrb)
                {
                    DustAuraPacketHelper.addAura(player,1,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof WaterOrbEntity magicOrb)
                {
                    DustAuraPacketHelper.addAura(player,2,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof EarthOrbEntity magicOrb)
                {
                    DustAuraPacketHelper.addAura(player,3,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof ChaosOrbEntity magicOrb)
                {
                    DustAuraPacketHelper.addAura(player,4,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof OrderOrbEntity magicOrb)
                {
                    DustAuraPacketHelper.addAura(player,5,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }


        return super.applyEffectTick(serverLevel, mob, amplification);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        return true;
    }

}