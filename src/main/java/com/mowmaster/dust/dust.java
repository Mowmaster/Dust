package com.mowmaster.dust;

import com.mojang.logging.LogUtils;
import com.mowmaster.dust.Loot.LootModifierHandler;
import com.mowmaster.dust.Registry.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static com.mowmaster.dust.Registry.DustReferences.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class dust
{
    // Define mod id in a common place for everything to reference
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public dust()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);


        // Register the Deferred Register to the mod event bus so blocks get registered

        // Register the Deferred Register to the mod event bus so items get registered
        DeferredRegisterItems.ITEMS.register(modEventBus);
        DeferredRegisterBlocks.BLOCKS.register(modEventBus);
        DeferredRegisterTileBlocks.BLOCKS.register(modEventBus);
        DeferredBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
        //DeferredRegisterFluidTypes.FLUID_TYPES.register(modEventBus);
        DeferredRegisterFluids.FLUIDS.register(modEventBus);
        //Register LootTable Modifiers
        LootModifierHandler.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        DeferredCreativeTabRegistry.DEF_REG.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        //LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        // Some client setup code
        LOGGER.info("HELLO FROM Client SETUP");
        DustClientRegistry.registerBlockEntityRenderers();

        //ItemBlockRenderTypes.setRenderLayer(DeferredRegisterFluids.SOURCE_COLORABLE_PAPER_PULP_FLUID.get(), RenderType.translucent());
        //ItemBlockRenderTypes.setRenderLayer(DeferredRegisterFluids.FLOWING_COLORABLE_PAPER_PULP_FLUID.get(), RenderType.translucent());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            //LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
