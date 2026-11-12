package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
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
import net.minecraft.client.renderer.state.level.CameraRenderState;
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
    private static final float ITEM_SCALE = 0.35F;
    private static final float LIFT_TICKS = 20F;
    private static final float SETTLE_TICKS = 10F;

    private static final Identifier GLYPH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/scroll_glyph.png");

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

        if (running) {
            StationRendering.glyph(GLYPH, SeparationLayout.ARRAY.add(0, liftPx + 0.05, 0), SeparationLayout.GLYPH_WIDTH, SeparationLayout.GLYPH_DEPTH,
                    state.spin, state.progress, state.facing, poseStack, submitNodeCollector);
        }
        // The item: lying on the scroll, or riding above the glyph while separating
        if (!state.input.isEmpty()) {
            Vec3 px = running ? SeparationLayout.ARRAY.add(0, liftPx + 1.5, 0) : SeparationLayout.SCROLL;
            StationRendering.flatItem(state.input, px, state.facing, running ? state.spin : 0, ITEM_SCALE,
                    running ? StationRendering.FULL_BRIGHT : state.lightCoords, poseStack, submitNodeCollector);
        }
        StationRendering.flatItem(state.paper, SeparationLayout.PAPER_PAD, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.left, SeparationLayout.LEFT_OUTPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.right, SeparationLayout.RIGHT_OUTPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
    }
}
