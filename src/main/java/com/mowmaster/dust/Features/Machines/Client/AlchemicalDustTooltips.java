package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.datafixers.util.Either;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.DustComposition.DustElement;
import com.mowmaster.dust.Features.Machines.AlchemicalDust;
import com.mowmaster.dust.Features.Machines.AlchemicalDustTooltip;
import com.mowmaster.dust.Features.Machines.Compounds;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

// Adds Alchemical Dust's element line (AlchemicalDustTooltip) right under its name
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class AlchemicalDustTooltips
{
    private AlchemicalDustTooltips() {}

    @SubscribeEvent
    public static void onGatherTooltip(RenderTooltipEvent.GatherComponents event)
    {
        Minecraft minecraft = Minecraft.getInstance();
        DustElement element = AlchemicalDust.of(event.getItemStack());
        if (minecraft.player == null || element == null) return;
        boolean known = Compounds.knows(minecraft.player, element);
        event.getTooltipElements().add(Math.min(1, event.getTooltipElements().size()), Either.right(new AlchemicalDustTooltip(element, known)));
    }
}
