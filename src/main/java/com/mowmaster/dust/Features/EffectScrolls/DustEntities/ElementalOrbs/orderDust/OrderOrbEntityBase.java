package com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.orderDust;

import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementalOrbEntityBase;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class OrderOrbEntityBase extends ElementalOrbEntityBase {

    public OrderOrbEntityBase(EntityType<OrderOrbEntityBase> type, Level level) {
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
        int added = DustElementAttachmentHelper.addToElementOrder(serverPlayer, modifiedAmount, true);
        if(added>0)
        {
            actuallyAdded = DustElementAttachmentHelper.addToElementOrder(serverPlayer, modifiedAmount, false);
            System.out.println("Added By OrderOrb: " + actuallyAdded);
            this.discard();
        }
    }
}