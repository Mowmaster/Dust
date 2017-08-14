package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class RenderTileCrystalCluster extends TileEntitySpecialRenderer<TileCrystalCluster>
{


    @Override
    public void render(TileCrystalCluster te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage,alpha);



        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x,y,z);
            GlStateManager.translate(0,0,0);
            GlStateManager.rotate(90f,1,0,0);
            GlStateManager.translate(0.3,0.5,-0.2);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.enableLighting();
            for(int j=0;j<te.CrystalList.size()-1;j++)
            {
                //Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.COAL), ItemCameraTransforms.TransformType.GROUND);
                //Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ItemRegistry.crystal), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.pushMatrix();
                Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ItemRegistry.crystal,1,te.getCrystalInList(j)), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.translate(0,0,-0.25);
                GlStateManager.popMatrix();

            }
        }
        GlStateManager.popMatrix();
        {}





    }
}
