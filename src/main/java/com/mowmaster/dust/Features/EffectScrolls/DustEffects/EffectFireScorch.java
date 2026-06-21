package com.mowmaster.dust.Features.EffectScrolls.DustEffects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;

import javax.xml.transform.Source;
import java.util.List;

public class EffectFireScorch extends InstantenousMobEffect {

    public EffectFireScorch(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {

        if (!mob.fireImmune())
        {
            int addedFireTicks = 20 * (amplification+1);
            if (mob.getRemainingFireTicks() < 0) {
                mob.setRemainingFireTicks(mob.getRemainingFireTicks() + addedFireTicks);
            } else {
                mob.setRemainingFireTicks(mob.getRemainingFireTicks() + addedFireTicks);
            }

            if (mob.getRemainingFireTicks() >= 0) {
                if(mob.getRemainingFireTicks() < 2000)
                {
                    mob.igniteForTicks(addedFireTicks);
                }
                else {
                    int exploRange = 3;
                    /*AABB rangeAABB = mob.getBoundingBox().inflate(20);
                    List<Entity> entities = serverLevel.getEntities(mob, rangeAABB);
                    for(Entity entity : entities)
                    {
                        if (entity.getRemainingFireTicks() < 0) {
                            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + addedFireTicks);
                        } else if (entity instanceof ServerPlayer) {
                            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + addedFireTicks);
                        }
                    }*/
                    mob.clearFire();
                    mob.level().explode(mob, mob.getX(), mob.getY(), mob.getZ(), (float)exploRange, Level.ExplosionInteraction.NONE);
                    mob.hurtServer(serverLevel, DustDamageTypes.create(serverLevel,DustDamageTypes.IGNITION_KEY), 10F);
                }
            }

            System.out.println("apply current fire ticks: " + mob.getRemainingFireTicks());
        }

        return true;
    }

    @Override
    public void applyInstantenousEffect(ServerLevel level, @Nullable Entity source, @Nullable Entity owner, LivingEntity mob, int amplification, double scale) {
        if (!mob.fireImmune())
        {
            int addedFireTicks = 20 * (amplification+1);
            if (mob.getRemainingFireTicks() < 0) {
                mob.setRemainingFireTicks(mob.getRemainingFireTicks() + addedFireTicks);
            } else {
                mob.setRemainingFireTicks(mob.getRemainingFireTicks() + addedFireTicks);
            }

            if (mob.getRemainingFireTicks() >= 0) {
                if(mob.getRemainingFireTicks() < 2000)
                {
                    mob.igniteForTicks(addedFireTicks);
                }
                else {
                    int exploRange = 3;
                    /*AABB rangeAABB = mob.getBoundingBox().inflate(20);
                    List<Entity> entities = serverLevel.getEntities(mob, rangeAABB);
                    for(Entity entity : entities)
                    {
                        if (entity.getRemainingFireTicks() < 0) {
                            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + addedFireTicks);
                        } else if (entity instanceof ServerPlayer) {
                            entity.setRemainingFireTicks(entity.getRemainingFireTicks() + addedFireTicks);
                        }
                    }*/
                    mob.clearFire();
                    mob.level().explode(mob, mob.getX(), mob.getY(), mob.getZ(), (float)exploRange, Level.ExplosionInteraction.NONE);
                    mob.hurtServer(level, mob.getLastDamageSource(), 10F);
                }
            }

            System.out.println("instant current fire ticks: " + mob.getRemainingFireTicks());
        }
    }
}
