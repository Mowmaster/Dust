package com.mowmaster.dust;

import com.mowmaster.dust.init.items;
import com.mowmaster.dust.proxy.commonproxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = reference.MOD_ID, name = reference.MOD_NAME, version = reference.VERSION)
public class dust {

    @SidedProxy(clientSide = reference.CLIENT_PROXY_CLASS, serverSide = reference.COMMON_PROXY_CLASS)
    public static commonproxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        items.init();
        items.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.registerRenders();

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){

    }
}
