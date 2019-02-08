package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.blocks.machines.TierZero.CrystalCrusherBasic;
import com.mowmaster.dust.blocks.machines.TierZero.CrystalFurnaceBasic;
import com.mowmaster.dust.blocks.treebits.BlockCharcoal;
import com.mowmaster.dust.items.ItemRegistry;
import com.mowmaster.dust.tiles.TileCrystalCrusherBasic;
import com.mowmaster.dust.tiles.TileCrystalFurnaceBasic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;


public class RenderTileCrystalFurnaceBasic extends TileEntitySpecialRenderer<TileCrystalFurnaceBasic>
{
    @Override
    public void render(TileCrystalFurnaceBasic te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(te==null)
        {
            return;
        }

        else if(te.getBlockType() instanceof CrystalFurnaceBasic)
        {
            ItemStack fuelRender = new ItemStack(BlockCharcoal.charcoalBlockColors,1,0);
            ItemStack fuel = te.getStackInSlot(1);
            ItemStack input = te.getStackInSlot(0);
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            float yF = 0.307f + (fuel.getCount() * 0.0625f);
            renderItemScale(itemRenderer,fuelRender,0.75f,0.75f,0.75f,0.5f,yF,0.5f,0,0,0,0);
            renderItemRotatingOpaque(itemRenderer,input,0.5f,1.1875f,0.5f);

            GlStateManager.popMatrix();
        }

    }

    public static void renderItemRotating(RenderItem itemRenderer, ItemStack itemStack, float x,float y,float z)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(1f,1f,1f);
        double boop = Minecraft.getSystemTime()/800D;
        GlStateManager.translate(0D, 0D, 0D);
        GlStateManager.rotate((float)(((boop*40D)%360)), 0f, 0f, 1f);
        if (!itemStack.isEmpty()) {
            renderItem(itemRenderer, itemStack , 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }

        GlStateManager.popMatrix();
    }

    public static void renderItemRotatingOpaque(RenderItem itemRenderer, ItemStack itemStack, float x,float y,float z)
    {
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(0.75f,0.75f,0.75f);
        double boop = Minecraft.getSystemTime()/800D;
        GlStateManager.translate(0D, 0D, 0D);
        GlStateManager.rotate((float)(((boop*20D)%360)), 0f, 1f, 0f);
        if (!itemStack.isEmpty()) {
            renderItem(itemRenderer, itemStack , 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }
    }

    public static void renderItem(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(0.5f,0.5f,0.5f);
        GlStateManager.rotate(angle, xr, yr, zr);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {GlStateManager.rotate(180f, 0f, 1f, 0f);}
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public static void renderItemScale(RenderItem itemRenderer, ItemStack itemStack,float sx, float sy, float sz, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(sx,sy,sz);
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
