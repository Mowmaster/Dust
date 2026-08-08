package com.mowmaster.dust.Features.EffectScrolls.DustEntities.orderDust;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.baseOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.Networking.DustAuraPacketHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class OrderOrbEntity extends baseOrbEntity {

    public OrderOrbEntity(EntityType<? extends baseOrbEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void playerTouch(Player player) {
        if (!this.level().isClientSide()) {
            int orbValue = this.getValue();
            // Prevent unreasonable values
            orbValue = Math.clamp(orbValue, 1, 100);

            DustAuraPacketHelper.addAuraWithConsumedCount((ServerPlayer) player,5, orbValue);
            this.discard();
        }
        player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.1F, 0.9F + this.level().getRandom().nextFloat() * 0.2F);
    }
}