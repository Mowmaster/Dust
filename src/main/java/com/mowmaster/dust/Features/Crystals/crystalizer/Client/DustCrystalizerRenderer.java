package com.mowmaster.dust.Features.Crystals.crystalizer.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.Features.Crystals.crystalizer.DustCrystalizerBlockEntity;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The Dust Crystalizer's first-stage crystal: the element's crystal item, turning slowly and bobbing just above the top.
// The larger stages are real crystal blocks on top of it, drawn as blocks.
public class DustCrystalizerRenderer implements BlockEntityRenderer<DustCrystalizerBlockEntity, DustCrystalizerRenderer.State>
{
    private static final float ITEM_SCALE = 0.5F;

    private final ItemModelResolver itemModelResolver;

    public DustCrystalizerRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState crystal = new ItemStackRenderState();
        float time;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(DustCrystalizerBlockEntity crystalizer, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(crystalizer, state, partialTick, cameraPos, crumblingOverlay);
        ItemStack shown = crystalizer.stage() == 1 ? crystalizer.crystal() : ItemStack.EMPTY;
        itemModelResolver.updateForTopItem(state.crystal, shown, ItemDisplayContext.GROUND, crystalizer.getLevel(), null, 0);
        state.time = crystalizer.getLevel() == null ? 0 : (crystalizer.getLevel().getGameTime() % 3600) + partialTick;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.crystal.isEmpty()) return;
        poseStack.pushPose();
        poseStack.translate(0.5, 1.15 + Math.sin(state.time / 12F) * 0.03, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(state.time * 2F));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        state.crystal.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
