package com.mowmaster.dust.Block.BlockEntities.Tier1.Crafter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

//import static com.mowmaster.dust.Block.BlockEntities.Tier1.Crafter.CrafterBlock_T15.SIDED_ROTATION_4;

public class CrafterBlockEntityRender_T15 //implements BlockEntityRenderer<CrafterBlockEntity_T15>
{

    /*public CrafterBlockEntityRender_T15(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CrafterBlockEntity_T15 p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if (!p_112307_.isRemoved()) {
            Level world = p_112307_.getLevel();
            Direction facing = p_112307_.getBlockState().getValue(SIDED_ROTATION_4);
            List<ItemStack> stacksList = p_112307_.getListOfInsertedItemsToDisplay();
            List<ItemStack> stacksInTable = p_112307_.getStacksInTable();
            //    11/32 = 0.34375f
            //    5/32 = 0.15625f
            float offset = 0.34375f;
            float itemOffset = 0.15625f;
            float itemYOffset = 0.9625f;
            ItemStack crafterOutput = p_112307_.getCraftingOutput();

            if(!(stacksList.size() == 0))
            {
                if(facing == Direction.SOUTH) {
                    //p_112309_.mulPose(Vector3f.YP.rotationDegrees(180));
                    //p_112309_.translate(0, 0, 0);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        if(floatValues.size()>0)renderItem(world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6),0, p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                    if(stacksInTable.size()>0)
                    {
                        if(stacksInTable.size()<=4)
                        {
                            int counterX = 0;
                            int counterY = 0;
                            for(int i=0;i<stacksInTable.size();i++)
                            {
                                if(!(stacksInTable.get(i).getItem().equals(Items.BARRIER)))renderItem(world,p_112309_,p_112310_,stacksInTable.get(i),p_112311_,p_112312_, offset+(itemOffset*counterX), itemYOffset, offset+(itemOffset*counterY), 0.15f, 0.15f, 0.15f,270,1, (stacksInTable.get(i).getItem() instanceof BlockItem)?(true):(false));
                                counterX++;
                                if(counterX>=2)
                                {
                                    counterX=0;
                                    counterY++;
                                }
                            }
                        }
                        else if(stacksInTable.size() > 4)
                        {
                            int counterX = 0;
                            int counterY = 0;
                            for(int i=0;i<stacksInTable.size();i++)
                            {
                                if(!(stacksInTable.get(i).getItem().equals(Items.BARRIER)))renderItem(world,p_112309_,p_112310_,stacksInTable.get(i),p_112311_,p_112312_, offset+(itemOffset*counterX), itemYOffset, offset+(itemOffset*counterY), 0.15f, 0.15f, 0.15f,270,1, (stacksInTable.get(i).getItem() instanceof BlockItem)?(true):(false));
                                counterX++;
                                if(counterX>=3)
                                {
                                    counterX=0;
                                    counterY++;
                                }
                            }
                        }
                    }

                    if(!crafterOutput.isEmpty())renderItemRotating(world,p_112309_,p_112310_, crafterOutput, p_112311_, p_112312_);
                }
                if(facing == Direction.NORTH) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(180));
                    p_112309_.translate(-1, 0, -1);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        if(floatValues.size()>0)renderItem(world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6),0, p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                    if(stacksInTable.size()>0)
                    {
                        if(stacksInTable.size()<=4)
                        {
                            int counterX = 0;
                            int counterY = 0;
                            for(int i=0;i<stacksInTable.size();i++)
                            {
                                if(!(stacksInTable.get(i).getItem().equals(Items.BARRIER)))renderItem(world,p_112309_,p_112310_,stacksInTable.get(i),p_112311_,p_112312_, offset+(itemOffset*counterX), itemYOffset, offset+(itemOffset*counterY), 0.15f, 0.15f, 0.15f,270,1, (stacksInTable.get(i).getItem() instanceof BlockItem)?(true):(false));
                                counterX++;
                                if(counterX>=2)
                                {
                                    counterX=0;
                                    counterY++;
                                }
                            }
                        }
                        else if(stacksInTable.size() > 4)
                        {
                            int counterX = 0;
                            int counterY = 0;
                            for(int i=0;i<stacksInTable.size();i++)
                            {
                                if(!(stacksInTable.get(i).getItem().equals(Items.BARRIER)))renderItem(world,p_112309_,p_112310_,stacksInTable.get(i),p_112311_,p_112312_, offset+(itemOffset*counterX), itemYOffset, offset+(itemOffset*counterY), 0.15f, 0.15f, 0.15f,270,1, (stacksInTable.get(i).getItem() instanceof BlockItem)?(true):(false));
                                counterX++;
                                if(counterX>=3)
                                {
                                    counterX=0;
                                    counterY++;
                                }
                            }
                        }
                    }

                    if(!crafterOutput.isEmpty())renderItemRotating(world,p_112309_,p_112310_, crafterOutput, p_112311_, p_112312_);
                }
                if(facing == Direction.EAST) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(90));
                    p_112309_.translate(-1, 0, 0);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        if(floatValues.size()>0)renderItem(world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6),0, p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                    if(stacksInTable.size()>0)
                    {
                        if(stacksInTable.size()<=4)
                        {
                            int counterX = 0;
                            int counterY = 0;
                            for(int i=0;i<stacksInTable.size();i++)
                            {
                                if(!(stacksInTable.get(i).getItem().equals(Items.BARRIER)))renderItem(world,p_112309_,p_112310_,stacksInTable.get(i),p_112311_,p_112312_, offset+(itemOffset*counterX), itemYOffset, offset+(itemOffset*counterY), 0.15f, 0.15f, 0.15f,270,1, (stacksInTable.get(i).getItem() instanceof BlockItem)?(true):(false));
                                counterX++;
                                if(counterX>=2)
                                {
                                    counterX=0;
                                    counterY++;
                                }
                            }
                        }
                        else if(stacksInTable.size() > 4)
                        {
                            int counterX = 0;
                            int counterY = 0;
                            for(int i=0;i<stacksInTable.size();i++)
                            {
                                if(!(stacksInTable.get(i).getItem().equals(Items.BARRIER)))renderItem(world,p_112309_,p_112310_,stacksInTable.get(i),p_112311_,p_112312_, offset+(itemOffset*counterX), itemYOffset, offset+(itemOffset*counterY), 0.15f, 0.15f, 0.15f,270,1, (stacksInTable.get(i).getItem() instanceof BlockItem)?(true):(false));
                                counterX++;
                                if(counterX>=3)
                                {
                                    counterX=0;
                                    counterY++;
                                }
                            }
                        }
                    }

                    if(!crafterOutput.isEmpty())renderItemRotating(world,p_112309_,p_112310_, crafterOutput, p_112311_, p_112312_);
                }
                if(facing == Direction.WEST) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(270));
                    p_112309_.translate(0, 0, -1);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        if(floatValues.size()>0)renderItem(world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6),0, p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                    if(stacksInTable.size()>0)
                    {
                        if(stacksInTable.size()<=4)
                        {
                            int counterX = 0;
                            int counterY = 0;
                            for(int i=0;i<stacksInTable.size();i++)
                            {
                                if(!(stacksInTable.get(i).getItem().equals(Items.BARRIER)))renderItem(world,p_112309_,p_112310_,stacksInTable.get(i),p_112311_,p_112312_, offset+(itemOffset*counterX), itemYOffset, offset+(itemOffset*counterY), 0.15f, 0.15f, 0.15f,270,1, (stacksInTable.get(i).getItem() instanceof BlockItem)?(true):(false));
                                counterX++;
                                if(counterX>=2)
                                {
                                    counterX=0;
                                    counterY++;
                                }
                            }
                        }
                        else if(stacksInTable.size() > 4)
                        {
                            int counterX = 0;
                            int counterY = 0;
                            for(int i=0;i<stacksInTable.size();i++)
                            {
                                if(!(stacksInTable.get(i).getItem().equals(Items.BARRIER)))renderItem(world,p_112309_,p_112310_,stacksInTable.get(i),p_112311_,p_112312_, offset+(itemOffset*counterX), itemYOffset, offset+(itemOffset*counterY), 0.15f, 0.15f, 0.15f,270,1, (stacksInTable.get(i).getItem() instanceof BlockItem)?(true):(false));
                                counterX++;
                                if(counterX>=3)
                                {
                                    counterX=0;
                                    counterY++;
                                }
                            }
                        }
                    }

                    if(!crafterOutput.isEmpty())renderItemRotating(world,p_112309_,p_112310_, crafterOutput, p_112311_, p_112312_);
                }
            }
        }
    }

    public static void renderItemRotating(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack itemStack, int p_112311_, int p_112312_)
    {
        if (!itemStack.isEmpty()) {
            p_112309_.pushPose();
            p_112309_.translate(0.5, 1.25, 0.5);
            //p_112309_.translate(0, MathHelper.sin((worldIn.getGameTime()) / 10.0F) * 0.1 + 0.1, 0); BOBBING ITEM
            p_112309_.scale(0.5F, 0.5F, 0.5F);
            long time = System.currentTimeMillis();
            float angle = (time/50) % 360;
            //float angle = (worldIn.getGameTime()) / 20.0F * (180F / (float) Math.PI);
            p_112309_.mulPose(Vector3f.YP.rotationDegrees(angle));
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel baked = renderer.getModel(itemStack,worldIn,null,0);
            renderer.render(itemStack, ItemTransforms.TransformType.GROUND,true,p_112309_,p_112310_,p_112311_,p_112312_,baked);

            //Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.GROUND, p_112311_, p_112312_, p_112309_, p_112310_);
            p_112309_.popPose();
        }
    }

    public static void renderItem(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack itemStack, int p_112311_, int p_112312_, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float angle,int axis, boolean renderAsBlock)
    {
        if (!itemStack.isEmpty()) {
            p_112309_.pushPose();
            p_112309_.translate(x, y, z);
            p_112309_.scale(scaleX, scaleY, scaleZ);
            switch(axis)
            {
                case 1: p_112309_.mulPose(Vector3f.XP.rotationDegrees(angle));
                    break;
                case 2: p_112309_.mulPose(Vector3f.ZP.rotationDegrees(angle));
                    break;
                default: p_112309_.mulPose(Vector3f.YP.rotationDegrees(angle));
                    break;
            }
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

    public static void renderItemStacked(Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack itemStack, int p_112311_, int p_112312_, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float angle)
    {
        if (!itemStack.isEmpty()) {
            int displayLayersByStackSize = itemStack.getCount() / 8;
            if(!itemStack.isEmpty() && displayLayersByStackSize==0) displayLayersByStackSize=1;
            p_112309_.pushPose();
            for(int i=0;i<displayLayersByStackSize;i++)
            {
                p_112309_.translate(0, 0.015625, 0);
                renderItem(worldIn,p_112309_,p_112310_,itemStack,p_112311_,p_112312_, x, y, z, scaleX, scaleY, scaleZ,angle,1, false);
            }
            p_112309_.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(CrafterBlockEntity_T15 p_112306_) {
        return true;
    }*/
}
