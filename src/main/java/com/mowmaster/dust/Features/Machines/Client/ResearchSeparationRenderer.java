package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.CrusherLayout;
import com.mowmaster.dust.Features.Machines.ResearchSeparationBlock;
import com.mowmaster.dust.Features.Machines.ResearchSeparationBlockEntity;
import com.mowmaster.dust.Features.Machines.SeparationLayout;
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
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Alchemical Separation Table's moving parts (the table and scroll are the block model):
//   the magical array (models/block/station/research_separation_array.json, a standalone model): flat on the scroll
//     when idle; while separating it lifts, spins, and glows (drawn at full brightness, pulsing)
//   the item: on its pad, or riding above the spinning array while it's separated
//   the two outputs on their pads, and the paper the table holds
public class ResearchSeparationRenderer implements BlockEntityRenderer<ResearchSeparationBlockEntity, ResearchSeparationRenderer.State>
{
    private static final int FULL_BRIGHT = 0xF000F0;
    private static final float ITEM_SCALE = 0.35F;
    // Ticks to lift the array at the start, and to set it down at the end
    private static final float LIFT_TICKS = 20F;
    private static final float SETTLE_TICKS = 10F;

    public static final StandaloneModelKey<BlockStateModelPart> ARRAY = new StandaloneModelKey<>(() -> "dust:research_separation_array");

    // Called from DustClient's ModelEvent.RegisterStandalone handler
    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(ARRAY, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/station/research_separation_array")));
    }

    private final ItemModelResolver itemModelResolver;

    public ResearchSeparationRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState input = new ItemStackRenderState();
        final ItemStackRenderState left = new ItemStackRenderState();
        final ItemStackRenderState right = new ItemStackRenderState();
        final ItemStackRenderState paper = new ItemStackRenderState();
        Direction facing = Direction.NORTH;
        float progress = -1;
        // 0-1: how far the array is lifted
        float lift;
        float spin;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ResearchSeparationBlockEntity table, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(table, state, partialTick, cameraPos, crumblingOverlay);
        itemModelResolver.updateForTopItem(state.input, table.getInput(), ItemDisplayContext.GROUND, table.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.left, table.getLeft(), ItemDisplayContext.GROUND, table.getLevel(), null, 1);
        itemModelResolver.updateForTopItem(state.right, table.getRight(), ItemDisplayContext.GROUND, table.getLevel(), null, 2);
        itemModelResolver.updateForTopItem(state.paper, table.getPaper(), ItemDisplayContext.GROUND, table.getLevel(), null, 3);
        state.facing = table.getBlockState().getValue(ResearchSeparationBlock.FACING);
        state.progress = table.progress(partialTick);
        float p = state.progress;
        if (p < 0) {
            state.lift = 0;
        } else {
            // Up over LIFT_TICKS, down over the last SETTLE_TICKS; Mth.clamp keeps it between 0 and 1
            float up = Mth.clamp(p / LIFT_TICKS, 0, 1);
            float down = Mth.clamp((ResearchSeparationBlockEntity.SEPARATE_TICKS - p) / SETTLE_TICKS, 0, 1);
            state.lift = Math.min(up, down);
        }
        // Spins faster as it lifts
        state.spin = p < 0 ? 0 : (p * 9F * state.lift) % 360F;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        boolean running = state.progress >= 0;
        Vec3 arrayPx = SeparationLayout.ARRAY;
        double liftPx = SeparationLayout.ARRAY_LIFT * state.lift;

        // The array: in the model's own (north-facing) space, turned to the facing about the block's centre
        BlockStateModelPart array = Minecraft.getInstance().getModelManager().getStandaloneModel(ARRAY);
        if (array != null) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0, 0.5);
            poseStack.rotate(Axis.YP.rotationDegrees(-quarterTurns(state.facing) * 90F));
            poseStack.translate(-0.5, 0, -0.5);
            poseStack.translate(arrayPx.x / 16, (arrayPx.y + liftPx) / 16, arrayPx.z / 16);
            poseStack.rotate(Axis.YP.rotationDegrees(state.spin));
            poseStack.translate(-arrayPx.x / 16, -arrayPx.y / 16, -arrayPx.z / 16);
            // Glowing while it works: full brightness, flickering off for a moment every few ticks
            boolean glow = running && ((int) state.progress / 3) % 5 != 0;
            submitNodeCollector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(array), BlockModelRenderState.EMPTY_TINTS,
                    glow ? FULL_BRIGHT : state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        // The item: on its pad, or riding above the array while separating
        if (!state.input.isEmpty()) {
            Vec3 at = running
                    ? CrusherLayout.turned(new Vec3(arrayPx.x, arrayPx.y + liftPx + 3, arrayPx.z), state.facing)
                    : CrusherLayout.turned(SeparationLayout.INPUT, state.facing);
            poseStack.pushPose();
            poseStack.translate(at.x, at.y, at.z);
            if (running) poseStack.rotate(Axis.YP.rotationDegrees(state.spin));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            state.input.submit(poseStack, submitNodeCollector, running ? FULL_BRIGHT : state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        submitItem(state.left, SeparationLayout.LEFT_OUTPUT, state, poseStack, submitNodeCollector);
        submitItem(state.right, SeparationLayout.RIGHT_OUTPUT, state, poseStack, submitNodeCollector);
        submitItem(state.paper, SeparationLayout.PAPER, state, poseStack, submitNodeCollector);
    }

    private static void submitItem(ItemStackRenderState item, Vec3 pixels, State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        if (item.isEmpty()) return;
        Vec3 at = CrusherLayout.turned(pixels, state.facing);
        poseStack.pushPose();
        poseStack.translate(at.x, at.y, at.z);
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

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
