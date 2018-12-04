package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.blocks.BlockDustCloud;
import com.mowmaster.dust.tiles.TileDustBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDustBlock extends TileEntitySpecialRenderer<TileDustBlock>
{

    public void render(TileDustBlock te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        float value = 0.5f;

        if(!te.getStackInSlot(0).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(0).getItem(),1,te.getStackInSlot(0).getMetadata()),value,0.0625f,value); }
        if(!te.getStackInSlot(1).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(1).getItem(),1,te.getStackInSlot(1).getMetadata()),value,0.1875f,value); }
        if(!te.getStackInSlot(2).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(2).getItem(),1,te.getStackInSlot(2).getMetadata()),value,0.3125f,value); }
        if(!te.getStackInSlot(3).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(3).getItem(),1,te.getStackInSlot(3).getMetadata()),value,0.4375f,value); }
        if(!te.getStackInSlot(4).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(4).getItem(),1,te.getStackInSlot(4).getMetadata()),value,0.5625f,value); }
        if(!te.getStackInSlot(5).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(5).getItem(),1,te.getStackInSlot(5).getMetadata()),value,0.6875f,value); }
        if(!te.getStackInSlot(6).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(6).getItem(),1,te.getStackInSlot(6).getMetadata()),value,0.8125f,value); }
        if(!te.getStackInSlot(7).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(7).getItem(),1,te.getStackInSlot(7).getMetadata()),value,0.9375f,value); }

        if(te.getWorld().getBlockState(te.getPos().add(0,1,0)).getBlock() instanceof BlockDustCloud)
        {
            renderBlockAbove(te,itemRenderer,(float)x,(float)y,(float)z);
        }

        GlStateManager.popMatrix();
    }

    public static void renderLayer(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(2.0f,0.25f,2.0f);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {GlStateManager.rotate(180f, 0f, 1f, 0f);}
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public static void renderBlockAbove(TileDustBlock te, RenderItem itemRenderer, float x, float y, float z) {
        float yval = (float)(1-(((te.getNextAvailSlot()-1)*0.125)-0.625));
        float ypos = (float)(((te.getNextAvailSlot()-1)*0.125)-0.625);
        ItemStack getBlockAbove = te.getBlockAbove();





        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y+ypos, z);
        GlStateManager.scale(2.0f,yval,2.0f);
        if (!itemRenderer.shouldRenderItemIn3D(getBlockAbove)) {GlStateManager.rotate(180f, 0f, 1f, 0f);}
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(getBlockAbove, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

}
