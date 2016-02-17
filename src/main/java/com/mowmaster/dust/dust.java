package com.mowmaster.dust;

import com.mowmaster.dust.block.BlockRenderRegister;
import com.mowmaster.dust.configuration.configFile;
import com.mowmaster.dust.item.ItemRenderRegister;
import com.mowmaster.dust.proxy.IProxy;
import com.mowmaster.dust.reference.reference;
import com.mowmaster.dust.init.*;
import com.mowmaster.dust.world.biomes.biomeRegistration;
import com.mowmaster.dust.world.oregeneration.CrystalOreGenRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod(modid = reference.MOD_ID, name = reference.MOD_NAME, version = reference.VERSION)
public class dust {

    @Mod.Instance(reference.MOD_ID)
    public static dust instance;

    @SidedProxy(clientSide = reference.CLIENT_PROXY_CLASS, serverSide = reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;


    public static Configuration config;

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equals(reference.MOD_ID))
        {
            configFile.syncConfig();

        }
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        configFile.syncConfig();

        ModItems.dustItems();
        ModItems.dustTools();
        ModItems.dustArmors();

        ModBlocks.dustBlocks();

        fuelHandlers.fuelHandles();

        biomeRegistration.biomeReg();




        //ModEvents handler = new ModEvents();
        //MinecraftForge.EVENT_BUS.register(handler);
        //FMLCommonHandler.instance().bus().register(handler);

    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ItemRenderRegister.registerItemRenderer();
        BlockRenderRegister.registerBlockRenderer();
        craftingCrystals.init();


        CrystalOreGenRegistry.OreGenReg();


    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
