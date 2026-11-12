package com.mowmaster.dust.Features.Machines.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustComposition;
import com.mowmaster.dust.Features.Machines.Compounds;
import com.mowmaster.dust.Features.Machines.ProductSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

// Tooltip lines for process products: what an alchemical compound holds (only the elements the local player has
// discovered; the rest are "???"), and what a product was made from.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class CompoundTooltips
{
    private CompoundTooltips() {}

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        Player player = Minecraft.getInstance().player;
        // Under the name: what it holds (for a compound), then the material it came from (any product)
        int line = Math.min(1, event.getToolTip().size());
        DustComposition compound = Compounds.of(stack);
        if (compound != null && player != null) {
            Component elements = Compounds.describe(compound, element -> Compounds.knows(player, element));
            event.getToolTip().add(line++, Component.translatable("item.dust.alchemical_compound.elements", elements).withStyle(ChatFormatting.GRAY));
        }
        ItemStack source = ProductSource.sourceOf(stack);
        if (source != null && !source.isEmpty()) {
            event.getToolTip().add(Math.min(line, event.getToolTip().size()),
                    Component.translatable("item.dust.product.from", ProductSource.original(stack).getHoverName()).withStyle(ChatFormatting.GRAY));
        }
    }
}
