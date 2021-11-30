package com.mowmaster.dust;

import com.mowmaster.dust.DeferredRegistery.Client.ClientRegistry;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterTileBlocks;
import com.mowmaster.dust.Configs.DustGenerationConfig;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterBlocks;
import com.mowmaster.dust.DeferredRegistery.DeferredRegisterItems;
import com.mowmaster.dust.Networking.DustPacketHandler;
import com.mowmaster.dust.World.DustGeneration;
import com.mowmaster.dust.World.GeodeGen.GeodeFeatures;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.mowmaster.dust.References.Constants.MODNAME;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dust")
public class Dust
{
    public static final Logger LOGGER = LogManager.getLogger();

    public Dust() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new DustGeneration());

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DustGenerationConfig.commonSpec);

        eventBus.register(DustGenerationConfig.class);

        DeferredRegisterBlocks.DECORATORS.register(eventBus);
        DeferredRegisterItems.ITEMS.register(eventBus);
        DeferredRegisterBlocks.BLOCKS.register(eventBus);
        DeferredRegisterTileBlocks.BLOCKS.register(eventBus);
        DeferredBlockEntityTypes.BLOCK_ENTITIES.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Initialize "+MODNAME+" WorldGen");
        GeodeFeatures.initialize();
        DustPacketHandler.registerMessages();
    }

    private void setupClient(final FMLClientSetupEvent event)
    {
        LOGGER.info("Initialize "+MODNAME+" Block Entity Renders");
        ClientRegistry.registerBlockEntityRenderers();
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("dust", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        //LOGGER.info("Got IMC {}", event.getIMCStream().map(m->m.messageSupplier().get()).collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            //BlockRegistry.onBlockRegistryReady(blockRegistryEvent);
        }
        @SubscribeEvent
        public static void onItemRegistry(RegistryEvent.Register<Item> event)
        {
            //BlockRegistry.onItemRegistryReady(event);
            //ItemRegistry.onItemRegistryReady(event);
        }
    }
}
