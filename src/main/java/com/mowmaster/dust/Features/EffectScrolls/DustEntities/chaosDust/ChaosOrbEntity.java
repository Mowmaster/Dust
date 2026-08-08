package com.mowmaster.dust.Features.EffectScrolls.DustEntities.chaosDust;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.baseOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.Networking.DustAuraPacketHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ChaosOrbEntity extends baseOrbEntity {

    public ChaosOrbEntity(EntityType<? extends baseOrbEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void playerTouch(Player player) {
        if (!this.level().isClientSide()) {
            int orbValue = this.getValue();
            // Prevent unreasonable values
            orbValue = Math.clamp(orbValue, 1, 100);

            DustAuraPacketHelper.addAuraWithConsumedCount((ServerPlayer) player,4, orbValue);
            this.discard();
        }
        player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.1F, 0.9F + this.level().getRandom().nextFloat() * 0.2F);
    }
}