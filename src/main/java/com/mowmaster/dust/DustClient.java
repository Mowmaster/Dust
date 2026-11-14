package com.mowmaster.dust;

import com.mowmaster.dust.DustRegistries.*;
import com.mowmaster.dust.Features.AncientLanguage.Client.DecipherScreen;
import com.mowmaster.dust.Features.DustComposition.Client.ClientElementTooltip;
import com.mowmaster.dust.Features.Machines.Client.CompoundTint;
import com.mowmaster.dust.Features.Machines.Client.ResearchCrusherRenderer;
import com.mowmaster.dust.Features.Machines.Client.AlchemicalDustTint;
import com.mowmaster.dust.Features.Machines.Client.ClientAlchemicalDustTooltip;
import com.mowmaster.dust.Features.Machines.Client.ResearchCombinationRenderer;
import com.mowmaster.dust.Features.Machines.Client.ResearchDistillationRenderer;
import com.mowmaster.dust.Features.Machines.Client.ResearchMixingRenderer;
import com.mowmaster.dust.Features.Machines.Client.ResearchTransformationRenderer;
import com.mowmaster.dust.Features.Machines.AlchemicalDustTooltip;
import com.mowmaster.dust.Features.Machines.Client.ResearchEvaporationRenderer;
import com.mowmaster.dust.Features.Machines.Client.ResearchSeparationRenderer;
import com.mowmaster.dust.Features.Machines.Client.SourceBlockTint;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.minecraft.resources.Identifier;
import com.mowmaster.dust.Features.DustComposition.Client.ElementTesterRenderer;
import com.mowmaster.dust.Features.DustComposition.ElementTooltip;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import com.mowmaster.dust.Features.DustComposition.Client.ElementTesterTint;
import com.mowmaster.dust.Features.AncientLanguage.Client.GlyphStoneTint;
import com.mowmaster.dust.Features.EffectScrolls.DustEntities.ElementalOrbs.ElementalOrbEntityRenderer;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustElementAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.DustMagic.DustMagicAttachmentHelper;
import com.mowmaster.dust.Features.EffectScrolls.KeyMappings.DustKeyMappings;
import com.mowmaster.dust.Features.EffectScrolls.Networking.PacketOfDustAuraC2S;
import com.mowmaster.dust.Features.EffectScrolls.Particles.ParticleSpellFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.fml.config.ModConfig;
import com.mowmaster.mowlib.client.ClientActivations;
import com.mowmaster.dust.Features.Research.ObservationPayloads;
import com.mowmaster.dust.Features.Research.ResearchBookItem;
import com.mowmaster.dust.Features.Research.Client.ObservationClient;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import com.mowmaster.dust.Features.Research.Client.ResearchClient;

import java.util.List;


// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = DustReferences.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = DustReferences.MODID, value = Dist.CLIENT)
public class DustClient {
    public DustClient(ModContainer container) {
        DustKeyMappings.register();
        // config/dust-client.toml, editable from Mods > Dust > Config
        container.registerConfig(ModConfig.Type.CLIENT, DustClientConfig.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        // MowLib's activation key (default V) on a held research book opens it at what you're looking at, like
        // sneak + right-click
        ClientActivations.register(stack -> stack.getItem() instanceof ResearchBookItem, (stack, hand) -> ResearchClient.openAtLookedAt(stack));
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
        // The Research Crusher: its turning wheels, the block going in and the pebbles coming out
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.RESEARCH_CRUSHER.get(), ResearchCrusherRenderer::new);
        // The Alchemical Separation Table: its array, the item on it and the outputs
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.RESEARCH_SEPARATION.get(), ResearchSeparationRenderer::new);
        // The Evaporation Table: its spoon, the liquid, the glyph and the items
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.RESEARCH_EVAPORATION.get(), ResearchEvaporationRenderer::new);
        // The Distillation Table: the glyph, the liquid in the flask and jar, and the items
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.RESEARCH_DISTILLATION.get(), ResearchDistillationRenderer::new);
        // The Transformation, Combination and Mixing Tables: their glyphs, the Mixing Table's spoon and liquid, the items
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.RESEARCH_TRANSFORMATION.get(), ResearchTransformationRenderer::new);
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.RESEARCH_COMBINATION.get(), ResearchCombinationRenderer::new);
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.RESEARCH_MIXING.get(), ResearchMixingRenderer::new);
        // The Spell Pylon's ghosts (where the next pylon can go)
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.SPELL_PYLON.get(), com.mowmaster.dust.Features.Spells.Client.SpellPylonRenderer::new);
        // The Element Tester: the item on its plate
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.ELEMENT_TESTER.get(), ElementTesterRenderer::new);
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.ELEMENT_CRAFTER.get(), com.mowmaster.dust.Features.Crafting.Client.ElementCrafterRenderer::new);
        // Element orbs: one renderer, tinted with each orb's element colour
        event.registerEntityRenderer(DustEntityRegistry.ELEMENT_ORB.get(), ElementalOrbEntityRenderer::new);
        // A spell in flight draws nothing itself: its trail is particles (SpellProjectileEntity.tick)
        event.registerEntityRenderer(DustEntityRegistry.SPELL_PROJECTILE.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        // Blocks brought to life by spells, drawn as their blocks (the animated one hops as it scurries)
        event.registerEntityRenderer(DustEntityRegistry.FLOATING_BLOCK.get(), context -> new com.mowmaster.dust.Features.Spells.Client.CarriedBlockRenderer<>(context, 0));
        event.registerEntityRenderer(DustEntityRegistry.ANIMATED_BLOCK.get(), context -> new com.mowmaster.dust.Features.Spells.Client.CarriedBlockRenderer<>(context, 0.15F));
        // Void: the winged chest; the rift is only particles (spawned by the entity itself)
        event.registerEntityRenderer(DustEntityRegistry.VOID_CHEST.get(), com.mowmaster.dust.Features.Spells.Client.VoidChestRenderer::new);
        event.registerEntityRenderer(DustEntityRegistry.VOID_RIFT.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        // Soul: the wisp is only particles (spawned by the entity itself)
        event.registerEntityRenderer(DustEntityRegistry.SOUL_WISP.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
    }

    // Models drawn by code rather than placed as blocks (the crusher's wheels)
    @SubscribeEvent
    public static void registerStandaloneModels(ModelEvent.RegisterStandalone event)
    {
        ResearchCrusherRenderer.registerModels(event);
        ResearchEvaporationRenderer.registerModels(event);
    }

    // Item model tints: "dust:source_block", the colour of the block a product was made from (crushed blocks)
    @SubscribeEvent
    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event)
    {
        event.register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "source_block"), SourceBlockTint.MAP_CODEC);
        // "dust:compound": an alchemical compound's colour, for its dust pile
        event.register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "compound"), CompoundTint.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "alchemical_dust"), AlchemicalDustTint.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "spell"), com.mowmaster.dust.Features.Spells.Client.SpellTint.MAP_CODEC);
    }

    // The element row in item tooltips (ElementTooltip -> ClientElementTooltip)
    @SubscribeEvent
    public static void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(ElementTooltip.class, ClientElementTooltip::new);
        event.register(AlchemicalDustTooltip.class, ClientAlchemicalDustTooltip::new);
    }

    // Glyph stone carving colour: dark, or worn blank on stones the local player has transcribed (tint index 0)
    @SubscribeEvent
    public static void registerBlockTints(RegisterColorHandlersEvent.BlockTintSources event)
    {
        event.register(List.of(new GlyphStoneTint()), DustBlockRegistry.GLYPH_STONE.get());
        // Element Tester: crystal colours and gauge lines, one tint source per tint index (see ExaminerLayout)
        event.register(ElementTesterTint.all(), DustBlockRegistry.ELEMENT_TESTER.get());
        event.register(com.mowmaster.dust.Features.Crafting.Client.ElementCrafterTint.all(), DustBlockRegistry.ELEMENT_CRAFTER.get());
    }

    // Client-side handlers for server -> client packets that touch client-only code (the packets are registered in
    // DustMagicEvents#registerPayloads without a handler)
    @SubscribeEvent
    public static void registerClientPayloadHandlers(RegisterClientPayloadHandlersEvent event)
    {
        event.register(ObservationPayloads.Progress.TYPE, ObservationClient::onProgress);
        event.register(ObservationPayloads.Discovered.TYPE, ObservationClient::onDiscovered);
        event.register(ObservationPayloads.Noted.TYPE, ObservationClient::onNoted);
    }

    // Which screen opens for which menu type
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(DustMenuRegistry.DECIPHER.get(), DecipherScreen::new);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(DustParticleRegistry.DUSTPARTICLES_SPELLS_FIRE.get(), ParticleSpellFire.Provider::new);
    }

    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative() && !player.isSpectator()
                    && DustMagicAttachmentHelper.hasUnlockedMana(player)) {
                for (int i = 0; i < 100; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_iconh_bg"),
                            1, 4, 0, 0, x - 200 + i, y - 20, 1, 4);
                }

                int maxMana = DustMagicAttachmentHelper.getManaMaximum(player);
                int currentMana = DustMagicAttachmentHelper.getManaLevel(player);
                int calcPercentManaLeft = Math.round(((float)currentMana/maxMana)*100);
                for (int i = 0; i < calcPercentManaLeft; i++) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_iconh"),
                            1, 4, 0, 0, x - 200 + i, y - 20, 1, 4);
                }

                //Magic meter Overlay
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "magicmeter_overlay"),
                        116, 20, 0, 0, x - 208, y - 28, 116, 20);

                /*EnumAffinity affinity = DustElementAttachmentHelper.getAffinity(player);
                if(!affinity.equals(EnumAffinity.NONE))
                {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "magicmeter_overlay"),
                            16, 16, 0, 0, x - 222, y - 40, 8, 8);
                }*/

            }
        });

        // The dust carried (Features/Magic/PlayerMagic): each element's icon in its colour and how much, in a row above
        // the mana bar, once the player's magic has awakened
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_store"), (guiGraphics, deltaTracker) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null || player.isSpectator() || !com.mowmaster.dust.Features.Magic.PlayerMagicHelper.isAwakened(player)) return;
            var magic = com.mowmaster.dust.Features.Magic.PlayerMagicHelper.get(player);
            var font = Minecraft.getInstance().font;
            int x = guiGraphics.guiWidth() / 2 - 208;
            int y = guiGraphics.guiHeight() - 40;
            for (var carried : magic.dust().entrySet()) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, carried.getKey().icon(), x, y, 9, 9, 0xFF000000 | carried.getKey().color());
                String amount = String.valueOf(carried.getValue());
                guiGraphics.text(font, amount, x + 10, y + 1, 0xFFFFFFFF, true);
                x += 10 + font.width(amount) + 4;
            }
        });

        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_fire_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative() && !player.isSpectator()
                    && player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_FIRE)
                    && DustMagicAttachmentHelper.hasUnlockedMana(player)) {
                DustElementAttachmentHelper.ElementFireInfo result = DustElementAttachmentHelper.getElementInfoFire(player);
                int maxElement = result.max();
                int currentElement = result.count();
                int calcPercentManaLeft = Math.round(((float)currentElement/maxElement)*100);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmeter_fire"),
                        11, 110, 0, calcPercentManaLeft, x - 210, y - 40, 11, 10);
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_water_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative() && !player.isSpectator()
                    && player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_WATER)
                    && DustMagicAttachmentHelper.hasUnlockedMana(player)) {
                DustElementAttachmentHelper.ElementWaterInfo result = DustElementAttachmentHelper.getElementInfoWater(player);
                int maxElement = result.max();
                int currentElement = result.count();
                int calcPercentManaLeft = Math.round(((float)currentElement/maxElement)*100);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmeter_water"),
                        11, 110, 0, calcPercentManaLeft, x - 198, y - 40, 11, 10);
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_earth_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative() && !player.isSpectator()
                    && player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_EARTH)
                    && DustMagicAttachmentHelper.hasUnlockedMana(player)) {
                DustElementAttachmentHelper.ElementEarthInfo result = DustElementAttachmentHelper.getElementInfoEarth(player);
                int maxElement = result.max();
                int currentElement = result.count();
                int calcPercentManaLeft = Math.round(((float)currentElement/maxElement)*100);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmeter_earth"),
                        11, 110, 0, calcPercentManaLeft, x - 186, y - 40, 11, 10);
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_chaos_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative() && !player.isSpectator()
                    && player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_CHAOS)
                    && DustMagicAttachmentHelper.hasUnlockedMana(player)) {
                DustElementAttachmentHelper.ElementChaosInfo result = DustElementAttachmentHelper.getElementInfoChaos(player);
                int maxElement = result.max();
                int currentElement = result.count();
                int calcPercentManaLeft = Math.round(((float)currentElement/maxElement)*100);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmeter_chaos"),
                        11, 110, 0, calcPercentManaLeft, x - 174, y - 40, 11, 10);
            }
        });
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmagic_order_meter"), (guiGraphics, deltaTracker) -> {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight();

            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative() && !player.isSpectator()
                    && player.hasData(DustAttachmentTypeRegistry.DUSTMAGIC_ELEMENTMAX_ORDER)
                    && DustMagicAttachmentHelper.hasUnlockedMana(player)) {
                DustElementAttachmentHelper.ElementOrderInfo result = DustElementAttachmentHelper.getElementInfoOrder(player);
                int maxElement = result.max();
                int currentElement = result.count();
                int calcPercentManaLeft = Math.round(((float)currentElement/maxElement)*100);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(DustReferences.MODID, "dustmeter_order"),
                        11, 110, 0, calcPercentManaLeft, x - 162, y - 40, 11, 10);
            }
        });

    }


}
