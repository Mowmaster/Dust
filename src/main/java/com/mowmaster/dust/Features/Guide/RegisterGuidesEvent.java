package com.mowmaster.dust.Features.Guide;

import com.mowmaster.dust.DustReferences;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

// Fired once on every mod's event bus during client setup, for mods to add guide scenes made in code:
//   @SubscribeEvent
//   public static void guides(RegisterGuidesEvent event)
//   {
//       event.register(Identifier.fromNamespaceAndPath("mymod", "press/basics"), GuideScene.builder(...)...build());
//   }
// (Scenes can also be plain JSON files in assets/<namespace>/guides/, needing no code at all: GuideScene.)
// IModBusEvent: it goes to each mod's own bus (where registration events go), not the game's event bus.
public class RegisterGuidesEvent extends Event implements IModBusEvent
{
    public void register(Identifier id, GuideScene scene)
    {
        GuideRegistry.registerCode(id, scene);
    }

    @EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
    public static final class Fire
    {
        private Fire() {}

        // enqueueWork: run on the main thread once client setup's parallel part is done
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> ModLoader.postEvent(new RegisterGuidesEvent()));
        }
    }
}
