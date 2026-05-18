package com.mowmaster.dust.Features.DustyDelights;

import com.mowmaster.dust.Features.EffectScrolls.DustEffects.AuraReplentishEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class DDFoodProperties
{
    public static final FoodProperties CORNBREAD = new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).alwaysEdible().build();
    public static final FoodProperties LETTUCE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.6F).build();

    public static final Consumable CORNBREAD_EDIBLE = Consumables.defaultFood()
            //.onConsume(new ApplyStatusEffectsConsumeEffect( new MobEffectInstance(MobEffects.SATURATION,1, 5), 0.45f))
            .onConsume(new AuraReplentishEffect())
            .build();
}
