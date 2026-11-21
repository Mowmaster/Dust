package com.mowmaster.dust.Features.Apparatus.mixer.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlock;
import com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlockEntity;
import com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlockEntity.Phase;
import com.mowmaster.dust.Features.Machines.Client.StationRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Dust Mixer's moving parts, standalone models cut from dust_mixer.json (made with its front to the south):
//   the bowl (dust_mixer_body.json)     tips TIP_DEGREES toward the front about its middle (y 8.85, z 8 pixels) to
//                                        tip out what it made, and back
//   the grate (dust_mixer_grate.json)   rides on the bowl, swinging open about its back edge (y 14, z 2.9) to let dust
//                                        in and out, shut while it mixes
//   the paddles (dust_mixer_paddles.json) ride in the bowl, turning about its middle while it mixes
//   the dust in the bowl (the white dust block's texture tinted): the two elements side by side as they pour in, each
//     half full at SLOT_CAPACITY; while it mixes both turn to the colour of what they're making; then it goes down as
//     it's tipped out
// Played out from the block entity's phase and how far into it it is (DustMixerBlockEntity).
public class DustMixerRenderer implements BlockEntityRenderer<DustMixerBlockEntity, DustMixerRenderer.State>
{
    public static final StandaloneModelKey<BlockStateModelPart> BODY = new StandaloneModelKey<>(() -> "dust:dust_mixer_body");
    public static final StandaloneModelKey<BlockStateModelPart> GRATE = new StandaloneModelKey<>(() -> "dust:dust_mixer_grate");
    public static final StandaloneModelKey<BlockStateModelPart> PADDLES = new StandaloneModelKey<>(() -> "dust:dust_mixer_paddles");
    private static final float GRATE_OPEN_DEGREES = 100F;
    private static final float PADDLE_SPEED = 18F;
    private static final net.minecraft.client.renderer.rendertype.RenderType DUST = net.minecraft.client.renderer.rendertype.RenderTypes.entityCutout(
            Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/blockofdust_white.png"));
    // The bowl's inside (pixels, front to the south): across, front to back, and from its floor up to half full
    private static final double IN_X0 = 3.1, IN_X1 = 12.9, IN_Z0 = 4.2, IN_Z1 = 11.8, FLOOR = 2.2, HALF = 10.9;

    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(BODY, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_mixer_body")));
        event.register(GRATE, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_mixer_grate")));
        event.register(PADDLES, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_mixer_paddles")));
    }

    public DustMixerRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        Direction facing = Direction.NORTH;
        float tip;
        float grate;
        float paddles;
        // The dust in the bowl: left half and right half (heights in pixels, FLOOR: none) and their colours (0xAARRGGBB)
        double leftHeight = FLOOR, rightHeight = FLOOR;
        int leftColor = 0xFFFFFFFF, rightColor = 0xFFFFFFFF;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(DustMixerBlockEntity mixer, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(mixer, state, partialTick, cameraPos, crumblingOverlay);
        state.facing = mixer.getBlockState().getValue(DustMixerBlock.FACING);
        Phase phase = mixer.phase();
        float t = mixer.phaseTicks() + partialTick;
        float grateShare = switch (phase) {
            case OPEN, TIPPING, DUMPING, HELD, RETURNING -> 1;
            case CLOSING -> 1 - Math.min(1, t / DustMixerBlockEntity.GRATE_TICKS);
            case OPENING -> Math.min(1, t / DustMixerBlockEntity.GRATE_TICKS);
            case MIXING -> 0;
        };
        float tipShare = switch (phase) {
            case TIPPING -> Math.min(1, t / DustMixerBlockEntity.TIP_TICKS);
            case DUMPING, HELD -> 1;
            case RETURNING -> 1 - Math.min(1, t / DustMixerBlockEntity.TIP_TICKS);
            default -> 0;
        };
        state.grate = ease(grateShare) * GRATE_OPEN_DEGREES;
        state.tip = ease(tipShare) * DustMixerBlockEntity.TIP_DEGREES;
        dust(mixer, phase, t, state);
        var level = mixer.getLevel();
        state.paddles = phase == Phase.MIXING && level != null ? ((level.getGameTime() % 360) + partialTick) * PADDLE_SPEED % 360 : 0;
    }

    // What's in the bowl to draw, for this phase
    private static void dust(DustMixerBlockEntity mixer, Phase phase, float t, State state)
    {
        state.leftHeight = state.rightHeight = FLOOR;
        var first = mixer.first();
        var second = mixer.second();
        switch (phase) {
            case OPEN, CLOSING, RETURNING -> {
                if (first != null) { state.leftHeight = height(mixer.firstAmount()); state.leftColor = 0xFF000000 | first.color(); }
                if (second != null) { state.rightHeight = height(mixer.secondAmount()); state.rightColor = 0xFF000000 | second.color(); }
            }
            case MIXING -> {
                // Both halves the batch, turning to the colour of what they make
                int total = Math.max(1, mixer.batch() * DustMixerBlockEntity.MIX_TICKS_PER_DUST);
                float share = Math.min(1, t / total);
                var child = first == null || second == null ? null : com.mowmaster.dust.Features.Machines.ElementCombining.combine(first, second);
                int target = child == null ? 0xFFFFFF : child.color();
                state.leftHeight = state.rightHeight = height(mixer.batch());
                state.leftColor = 0xFF000000 | StationRendering.lerpColor(first == null ? target : first.color(), target, share);
                state.rightColor = 0xFF000000 | StationRendering.lerpColor(second == null ? target : second.color(), target, share);
            }
            case OPENING, TIPPING, DUMPING, HELD -> {
                // What's tipped out, going down as it pours
                int amount = 0;
                long red = 0, green = 0, blue = 0;
                for (var entry : mixer.dumping().entrySet()) {
                    int c = entry.getKey().color(), n = entry.getValue();
                    amount += n;
                    red += (long) ((c >> 16) & 0xFF) * n;
                    green += (long) ((c >> 8) & 0xFF) * n;
                    blue += (long) (c & 0xFF) * n;
                }
                if (amount > 0) {
                    state.leftHeight = state.rightHeight = height(amount);
                    state.leftColor = state.rightColor = 0xFF000000 | (int) ((red / amount) << 16 | (green / amount) << 8 | (blue / amount));
                }
            }
        }
    }

    // How high this much dust stands in the bowl: half full at SLOT_CAPACITY (a thin layer at least)
    private static double height(int amount)
    {
        if (amount <= 0) return FLOOR;
        return FLOOR + Math.max(0.3, (HALF - FLOOR) * Math.min(1.0, amount / (double) DustMixerBlockEntity.SLOT_CAPACITY));
    }

    // Smooth in and out
    private static float ease(float share)
    {
        return share * share * (3 - 2 * share);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        var models = Minecraft.getInstance().getModelManager();
        poseStack.pushPose();
        // Turned with the block: the model's front is south, so it turns as a north-facing model turns for the opposite way
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(StationRendering.yaw(state.facing.getOpposite())));
        poseStack.translate(-0.5, 0, -0.5);
        // The bowl tips toward the front (+z in the model): the top swings forward and down
        pivotX(poseStack, 8.85, 8, state.tip);
        part(models.getStandaloneModel(BODY), state, poseStack, collector);
        // The dust in it, tipping with it
        double middle = (IN_X0 + IN_X1) / 2;
        if (state.leftHeight > FLOOR) StationRendering.box(DUST, IN_X0, FLOOR, IN_Z0, middle, state.leftHeight, IN_Z1,
                state.leftColor, state.lightCoords, Direction.NORTH, poseStack, collector);
        if (state.rightHeight > FLOOR) StationRendering.box(DUST, middle, FLOOR, IN_Z0, IN_X1, state.rightHeight, IN_Z1,
                state.rightColor, state.lightCoords, Direction.NORTH, poseStack, collector);
        // The paddles turn in the bowl
        poseStack.pushPose();
        pivotX(poseStack, 8.85, 8, state.paddles);
        part(models.getStandaloneModel(PADDLES), state, poseStack, collector);
        poseStack.popPose();
        // The grate swings open about its back edge, its front lifting
        pivotX(poseStack, 14, 2.9, -state.grate);
        part(models.getStandaloneModel(GRATE), state, poseStack, collector);
        poseStack.popPose();
    }

    // A turn of degrees about the model's x axis through (y, z) in pixels
    private static void pivotX(PoseStack poseStack, double y, double z, float degrees)
    {
        poseStack.translate(0, y / 16, z / 16);
        poseStack.rotate(Axis.XP.rotationDegrees(degrees));
        poseStack.translate(0, -y / 16, -z / 16);
    }

    private static void part(@Nullable BlockStateModelPart model, State state, PoseStack poseStack, SubmitNodeCollector collector)
    {
        if (model == null) return;
        collector.submitBlockModel(poseStack, Sheets.cutoutBlockItemSheet(), List.of(model), BlockModelRenderState.EMPTY_TINTS,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }
}
