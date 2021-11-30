package com.mowmaster.dust.Block.Pedestal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mowmaster.dust.DeferredRegistery.DeferredBlockEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

import static com.mowmaster.dust.Block.Pedestal.BasePedestalBlock.FACING;

public class BasePedestalBlockEntityRenderer implements BlockEntityRenderer<BasePedestalBlockEntity>
{

    public BasePedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(BasePedestalBlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if (!p_112307_.isRemoved()) {
            Direction facing = p_112307_.getBlockState().getValue(FACING);
            ItemStack stack = p_112307_.getItemInPedestal();
            ItemStack coin = p_112307_.getCoinOnPedestal();
            Level world = p_112307_.getLevel();
            // 0 - No Particles
            // 1 - No Render Item
            // 2 - No Render Upgrade
            // 3 - No Particles/No Render Item
            // 4 - No Particles/No Render Upgrade
            // 5 - No Render Item/No Render Upgrade
            // 6 - No Particles/No Render Item/No Render Upgrade
            // 7 - No Augment exists and thus all rendering is fine.
            int renderAugmentType = p_112307_.getRendererType();

            if(renderAugmentType !=6)
            {
                if(facing== Direction.UP)//when placed on ground
                {
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.DOWN) {
                    //p_112309_.rotate(new Quaternion(0, 0, 1,180));
                    p_112309_.mulPose(Vector3f.ZP.rotationDegrees(180));
                    p_112309_.translate(0, -1, 0);
                    p_112309_.translate(-1, 0, 0);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.NORTH) {
                    //p_112309_.rotate(new Quaternion(1, 0, 0,270));
                    p_112309_.mulPose(Vector3f.XP.rotationDegrees(270));
                    p_112309_.translate(0, -1, 0);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.EAST) {
                    //p_112309_.mulPose(270, 0, 0, 1);
                    p_112309_.mulPose(Vector3f.ZP.rotationDegrees(270));
                    p_112309_.translate(-1, 0, 0);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.SOUTH) {
                    //p_112309_.mulPose(90, 1, 0, 0);
                    p_112309_.mulPose(Vector3f.XP.rotationDegrees(90));
                    p_112309_.translate(0, 0, -1);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
                if(facing== Direction.WEST) {
                    //p_112309_.mulPose(90, 0, 0, 1);
                    p_112309_.mulPose(Vector3f.ZP.rotationDegrees(90));
                    p_112309_.translate(0, -1, 0);
                    renderTile(world,p_112309_,p_112310_,stack,coin,p_112311_,p_112312_,renderAugmentType);
                    //renderTile(world,p_112309_,p_112310_,coin,stack,p_112311_,p_112312_,renderAugmentType);
                }
            }
        }
    }
    //public static void  renderTile(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack coin, ItemStack item, int p_112311_, int p_112312_, int renderAugmentType)
    public static void  renderTile(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack item, ItemStack coin, int p_112311_, int p_112312_, int renderAugmentType)
    {
        switch (renderAugmentType)
        {
            case 0:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
            break;
            case 1:
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
                break;
            case 2:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                break;
            case 3:
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
                break;
            case 4:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                break;
            case 5: break;
            case 6: break;
            case 7:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
                break;
            default:
                renderItemRotating(worldIn,p_112309_,p_112310_,item,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.3125f,0,0,0,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.3125f,0.475f,0.5f,90,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.5f,0.475f,0.6875f,180,0,1f,0,p_112311_,p_112312_);
                renderCoin(worldIn,coin,p_112309_,p_112310_,0.6875f,0.475f,0.5f,270,0,1f,0,p_112311_,p_112312_);
                break;

        }
    }

    public static void renderItemRotating(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack itemStack, int p_112311_, int p_112312_)
    {
        if (!itemStack.isEmpty()) {
            p_112309_.pushPose();
            p_112309_.translate(0.5, 1.0, 0.5);
            //p_112309_.translate(0, MathHelper.sin((worldIn.getGameTime()) / 10.0F) * 0.1 + 0.1, 0); BOBBING ITEM
            p_112309_.scale(0.75F, 0.75F, 0.75F);
            long time = System.currentTimeMillis();
            float angle = (time/25) % 360;
            //float angle = (worldIn.getGameTime()) / 20.0F * (180F / (float) Math.PI);
            p_112309_.mulPose(Vector3f.YP.rotationDegrees(angle));
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel baked = renderer.getModel(itemStack,worldIn,null,0);
            renderer.render(itemStack, ItemTransforms.TransformType.GROUND,true,p_112309_,p_112310_,p_112311_,p_112312_,baked);

            //Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.GROUND, p_112311_, p_112312_, p_112309_, p_112310_);
            p_112309_.popPose();
        }
    }
    public static void renderCoin(Level worldIn,ItemStack itemCoin, PoseStack p_112309_, MultiBufferSource p_112310_, float x, float y, float z, float angle, float xr, float yr, float zr, int p_112311_, int p_112312_) {
        if (!itemCoin.isEmpty()) {
            p_112309_.pushPose();
            p_112309_.translate(x, y, z);
            p_112309_.scale(0.1875f, 0.1875f, 0.1875f);
            p_112309_.mulPose(Vector3f.YP.rotationDegrees(angle));
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel baked = renderer.getModel(itemCoin,worldIn,null,0);
            renderer.render(itemCoin,ItemTransforms.TransformType.FIXED,true,p_112309_,p_112310_,p_112311_,p_112312_,baked);
            //Minecraft.getInstance().getItemRenderer().renderItem(itemCoin, ItemCameraTransforms.TransformType.FIXED, p_112311_, p_112312_, p_112309_, p_112310_);
            p_112309_.popPose();
        }
    }
}
