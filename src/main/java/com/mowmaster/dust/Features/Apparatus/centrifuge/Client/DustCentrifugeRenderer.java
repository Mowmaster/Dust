package com.mowmaster.dust.Features.Apparatus.centrifuge.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Apparatus.centrifuge.DustCentrifugeBlock;
import com.mowmaster.dust.Features.Apparatus.centrifuge.DustCentrifugeBlockEntity;
import com.mowmaster.dust.Features.Apparatus.centrifuge.DustCentrifugeBlockEntity.Phase;
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

// The Dust Centrifuge's moving parts, standalone models cut from dust_centrifuge.json (made facing north):
//   the arms (dust_centrifuge_spinner.json) and the four vials (dust_centrifuge_vial_<side>.json: corks and glass) spin
//     about the block's middle (x 8, z 8 pixels); each vial swings out from its top as it goes faster (to MAX_TILT)
//   the dust in each vial (the white dust block's texture tinted, a box inside its glass): the element filling all four
//     as it's piped in; once it's come apart, the north and south vials the first part, the east and west the second,
//     each going down as it drains
public class DustCentrifugeRenderer implements BlockEntityRenderer<DustCentrifugeBlockEntity, DustCentrifugeRenderer.State>
{
    public static final StandaloneModelKey<BlockStateModelPart> SPINNER = new StandaloneModelKey<>(() -> "dust:dust_centrifuge_spinner");
    private static final String[] SIDES = {"north", "south", "east", "west"};
    @SuppressWarnings("unchecked")
    public static final StandaloneModelKey<BlockStateModelPart>[] VIALS = new StandaloneModelKey[SIDES.length];
    private static final RenderType DUST = RenderTypes.entityCutout(Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/blockofdust_white.png"));
    // Each vial (north, south, east, west): the middle of its top it swings from (x, z pixels), the inside of its glass
    // (x0, z0, x1, z1), and which way it swings out (about x for north and south, about z for east and west)
    private static final double PIVOT_Y = 20.5, DUST_BOTTOM = 15.02, DUST_TOP = 18.98;
    private static final double[][] VIAL = {
            {8, 4, 7.52, 3.52, 8.48, 4.48},
            {8, 12, 7.52, 11.52, 8.48, 12.48},
            {12, 8, 11.52, 7.52, 12.48, 8.48},
            {4, 8, 3.52, 7.52, 4.48, 8.48}};

    static {
        for (int i = 0; i < SIDES.length; i++) {
            String name = "dust:dust_centrifuge_vial_" + SIDES[i];
            VIALS[i] = new StandaloneModelKey<>(() -> name);
        }
    }

    public static void registerModels(ModelEvent.RegisterStandalone event)
    {
        event.register(SPINNER, SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_centrifuge_spinner")));
        for (int i = 0; i < SIDES.length; i++) {
            event.register(VIALS[i], SimpleUnbakedStandaloneModel.simpleModelWrapper(Identifier.fromNamespaceAndPath(DustReferences.MODID, "block/apparatus/dust_centrifuge_vial_" + SIDES[i])));
        }
    }

    public DustCentrifugeRenderer(BlockEntityRendererProvider.Context context) {}

    public static class State extends BlockEntityRenderState
    {
        Direction facing = Direction.NORTH;
        float angle;
        float tilt;
        // Each vial's dust: how full (0-1) and its colour (0xAARRGGBB)
        final float[] fill = new float[4];
        final int[] color = new int[4];
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    // Its vials stand above its block, and swing out past its sides
    @Override
    public AABB getRenderBoundingBox(DustCentrifugeBlockEntity centrifuge)
    {
        return new AABB(centrifuge.getBlockPos()).inflate(1);
    }

    @Override
    public void extractRenderState(DustCentrifugeBlockEntity centrifuge, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(centrifuge, state, partialTick, cameraPos, crumblingOverlay);
        state.facing = centrifuge.getBlockState().getValue(DustCentrifugeBlock.FACING);
        state.angle = centrifuge.angle(partialTick);
        float speed = centrifuge.speedShare(partialTick);
        state.tilt = speed * speed * (3 - 2 * speed) * DustCentrifugeBlockEntity.MAX_TILT;
        if (centrifuge.phase() == Phase.DRAINING) {
            float made = Math.max(1, centrifuge.made());
            set(state, 0, centrifuge.firstLeft() / made, centrifuge.first() == null ? 0xFFFFFF : centrifuge.first().color());
            set(state, 1, centrifuge.firstLeft() / made, centrifuge.first() == null ? 0xFFFFFF : centrifuge.first().color());
            set(state, 2, centrifuge.secondLeft() / made, centrifuge.second() == null ? 0xFFFFFF : centrifuge.second().color());
            set(state, 3, centrifuge.secondLeft() / made, centrifuge.second() == null ? 0xFFFFFF : centrifuge.second().color());
        } else {
            float share = centrifuge.input() == null ? 0 : centrifuge.amount() / (float) DustCentrifugeBlockEntity.CAPACITY;
            int color = centrifuge.input() == null ? 0xFFFFFF : centrifuge.input().color();
            for (int i = 0; i < 4; i++) set(state, i, share, color);
        }
    }

    private static void set(State state, int vial, float fill, int color)
    {
        state.fill[vial] = Math.clamp(fill, 0F, 1F);
        state.color[vial] = 0xFF000000 | color;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera)
    {
        var models = Minecraft.getInstance().getModelManager();
        poseStack.pushPose();
        // Turned with the block (the model faces north), then spun about its middle
        poseStack.translate(0.5, 0, 0.5);
        poseStack.rotate(Axis.YP.rotationDegrees(StationRendering.yaw(state.facing) - state.angle));
        poseStack.translate(-0.5, 0, -0.5);
        part(models.getStandaloneModel(SPINNER), Sheets.cutoutBlockItemSheet(), state, poseStack, collector);
        for (int i = 0; i < 4; i++) {
            double[] vial = VIAL[i];
            poseStack.pushPose();
            // Swung out from its top: north +x-turn, south -x-turn, east +z-turn, west -z-turn
            poseStack.translate(vial[0] / 16, PIVOT_Y / 16, vial[1] / 16);
            switch (i) {
                case 0 -> poseStack.rotate(Axis.XP.rotationDegrees(state.tilt));
                case 1 -> poseStack.rotate(Axis.XP.rotationDegrees(-state.tilt));
                case 2 -> poseStack.rotate(Axis.ZP.rotationDegrees(state.tilt));
                default -> poseStack.rotate(Axis.ZP.rotationDegrees(-state.tilt));
            }
            poseStack.translate(-vial[0] / 16, -PIVOT_Y / 16, -vial[1] / 16);
            // Glass: drawn see-through
            part(models.getStandaloneModel(VIALS[i]), Sheets.translucentBlockItemSheet(), state, poseStack, collector);
            if (state.fill[i] > 0) {
                double top = DUST_BOTTOM + Math.max(0.2, (DUST_TOP - DUST_BOTTOM) * state.fill[i]);
                StationRendering.box(DUST, vial[2], DUST_BOTTOM, vial[3], vial[4], top, vial[5], state.color[i], state.lightCoords, Direction.NORTH, poseStack, collector);
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    private static void part(@Nullable BlockStateModelPart model, RenderType sheet, State state, PoseStack poseStack, SubmitNodeCollector collector)
    {
        if (model == null) return;
        collector.submitBlockModel(poseStack, sheet, List.of(model), BlockModelRenderState.EMPTY_TINTS,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }
}
