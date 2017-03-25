package com.mowmaster.dust;


import com.mowmaster.dust.blocks.BlockRegistry;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.recipes.CraftingRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.mowmaster.dust.proxies.CommonProxy;
import com.mowmaster.dust.references.Reference;


@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION)
public class Dust {
    @Mod.Instance(Reference.MODID)
    public static Dust instance;

    @SidedProxy(serverSide = Reference.SERVER_SIDE, clientSide = Reference.CLIENT_SIDE)
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        BlockRegistry.init();
        BlockRegistry.register();
        ItemRegistry.init();
        ItemRegistry.register();
        proxy.PreInit();
        proxy.registerTile();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        proxy.registerModelBakeryVarients();
        MinecraftForge.EVENT_BUS.register(this);
        CraftingRecipes.ICraftingRecipes();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}

