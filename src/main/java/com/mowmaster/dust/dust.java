package com.mowmaster.dust;

import com.mowmaster.dust.block.BlockRenderRegister;
import com.mowmaster.dust.item.ItemRenderRegister;
import com.mowmaster.dust.proxy.ClientProxy;
import com.mowmaster.dust.proxy.IProxy;
import com.mowmaster.dust.reference.reference;
import com.mowmaster.dust.init.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
    public void preInit(FMLPreInitializationEvent event) {
        ModItems.dustItems();
        ModItems.dustTools();
        ModItems.dustArmors();

        ModBlocks.dustBlocks();


        ModEvents handler = new ModEvents();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ItemRenderRegister.registerItemRenderer();
        BlockRenderRegister.registerBlockRenderer();
        craftingCrystals.init();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
