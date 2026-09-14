package com.mowmaster.dust.Features.Pedestals;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RenderBEPedestal implements BlockEntityRenderer<EntityBlockPedestal, RenderStateBEPedestal> {

    private final ItemModelResolver modelResolver;

    public RenderBEPedestal(BlockEntityRendererProvider.Context context)
    {
        modelResolver = context.itemModelResolver();
    }

    @Override
    public RenderStateBEPedestal createRenderState() {
        return new RenderStateBEPedestal();
    }

    @Override
    public void extractRenderState(EntityBlockPedestal blockEntity, RenderStateBEPedestal state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.level = blockEntity.getLevel();
        state.rotation = (blockEntity.getLevel().getGameTime() + partialTicks * 0.5f) % 360f;

        modelResolver.updateForTopItem(state.renderState,
                blockEntity.inventory.getResource(0).toStack(),
                ItemDisplayContext.FIXED,
                blockEntity.getLevel(),
                null,
                0);
    }

    @Override
    public void submit(RenderStateBEPedestal state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        // render item on pedestal
        poseStack.pushPose();

        poseStack.translate(0.5f, 1.0f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));

        state.renderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }
}
