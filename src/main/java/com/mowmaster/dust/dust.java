package com.mowmaster.dust;

import com.mowmaster.dust.block.BlockRenderRegister;
import com.mowmaster.dust.item.ItemRenderRegister;
import com.mowmaster.dust.proxy.ClientProxy;
import com.mowmaster.dust.proxy.IProxy;
import com.mowmaster.dust.reference.reference;
import com.mowmaster.dust.init.*;
import com.mowmaster.dust.world.CrystalOreGen;
import com.mowmaster.dust.world.biomes.biomeCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


@Mod(modid = reference.MOD_ID, name = reference.MOD_NAME, version = reference.VERSION)
public class dust {

    @SidedProxy(clientSide = reference.CLIENT_PROXY_CLASS, serverSide = reference.SERVER_PROXY_CLASS)
    public static ClientProxy clientProxy;

    @Mod.Instance(reference.MOD_ID)
    public static dust instance;

    @SidedProxy(clientSide = reference.CLIENT_PROXY_CLASS, serverSide = reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;


    public static BiomeGenBase Crystal;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModItems.dustItems();
        ModItems.dustTools();
        ModItems.dustArmors();

        ModBlocks.dustBlocks();

        fuelHandlers.fuelHandles();


        Crystal = new biomeCrystal(117).setTopFillerBlock(Blocks.grass.getDefaultState(),Blocks.stone.getDefaultState()).setMinMaxHeight(0.5F,1.0F).setBiomeName("Crystal").setTemperatureRainfall(0.1F, 1.0F);

        BiomeManager.addBiome(BiomeManager.BiomeType.COOL,new BiomeManager.BiomeEntry(Crystal,5));

        //ModEvents handler = new ModEvents();
        //MinecraftForge.EVENT_BUS.register(handler);
        //FMLCommonHandler.instance().bus().register(handler);

    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ItemRenderRegister.registerItemRenderer();
        BlockRenderRegister.registerBlockRenderer();
        craftingCrystals.init();




        GameRegistry.registerWorldGenerator(new CrystalOreGen(),0);
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
