package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.CrusherLayout;
import com.mowmaster.dust.Features.Machines.ResearchCrusherBlock;
import com.mowmaster.dust.Features.Machines.ResearchCrusherBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Research Crusher's moving parts (the frame and funnel are the block model):
//   the two grinding wheels, turning while a block is in. They're models of their own
//     (models/block/station/research_crusher_wheel_a / _b.json) loaded as "standalone" models (registerModels below)
//     and drawn here, turned about their axles.
//   the block being crushed, sinking from the funnel into the wheels
//   the pebbles in the tray
public class ResearchCrusherRenderer implements BlockEntityRenderer<ResearchCrusherBlockEntity, ResearchCrusherRenderer.State>
{
    // Degrees a wheel turns per tick while running; the two turn opposite ways, like a mill
    private static final float WHEEL_SPEED = 12F;
    private static final float BLOCK_SCALE = 0.35F;
    private static final float TRAY_SCALE = 0.4F;

    // A standalone model: one NeoForge loads and bakes alongside the block models, for code to draw. The key is how
    // it's asked for afterwards; ModelDebugName (the lambda) is only its name in error messages.
    public static final StandaloneModelKey<BlockStateModelPart> WHEEL_A = new StandaloneModelKey<>(() -> "dust:research_crusher_wheel_a");
    public static final StandaloneModelKey<BlockStateModelPart> WHEEL_B = new StandaloneModelKey<>(() -> "dust:research_crusher_wheel_b");

    // Called from DustClient's ModelEvent.RegisterStandalone handler
    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(WHEEL_A, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_crusher_wheel_a")));
        event.register(WHEEL_B, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_crusher_wheel_b")));
    }

    private final ItemModelResolver itemModelResolver;

    public ResearchCrusherRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState block = new ItemStackRenderState();
        final ItemStackRenderState tray = new ItemStackRenderState();
        Direction facing = Direction.NORTH;
        float descent;
        float wheelAngle;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ResearchCrusherBlockEntity crusher, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(crusher, state, partialTick, cameraPos, crumblingOverlay);
        itemModelResolver.updateForTopItem(state.block, crusher.getInput(), ItemDisplayContext.FIXED, crusher.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.tray, crusher.getOutput(), ItemDisplayContext.GROUND, crusher.getLevel(), null, 1);
        state.facing = crusher.getBlockState().getValue(ResearchCrusherBlock.FACING);
        state.descent = crusher.progress(partialTick);
        long gameTime = crusher.getLevel() != null ? crusher.getLevel().getGameTime() : 0;
        // Only turning while there's a block in; % 360 keeps the number small so the float stays precise
        state.wheelAngle = crusher.isRunning() ? ((gameTime % 360) + partialTick) * WHEEL_SPEED % 360F : 0F;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        // The wheels: drawn in the model's own (north-facing) space, turned to the block's facing about its centre
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(-quarterTurns(state.facing) * 90F));
        poseStack.translate(-0.5, 0, -0.5);
        submitWheel(WHEEL_A, CrusherLayout.WHEEL_A_AXLE, state.wheelAngle, state, poseStack, submitNodeCollector);
        submitWheel(WHEEL_B, CrusherLayout.WHEEL_B_AXLE, -state.wheelAngle, state, poseStack, submitNodeCollector);
        poseStack.popPose();

        if (!state.block.isEmpty()) {
            Vec3 at = CrusherLayout.turned(CrusherLayout.descent(state.descent), state.facing);
            poseStack.pushPose();
            poseStack.translate(at.x, at.y, at.z);
            // Shrinks a little as it's ground down
            float scale = BLOCK_SCALE * (1F - 0.4F * state.descent);
            poseStack.scale(scale, scale, scale);
            state.block.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        if (!state.tray.isEmpty()) {
            Vec3 at = CrusherLayout.turned(CrusherLayout.TRAY, state.facing);
            poseStack.pushPose();
            poseStack.translate(at.x, at.y, at.z);
            // Lying in the tray face up, its top away from the player at the tray side: turned with the block (and
            // half a turn, since the tray faces the model's north), then laid down (the GROUND model stands upright)
            poseStack.rotate(Axis.YP.rotationDegrees(-quarterTurns(state.facing) * 90F + 180F));
            poseStack.rotate(Axis.XP.rotationDegrees(-90));
            poseStack.scale(TRAY_SCALE, TRAY_SCALE, TRAY_SCALE);
            state.tray.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    // A wheel model turned by angle about its axle (which runs along x). submitBlockModel draws baked block model
    // parts with a block render type (cutout: see-through pixels are cut, like most blocks).
    private static void submitWheel(StandaloneModelKey<BlockStateModelPart> key, Vec3 axlePixels, float angle, State state,
                                    PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        BlockStateModelPart wheel = Minecraft.getInstance().getModelManager().getStandaloneModel(key);
        if (wheel == null) return;
        poseStack.pushPose();
        Vec3 axle = axlePixels.scale(1 / 16.0);
        poseStack.translate(axle.x, axle.y, axle.z);
        poseStack.rotate(Axis.XP.rotationDegrees(angle));
        poseStack.translate(-axle.x, -axle.y, -axle.z);
        submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(wheel), BlockModelRenderState.EMPTY_TINTS,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    // How far the blockstate turns the model for each facing (the models face north): clockwise quarter turns
    private static int quarterTurns(Direction facing)
    {
        return switch (facing) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
    }
}
