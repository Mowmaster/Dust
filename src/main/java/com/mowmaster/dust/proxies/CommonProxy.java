package com.mowmaster.dust.proxies;

import com.mowmaster.dust.dust;
import com.mowmaster.dust.handlers.*;
import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import com.mowmaster.dust.tiles.TileRegistry;
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

        LootHandler loots = new LootHandler();
        MinecraftForge.EVENT_BUS.register(loots);
        FMLCommonHandler.instance().bus().register(loots);

        NetworkRegistry.INSTANCE.registerGuiHandler(dust.instance,new GuiHandler());

    }

    public void PreInit()
    {

    }

    public void registerTile()
    {
        TileRegistry.registerTile();
    }

    public void registerModelBakeryVarients()
    {

    }
}
