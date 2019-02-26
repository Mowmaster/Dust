package com.mowmaster.dust.tiles;

import com.mowmaster.dust.references.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileRegistry
{
    public static void registerTile()
    {
        GameRegistry.registerTileEntity(TileCrystalCluster.class, Reference.MODID + "tilecrystalcluster");
        GameRegistry.registerTileEntity(TilePedestal.class, Reference.MODID + "tilepedestal");
        GameRegistry.registerTileEntity(TileCrystalCrusherBasic.class, Reference.MODID + "tilecrystalcrusherbasic");
        GameRegistry.registerTileEntity(TileCrystalFurnace.class, Reference.MODID + "tilecrystalfurnace");
        GameRegistry.registerTileEntity(TileLootBlock.class, Reference.MODID + "tilelootblock");
        GameRegistry.registerTileEntity(TileTrapBlock.class, Reference.MODID + "tiletrapblock");
        GameRegistry.registerTileEntity(TileDustBlock.class, Reference.MODID + "tiledustblock");
        GameRegistry.registerTileEntity(TileCrystalFurnaceBasic.class, Reference.MODID + "tilecrystalfurnacebasic");
        GameRegistry.registerTileEntity(TileDustStructureSpawner.class, Reference.MODID + "tileduststructurespawner");

    }
}
