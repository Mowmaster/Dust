package com.mowmaster.dust.Features.Crafting.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mowmaster.dust.Features.Crafting.ElementCrafterBlockEntity;
import com.mowmaster.dust.Features.Crafting.ElementCrafterLayout;
import com.mowmaster.dust.Features.Machines.Client.StationRendering;
import com.mowmaster.dust.Features.Machines.StationBlock;
import com.mowmaster.mowlib.client.GhostItemRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

// The Element Crafter's grid items, lying flat in their cells, and the result floating over the grid as a ghost
// (MowLib's GhostItemRenderer: the result's name above it, the dust it takes below)
public class ElementCrafterRenderer implements BlockEntityRenderer<ElementCrafterBlockEntity, ElementCrafterRenderer.State>
{
    private static final float ITEM_SCALE = 0.28F;

    private final ItemModelResolver itemModelResolver;
    private final GhostItemRenderer ghostRenderer;

    public ElementCrafterRenderer(BlockEntityRendererProvider.Context context)
    {
        this.itemModelResolver = context.itemModelResolver();
        this.ghostRenderer = new GhostItemRenderer(context.itemModelResolver(), context.font());
    }

    public static class State extends BlockEntityRenderState
    {
        final ItemStackRenderState[] cells = new ItemStackRenderState[ElementCrafterLayout.CELLS];
        final GhostItemRenderer.Ghosts ghosts = new GhostItemRenderer.Ghosts();
        Direction facing = Direction.NORTH;
        float spin;

        State()
        {
            for (int i = 0; i < cells.length; i++) cells[i] = new ItemStackRenderState();
        }
    }

    @Override
    public State createRenderState()
    {
        return new State();
    }

    @Override
    public void extractRenderState(ElementCrafterBlockEntity crafter, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(crafter, state, partialTick, cameraPos, crumblingOverlay);
        for (int i = 0; i < ElementCrafterLayout.CELLS; i++) {
            itemModelResolver.updateForTopItem(state.cells[i], crafter.getCell(i), ItemDisplayContext.GROUND, crafter.getLevel(), null, i);
        }
        ghostRenderer.extract(state.ghosts, crafter.getGhosts(), crafter.getLevel(), crafter.getBlockPos(), ElementCrafterLayout.CELLS);
        state.facing = crafter.getBlockState().getValue(StationBlock.FACING);
        state.spin = crafter.getLevel() == null ? 0 : ((crafter.getLevel().getGameTime() + partialTick) * 2F) % 360F;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera)
    {
        for (int i = 0; i < ElementCrafterLayout.CELLS; i++) {
            StationRendering.flatItem(state.cells[i], ElementCrafterLayout.cellCentre(i), state.facing, 0, ITEM_SCALE, state.lightCoords, poseStack, submitNodeCollector);
        }
        ghostRenderer.submit(state.ghosts, Direction.UP, state.spin, poseStack, submitNodeCollector, camera);
    }

    // The ghost floats above the block: draw while that's in view too
    @Override
    public AABB getRenderBoundingBox(ElementCrafterBlockEntity crafter)
    {
        return GhostItemRenderer.boundingBox(crafter.getBlockPos(), crafter.getGhosts());
    }
}
