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

            if(te.getCrystalCount()==1)
            {
                Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ItemRegistry.crystal,1,te.getCrystalInList(0)), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.translate(0,0,-0.1);
            }
            else if(te.getCrystalCount()==2)
            {
                Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ItemRegistry.crystal,1,te.getCrystalInList(0)), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.translate(0,0,-0.1);
                Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ItemRegistry.crystal,1,te.getCrystalInList(1)), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.translate(0,0,-0.2);
            }
            else if(te.getCrystalCount()==3)
            {
                Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ItemRegistry.crystal,1,te.getCrystalInList(0)), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.translate(0,0,-0.1);
                Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ItemRegistry.crystal,1,te.getCrystalInList(1)), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.translate(0,0,-0.2);
                Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ItemRegistry.crystal,1,te.getCrystalInList(2)), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.translate(0,0,-0.3);
            }
        }
        GlStateManager.popMatrix();
        {}
    }
}
