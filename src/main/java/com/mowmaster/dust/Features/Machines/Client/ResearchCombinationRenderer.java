package com.mowmaster.dust.Features.Machines.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.DustReferences;
import com.mowmaster.dust.Features.Machines.CombinationLayout;
import com.mowmaster.dust.Features.Machines.ResearchCombinationBlockEntity;
import com.mowmaster.dust.Features.Machines.StationBlock;
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

// The Combination Table's moving parts (the table and scroll are the block model): the glyph
// (textures/block/station/scroll_combination_glyph.png) rising off the scroll and spinning; the dusts on their pads
// and the output, lying flat.
public class ResearchCombinationRenderer implements BlockEntityRenderer<ResearchCombinationBlockEntity, ResearchCombinationRenderer.State>
{
    private static final float ITEM_SCALE = 0.35F;
    private static final Identifier GLYPH = Identifier.fromNamespaceAndPath(DustReferences.MODID, "textures/block/station/scroll_combination_glyph.png");

    private final ItemModelResolver itemModelResolver;

    public ResearchCombinationRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState left = new ItemStackRenderState();
        final ItemStackRenderState right = new ItemStackRenderState();
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
    public void extractRenderState(ResearchCombinationBlockEntity table, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(table, state, partialTick, cameraPos, crumblingOverlay);
        itemModelResolver.updateForTopItem(state.left, table.getLeft(), ItemDisplayContext.GROUND, table.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(state.right, table.getRight(), ItemDisplayContext.GROUND, table.getLevel(), null, 1);
        itemModelResolver.updateForTopItem(state.output, table.getOutput(), ItemDisplayContext.GROUND, table.getLevel(), null, 2);
        state.facing = table.getBlockState().getValue(StationBlock.FACING);
        state.progress = table.progress(partialTick);
        state.lift = StationRendering.lift(state.progress, ResearchCombinationBlockEntity.COMBINE_TICKS);
        state.spin = state.progress < 0 ? 0 : (state.progress * 9F * state.lift) % 360F;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        if (state.progress >= 0) {
            StationRendering.glyph(GLYPH, CombinationLayout.ARRAY.add(0, CombinationLayout.ARRAY_LIFT * state.lift + 0.05, 0),
                    CombinationLayout.GLYPH_WIDTH, CombinationLayout.GLYPH_DEPTH, state.spin, state.progress, state.facing, poseStack, submitNodeCollector);
        }
        StationRendering.flatItem(state.left, CombinationLayout.LEFT_INPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.right, CombinationLayout.RIGHT_INPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        StationRendering.flatItem(state.output, CombinationLayout.OUTPUT, state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
    }
}
