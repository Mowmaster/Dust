package com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.mowmaster.dust.Block.BlockEntities.Pedestal.BasePedestalBlock.FACING;
import static com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15.ScrollCrafterBlock_T15.SIDED_ROTATION_4;
import static com.mowmaster.dust.References.Constants.MODID;

public class ScrollCrafterBlockEntityRender_T15 implements BlockEntityRenderer<ScrollCrafterBlockEntity_T15> {

    public ScrollCrafterBlockEntityRender_T15(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ScrollCrafterBlockEntity_T15 p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if (!p_112307_.isRemoved()) {
            Level world = p_112307_.getLevel();
            Direction facing = p_112307_.getBlockState().getValue(SIDED_ROTATION_4);
            List<ItemStack> stacksList = p_112307_.getListOfInsertedItemsToDisplay();

            if(!(stacksList.size() == 0))
            {
                if(facing == Direction.SOUTH) {
                    //p_112309_.mulPose(Vector3f.YP.rotationDegrees(180));
                    //p_112309_.translate(0, 0, 0);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        renderItem(world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6), p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                }
                if(facing == Direction.NORTH) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(180));
                    p_112309_.translate(-1, 0, -1);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        renderItem(world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6), p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                }
                if(facing == Direction.EAST) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(90));
                    p_112309_.translate(-1, 0, 0);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        renderItem(world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6), p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                }
                if(facing == Direction.WEST) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(270));
                    p_112309_.translate(0, 0, -1);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        renderItem(world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6), p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                }
            }
        }
    }

    public static void renderItem(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack itemStack, int p_112311_, int p_112312_, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float angle, boolean renderAsBlock)
    {
        if (!itemStack.isEmpty()) {
            p_112309_.pushPose();
            p_112309_.translate(x, y, z);
            p_112309_.scale(scaleX, scaleY, scaleZ);
            p_112309_.mulPose(Vector3f.YP.rotationDegrees(angle));
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BlockRenderDispatcher renderBlock = Minecraft.getInstance().getBlockRenderer();
            BakedModel baked = renderer.getModel(itemStack,worldIn,null,0);
            if(renderAsBlock)
            {
                baked = renderBlock.getBlockModel(Block.byItem(itemStack.getItem()).defaultBlockState());
            }
            renderer.render(itemStack, ItemTransforms.TransformType.FIXED,true,p_112309_,p_112310_,p_112311_,p_112312_,baked);
            p_112309_.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(ScrollCrafterBlockEntity_T15 p_112306_) {
        return true;
    }
}
