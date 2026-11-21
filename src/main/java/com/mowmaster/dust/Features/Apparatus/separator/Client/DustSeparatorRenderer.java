package com.mowmaster.dust.Features.Apparatus.separator.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.Features.Apparatus.separator.DustSeparatorBlockEntity;
import com.mowmaster.dust.Features.Machines.StationBlock;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The Dust Separator's turning drum (DrumRendering)
public class DustSeparatorRenderer implements BlockEntityRenderer<DustSeparatorBlockEntity, DustSeparatorRenderer.State>
{
    public DustSeparatorRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        Direction facing = Direction.NORTH;
        float angle;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(DustSeparatorBlockEntity separator, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(separator, state, partialTick, cameraPos, crumblingOverlay);
        state.facing = separator.getBlockState().getValue(StationBlock.FACING);
        state.angle = DrumRendering.angle(separator.isSpinning(), partialTick);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        DrumRendering.submit(DrumRendering.DRUM, state.facing, state.angle, state.lightCoords, poseStack, collector);
    }
}
