package com.mowmaster.dust.Features.EffectScrolls.DustEffects;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.chaosDust.ChaosOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.earthDust.EarthOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.fireDust.FireOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.orderDust.OrderOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.waterDust.WaterOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.ElementEnum;
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
                    DustElementAttachmentHelper.addToElementWithAffinityMultiplier(player, ElementEnum.FIRE,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof WaterOrbEntity magicOrb)
                {
                    DustElementAttachmentHelper.addToElementWithAffinityMultiplier(player, ElementEnum.WATER,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof EarthOrbEntity magicOrb)
                {
                    DustElementAttachmentHelper.addToElementWithAffinityMultiplier(player, ElementEnum.EARTH,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof ChaosOrbEntity magicOrb)
                {
                    DustElementAttachmentHelper.addToElementWithAffinityMultiplier(player, ElementEnum.CHAOS,magicOrb.getValue());
                    magicOrb.remove(Entity.RemovalReason.DISCARDED);
                }
                if(entity instanceof OrderOrbEntity magicOrb)
                {
                    DustElementAttachmentHelper.addToElementWithAffinityMultiplier(player, ElementEnum.ORDER,magicOrb.getValue());
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