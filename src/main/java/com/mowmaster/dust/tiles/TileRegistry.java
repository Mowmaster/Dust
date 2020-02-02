package com.mowmaster.dust.tiles;

import com.mowmaster.dust.references.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileRegistry
{
    public static void registerTile()
    {
        GameRegistry.registerTileEntity(TileCrystalCluster.class, new ResourceLocation(Reference.MODID,"tilecrystalcluster"));
        GameRegistry.registerTileEntity(TilePedestal.class, new ResourceLocation(Reference.MODID,"tilepedestal"));
        GameRegistry.registerTileEntity(TileCrystalCrusherBasic.class, new ResourceLocation(Reference.MODID,"tilecrystalcrusherbasic"));
        GameRegistry.registerTileEntity(TileCrystalFurnace.class, new ResourceLocation(Reference.MODID,"tilecrystalfurnace"));
        GameRegistry.registerTileEntity(TileLootBlock.class, new ResourceLocation(Reference.MODID,"tilelootblock"));
        GameRegistry.registerTileEntity(TileTrapBlock.class, new ResourceLocation(Reference.MODID,"tiletrapblock"));
        GameRegistry.registerTileEntity(TileDustBlock.class, new ResourceLocation(Reference.MODID,"tiledustblock"));
        GameRegistry.registerTileEntity(TileCrystalFurnaceBasic.class, new ResourceLocation(Reference.MODID,"tilecrystalfurnacebasic"));
        GameRegistry.registerTileEntity(TileDustStructureSpawner.class, new ResourceLocation(Reference.MODID,"tileduststructurespawner"));
    }
}
