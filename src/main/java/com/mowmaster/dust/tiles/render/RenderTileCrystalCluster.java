package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;


public class RenderTileCrystalCluster extends TileEntitySpecialRenderer<TileCrystalCluster>
{
    @Override
    public void renderTileEntityFast(TileCrystalCluster te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        super.renderTileEntityFast(te, x, y, z, partialTicks, destroyStage, partial, buffer);

        GlStateManager.pushMatrix();
        {

        }
        GlStateManager.popMatrix();
    }
}
