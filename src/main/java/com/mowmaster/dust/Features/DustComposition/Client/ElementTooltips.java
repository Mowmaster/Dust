package com.mowmaster.dust.Features.DustComposition.Client;

import com.mojang.datafixers.util.Either;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.ElementTooltip;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

// Adds the element row to item tooltips (ElementTooltip) for items the local player has fully worked out.
// GatherComponents is fired as a tooltip is put together; its list holds each line, as text (Either.left) or as a
// TooltipComponent drawn by its client component (Either.right). Either is DataFixerUpper's "one or the other".
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class ElementTooltips
{
    private ElementTooltips() {}

    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || event.getItemStack().isEmpty()) return;
        ElementTooltip tooltip = ElementTooltip.forPlayer(event.getItemStack(), minecraft.player);
        // Right under the name (index 1), above the rest of the tooltip
        if (tooltip != null) event.getTooltipElements().add(Math.min(1, event.getTooltipElements().size()), Either.right(tooltip));
    }
}
