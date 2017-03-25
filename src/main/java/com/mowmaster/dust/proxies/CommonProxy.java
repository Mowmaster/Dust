package com.mowmaster.dust.proxies;

import com.mowmaster.dust.references.Reference;
import com.mowmaster.dust.tiles.TileRedOre;
import com.mowmaster.dust.world.OreGeneration;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by KingMowmaster on 2/20/2017.
 */
public class CommonProxy
{
    public void init()
    {
        GameRegistry.registerWorldGenerator(new OreGeneration(),0);
    }

    public void PreInit()
    {

    }

    public void registerTile()
    {
        GameRegistry.registerTileEntity(TileRedOre.class, Reference.MODID + ":red_ore");
    }

    public void registerModelBakeryVarients()
    {

    }
}
