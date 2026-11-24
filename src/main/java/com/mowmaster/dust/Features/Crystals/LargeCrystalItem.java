package com.mowmaster.dust.Features.Crystals;

import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

// A Large Crystal picked with Silk Touch: placed back as a crystal, or set on a Crystal Display to give off its element's
// effect. It holds MAX_CHARGE dust (its value); a display uses it up and its tubes fill it again. The charge left goes
// with it as dust:crystal_charge when it's taken off a display (none: full). A spent crystal (not full) can only go on
// a display, so it can't be "refilled" by placing it and mining it again.
public class LargeCrystalItem extends BlockItem
{
    public static final int MAX_CHARGE = CrystalSize.LARGE.value;

    public LargeCrystalItem(Block block, Properties properties)
    {
        super(block, properties);
    }

    public static int charge(ItemStack stack)
    {
        return Math.clamp(stack.getOrDefault(DustComponentDataRegistry.CRYSTAL_CHARGE.get(), MAX_CHARGE), 0, MAX_CHARGE);
    }

    // A crystal of this item with this much charge left (full: no component)
    public static ItemStack withCharge(ItemStack stack, int charge)
    {
        if (charge >= MAX_CHARGE) stack.remove(DustComponentDataRegistry.CRYSTAL_CHARGE.get());
        else stack.set(DustComponentDataRegistry.CRYSTAL_CHARGE.get(), Math.max(0, charge));
        return stack;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context)
    {
        if (charge(context.getItemInHand()) < MAX_CHARGE) {
            if (context.getPlayer() != null && !context.getLevel().isClientSide()) {
                context.getPlayer().sendOverlayMessage(Component.translatable("dust.crystal.spent"));
            }
            return InteractionResult.FAIL;
        }
        return super.place(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag)
    {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(Component.translatable("dust.crystal.charge", charge(stack), MAX_CHARGE).withStyle(net.minecraft.ChatFormatting.GRAY));
    }

    // A spent crystal shows the bar items show for wear: how much dust it has left
    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        return charge(stack) < MAX_CHARGE;
    }

    @Override
    public int getBarWidth(ItemStack stack)
    {
        return Math.round(13F * charge(stack) / MAX_CHARGE);
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return 0x9FD8FF;
    }
}
