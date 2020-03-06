package com.mowmaster.dust.tiles.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mowmaster.dust.blocks.BlockPedestal;
import com.mowmaster.dust.tiles.TilePedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nonnull;

import static net.minecraft.block.DirectionalBlock.FACING;
import static net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer.TEXTURE_BEACON_BEAM;

public class RenderPedestal extends TileEntityRenderer<TilePedestal> {

    /*https://youtu.be/gZ-8F94UT7k?t=488*/
    public RenderPedestal(TileEntityRendererDispatcher rendererDispatcher)
    {
        super(rendererDispatcher);
    }

    /*public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");
*/
    @Override
    public void render(@Nonnull TilePedestal tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        /*World worldIn = tileEntityIn.getWorld();
        int num = 1;
        ItemStack item = tileEntityIn.getDisplay();
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
        matrixStackIn.translate(0.5D, 1.0D, 0.5D);
        matrixStackIn.scale(16, 16, 16);
        matrixStackIn.translate(.5, .5, 0);
        IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(item, Minecraft.getInstance().world, null);
        int lightmapValue = 0xf000f0;
        itemRenderer.renderItem(item, ItemCameraTransforms.TransformType.FIXED, false, matrixStackIn, bufferIn, lightmapValue, OverlayTexture.NO_OVERLAY, ibakedmodel);
        matrixStackIn.pop();*/

        /*matrixStackIn.push();
        matrixStackIn.translate(0.5, 1.0, 0.5);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = tileEntityIn.getItemInPedestal();
        IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(stack, tileEntityIn.getWorld(), null);
        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
        matrixStackIn.translate(-.5, 1, -.5);
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
        BlockState state = Blocks.ENDER_CHEST.getDefaultState();
        blockRenderer.renderBlock(state, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);
        matrixStackIn.pop();*/

        if (!tileEntityIn.isRemoved()) {
            ItemStack stack = tileEntityIn.getItemInPedestal();
            if (!stack.isEmpty()) {
                matrixStackIn.push();
                matrixStackIn.translate(0.5, 1.0, 0.5);
                matrixStackIn.translate(0, MathHelper.sin((tileEntityIn.getWorld().getGameTime() + partialTicks) / 10.0F) * 0.1 + 0.1, 0);
                matrixStackIn.scale(0.75F, 0.75F, 0.75F);
                float angle = (tileEntityIn.getWorld().getGameTime() + partialTicks) / 20.0F * (180F / (float) Math.PI);
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(angle));
                Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                matrixStackIn.pop();
            }
        }

        /*matrixStackIn.push(); // push
        matrixStackIn.translate(0.5D, 1.0D, 0.5D); // translate
        ResourceLocation beamTextureRL = TEXTURE_BEACON_BEAM;
//    matrixStack.func_227860_a_(); //push
//    matrixStack.func_227863_a_(   //rotate
//            Vector3f.field_229181_d_.func_229187_a_(f * 2.25F - 45.0F));    //  YP.rotationDegrees
        IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderType.getBeaconBeam(beamTextureRL, false));
        float red = 0;
        float green = 0;
        float blue = 0;
        float alpha = 1.0f;
        int startHeight = 0;
        int endHeight = 1;
        float u1 = 0;
        float u2 = 1;
        float v1 = 0;
        float v2 = 0;
        float beamRadius = 1;
        float x1 = 0; float z1 = 0;
        float x2 = 1; float z2 = 0;
        float x3 = 0; float z3 = 1;
        float x4 = 1; float z4 = 1;
        renderPart(matrixStackIn, vertexBuilder, red, green, blue, alpha, startHeight, endHeight,
                x1, z1, x2, z2, x3, z3, x4, z4, u1, u2, v1, v2);
        matrixStackIn.pop();*/ // pop
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

    /*private static void renderPart(MatrixStack matrixStack, IVertexBuilder vertexBuilder,
                                   float red, float green, float blue, float alpha, int ymin, int ymax,
                                   float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4,
                                   float u1, float u2, float v1, float v2) {
        MatrixStack.Entry currentTransformMatrix = matrixStack.getLast();  // getLast
        Matrix4f positionMatrix = currentTransformMatrix.getMatrix();  // getPositionMatrix
        Matrix3f normalMatrix = currentTransformMatrix.getNormal();  // getNormalMatrix
        addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x1, z1, x2, z2, u1, u2, v1, v2);
        addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x4, z4, x3, z3, u1, u2, v1, v2);
        addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x2, z2, x4, z4, u1, u2, v1, v2);
        addQuad(positionMatrix, normalMatrix, vertexBuilder, red, green, blue, alpha, ymin, ymax, x3, z3, x1, z1, u1, u2, v1, v2);
    }
    private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder vertexBuilder,
                                float red, float green, float blue, float alpha, int yMin, int yMax,
                                float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        addVertex(matrixPos, matrixNormal, vertexBuilder, red, green, blue, alpha, yMax, x1, z1, u2, v1);
        addVertex(matrixPos, matrixNormal, vertexBuilder, red, green, blue, alpha, yMin, x1, z1, u2, v2);
        addVertex(matrixPos, matrixNormal, vertexBuilder, red, green, blue, alpha, yMin, x2, z2, u1, v2);
        addVertex(matrixPos, matrixNormal, vertexBuilder, red, green, blue, alpha, yMax, x2, z2, u1, v1);
    }
    private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder vertexBuilder,
                                  float red, float green, float blue, float alpha,
                                  int y, float x, float z, float texU, float texV) {
        vertexBuilder.pos(matrixPos, x, (float)y, z) // pos
                .color(red, green, blue, alpha)        // color
                .tex(texU, texV)                     // tex
                .overlay(OverlayTexture.NO_OVERLAY) // overlay; field_229196_a_ = no modifier
                .lightmap(0xf000f0)                       // lightmap with full brightness
                .normal(matrixNormal, 0.0F, 1.0F, 0.0F)  // normal
                .endVertex();
    }*/

    public static void init(final FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntityRenderer(TilePedestal.pedestal_stone,RenderPedestal::new);
    }
}