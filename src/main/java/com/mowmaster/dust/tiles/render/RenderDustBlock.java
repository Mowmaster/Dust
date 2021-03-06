package com.mowmaster.dust.tiles.render;

import com.mowmaster.dust.tiles.TileDustBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
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
/*
        if(!te.getStackInSlot(0).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,0),value,0.0625f,value); }
        if(!te.getStackInSlot(1).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,1),value,0.1875f,value); }
        if(!te.getStackInSlot(2).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(2).getItem(),1,te.getStackInSlot(2).getMetadata()),value,0.3125f,value); }
        if(!te.getStackInSlot(3).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(3).getItem(),1,te.getStackInSlot(3).getMetadata()),value,0.4375f,value); }
        if(!te.getStackInSlot(4).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(4).getItem(),1,te.getStackInSlot(4).getMetadata()),value,0.5625f,value); }
        if(!te.getStackInSlot(5).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(5).getItem(),1,te.getStackInSlot(5).getMetadata()),value,0.6875f,value); }
        if(!te.getStackInSlot(6).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(6).getItem(),1,te.getStackInSlot(6).getMetadata()),value,0.8125f,value); }
*/
        if(te.getStackInSlot(1).isEmpty() && !te.getStackInSlot(0).isEmpty()) { renderLayerLast(itemRenderer,newItemRender(te,0),value,0.5f,value,0); }
        else if(!te.getStackInSlot(0).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,0),value,0.0625f,value); }
        if(te.getStackInSlot(2).isEmpty() && !te.getStackInSlot(1).isEmpty()) { renderLayerLast(itemRenderer,newItemRender(te,1),value,0.5625f,value,1); }
        else if(!te.getStackInSlot(1).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,1),value,0.1875f,value); }
        if(te.getStackInSlot(3).isEmpty() && !te.getStackInSlot(2).isEmpty()) { renderLayerLast(itemRenderer,newItemRender(te,2),value,0.625f,value,2); }
        else if(!te.getStackInSlot(2).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,2),value,0.3125f,value); }
        if(te.getStackInSlot(4).isEmpty() && !te.getStackInSlot(3).isEmpty()) { renderLayerLast(itemRenderer,newItemRender(te,3),value,0.6875f,value,3); }
        else if(!te.getStackInSlot(3).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,3),value,0.4375f,value); }
        if(te.getStackInSlot(5).isEmpty() && !te.getStackInSlot(4).isEmpty()) { renderLayerLast(itemRenderer,newItemRender(te,4),value,0.75f,value,4); }
        else if(!te.getStackInSlot(4).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,4),value,0.5625f,value); }
        if(te.getStackInSlot(6).isEmpty() && !te.getStackInSlot(5).isEmpty()) { renderLayerLast(itemRenderer,newItemRender(te,5),value,0.8125f,value,5); }
        else if(!te.getStackInSlot(5).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,5),value,0.6875f,value); }
        if(te.getStackInSlot(7).isEmpty() && !te.getStackInSlot(7).isEmpty()) { renderLayerLast(itemRenderer,newItemRender(te,6),value,0.875f,value,6); }
        else if(!te.getStackInSlot(7).isEmpty()) { renderLayer(itemRenderer,newItemRender(te,6),value,0.8125f,value); }
        if(!te.getStackInSlot(7).isEmpty()) { renderLayer(itemRenderer,new ItemStack(te.getStackInSlot(7).getItem(),1,te.getStackInSlot(7).getMetadata()),value,0.9375f,value); }

        GlStateManager.popMatrix();
    }

    private ItemStack newItemRender(TileDustBlock te, int slot)
    {
        return new ItemStack(te.getStackInSlot(slot).getItem(),1,te.getStackInSlot(slot).getMetadata());
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

    public static void renderLayerLast(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z, int slot) {
        float yf = 2.0f;

        switch (slot)
        {
            case 0:
                yf=2.0f;
                break;
            case 1:
                yf=1.75f;
                break;
            case 2:
                yf=1.5f;
                break;
            case 3:
                yf=1.25f;
                break;
            case 4:
                yf=1.0f;
                break;
            case 5:
                yf=0.75f;
                break;
            case 6:
                yf= 0.5f;
                break;
            case 7:
                yf=0.25f;
                break;
        }


        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(2.0f,yf,2.0f);
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
