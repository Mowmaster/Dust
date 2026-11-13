package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.Features.Spells.entities.CarriesBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.RenderShape;

// Draws an entity as the block it carries (the floating and animated blocks), the way vanilla draws a falling block:
// a MovingBlockRenderState (the block, and the light and biome tint where it is) handed to submitMovingBlock.
// bob: how much it hops as it goes (the animated block), in blocks.
// <T extends Entity & CarriesBlock>: T must be an Entity that also implements CarriesBlock.
public class CarriedBlockRenderer<T extends Entity & CarriesBlock> extends EntityRenderer<T, CarriedBlockRenderer.State>
{
    private final float bob;

    public CarriedBlockRenderer(EntityRendererProvider.Context context, float bob)
    {
        super(context);
        this.bob = bob;
        this.shadowRadius = 0.5F;
    }

    public static class State extends EntityRenderState
    {
        final MovingBlockRenderState block = new MovingBlockRenderState();
        float hop;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(T entity, State state, float partialTick)
    {
        super.extractRenderState(entity, state, partialTick);
        BlockPos at = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
        state.block.randomSeedPos = entity.blockPosition();
        state.block.blockPos = at;
        state.block.blockState = entity.carriedBlock();
        if (entity.level() instanceof ClientLevel level) {
            state.block.biome = level.getBiome(at);
            state.block.cardinalLighting = level.cardinalLighting();
            state.block.lightEngine = level.getLightEngine();
        }
        // A little hop while it's moving
        double speed = entity.getDeltaMovement().horizontalDistance();
        state.hop = speed > 0.01 ? Math.abs(Mth.sin(state.ageInTicks * 0.8F)) * bob : 0;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        if (state.block.blockState != null && state.block.blockState.getRenderShape() == RenderShape.MODEL) {
            poseStack.pushPose();
            poseStack.translate(-0.5, state.hop, -0.5);
            collector.submitMovingBlock(poseStack, state.block, state.outlineColor);
            poseStack.popPose();
        }
        super.submit(state, poseStack, collector, camera);
    }
}
