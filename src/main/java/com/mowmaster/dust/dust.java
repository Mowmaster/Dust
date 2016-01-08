package com.mowmaster.dust;

import com.mowmaster.dust.proxy.ClientProxy;
import com.mowmaster.dust.proxy.CommonProxy;
import com.mowmaster.dust.proxy.IProxy;
import com.mowmaster.dust.reference.reference;
import com.mowmaster.dust.init.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = reference.MOD_ID, name = reference.MOD_NAME, version = reference.VERSION)
public class dust {

    @SidedProxy(clientSide = reference.CLIENT_PROXY_CLASS, serverSide = reference.SERVER_PROXY_CLASS)
    public static ClientProxy clientProxy;

    @Mod.Instance(reference.MOD_ID)
    public static dust instance;

    @SidedProxy(clientSide = reference.CLIENT_PROXY_CLASS, serverSide = reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

        ModItems.init();
        ModItems.register();


    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        clientProxy.registerRenders();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){

    }
}
