package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.blocks.BlockCrystalClusterBasic;
import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TileCrystalCluster;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import static net.minecraft.block.BlockDirectional.FACING;


public class RenderTilePedestal extends TileEntitySpecialRenderer<TilePedestal>
{

    @Override
    public void render(TilePedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(te==null)
        {
            return;
        }

        else if(te.getBlockType() instanceof BlockPedestal)
        {
            int num = 1;
            ItemStack item = te.getItemInPedestal();
            ItemStack coin = te.getCoinOnPedestal();
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            renderItem(itemRenderer,coin,0.5f,0.475f,0.3125f,0,0,0,0);
            renderItem(itemRenderer,coin,0.3125f,0.475f,0.5f,90,0,1f,0);
            renderItem(itemRenderer,coin,0.5f,0.475f,0.6875f,180,0,1f,0);
            renderItem(itemRenderer,coin,0.6875f,0.475f,0.5f,270,0,1f,0);

            renderItemRotating(itemRenderer,item,1f);
            GlStateManager.popMatrix();
        }

    }

    public static void renderItemRotating(RenderItem itemRenderer, ItemStack itemStack, float y)
    {
        GlStateManager.translate(0.5f, y, 0.5f);
        GlStateManager.scale(2f,2f,2f);
        double boop = Minecraft.getSystemTime()/800D;
        GlStateManager.translate(0D, 0D, 0D);
        GlStateManager.rotate((float)(((boop*40D)%360)), 0f, 1f, 0f);
        if (!itemStack.isEmpty()) {
            renderItem(itemRenderer, itemStack , 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }
    }

    public static void renderItem(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(0.1875f,0.1875f,0.1875f);
        GlStateManager.rotate(angle, xr, yr, zr);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {GlStateManager.rotate(180f, 0f, 1f, 0f);}
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
