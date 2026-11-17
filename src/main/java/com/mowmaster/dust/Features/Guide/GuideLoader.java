package com.mowmaster.dust.Features.Guide;

import com.mowmaster.dust.DustReferences;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.Map;

// Reads guide scenes from the client's resources: every assets/<namespace>/guides/**.json (any mod or resource pack),
// each one GuideScene. The file's path is the scene's id (assets/dust/guides/research_crusher.json -> dust:research_crusher).
// Loaded with the textures and models, and again on F3+T, so a scene can be edited and reloaded without restarting.
// A file that doesn't parse is logged and skipped.
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public final class GuideLoader extends SimpleJsonResourceReloadListener<GuideScene>
{
    public static final String DIRECTORY = "guides";

    private GuideLoader()
    {
        super(GuideScene.CODEC, FileToIdConverter.json(DIRECTORY));
    }

    @Override
    protected void apply(Map<Identifier, GuideScene> scenes, ResourceManager resources, ProfilerFiller profiler)
    {
        GuideRegistry.setResources(scenes);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddClientReloadListenersEvent event)
    {
        event.addListener(Identifier.fromNamespaceAndPath(DustReferences.MODID, DIRECTORY), new GuideLoader());
    }
}
