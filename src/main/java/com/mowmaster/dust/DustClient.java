package com.mowmaster.dust;

import com.mowmaster.dust.DustRegistries.DustAttachmentTypeRegistry;
import com.mowmaster.dust.DustRegistries.DustBlockRegistry;
import com.mowmaster.dust.DustRegistries.DustEntityRegistry;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.chaosDust.ChaosOrbRenderer;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.earthDust.EarthOrbRenderer;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.fireDust.FireOrbRenderer;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.orderDust.OrderOrbRenderer;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.waterDust.WaterOrbRenderer;
import com.mowmaster.dust.Features.EffectScrolls.KeyMappings.DustKeyMappings;
import com.mowmaster.dust.Features.EffectScrolls.Networking.DustAuraPacketHelper;
import com.mowmaster.dust.Features.EffectScrolls.Networking.PacketOfDustAuraC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.List;

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
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DustEntityRegistry.FIRE_ORB.get(), FireOrbRenderer::new);
        event.registerEntityRenderer(DustEntityRegistry.EARTH_ORB.get(), EarthOrbRenderer::new);
        event.registerEntityRenderer(DustEntityRegistry.WATER_ORB.get(), WaterOrbRenderer::new);
        event.registerEntityRenderer(DustEntityRegistry.CHAOS_ORB.get(), ChaosOrbRenderer::new);
        event.registerEntityRenderer(DustEntityRegistry.ORDER_ORB.get(), OrderOrbRenderer::new);
    }

    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();

            if (!Minecraft.getInstance().player.isCreative() && !Minecraft.getInstance().player.isSpectator()
                    && Minecraft.getInstance().player.hasData(DustAttachmentTypeRegistry.DUST_AURA)) {
                int limiter = DustAuraPacketHelper.getAuraLimit(Minecraft.getInstance().player);
                for (int i = 0; i < limiter; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_iconh_bg"),
                            1, 4, 0, 0, x - 200 + i, y - 20, 1, 4);
                }

                for (int i = 0; i < Minecraft.getInstance().player.getData(DustAttachmentTypeRegistry.DUST_AURA); i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_iconh"),
                            1, 4, 0, 0, x - 200 + i, y - 20, 1, 4);
                }
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_fire_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();


            if (!Minecraft.getInstance().player.isCreative() && !Minecraft.getInstance().player.isSpectator()
                    && Minecraft.getInstance().player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE)) {
                int limiter = DustAuraPacketHelper.getMagicLimit(Minecraft.getInstance().player,1);
                for (int i = 0; i < limiter; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_iconbg"),
                            4, 1, 0, 0, x - 200, y - 30 - i, 4, 1);
                }

                for (int i = 0; i < Minecraft.getInstance().player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_FIRE); i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_fire_icon"),
                            4, 1, 0, 0, x - 200, y - 30 - i, 4, 1);
                }
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_water_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();


            if (!Minecraft.getInstance().player.isCreative() && !Minecraft.getInstance().player.isSpectator()
                    && Minecraft.getInstance().player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER)) {
                int limiter = DustAuraPacketHelper.getMagicLimit(Minecraft.getInstance().player,1);
                for (int i = 0; i < limiter; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_iconbg"),
                            4, 1, 0, 0, x - 195, y - 30 - i, 4, 1);
                }

                for (int i = 0; i < Minecraft.getInstance().player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_WATER); i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_water_icon"),
                            4, 1, 0, 0, x - 195, y - 30 - i, 4, 1);
                }
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_earth_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();


            if (!Minecraft.getInstance().player.isCreative() && !Minecraft.getInstance().player.isSpectator()
                    && Minecraft.getInstance().player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH)) {
                int limiter = DustAuraPacketHelper.getMagicLimit(Minecraft.getInstance().player,1);
                for (int i = 0; i < limiter; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_iconbg"),
                            4, 1, 0, 0, x - 190, y - 30 - i, 4, 1);
                }

                for (int i = 0; i < Minecraft.getInstance().player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_EARTH); i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_earth_icon"),
                            4, 1, 0, 0, x - 190, y - 30 - i, 4, 1);
                }
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_chaos_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();


            if (!Minecraft.getInstance().player.isCreative() && !Minecraft.getInstance().player.isSpectator()
                    && Minecraft.getInstance().player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS)) {
                int limiter = DustAuraPacketHelper.getMagicLimit(Minecraft.getInstance().player,1);
                for (int i = 0; i < limiter; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_iconbg"),
                            4, 1, 0, 0, x - 185, y - 30 - i, 4, 1);
                }

                for (int i = 0; i < Minecraft.getInstance().player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_CHAOS); i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_chaos_icon"),
                            4, 1, 0, 0, x - 185, y - 30 - i, 4, 1);
                }
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_order_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();


            if (!Minecraft.getInstance().player.isCreative() && !Minecraft.getInstance().player.isSpectator()
                    && Minecraft.getInstance().player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER)) {
                int limiter = DustAuraPacketHelper.getMagicLimit(Minecraft.getInstance().player,1);
                for (int i = 0; i < limiter; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_iconbg"),
                            4, 1, 0, 0, x - 180, y - 30 - i, 4, 1);
                }

                for (int i = 0; i < Minecraft.getInstance().player.getData(DustAttachmentTypeRegistry.DUSTMAGIC_ORDER); i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_order_icon"),
                            4, 1, 0, 0, x - 180, y - 30 - i, 4, 1);
                }
            }
        });
    }


    @SubscribeEvent
    public static void registerTintBlocks(RegisterColorHandlersEvent.BlockTintSources event)
    {
        event.register(List.of(BlockTintSources.constant(16711680)), DustBlockRegistry.CRYSTAL_STONE_RED.get());
        event.register(List.of(BlockTintSources.constant(65280)), DustBlockRegistry.CRYSTAL_STONE_GREEN.get());
        event.register(List.of(BlockTintSources.constant(255)), DustBlockRegistry.CRYSTAL_STONE_BLUE.get());
        event.register(List.of(BlockTintSources.constant(16777215)), DustBlockRegistry.CRYSTAL_STONE_WHITE.get());
        event.register(List.of(BlockTintSources.constant(2763306)), DustBlockRegistry.CRYSTAL_STONE_BLACK.get());
    }

}
