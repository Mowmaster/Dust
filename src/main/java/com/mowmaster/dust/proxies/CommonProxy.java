package com.mowmaster.dust.proxies;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.handlers.EnchantAndEffectHandlers;
import com.mowmaster.dust.handlers.DebugAndLogging;
import com.mowmaster.dust.handlers.GuiHandler;
import com.mowmaster.dust.handlers.PlaceableCrystals;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import com.mowmaster.dust.world.OreGeneration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by KingMowmaster on 2/20/2017.
 */
public class CommonProxy
{
    public void init()
    {
        GameRegistry.registerWorldGenerator(new OreGeneration(),0);


        PlaceableCrystals handler = new PlaceableCrystals();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);

        DebugAndLogging handled = new DebugAndLogging();
        MinecraftForge.EVENT_BUS.register(handled);
        FMLCommonHandler.instance().bus().register(handled);

        EnchantAndEffectHandlers enchants = new EnchantAndEffectHandlers();
        MinecraftForge.EVENT_BUS.register(enchants);
        FMLCommonHandler.instance().bus().register(enchants);

        NetworkRegistry.INSTANCE.registerGuiHandler(dust.instance,new GuiHandler());

    }

    public void PreInit()
    {

    }

    public void registerTile()
    {
        GameRegistry.registerTileEntity(TileCrystalCluster.class,Reference.MODID + "TileCrystalCluster");
    }

    public void registerModelBakeryVarients()
    {

    }
}
