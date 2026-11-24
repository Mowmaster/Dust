package com.mowmaster.dust.Features.Trees;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

// One of the elemental woods' charcoals (Charcoal): a fuel whose tooltip says how many items it smelts in a furnace
// (its burn time over vanilla's 200 ticks a smelt). It also lights torches and campfires (the coals item tag).
public class CharcoalItem extends Item
{
    private static final int TICKS_PER_SMELT = 200;

    private final Charcoal charcoal;

    public CharcoalItem(Charcoal charcoal, Properties properties)
    {
        super(properties);
        this.charcoal = charcoal;
    }

    public Charcoal charcoal()
    {
        return charcoal;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        int smelts = charcoal.burnTime / TICKS_PER_SMELT;
        builder.accept((smelts == 1 ? Component.translatable("dust.charcoal.smelts_one") : Component.translatable("dust.charcoal.smelts", smelts))
                .withStyle(ChatFormatting.GRAY));
    }
}
