package com.mowmaster.dust.Features.CrystalTools.Item;

import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CrystalWeaponSword extends Item {
    public CrystalWeaponSword(Properties properties) {
        super(properties);
    }

    //Happens Durring hurt action
    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        super.hurtEnemy(itemStack, mob, attacker);
    }

    //Happens After Hurt action
    @Override
    public void postHurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {

        int ampAmount = 1;
        if(attacker instanceof ServerPlayer player)
        {
            if(DustMagicAttachmentHelper.canRemoveMana(player,ampAmount))
            {
                mob.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE,1,ampAmount), attacker);
                DustMagicAttachmentHelper.removeMana(player,ampAmount);
            }
        }
        super.postHurtEnemy(itemStack, mob, attacker);
    }
}
