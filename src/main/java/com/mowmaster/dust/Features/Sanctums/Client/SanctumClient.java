package com.mowmaster.dust.Features.Sanctums.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Sanctums.Sanctums;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

// The sanctums' client parts: the Arcane Spawner's crystal and what turns inside an Arcane Vault (MowLib's
// SpinningItemRenderer: both block entities are ISpinningItemHolders), and the element tints (SanctumTint), the
// labyrinth's seals and runes too
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class SanctumClient
{
    private SanctumClient() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(Sanctums.ARCANE_SPAWNER_ENTITY.get(), com.mowmaster.mowlib.client.SpinningItemRenderer::new);
        event.registerBlockEntityRenderer(Sanctums.ARCANE_VAULT_ENTITY.get(), com.mowmaster.mowlib.client.SpinningItemRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockTints(RegisterColorHandlersEvent.BlockTintSources event)
    {
        event.register(List.of(new SanctumTint()), Sanctums.ARCANE_SPAWNER.get(), Sanctums.ARCANE_VAULT.get(), Sanctums.ARCANE_PORTAL.get(),
                com.mowmaster.dust.Features.Labyrinth.LabyrinthBlocks.SEAL.get(), com.mowmaster.dust.Features.Labyrinth.LabyrinthBlocks.RUNE.get());
    }
}
