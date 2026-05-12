package com.mowmaster.dust;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.Features.EffectScrolls.KeyMappings.DustKeyMappings;
import com.mowmaster.dust.Features.EffectScrolls.Networking.PacketOfDustAuraC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = DustReferences.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public class DustClient {
    public DustClient(ModContainer container) {
        DustKeyMappings.register();
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void registerKeyMapping(RegisterKeyMappingsEvent event)
    {
        event.register(DustKeyMappings.PRESSKEY.get());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event)
    {
        //Does the action once
        //if(DustKeyMappings.PRESSKEY.get().consumeClick()) {Minecraft.getInstance().player.sendOpenInventory();}
        //repetidly does the action
        while(DustKeyMappings.PRESSKEY.get().consumeClick()) {
            //Client side stuff here
            //Minecraft.getInstance().player.sendSystemMessage(Component.literal("Key Pressed"));
            ClientPacketDistributor.sendToServer(new PacketOfDustAuraC2S("Key Pressed", 1));
        }
    }

    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();

            if (!Minecraft.getInstance().player.isCreative() && !Minecraft.getInstance().player.isSpectator()
                    && Minecraft.getInstance().player.hasData(DustAttachmentTypeRegistry.DUST_AURA)) {
                for (int i = 0; i < 10; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_icon_bg"),
                            8, 8, 0, 0, x - 200, y - 20 - i * 9, 8, 8);
                }

                for (int i = 0; i < Minecraft.getInstance().player.getData(DustAttachmentTypeRegistry.DUST_AURA); i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_icon"),
                            8, 8, 0, 0, x - 200, y - 20 - i * 9, 8, 8);
                }
            }
        });
    }

}
