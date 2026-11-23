package com.mowmaster.dust.Features.Apparatus.mixer.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlock;
import com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlockEntity;
import com.mowmaster.dust.Features.Apparatus.mixer.DustMixerBlockEntity.Phase;
import com.mowmaster.dust.Features.Apparatus.mixer.DustWetMixerBlockEntity;
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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;

// The Wet Dust Mixer's moving parts, standalone models cut from dust_mixer_wet.json (made facing north: the pot's open
// side north, the stand at the back, south):
//   the head (dust_mixer_wet_stand.json)     tilts back about the top of the column (y 9, z 14.5 pixels) while it
//                                             fills, and down over the pot to mix
//   the beater (dust_mixer_wet_beater.json)   rides on the head, turning about its shaft (x 8, z 6) while it mixes
//   the pot (dust_mixer_wet_pot.json)        tips forward about its middle, where the stand's arms hold it (y 7.5,
//                                             z 6), to pour out what it made, and back
//   the dust in the pot (the white dust block's texture tinted: MixerContents) rides in it, up to 75% full
// Played out from the block entity's phase and how far into it it is (DustMixerBlockEntity).
public class DustWetMixerRenderer implements BlockEntityRenderer<DustWetMixerBlockEntity, DustWetMixerRenderer.State>
{
    public static final StandaloneModelKey<BlockStateModelPart> POT = new StandaloneModelKey<>(() -> "dust:dust_mixer_wet_pot");
    public static final StandaloneModelKey<BlockStateModelPart> STAND = new StandaloneModelKey<>(() -> "dust:dust_mixer_wet_stand");
    public static final StandaloneModelKey<BlockStateModelPart> BEATER = new StandaloneModelKey<>(() -> "dust:dust_mixer_wet_beater");
    private static final float HEAD_TILT_DEGREES = 60F;
    private static final float POT_TIP_DEGREES = 105F;
    // Where the pot turns: its middle, between the stand's arms (pixels)
    private static final double POT_PIVOT_Y = 7.5, POT_PIVOT_Z = 6;
    private static final float BEATER_SPEED = 24F;
    private static final RenderType DUST = RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/blockofdust_white.png"));
    // The pot's inside (pixels, facing north): across, front to back, and from its floor up to 75% full
    private static final double IN_X0 = 3.05, IN_X1 = 12.95, IN_Z0 = 1.05, IN_Z1 = 10.95, FLOOR = 3.05, FULL = 9.75;

    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(POT, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_mixer_wet_pot")));
        event.register(STAND, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_mixer_wet_stand")));
        event.register(BEATER, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_mixer_wet_beater")));
    }

    public DustWetMixerRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        Direction facing = Direction.NORTH;
        float head;
        float pot;
        float beater;
        double leftHeight = FLOOR, rightHeight = FLOOR;
        int leftColor = 0xFFFFFFFF, rightColor = 0xFFFFFFFF;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    // It reaches out of its block (the head tilted back, the pot tipped forward)
    @Override
    public AABB getRenderBoundingBox(DustWetMixerBlockEntity mixer)
    {
        return new AABB(mixer.getBlockPos()).inflate(1);
    }

    @Override
    public void extractRenderState(DustWetMixerBlockEntity mixer, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(mixer, state, partialTick, cameraPos, crumblingOverlay);
        state.facing = mixer.getBlockState().getValue(DustMixerBlock.FACING);
        Phase phase = mixer.phase();
        float t = mixer.phaseTicks() + partialTick;
        // The head: tilted back except while mixing
        float headShare = switch (phase) {
            case OPEN, TIPPING, DUMPING, HELD, RETURNING -> 1;
            case CLOSING -> 1 - Math.min(1, t / DustMixerBlockEntity.GRATE_TICKS);
            case OPENING -> Math.min(1, t / DustMixerBlockEntity.GRATE_TICKS);
            case MIXING -> 0;
        };
        float potShare = switch (phase) {
            case TIPPING -> Math.min(1, t / DustMixerBlockEntity.TIP_TICKS);
            case DUMPING, HELD -> 1;
            case RETURNING -> 1 - Math.min(1, t / DustMixerBlockEntity.TIP_TICKS);
            default -> 0;
        };
        state.head = ease(headShare) * HEAD_TILT_DEGREES;
        state.pot = ease(potShare) * POT_TIP_DEGREES;
        var level = mixer.getLevel();
        state.beater = phase == Phase.MIXING && level != null ? ((level.getGameTime() % 360) + partialTick) * BEATER_SPEED % 360 : 0;
        MixerContents contents = MixerContents.of(mixer, t);
        state.leftHeight = MixerContents.height(contents.left(), FLOOR, FULL);
        state.rightHeight = MixerContents.height(contents.right(), FLOOR, FULL);
        state.leftColor = contents.leftColor();
        state.rightColor = contents.rightColor();
    }

    private static float ease(float share)
    {
        return share * share * (3 - 2 * share);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        var models = Minecraft.getInstance().getModelManager();
        poseStack.pushPose();
        // Turned with the block (the model faces north)
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(StationRendering.yaw(state.facing)));
        poseStack.translate(-0.5, 0, -0.5);

        // The pot tips forward (its top toward -z in the model) about its middle, on the stand's arms, with the dust in it
        poseStack.pushPose();
        pivotX(poseStack, POT_PIVOT_Y, POT_PIVOT_Z, -state.pot);
        part(models.getStandaloneModel(POT), state, poseStack, collector);
        double middle = (IN_X0 + IN_X1) / 2;
        if (state.leftHeight > FLOOR) StationRendering.box(DUST, IN_X0, FLOOR, IN_Z0, middle, state.leftHeight, IN_Z1, state.leftColor, state.lightCoords,
                Direction.NORTH, poseStack, collector);
        if (state.rightHeight > FLOOR) StationRendering.box(DUST, middle, FLOOR, IN_Z0, IN_X1, state.rightHeight, IN_Z1, state.rightColor, state.lightCoords,
                Direction.NORTH, poseStack, collector);
        poseStack.popPose();

        // The head tilts back about the top of the column (its front lifting), the beater with it
        pivotX(poseStack, 9, 14.5, state.head);
        part(models.getStandaloneModel(STAND), state, poseStack, collector);
        poseStack.translate(8 / 16.0, 0, 6 / 16.0);
        poseStack.rotate(Axis.YP.rotationDegrees(state.beater));
        poseStack.translate(-8 / 16.0, 0, -6 / 16.0);
        part(models.getStandaloneModel(BEATER), state, poseStack, collector);
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
