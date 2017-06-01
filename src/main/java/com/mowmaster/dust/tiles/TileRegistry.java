package com.mowmaster.dust.tiles;

import com.mowmaster.dust.blocks.BlockCrystalBase;
import com.mowmaster.dust.references.Reference;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileRegistry
{
    public static void registerTile()
    {
        GameRegistry.registerTileEntity(TileCrystalCluster.class, Reference.MODID + "tilecrystalcluster");
    }
}
