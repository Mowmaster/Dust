package com.mowmaster.dust.Features.Apparatus.still.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.Features.Apparatus.still.DustChuteBlock;
import com.mowmaster.dust.Features.Apparatus.still.DustChuteBlockEntity;
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

// A filtered Dust Chute's element symbol, on the brass plaque on its back plate (dust_chute_filtered.json: pixels
// 6.25-9.75 across, 8.25-11 up, its front 1.25 out from the back). Nothing for an element the player hasn't discovered.
public class DustChuteRenderer implements BlockEntityRenderer<DustChuteBlockEntity, DustChuteRenderer.State>
{
    public DustChuteRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        @Nullable Identifier icon;
        Direction facing = Direction.SOUTH;
        int inset;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(DustChuteBlockEntity chute, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(chute, state, partialTick, cameraPos, crumblingOverlay);
        state.icon = LabelQuads.iconFor(chute.filter());
        state.facing = chute.getBlockState().getValue(DustChuteBlock.FACING);
        state.inset = chute.getBlockState().getValue(DustChuteBlock.INSET);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        if (state.icon == null) return;
        // Drawn on the face looking out the way the chute faces: as on a north face 1.3 pixels in from... the facing side
        // is the far side of the plaque, so it's drawn turned to the facing at depth 16 - 1.3
        LabelQuads.icon(state.facing, state.icon, 6.5F, 8.5F, 9.5F, 10.75F, 16 - 1.3F + state.inset, state.lightCoords, poseStack, collector);
    }
}
