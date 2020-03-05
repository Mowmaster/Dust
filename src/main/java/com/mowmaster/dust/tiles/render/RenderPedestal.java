package com.mowmaster.dust.tiles.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
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

        World worldIn = tileEntityIn.getWorld();
        int num = 1;
        ItemStack item = tileEntityIn.getItemInPedestal();
        ItemStack coin = tileEntityIn.getCoinOnPedestal();
        BlockState state = tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos());
        Block block = state.getBlock();
        if (!(block instanceof BlockPedestal)) {
            return;
        }

        //else// if(tileEntityIn.getBlockState().getBlock() instanceof BlockPedestal)
        //{

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BlockPedestal blockPedestal = (BlockPedestal) block;
        matrixStackIn.push();
        Direction enumfacing = tileEntityIn.getBlockState().get(FACING);
        matrixStackIn.translate(0f,1f,0f);
        matrixStackIn.scale(16, 16, 16);
        matrixStackIn.translate(.5, .5, 0);
        IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(item, Minecraft.getInstance().world, null);
        int lightmapValue = 0xf000f0;
        itemRenderer.renderItem(item, ItemCameraTransforms.TransformType.FIXED, false, matrixStackIn, bufferIn, lightmapValue, OverlayTexture.NO_OVERLAY, ibakedmodel);

        //itemRenderer.renderItem(item,ItemCameraTransforms.TransformType.FIXED,combinedLightIn,combinedOverlayIn,matrixStackIn,bufferIn);

            /*if(enumfacing==Direction.UP)//when placed on ground
            {
                renderTile(worldIn,itemRenderer,tileEntityIn,matrixStackIn,bufferIn,enumfacing,coin,item,combinedLightIn,combinedOverlayIn);
            }*/
            /*if(enumfacing==Direction.DOWN) {
              //matrixStackIn.rotate(new Quaternion(0, 0, 1,180));
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
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
            }*/
            matrixStackIn.pop();
        //}
    }

    /*public static void  renderTile(World worldIn, ItemRenderer itemRenderer, TilePedestal tileEntityIn, MatrixStack matrixStackIn,IRenderTypeBuffer bufferIn, Direction enumfacing, ItemStack coin, ItemStack item,int combinedLightIn, int combinedOverlayIn)
    {
        renderItem(itemRenderer,coin,matrixStackIn,bufferIn,0.5f,0.475f,0.3125f,0,0,0,0,combinedLightIn,combinedOverlayIn);
        renderItem(itemRenderer,coin,matrixStackIn,bufferIn,0.3125f,0.475f,0.5f,90,0,1f,0,combinedLightIn,combinedOverlayIn);
        renderItem(itemRenderer,coin,matrixStackIn,bufferIn,0.5f,0.475f,0.6875f,180,0,1f,0,combinedLightIn,combinedOverlayIn);
        renderItem(itemRenderer,coin,matrixStackIn,bufferIn,0.6875f,0.475f,0.5f,270,0,1f,0,combinedLightIn,combinedOverlayIn);

        renderItemRotating(worldIn,itemRenderer,matrixStackIn,bufferIn,item,1f,combinedLightIn,combinedOverlayIn);

    }

    public static void renderItemRotating(World worldIn, ItemRenderer itemRenderer, MatrixStack matrixStackIn,IRenderTypeBuffer bufferIn, ItemStack itemStack, float y,int combinedLightIn, int combinedOverlayIn)
    {

        matrixStackIn.translate(0.5f, y, 0.5f);
        matrixStackIn.scale(2f,2f,2f);
        float boop = (float)worldIn.getGameTime()%10;
        matrixStackIn.translate(0D, 0D, 0D);
        matrixStackIn.rotate(new Quaternion(0f, 1f, 0f,((boop*36)+36)));
        if (!itemStack.isEmpty()) {
            renderItem(itemRenderer, itemStack , matrixStackIn ,bufferIn,0f, 0f, 0f, 0f, 0f, 0f, 0f,combinedLightIn,combinedOverlayIn);
        }
    }

    public static void renderItem(ItemRenderer itemRenderer, ItemStack itemStack, MatrixStack matrixStackIn,IRenderTypeBuffer bufferIn, float x, float y, float z, float angle, float xr, float yr, float zr,int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(x, y, z);
        matrixStackIn.scale(0.1875f,0.1875f,0.1875f);
        matrixStackIn.rotate(new Quaternion(xr, yr, zr, angle));
        //if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {matrixStackIn.rotate(180f, 0f, 1f, 0f);}
        //GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        //renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        itemRenderer.renderItem(itemStack,ItemCameraTransforms.TransformType.FIXED,combinedLightIn,combinedOverlayIn,matrixStackIn,bufferIn);
        RenderHelper.disableStandardItemLighting();
        //GlStateManager.popAttrib();
        matrixStackIn.pop();
    }*/

    public static void init(final FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntityRenderer(TilePedestal.pedestal_stone,RenderPedestal::new);
    }
}
