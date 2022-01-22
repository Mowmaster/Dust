package com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.Furnaces.BlastFurnace;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.DustFueledMachineBaseBlockEntity;
import com.mowmaster.dust.Block.BlockEntities.Tier1.FueledMachines.DustFueledMachineBlockEntityRenderer;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.mowmaster.dust.Block.BlockEntities.Tier1.ScrollCrafter.T15.ScrollCrafterBlock_T15.SIDED_ROTATION_4;

public class BlastFurnaceBlockEntityRenderer_T15 extends DustFueledMachineBlockEntityRenderer implements BlockEntityRenderer<BlastFurnaceBlockEntity_T15>
{
    public BlastFurnaceBlockEntityRenderer_T15(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(BlastFurnaceBlockEntity_T15 p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        if (!p_112307_.isRemoved()) {
            Level world = p_112307_.getLevel();
            Direction facing = p_112307_.getBlockState().getValue(SIDED_ROTATION_4);
            List<ItemStack> stacksList = p_112307_.getListOfInsertedItemsToDisplay();
            ItemStack inputStack = p_112307_.getInputItemInSlot(1);

            if(!(stacksList.size() == 0))
            {
                if(facing == Direction.SOUTH) {
                    //p_112309_.mulPose(Vector3f.YP.rotationDegrees(180));
                    //p_112309_.translate(0, 0, 0);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        if(!inputStack.isEmpty())renderItemStacked(p_112307_,world,p_112309_,p_112310_,inputStack,p_112311_,p_112312_, 0.5f, (inputStack.getItem() instanceof BlockItem)?(0.55f):(0.4f), 0.5f, 0.225f, 0.25f, 0.225f,(inputStack.getItem() instanceof BlockItem)?(0.0f):(270.0f), (inputStack.getItem() instanceof BlockItem)?(true):(false));
                        if(floatValues.size()>0)renderItem(p_112307_,world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6),0, p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                }
                if(facing == Direction.NORTH) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(180));
                    p_112309_.translate(-1, 0, -1);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        if(!inputStack.isEmpty())renderItemStacked(p_112307_,world,p_112309_,p_112310_,inputStack,p_112311_,p_112312_, 0.5f, (inputStack.getItem() instanceof BlockItem)?(0.55f):(0.4f), 0.5f, 0.225f, 0.25f, 0.225f,(inputStack.getItem() instanceof BlockItem)?(0.0f):(270.0f), (inputStack.getItem() instanceof BlockItem)?(true):(false));
                        if(floatValues.size()>0)renderItem(p_112307_,world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6),0, p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                }
                if(facing == Direction.EAST) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(90));
                    p_112309_.translate(-1, 0, 0);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        if(!inputStack.isEmpty())renderItemStacked(p_112307_,world,p_112309_,p_112310_,inputStack,p_112311_,p_112312_, 0.5f, (inputStack.getItem() instanceof BlockItem)?(0.55f):(0.4f), 0.5f, 0.225f, 0.25f, 0.225f,(inputStack.getItem() instanceof BlockItem)?(0.0f):(270.0f), (inputStack.getItem() instanceof BlockItem)?(true):(false));
                        if(floatValues.size()>0)renderItem(p_112307_,world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6),0, p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                }
                if(facing == Direction.WEST) {
                    p_112309_.mulPose(Vector3f.YP.rotationDegrees(270));
                    p_112309_.translate(0, 0, -1);
                    for(int i=0;i<stacksList.size();i++)
                    {
                        List<Float> floatValues = p_112307_.getRenderParams(stacksList.get(i));
                        if(!inputStack.isEmpty())renderItemStacked(p_112307_,world,p_112309_,p_112310_,inputStack,p_112311_,p_112312_, 0.5f, (inputStack.getItem() instanceof BlockItem)?(0.55f):(0.4f), 0.5f, 0.225f, 0.25f, 0.225f,0.0f, (inputStack.getItem() instanceof BlockItem)?(true):(false));
                        if(floatValues.size()>0)renderItem(p_112307_,world,p_112309_,p_112310_,p_112307_.getRenderItem(stacksList.get(i)),p_112311_,p_112312_, floatValues.get(0), floatValues.get(1), floatValues.get(2), floatValues.get(3), floatValues.get(4), floatValues.get(5),floatValues.get(6),0, p_112307_.getRenderAsBlock(stacksList.get(i)));
                    }
                }
            }
        }
    }

    public static Block getCauldronFilled(Block block, boolean filled)
    {
        if(filled)
        {
            return Blocks.LAVA_CAULDRON;
        }
        else return block;
    }

    public static void renderItem(DustFueledMachineBaseBlockEntity tile, Level worldIn, PoseStack p_112309_, MultiBufferSource p_112310_, ItemStack itemStack, int p_112311_, int p_112312_, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float angle, int axis, boolean renderAsBlock)
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
                Block blocky = Block.byItem(itemStack.getItem());
                if(itemStack.getItem().equals(Items.CAULDRON))
                {
                    blocky = getCauldronFilled(blocky, !tile.getInputItemInSlot(1).isEmpty());
                }
                BlockState block = getLitBlockState(blocky, tile.isFurnaceLit());
                baked = renderBlock.getBlockModel(block);
            }
            renderer.render(itemStack, ItemTransforms.TransformType.FIXED,true,p_112309_,p_112310_,p_112311_,p_112312_,baked);
            p_112309_.popPose();
        }
    }
}
