package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.CrusherLayout;
import com.mowmaster.dust.Features.Machines.ResearchSeparationBlock;
import com.mowmaster.dust.Features.Machines.ResearchSeparationBlockEntity;
import com.mowmaster.dust.Features.Machines.SeparationLayout;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The Alchemical Separation Table's moving parts (the table and scroll are the block model):
//   the glyph: while separating, a glowing copy of the scroll's glyph rises off it, spinning, and pulses
//     (textures/block/station/scroll_glyph.png: just the glyph's lines, white, drawn tinted and at full brightness)
//   the item: lying on the scroll, or riding above the glyph while it's separated
//   the paper on its pad and the two outputs, lying flat
public class ResearchSeparationRenderer implements BlockEntityRenderer<ResearchSeparationBlockEntity, ResearchSeparationRenderer.State>
{
    private static final int FULL_BRIGHT = 0xF000F0;
    private static final float ITEM_SCALE = 0.35F;
    private static final float LIFT_TICKS = 20F;
    private static final float SETTLE_TICKS = 10F;
    // The glyph's glow, between these two colours (0xAARRGGBB), and how fast it pulses
    private static final int GLOW_BRIGHT = 0xFFF0E0FF;
    private static final int GLOW_DIM = 0x80A070FF;

    private static final Identifier GLYPH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/scroll_glyph.png");
    // entityTranslucent: a texture drawn with see-through pixels blended, the way entities with transparency are
    private static final RenderType GLYPH_TYPE = RenderTypes.entityTranslucent(GLYPH);

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
        // 0-1: how far the glyph has risen
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
        state.spin = p < 0 ? 0 : (p * 9F * state.lift) % 360F;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        boolean running = state.progress >= 0;
        double liftPx = SeparationLayout.ARRAY_LIFT * state.lift;
        float yaw = -CrusherLayoutTurns.of(state.facing) * 90F;

        if (running) submitGlyph(state, liftPx, poseStack, submitNodeCollector);

        // The item: lying on the scroll, or riding above the glyph while separating
        if (!state.input.isEmpty()) {
            Vec3 px = running ? SeparationLayout.ARRAY.add(0, liftPx + 1.5, 0) : SeparationLayout.SCROLL;
            submitFlat(state.input, px, running ? yaw + state.spin : yaw, running ? FULL_BRIGHT : state.lightCoords, state, poseStack, submitNodeCollector);
        }
        submitFlat(state.paper, SeparationLayout.PAPER_PAD, yaw, state.lightCoords, state, poseStack, submitNodeCollector);
        submitFlat(state.left, SeparationLayout.LEFT_OUTPUT, yaw, state.lightCoords, state, poseStack, submitNodeCollector);
        submitFlat(state.right, SeparationLayout.RIGHT_OUTPUT, yaw, state.lightCoords, state, poseStack, submitNodeCollector);
    }

    // An item lying flat at a point of the model (pixels, facing north), turned yaw degrees about up
    private static void submitFlat(ItemStackRenderState item, Vec3 pixels, float yaw, int light, State state,
                                   PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        if (item.isEmpty()) return;
        Vec3 at = CrusherLayout.turned(pixels, state.facing);
        poseStack.pushPose();
        poseStack.translate(at.x, at.y, at.z);
        poseStack.rotate(Axis.YP.rotationDegrees(yaw));
        // Laid down face up, its top pointing away from the player at the front (the GROUND model stands upright;
        // tipping it -90 degrees about x lays its face upwards, where +90 laid it face down and upside down)
        poseStack.rotate(Axis.XP.rotationDegrees(-90));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        item.submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    // The glyph: a flat square the size of the scroll's top, risen and spinning, drawn at full brightness in a colour
    // that pulses between GLOW_DIM and GLOW_BRIGHT
    private static void submitGlyph(State state, double liftPx, PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        Vec3 centre = CrusherLayout.turned(SeparationLayout.ARRAY.add(0, liftPx + 0.05, 0), state.facing);
        float pulse = 0.5F + 0.5F * Mth.sin(state.progress * 0.6F);
        int color = lerpColor(GLOW_DIM, GLOW_BRIGHT, pulse);
        float halfX = (float) (SeparationLayout.GLYPH_WIDTH / 32);
        float halfZ = (float) (SeparationLayout.GLYPH_DEPTH / 32);
        poseStack.pushPose();
        poseStack.translate(centre.x, centre.y, centre.z);
        poseStack.rotate(Axis.YP.rotationDegrees(-CrusherLayoutTurns.of(state.facing) * 90F + state.spin));
        submitNodeCollector.submitCustomGeometry(poseStack, GLYPH_TYPE, (pose, buffer) -> {
            // Facing up, and a copy facing down so it shows from below while it hangs in the air
            vertex(buffer, pose, -halfX, -halfZ, 0, 0, color, 1);
            vertex(buffer, pose, -halfX, halfZ, 0, 1, color, 1);
            vertex(buffer, pose, halfX, halfZ, 1, 1, color, 1);
            vertex(buffer, pose, halfX, -halfZ, 1, 0, color, 1);
            vertex(buffer, pose, halfX, -halfZ, 1, 0, color, -1);
            vertex(buffer, pose, halfX, halfZ, 1, 1, color, -1);
            vertex(buffer, pose, -halfX, halfZ, 0, 1, color, -1);
            vertex(buffer, pose, -halfX, -halfZ, 0, 0, color, -1);
        });
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float z, float u, float v, int color, float normalY)
    {
        buffer.addVertex(pose, x, 0, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(FULL_BRIGHT).setNormal(pose, 0, normalY, 0);
    }

    // Between two 0xAARRGGBB colours, t 0-1
    private static int lerpColor(int from, int to, float t)
    {
        int out = 0;
        for (int shift = 0; shift <= 24; shift += 8) {
            int a = (from >>> shift) & 0xFF, b = (to >>> shift) & 0xFF;
            out |= (Math.round(a + (b - a) * t) & 0xFF) << shift;
        }
        return out;
    }

    // Clockwise quarter turns the blockstate gives each facing (the models face north)
    private static final class CrusherLayoutTurns
    {
        static int of(Direction facing)
        {
            return switch (facing) {
                case EAST -> 1;
                case SOUTH -> 2;
                case WEST -> 3;
                default -> 0;
            };
        }
    }
}
