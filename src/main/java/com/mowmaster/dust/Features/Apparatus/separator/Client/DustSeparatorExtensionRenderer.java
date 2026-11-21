package com.mowmaster.dust.Features.Apparatus.separator.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.Features.Apparatus.separator.DustSeparatorBlockEntity;
import com.mowmaster.dust.Features.Apparatus.separator.DustSeparatorExtensionBlock;
import com.mowmaster.dust.Features.Apparatus.separator.DustSeparatorExtensionBlockEntity;
import com.mowmaster.dust.Features.Apparatus.still.Client.LabelQuads;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// A Dust Separator Extension's drum, turning with its separator's, and its section's label (DrumRendering)
public class DustSeparatorExtensionRenderer implements BlockEntityRenderer<DustSeparatorExtensionBlockEntity, DustSeparatorExtensionRenderer.State>
{
    public DustSeparatorExtensionRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        Direction facing = Direction.NORTH;
        float angle;
        boolean labelled;
        @Nullable Identifier icon;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(DustSeparatorExtensionBlockEntity extension, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(extension, state, partialTick, cameraPos, crumblingOverlay);
        state.facing = extension.getBlockState().getValue(DustSeparatorExtensionBlock.FACING);
        DustSeparatorBlockEntity separator = extension.getLevel() == null ? null : extension.separator(extension.getLevel());
        state.angle = DrumRendering.angle(separator != null && separator.isSpinning(), partialTick);
        state.labelled = extension.section().labelled();
        state.icon = LabelQuads.iconFor(extension.section().element());
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        DrumRendering.submit(DrumRendering.EXTENSION_DRUM, state.facing, state.angle, state.icon, state.labelled, state.lightCoords, poseStack, collector);
    }
}
