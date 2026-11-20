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
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.PYLON.get(), com.mowmaster.dust.Features.Spells.Client.PylonCardRenderer::new);
        // The Element Tester: the item on its plate
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.ELEMENT_TESTER.get(), ElementTesterRenderer::new);
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.ELEMENT_CRAFTER.get(), com.mowmaster.dust.Features.Crafting.Client.ElementCrafterRenderer::new);
        // The Still's firebox: its flame, coloured by its fuel
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.STILL_FIREBOX.get(), com.mowmaster.dust.Features.Apparatus.still.StillFireboxRenderer::new);
        // The alchemical apparatus share one renderer (it draws each one's own moving parts)
        for (var type : List.of(DustBlockEntityRegistry.CRUSHING_MILL.get(), DustBlockEntityRegistry.ALCHEMICAL_SEPARATOR.get(),
                DustBlockEntityRegistry.EVAPORATING_KETTLE.get(), DustBlockEntityRegistry.TRANSMUTER.get(),
                DustBlockEntityRegistry.CONJOINING_CRUCIBLE.get(), DustBlockEntityRegistry.MIXING_VAT.get(), DustBlockEntityRegistry.DISSOLVING_RETORT.get())) {
            registerApparatusRenderer(event, type);
        }
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.ELEMENT_REPLICATOR.get(), com.mowmaster.dust.Features.Machines.Client.ElementReplicatorRenderer::new);
        // The Dust Grinder's turning top and the item in its hole
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.DUST_GRINDER.get(), com.mowmaster.dust.Features.DustStorage.Client.DustGrinderRenderer::new);
        // The dust inside a Dust Jar, filling it as it fills
        event.registerBlockEntityRenderer(DustBlockEntityRegistry.DUST_JAR.get(), com.mowmaster.dust.Features.DustStorage.Client.DustJarRenderer::new);
        // Element orbs: one renderer, tinted with each orb's element colour
        event.registerEntityRenderer(DustEntityRegistry.ELEMENT_ORB.get(), ElementalOrbEntityRenderer::new);
        // A spell in flight draws nothing itself: its trail is particles (SpellProjectileEntity.tick)
        event.registerEntityRenderer(DustEntityRegistry.SPELL_PROJECTILE.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        // Blocks brought to life by spells, drawn as their blocks (the animated one hops as it scurries)
        event.registerEntityRenderer(DustEntityRegistry.FLOATING_BLOCK.get(), context -> new com.mowmaster.dust.Features.Spells.Client.CarriedBlockRenderer<>(context, 0));
        event.registerEntityRenderer(DustEntityRegistry.TELEKINESIS_CUBE.get(), com.mowmaster.dust.Features.Spells.Client.TelekinesisCubeRenderer::new);
        event.registerEntityRenderer(DustEntityRegistry.ANIMATED_BLOCK.get(), context -> new com.mowmaster.dust.Features.Spells.Client.CarriedBlockRenderer<>(context, 0.15F));
        // Void: the winged chest; the rift is only particles (spawned by the entity itself)
        event.registerEntityRenderer(DustEntityRegistry.VOID_CHEST.get(), com.mowmaster.dust.Features.Spells.Client.VoidChestRenderer::new);
        event.registerEntityRenderer(DustEntityRegistry.VOID_RIFT.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        // Soul: the wisp is only particles (spawned by the entity itself)
        event.registerEntityRenderer(DustEntityRegistry.SOUL_WISP.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
    }

    // <T extends ApparatusBlockEntity>: a generic method, so each apparatus's block entity type can take the one renderer
    // written for them all
    private static <T extends com.mowmaster.dust.Features.Apparatus.ApparatusBlockEntity> void registerApparatusRenderer(EntityRenderersEvent.RegisterRenderers event,
                                                                                                                       net.minecraft.world.level.block.entity.BlockEntityType<T> type)
    {
        event.registerBlockEntityRenderer(type, context -> new com.mowmaster.dust.Features.Apparatus.Client.ApparatusRenderer(context));
    }

    // Models drawn by code rather than placed as blocks (the crusher's wheels)
    @SubscribeEvent
    public static void registerStandaloneModels(ModelEvent.RegisterStandalone event)
    {
        com.mowmaster.dust.Features.Apparatus.Client.ApparatusRenderer.registerModels(event);
        com.mowmaster.dust.Features.Apparatus.still.StillFireboxRenderer.registerModels(event);
        ResearchCrusherRenderer.registerModels(event);
        com.mowmaster.dust.Features.DustStorage.Client.DustGrinderRenderer.registerModels(event);
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
        // A Fused Crystal's two elements (layer 0 the first, layer 1 the second)
        event.register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "fused_crystal"), com.mowmaster.dust.Features.Spells.Client.FusedCrystalTint.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "spell"), com.mowmaster.dust.Features.Spells.Client.SpellTint.MAP_CODEC);
        // "dust:dust_jar": a Dust Jar item's lid in the colour of the dust it carries
        event.register(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_jar"), com.mowmaster.dust.Features.DustStorage.Client.DustJarItemTint.MAP_CODEC);
    }

    // The element row in item tooltips (ElementTooltip -> ClientElementTooltip)
    @SubscribeEvent
    public static void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(ElementTooltip.class, ClientElementTooltip::new);
        event.register(AlchemicalDustTooltip.class, ClientAlchemicalDustTooltip::new);
        // Element names in item names follow what the local player has discovered (ElementNames)
        com.mowmaster.dust.Features.DustComposition.ElementNames.setClientKnows(element -> {
            var player = Minecraft.getInstance().player;
            return player != null && com.mowmaster.dust.Features.Machines.Compounds.knows(player, element);
        });
    }

    // Glyph stone carving colour: dark, or worn blank on stones the local player has transcribed (tint index 0)
    @SubscribeEvent
    public static void registerBlockTints(RegisterColorHandlersEvent.BlockTintSources event)
    {
        event.register(List.of(new GlyphStoneTint()), DustBlockRegistry.GLYPH_STONE.get());
        // The Dust Jar's lid in its element's colour (tint index 1)
        event.register(com.mowmaster.dust.Features.DustStorage.Client.DustJarTint.all(), DustBlockRegistry.DUST_JAR.get());
        // A Dust Pile in the mix of its elements' colours (tint index 0)
        event.register(List.of(new com.mowmaster.dust.Features.DustStorage.Client.DustPileTint()), DustBlockRegistry.DUST_PILE.get());
        // The Still Tower's window in its element's colour (tint index 3)
        event.register(com.mowmaster.dust.Features.Apparatus.still.StillTowerTint.all(), DustBlockRegistry.STILL_TOWER.get());
        // Element Tester: crystal colours and gauge lines, one tint source per tint index (see ExaminerLayout)
        event.register(ElementTesterTint.all(), DustBlockRegistry.ELEMENT_TESTER.get());
        // The spell pylons: the Spell Pylon in its element's colour; the others by kind, lockout and upgrade nodes
        event.register(List.of(new com.mowmaster.dust.Features.Spells.Client.SpellPylonTint()), DustBlockRegistry.SPELL_PYLON.get());
        // A rune in its spell's element colour
        event.register(List.of(new com.mowmaster.dust.Features.Spells.Client.RuneTint()), DustBlockRegistry.RUNE.get());
        // The Blessed and Corrupted Pylons' nodes (their tree's blessing / corruption)
        event.register(com.mowmaster.dust.Features.Spells.Client.CounterPylonTint.all(), DustBlockRegistry.PYLON_BLESSED.get(), DustBlockRegistry.PYLON_CORRUPTED.get());
        event.register(com.mowmaster.dust.Features.Spells.Client.PylonTint.all(), DustBlockRegistry.SHAPE_PYLON.get(), DustBlockRegistry.NEUTRAL_PYLON.get(),
                DustBlockRegistry.THRIFTY_PYLON.get(), DustBlockRegistry.GIANT_PYLON.get(), DustBlockRegistry.DAMAGE_PYLON.get(), DustBlockRegistry.EXPERIENCED_PYLON.get());
    }

    // Client-side handlers for server -> client packets that touch client-only code (the packets are registered in
    // DustMagicEvents#registerPayloads without a handler)
    @SubscribeEvent
    public static void registerClientPayloadHandlers(RegisterClientPayloadHandlersEvent event)
    {
        event.register(ObservationPayloads.Progress.TYPE, ObservationClient::onProgress);
        event.register(ObservationPayloads.Discovered.TYPE, ObservationClient::onDiscovered);
        event.register(ObservationPayloads.Noted.TYPE, ObservationClient::onNoted);
        event.register(com.mowmaster.dust.Features.DustStorage.DustStream.TYPE, com.mowmaster.dust.Features.DustStorage.Client.DustStreams::onStream);
        event.register(com.mowmaster.dust.Features.DustStorage.DustTubeFlow.TYPE, com.mowmaster.dust.Features.DustStorage.Client.DustTubeFlows::onFlow);
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
        event.registerSpriteSet(DustParticleRegistry.STILL_SMOKE.get(), com.mowmaster.dust.Features.Apparatus.still.StillSmokeParticle.Provider::new);
    }

    // The mana bar and the dust carried (Features/Magic/Client/ManaHud: placement is [mana_hud] in the client config)
    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "aura_meter"), com.mowmaster.dust.Features.Magic.Client.ManaHud::renderMana);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "dust_store"), com.mowmaster.dust.Features.Magic.Client.ManaHud::renderDust);
        // The Spell Pylon's HUD while it's looked at (its deck and, with the Akashic, its pool)
        event.registerAboveAll(Identifier.fromNamespaceAndPath(DustReferences.MODID, "spell_pylon"), com.mowmaster.dust.Features.Spells.Client.SpellPylonHud::render);
    }


}
