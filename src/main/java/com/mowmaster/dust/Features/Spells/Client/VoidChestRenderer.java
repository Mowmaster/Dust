package com.mowmaster.dust.Features.Spells.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.Features.Spells.voidrift.VoidChestEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

// Draws the void chest as an ender chest item with two feathers for wings, flapping (slowly while it's open).
// Placeholder art made of vanilla items; swap for a proper model later.
// 26.3 entity rendering is split in two: extractRenderState copies what's needed off the entity into a State object
// (on the main thread), then submit draws from the State alone. Items are drawn through an ItemStackRenderState that
// the ItemModelResolver fills in.
public class VoidChestRenderer extends EntityRenderer<VoidChestEntity, VoidChestRenderer.State>
{
    // Made the first time it's drawn, not when the class loads: renderers are built before items' data components are
    // bound, and making an ItemStack that early throws (which shows up as "Could not initialize class")
    private @Nullable ItemStack chest;
    private @Nullable ItemStack feather;
    private final ItemModelResolver itemModelResolver;

    public VoidChestRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.shadowRadius = 0.2F;
    }

    public static class State extends EntityRenderState
    {
        final ItemStackRenderState chest = new ItemStackRenderState();
        final ItemStackRenderState wing = new ItemStackRenderState();
        float yaw;
        boolean open;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(VoidChestEntity entity, State state, float partialTick)
    {
        super.extractRenderState(entity, state, partialTick);
        if (chest == null) {
            chest = new ItemStack(Items.ENDER_CHEST);
            feather = new ItemStack(Items.FEATHER);
        }
        itemModelResolver.updateForNonLiving(state.chest, chest, ItemDisplayContext.FIXED, entity);
        itemModelResolver.updateForNonLiving(state.wing, feather, ItemDisplayContext.FIXED, entity);
        state.yaw = entity.getYRot(partialTick);
        state.open = entity.isOpen();
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        poseStack.pushPose();
        // Face the way its owner faces. 26.3: PoseStack.rotate (was mulPose); Axis makes the rotation quaternions.
        poseStack.translate(0, 0.25, 0);
        poseStack.rotate(Axis.YP.rotationDegrees(180 - state.yaw));

        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        state.chest.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();

        // Wings: up to 35 degrees each way, quick flaps while flying, lazy ones while it's open
        float flap = Mth.sin(state.ageInTicks * (state.open ? 0.25F : 0.9F)) * 35;
        wing(state, poseStack, collector, 1, flap);
        wing(state, poseStack, collector, -1, flap);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    // side: 1 for the left wing, -1 for the right. Pose stack steps apply to the model in reverse order: the feather is
    // laid flat, pushed out from the hinge, then swung about the hinge (the chest's top edge).
    private void wing(State state, PoseStack poseStack, SubmitNodeCollector collector, int side, float flap)
    {
        poseStack.pushPose();
        poseStack.translate(0.2 * side, 0.15, 0);                       // the hinge, at the chest's top edge
        poseStack.rotate(Axis.ZP.rotationDegrees(flap * side));         // flap up and down
        poseStack.translate(0.25 * side, 0, 0);                         // out from the hinge
        poseStack.rotate(Axis.XP.rotationDegrees(90));                  // lie flat
        poseStack.rotate(Axis.ZP.rotationDegrees(side > 0 ? -45 : 135)); // point the feather outward
        poseStack.scale(0.5F, 0.5F, 0.5F);
        state.wing.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
