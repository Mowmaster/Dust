package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.blocks.BlockPedestal;
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
            ItemStack itemInBlockBelow = te.getDisplay();
            ItemStack coin = te.getCoinOnPedestal();
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            if(enumfacing==EnumFacing.UP)//when placed on ground
            {
                renderTile(itemRenderer,te,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==EnumFacing.DOWN) {
                GlStateManager.rotate(180, 0, 0, 1);
                GlStateManager.translate(0, -1, 0);
                GlStateManager.translate(-1, 0, 0);
                renderTile(itemRenderer,te,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==EnumFacing.NORTH) {
                GlStateManager.rotate(270, 1, 0, 0);
                GlStateManager.translate(0, -1, 0);
                renderTile(itemRenderer,te,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==EnumFacing.EAST) {

                GlStateManager.rotate(270, 0, 0, 1);
                GlStateManager.translate(-1, 0, 0);
                renderTile(itemRenderer,te,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==EnumFacing.SOUTH) {
                GlStateManager.rotate(90, 1, 0, 0);
                GlStateManager.translate(0, 0, -1);
                renderTile(itemRenderer,te,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==EnumFacing.WEST) {
                GlStateManager.rotate(90, 0, 0, 1);
                GlStateManager.translate(0, -1, 0);
                renderTile(itemRenderer,te,enumfacing,coin,item,itemInBlockBelow);
            }
            GlStateManager.popMatrix();
        }

    }

    public static void  renderTile(RenderItem itemRenderer, TilePedestal te, EnumFacing enumfacing, ItemStack coin, ItemStack item, ItemStack itemInBlockBelow)
    {
        renderItem(itemRenderer,coin,0.5f,0.475f,0.3125f,0,0,0,0);
        renderItem(itemRenderer,coin,0.3125f,0.475f,0.5f,90,0,1f,0);
        renderItem(itemRenderer,coin,0.5f,0.475f,0.6875f,180,0,1f,0);
        renderItem(itemRenderer,coin,0.6875f,0.475f,0.5f,270,0,1f,0);

        if(enumfacing==EnumFacing.UP)
        {
            if(te.isBlockUnder(0,-1,0))
            {
                renderItemRotatingOpaque(itemRenderer,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,item,1f);
        }
        if(enumfacing==EnumFacing.DOWN)
        {
            if(te.isBlockUnder(0,1,0))
            {
                renderItemRotatingOpaque(itemRenderer,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,item,1f);
        }
        if (enumfacing.equals(EnumFacing.NORTH))
        {
            if(te.isBlockUnder(0,0,1))
            {
                renderItemRotatingOpaque(itemRenderer,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,item,1f);
        }
        if (enumfacing.equals(EnumFacing.SOUTH))
        {
            if(te.isBlockUnder(0,0,-1))
            {
                renderItemRotatingOpaque(itemRenderer,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,item,1f);
        }
        if (enumfacing.equals(EnumFacing.EAST))
        {
            if(te.isBlockUnder(-1,0,0))
            {
                renderItemRotatingOpaque(itemRenderer,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,item,1f);
        }
        if (enumfacing.equals(EnumFacing.WEST))
        {
            if(te.isBlockUnder(1,0,0))
            {
                renderItemRotatingOpaque(itemRenderer,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,item,1f);
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

    public static void renderItemRotatingOpaque(RenderItem itemRenderer, ItemStack itemStack, float y)
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
