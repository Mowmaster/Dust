package com.mowmaster.dust.Features.Trees;

import com.mowmaster.dust.DustRegistries.DustEffectsRegistry;
import com.mowmaster.dust.Features.DustComposition.ElementNames;
import com.mowmaster.dust.Features.Magic.PlayerMagicHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

// An elemental tree's magical fruit (picked from an ElementalFruitBlock). Eaten like an apple, it also gives back
// DUST_RESTORED of its element's dust (as much as there's room for) and a short Mana Surge (SURGE_TICKS), which speeds
// up mana coming back.
public class ElementalFruitItem extends Item
{
    public static final int DUST_RESTORED = 4;
    public static final int SURGE_TICKS = 300;
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
        if (!level.isClientSide()) {
            entity.addEffect(new MobEffectInstance(DustEffectsRegistry.MANA_SURGE, SURGE_TICKS, 0));
            if (entity instanceof Player player) PlayerMagicHelper.addDust(player, wood.element(), DUST_RESTORED, false);
        }
        return super.finishUsingItem(itemStack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(Component.translatable("dust.fruit.eat", DUST_RESTORED, ElementNames.forClient(wood.element())).withStyle(ChatFormatting.GRAY));
    }
}
