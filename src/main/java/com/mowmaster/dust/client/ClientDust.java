package com.mowmaster.dust.client;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.render.RenderCluster;
import com.mowmaster.dust.tiles.render.RenderPedestal;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientDust {

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent e) {
        RenderPedestal.init(e);
        RenderCluster.init(e);
    }
}
