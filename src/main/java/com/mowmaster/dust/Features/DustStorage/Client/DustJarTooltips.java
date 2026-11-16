package com.mowmaster.dust.Features.DustStorage.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.DustRegistries.DustComponentDataRegistry;
import com.mowmaster.dust.Features.DustStorage.DustJarContents;
import com.mowmaster.dust.Features.DustStorage.DustJarInteractions;
import net.minecraft.ChatFormatting;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

// A Dust Jar item's tooltip: what it holds ("Fire: 512 / 1000", the element in the ancient script until discovered).
// Client side (ItemTooltipEvent gives the player looking at it).
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class DustJarTooltips
{
    private DustJarTooltips() {}

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event)
    {
        if (event.getEntity() == null || !event.getItemStack().is(com.mowmaster.dust.DustRegistries.DustBlockRegistry.DUST_JAR.asItem())) return;
        DustJarContents contents = event.getItemStack().getOrDefault(DustComponentDataRegistry.DUST_JAR.get(), DustJarContents.EMPTY);
        event.getToolTip().add(Math.min(1, event.getToolTip().size()),
                DustJarInteractions.describe(contents.element().orElse(null), contents.amount(), event.getEntity()).copy().withStyle(ChatFormatting.GRAY));
    }
}
