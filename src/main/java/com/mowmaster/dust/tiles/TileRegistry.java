package com.mowmaster.dust.tiles;

import com.mowmaster.dust.references.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileRegistry
{
    public static void registerTile()
    {
        GameRegistry.registerTileEntity(TileCrystalCluster.class, Reference.MODID + "tilecrystalcluster");
        GameRegistry.registerTileEntity(TilePedestal.class, Reference.MODID + "tilepedestal");
        GameRegistry.registerTileEntity(TileCrystalCrusher.class, Reference.MODID + "tilecrystalcrusher");
    }
}
