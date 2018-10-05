package com.mowmaster.dust;


import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.effects.PotionRegistry;
import com.mowmaster.dust.enchantments.EnchantmentRegistry;
import com.mowmaster.dust.items.ItemArmorAndToolsRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.misc.AchievementHandler;
import com.mowmaster.dust.misc.DustConfigurationFile;
import com.mowmaster.dust.recipes.*;
import com.mowmaster.dust.recipes.fuels.FuelRegistry;
import com.mowmaster.dust.tiles.TileRegistry;
import com.mowmaster.dust.world.biome.BiomeRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.mowmaster.dust.proxies.CommonProxy;
import com.mowmaster.dust.references.Reference;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
        TileRegistry.registerTile();
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
        //proxy.registerModelBakeryVarients();
        //MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        CraftingRecipes.CraftingRecipes();
    }

}

