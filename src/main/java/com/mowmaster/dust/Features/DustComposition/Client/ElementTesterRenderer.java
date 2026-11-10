package com.mowmaster.dust.Features.DustComposition.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlock;
import com.mowmaster.dust.Features.DustComposition.ElementTesterBlockEntity;
import com.mowmaster.dust.Features.DustComposition.ExaminerLayout;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The item on the Element Tester's plate: small, turning slowly just above it. (The table, crystals and gauges are
// the block model; their colours come from ElementTesterTint.)
// 26.x renderers work in two steps: extractRenderState copies what's needed into a state object, then submit draws
// from that state only.
public class ElementTesterRenderer implements BlockEntityRenderer<ElementTesterBlockEntity, ElementTesterRenderer.State>
{
    private static final float SCALE = 0.4F;

    private final ItemModelResolver itemModelResolver;

    public ElementTesterRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState item = new ItemStackRenderState();
        Vec3 plate = Vec3.ZERO;
        float angle;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ElementTesterBlockEntity tester, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(tester, state, partialTick, cameraPos, crumblingOverlay);
        // GROUND: the model a dropped item uses. An empty stack gives an empty render state.
        itemModelResolver.updateForTopItem(state.item, tester.getItem(), ItemDisplayContext.GROUND, tester.getLevel(), null, 0);
        ExaminerLayout layout = ExaminerLayout.of(tester.getBlockState().getValue(ElementTesterBlock.CRYSTALS));
        state.plate = layout == null ? new Vec3(0.5, 0.75, 0.5) : ExaminerLayout.turned(layout.plate(), tester.getBlockState().getValue(ElementTesterBlock.FACING));
        long gameTime = tester.getLevel() != null ? tester.getLevel().getGameTime() : 0;
        state.angle = ((gameTime % 360) + partialTick) % 360F;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.item.isEmpty()) return;
        poseStack.pushPose();
        poseStack.translate(state.plate.x, state.plate.y + 0.02, state.plate.z);
        poseStack.scale(SCALE, SCALE, SCALE);
        poseStack.rotate(Axis.YP.rotationDegrees(state.angle));
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
