package com.mowmaster.dust.Features.Apparatus.still.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.Features.Apparatus.still.StillPotBlockEntity;
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

import java.util.ArrayList;
import java.util.List;

// A Still Pot's Jar Labels: a slip of paper on each labelled side, with the icon of the element it's kept to
public class StillPotRenderer implements BlockEntityRenderer<StillPotBlockEntity, StillPotRenderer.State>
{
    public StillPotRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        final List<Direction> labels = new ArrayList<>();
        @Nullable Identifier icon;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(StillPotBlockEntity pot, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(pot, state, partialTick, cameraPos, crumblingOverlay);
        state.labels.clear();
        state.labels.addAll(pot.labels());
        state.icon = LabelQuads.iconFor(pot.stored());
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        for (Direction side : state.labels) LabelQuads.label(side, 5, 4, 11, 12, -0.06F, state.icon, state.lightCoords, poseStack, collector);
    }
}
