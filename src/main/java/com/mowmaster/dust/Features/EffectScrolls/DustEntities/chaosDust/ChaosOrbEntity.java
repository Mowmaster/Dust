package com.mowmaster.dust.Features.EffectScrolls.DustEntities.chaosDust;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.baseOrbEntity;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.EnumElement;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

public class ChaosOrbEntity extends baseOrbEntity {

    public ChaosOrbEntity(EntityType<? extends baseOrbEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void playerTouch(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (player.takeXpDelay == 0) {
                if (((PlayerXpEvent.PickupXp) NeoForge.EVENT_BUS.post(new PlayerXpEvent.PickupXp(player, this))).isCanceled()) {
                    return;
                }

                player.takeXpDelay = 2;
                player.take(this, 1);
                int remaining = this.repairPlayerItems(serverPlayer, this.getValue());
                if (remaining > 0) {
                    player.giveExperiencePoints(remaining);
                }

                --this.count;
                if (this.count == 0) {
                    this.discard();
                }
            }
        }

        /*if (this.level().isClientSide()) {
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
        boolean added = DustElementAttachmentHelper.addToElementChaos(serverPlayer, modifiedAmount, false)>0;
        if(added)
        {
            this.level().playSound(serverPlayer,serverPlayer.getOnPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.05F, 0.5F + this.level().getRandom().nextFloat() * 0.2F);
            this.discard();
        }
*/
    }
}