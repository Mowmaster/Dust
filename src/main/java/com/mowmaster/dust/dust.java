/*
package com.mowmaster.dust;


import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.items.ItemArmorAndToolsRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.misc.DustConfigurationFile;
import com.mowmaster.dust.recipes.*;
import com.mowmaster.dust.recipes.fuels.FuelRegistry;
import com.mowmaster.dust.world.biome.BiomeRegistry;
import net.minecraftforge.fml.common.Mod;

import com.mowmaster.dust.proxies.CommonProxy;
import com.mowmaster.dust.references.Reference;

import java.io.File;


@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION)
public class dust {
    @Mod.Instance(Reference.MODID)
    public static dust instance;

    @SidedProxy(serverSide = Reference.SERVER_SIDE, clientSide = Reference.CLIENT_SIDE)
    public static CommonProxy proxy;

    public static File dustConfig;
    public static File getDustConfig(){return dustConfig;}

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        dustConfig = new File(event.getModConfigurationDirectory() + "/" + Reference.MODID + "/" + "configs");
        dustConfig.mkdirs();
        DustConfigurationFile.InitConfig(new File(dustConfig.getPath(),Reference.MODID +".cfg"));
        BlockRegistry.init();
        BlockRegistry.register();
        ItemRegistry.init();
        ItemRegistry.register();
        ItemArmorAndToolsRegistry.init();
        ItemArmorAndToolsRegistry.register();
        OreDictDust.addEntries();
        EnchantmentRegistry.Init();
        PotionRegistry.init();
        PotionRegistry.registerPotionTypes();
        proxy.PreInit();
        proxy.registerTile();
        FuelRegistry.init();
        SmeltingRecipes.init();
        BrewingRecipes.registerPotionRecipes();
        BiomeRegistry.BiomeReg();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        //RecipeSorter.register("dust_bread",RecipeDustBread.class, RecipeSorter.Category.SHAPED,"");
        //proxy.registerModelBakeryVarients();
        //MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        CraftingRecipes.CraftingRecipes();
    }

}
 */
package com.mowmaster.dust;

        import com.mowmaster.dust.blocks.BlockRegistry;
        import com.mowmaster.dust.effects.PotionRegistry;
        import com.mowmaster.dust.enchantments.EnchantmentRegistry;
        import com.mowmaster.dust.items.ItemArmorAndToolsRegistry;
        import com.mowmaster.dust.items.ItemRegistry;
        import com.mowmaster.dust.misc.DustConfigurationFile;
        import com.mowmaster.dust.recipes.BrewingRecipes;
        import com.mowmaster.dust.recipes.OreDictDust;
        import com.mowmaster.dust.recipes.SmeltingRecipes;
        import com.mowmaster.dust.recipes.fuels.FuelRegistry;
        import com.mowmaster.dust.references.Reference;
        import com.mowmaster.dust.world.biome.BiomeRegistry;
        import net.minecraft.block.Block;
        import net.minecraft.init.Blocks;
        import net.minecraftforge.common.MinecraftForge;
        import net.minecraftforge.event.RegistryEvent;
        import net.minecraftforge.eventbus.api.SubscribeEvent;
        import net.minecraftforge.fml.InterModComms;
        import net.minecraftforge.fml.common.Mod;
        import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
        import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
        import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
        import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
        import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
        import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
        import org.apache.logging.log4j.LogManager;
        import org.apache.logging.log4j.Logger;

        import java.io.File;
        import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Reference.MODID)
public class dust
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public dust() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        //dustConfig = new File(event.getModConfigurationDirectory() + "/" + Reference.MODID + "/" + "configs");
        //dustConfig.mkdirs();
        //DustConfigurationFile.InitConfig(new File(dustConfig.getPath(),Reference.MODID +".cfg"));
        //BlockRegistry.init();
        //BlockRegistry.register();
        ItemRegistry.init();
        ItemRegistry.register();
        //ItemArmorAndToolsRegistry.init();
        //ItemArmorAndToolsRegistry.register();
        //OreDictDust.addEntries();
        //EnchantmentRegistry.Init();
        //PotionRegistry.init();
        //PotionRegistry.registerPotionTypes();
        //proxy.PreInit();
        //proxy.registerTile();
        //FuelRegistry.init();
        //SmeltingRecipes.init();
        //BrewingRecipes.registerPotionRecipes();
        //BiomeRegistry.BiomeReg();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        ItemRegistry.registerRenders();
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("forge", "helloworld", () -> { LOGGER.info("Hello world"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD event bus
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}

