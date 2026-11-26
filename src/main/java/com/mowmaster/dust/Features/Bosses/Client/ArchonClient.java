package com.mowmaster.dust.Features.Bosses.Client;

import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Bosses.ArchonEntity;
import com.mowmaster.dust.Features.Bosses.Archons;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

// The Archons' renderer and model layer
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class ArchonClient
{
    private ArchonClient() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        for (DeferredHolder<EntityType<?>, EntityType<ArchonEntity>> type : Archons.TYPES.values()) event.registerEntityRenderer(type.get(), ArchonRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(ArchonRenderer.LAYER, ArchonRenderer::createLayer);
    }
}
