package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.ResearchTransformationBlockEntity;
import com.mowmaster.dust.Features.Machines.StationBlock;
import com.mowmaster.dust.Features.Machines.TransformationLayout;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The Transformation Table's moving parts (the table and scroll are the block model): the glyph
// (textures/block/station/scroll_transformation_glyph.png) rising off the scroll and spinning, with the item riding
// above it; the crystals on their pad and the output, lying flat.
public class ResearchTransformationRenderer implements BlockEntityRenderer<ResearchTransformationBlockEntity, ResearchTransformationRenderer.State>
{
    private static final float ITEM_SCALE = 0.35F;
    private static final Identifier GLYPH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/scroll_transformation_glyph.png");

    private final ItemModelResolver itemModelResolver;

    public ResearchTransformationRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState input = new ItemStackRenderState();
        final ItemStackRenderState crystals = new ItemStackRenderState();
        final ItemStackRenderState output = new ItemStackRenderState();
        Direction facing = Direction.NORTH;
        float progress = -1;
        float lift;
        float spin;
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ResearchTransformationBlockEntity table, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(table, state, partialTick, cameraPos, crumblingOverlay);
        itemModelResolver.updateForTopItem(state.input, table.getInput(), ItemDisplayContext.GROUND, table.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.crystals, table.getCrystals(), ItemDisplayContext.GROUND, table.getLevel(), null, 1);
        itemModelResolver.updateForTopItem(state.output, table.getOutput(), ItemDisplayContext.GROUND, table.getLevel(), null, 2);
        state.facing = table.getBlockState().getValue(StationBlock.FACING);
        state.progress = table.progress(partialTick);
        state.lift = StationRendering.lift(state.progress, ResearchTransformationBlockEntity.TRANSFORM_TICKS);
        state.spin = state.progress < 0 ? 0 : (state.progress * 9F * state.lift) % 360F;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        boolean running = state.progress >= 0;
        double liftPx = TransformationLayout.ARRAY_LIFT * state.lift;
        if (running) {
            StationRendering.glyph(GLYPH, TransformationLayout.ARRAY.add(0, liftPx + 0.05, 0), TransformationLayout.GLYPH_WIDTH, TransformationLayout.GLYPH_DEPTH,
                    state.spin, state.progress, state.facing, poseStack, submitNodeCollector);
        }
        // The item: lying on the scroll, or riding above the glyph while it's transformed
        if (!state.input.isEmpty()) {
            Vec3 px = running ? TransformationLayout.ARRAY.add(0, liftPx + 1.5, 0) : TransformationLayout.SCROLL;
            StationRendering.flatItem(state.input, px, state.facing, running ? state.spin : 0, ITEM_SCALE,
                    running ? StationRendering.FULL_BRIGHT : state.lightCoords, poseStack, submitNodeCollector);
        }
        StationRendering.flatItem(state.crystals, TransformationLayout.CRYSTAL_PAD, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.output, TransformationLayout.OUTPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
    }
}
