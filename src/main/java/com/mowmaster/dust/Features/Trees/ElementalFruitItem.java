package com.mowmaster.dust.Features.Trees;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Consumer;

// An elemental tree's magical fruit (picked from an ElementalFruitBlock). Eaten like an apple, it also gives its
// element's affinity for 5 minutes (FruitAffinity: at the one-affinity bonuses, on top of any attunement), replacing
// any fruit affinity already running. Six of them and a bowl make a Fruit Medley (FruitMedleyRecipe).
public class ElementalFruitItem extends Item
{
    public static final FoodProperties FOOD = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).alwaysEdible().build();

    private final ElementalWood wood;

    public ElementalFruitItem(ElementalWood wood, Properties properties)
    {
        super(properties.food(FOOD));
        this.wood = wood;
    }

    public ElementalWood wood()
    {
        return wood;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity)
    {
        if (!level.isClientSide()) FruitAffinity.give(entity, Map.of(wood.element(), 1));
        return super.finishUsingItem(itemStack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(FruitMedleyItem.affinityLine(wood.element(), 1));
        builder.accept(FruitMedleyItem.tierLine(1));
    }
}
