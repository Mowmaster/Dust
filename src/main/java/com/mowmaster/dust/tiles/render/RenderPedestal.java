package com.mowmaster.dust.tiles.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.minecraft.block.DirectionalBlock.FACING;

public class RenderPedestal extends TileEntityRenderer<TilePedestal> {

    /*https://youtu.be/gZ-8F94UT7k?t=488*/
    public RenderPedestal(TileEntityRendererDispatcher rendererDispatcher)
    {
        super(rendererDispatcher);
    }

    @Override
    public void render(TilePedestal tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {


        if(tileEntityIn==null)
        {
            return;
        }

        else if(tileEntityIn.getBlockState().getBlock() instanceof BlockPedestal)
        {
            int num = 1;
            ItemStack item = tileEntityIn.getItemInPedestal();
            ItemStack itemInBlockBelow = tileEntityIn.getDisplay();
            ItemStack coin = tileEntityIn.getCoinOnPedestal();
            BlockState state = tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos());
            Direction enumfacing = tileEntityIn.getBlockState().get(FACING);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            matrixStackIn.push();
            matrixStackIn.translate(0,0,0);

            if(enumfacing==Direction.UP)//when placed on ground
            {
                renderTile(itemRenderer,tileEntityIn,matrixStackIn,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==Direction.DOWN) {
                matrixStackIn.rotate(new Quaternion(0, 0, 1,180));
                matrixStackIn.translate(0, -1, 0);
                matrixStackIn.translate(-1, 0, 0);
                renderTile(itemRenderer,tileEntityIn,matrixStackIn,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==Direction.NORTH) {
                matrixStackIn.rotate(new Quaternion(1, 0, 0,270));
                matrixStackIn.translate(0, -1, 0);
                renderTile(itemRenderer,tileEntityIn,matrixStackIn,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==Direction.EAST) {

                matrixStackIn.rotate(270, 0, 0, 1);
                matrixStackIn.translate(-1, 0, 0);
                renderTile(itemRenderer,tileEntityIn,matrixStackIn,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==Direction.SOUTH) {
                matrixStackIn.rotate(90, 1, 0, 0);
                matrixStackIn.translate(0, 0, -1);
                renderTile(itemRenderer,tileEntityIn,matrixStackIn,enumfacing,coin,item,itemInBlockBelow);
            }
            if(enumfacing==Direction.WEST) {
                matrixStackIn.rotate(90, 0, 0, 1);
                matrixStackIn.translate(0, -1, 0);
                renderTile(itemRenderer,tileEntityIn,matrixStackIn,enumfacing,coin,item,itemInBlockBelow);
            }
            matrixStackIn.pop();
        }
    }

    public static void  renderTile(ItemRenderer itemRenderer, TilePedestal tileEntityIn, MatrixStack matrixStackIn, Direction enumfacing, ItemStack coin, ItemStack item, ItemStack itemInBlockBelow)
    {
        renderItem(itemRenderer,coin,matrixStackIn,0.5f,0.475f,0.3125f,0,0,0,0);
        renderItem(itemRenderer,coin,matrixStackIn,0.3125f,0.475f,0.5f,90,0,1f,0);
        renderItem(itemRenderer,coin,matrixStackIn,0.5f,0.475f,0.6875f,180,0,1f,0);
        renderItem(itemRenderer,coin,matrixStackIn,0.6875f,0.475f,0.5f,270,0,1f,0);

        if(enumfacing==Direction.UP)
        {
            if(tileEntityIn.isBlockUnder(0,-1,0))
            {
                renderItemRotatingOpaque(itemRenderer,matrixStackIn,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,matrixStackIn,item,1f);
        }
        if(enumfacing==Direction.DOWN)
        {
            if(tileEntityIn.isBlockUnder(0,1,0))
            {
                renderItemRotatingOpaque(itemRenderer,matrixStackIn,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,matrixStackIn,item,1f);
        }
        if (enumfacing.equals(Direction.NORTH))
        {
            if(tileEntityIn.isBlockUnder(0,0,1))
            {
                renderItemRotatingOpaque(itemRenderer,matrixStackIn,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,matrixStackIn,item,1f);
        }
        if (enumfacing.equals(Direction.SOUTH))
        {
            if(tileEntityIn.isBlockUnder(0,0,-1))
            {
                renderItemRotatingOpaque(itemRenderer,matrixStackIn,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,matrixStackIn,item,1f);
        }
        if (enumfacing.equals(Direction.EAST))
        {
            if(tileEntityIn.isBlockUnder(-1,0,0))
            {
                renderItemRotatingOpaque(itemRenderer,matrixStackIn,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,matrixStackIn,item,1f);
        }
        if (enumfacing.equals(Direction.WEST))
        {
            if(tileEntityIn.isBlockUnder(1,0,0))
            {
                renderItemRotatingOpaque(itemRenderer,matrixStackIn,itemInBlockBelow,1f);
            }
            else renderItemRotating(itemRenderer,matrixStackIn,item,1f);
        }

    }

    public static void renderItemRotating(ItemRenderer itemRenderer, MatrixStack matrixStackIn, ItemStack itemStack, float y)
    {
        matrixStackIn.translate(0.5f, y, 0.5f);
        matrixStackIn.scale(2f,2f,2f);
        double boop = Minecraft.getSystemTime()/800D;
        matrixStackIn.translate(0D, 0D, 0D);
        matrixStackIn.rotate((float)(((boop*40D)%360)), 0f, 1f, 0f);
        if (!itemStack.isEmpty()) {
            renderItem(itemRenderer, itemStack , 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }
    }

    public static void renderItemRotatingOpaque(ItemRenderer itemRenderer, MatrixStack matrixStackIn, ItemStack itemStack, float y)
    {
        matrixStackIn.translate(0.5f, y, 0.5f);
        matrixStackIn.scale(2f,2f,2f);
        double boop = Minecraft.getSystemTime()/800D;
        matrixStackIn.translate(0D, 0D, 0D);
        matrixStackIn.rotate((float)(((boop*40D)%360)), 0f, 1f, 0f);
        if (!itemStack.isEmpty()) {
            renderItem(itemRenderer, itemStack , matrixStackIn , 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }
    }

    public static void renderItem(ItemRenderer itemRenderer, ItemStack itemStack, MatrixStack matrixStackIn, float x, float y, float z, float angle, float xr, float yr, float zr) {
        matrixStackIn.push();
        matrixStackIn.translate(x, y, z);
        matrixStackIn.scale(0.1875f,0.1875f,0.1875f);
        matrixStackIn.rotate(angle, xr, yr, zr);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {matrixStackIn.rotate(180f, 0f, 1f, 0f);}
        //GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        //GlStateManager.popAttrib();
        matrixStackIn.pop();
    }

    public static void init(final FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntityRenderer(TilePedestal.pedestal_stone,RenderPedestal::new);
    }
}
