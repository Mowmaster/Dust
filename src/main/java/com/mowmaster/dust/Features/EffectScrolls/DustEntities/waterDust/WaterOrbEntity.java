package com.mowmaster.dust.Features.EffectScrolls.DustEntities.waterDust;

import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.baseOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.chaosDust.ChaosOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WaterOrbEntity extends baseOrbEntity {

    public WaterOrbEntity(EntityType<WaterOrbEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void playerTouch(Player player) {
        int actuallyAdded = 0;
        if(this.level().isClientSide()) {
            //player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.05F, 0.5F + this.level().getRandom().nextFloat() * 0.2F);
            this.discard();
            return;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            this.discard();
            return;
        }

        if (!DustMagicAttachmentHelper.hasMana(serverPlayer)){
            this.discard();
            return;
        }

        int orbValue = this.getValue();
        // Prevent unreasonable values
        orbValue = Math.clamp(orbValue, 1, 100);
        int modifiedAmount = Math.round(orbValue);
        int added = DustElementAttachmentHelper.addToElementWater(serverPlayer, modifiedAmount, true);
        if(added>0)
        {
            actuallyAdded = DustElementAttachmentHelper.addToElementWater(serverPlayer, modifiedAmount, false);
            System.out.println("Added By WaterOrb: " + actuallyAdded);
            this.discard();
        }
    }
}