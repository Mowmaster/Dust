package com.mowmaster.dust.Features.CrystalTools.Item;

import com.mowmaster.dust.DustDataGen.DustTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import static net.minecraft.world.item.enchantment.Enchantments.SHARPNESS;

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

        if(attacker.getOffhandItem().is(DustTags.Items.MAGICAL_DUST_ITEMS))
        {
            int ampAmount = 1;
            if(attacker.getOffhandItem().getCount() > ampAmount)
            {
                mob.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE,1,ampAmount), attacker);
                attacker.getOffhandItem().shrink(ampAmount);
            }
        }
        super.postHurtEnemy(itemStack, mob, attacker);
    }
}
